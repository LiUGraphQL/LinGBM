import cluster from "cluster";
import os from "os";
import { join } from "path";
import fs, { writeFile, lstatSync, readdirSync, readFileSync } from "fs";
import _ from "lodash";
import program from "commander";
import { Parser } from "json2csv";
import { create } from "domain";

// MASTER
export default () => {
  program
    .version("0.0.1", "-v, --version")
    .option("-a, --actual-queries <path>", "Path to actualQueries folder.")
    .option("-c, --clients <clients>", "Number of clients", 1)
    .option(
      "-s --server <url>",
      "URL to the GraphQL server to test",
      "localhost"
    )
    .option("-p --port <port>", "Port used by the GraphQL server", "4000")
    .option(
      "-t --type <type>",
      "Type of test to run, et (execution time)",
      "et"
    )
    .option("-o --name <name>", "Set the name of the output file", "0")
    .option("-q --queryTP <queryTP>", "Set the queryTemplate to test", 0)
    .option(
      "-r --repeat <repeat>",
      "Set the number of times to repeat the test",
      1
    )
    .option(
      "-n --numberQET <numberQET>",
      "Specify the number of queries per template that are used to test execution time",
      10
    )
    .parse(process.argv);

  // Constants
  const numCPUs = os.cpus().length;
  const maxClients = numCPUs - 1; // The extra one here runs the graphQL server
  const actualQueriesPath = program.actualQueries;
  const SERVER_URL = "http://" + program.server + ":" + program.port;
  const numWorkers =
    program.type === "tp" ? Math.min(maxClients, program.clients) : 1;

  // Helper functions
  const isDirectory = source => lstatSync(source).isDirectory();
  const isFile = source => lstatSync(source).isFile();
  const getDirectories = source =>
    readdirSync(source)
      .map(name => join(source, name))
      .filter(isDirectory);

  const getFiles = source =>
    readdirSync(source)
      .map(name => join(source, name))
      .filter(isFile);

  // Parase and sort the queries
  const queryTemplatesDirs = getDirectories(actualQueriesPath);
  const queryTemplates = queryTemplatesDirs.map(dirPath => {
    const queryPaths = getFiles(dirPath);
    let queryTemplate = dirPath.split("/").pop();
    let queryTemplateNumber = queryTemplate.split("T").pop();

    const queries = queryPaths.map((path, index) => {
      const data = readFileSync(path, "utf-8", (err, data) => {
        if (err) throw err;
        return data;
      });
      return { index: index + 1, data };
    });

    return {
      queryTemplate: parseInt(queryTemplateNumber, 10),
      queries
    };
  });

  let workerCount = 0;

  const createWorkers = () => {
    for (let i = 0; i < numWorkers; i++) {
      cluster.fork();
      workerCount += 1;
    }
  };

  const killWorkers = () => {
    for (const id in cluster.workers) {
      //console.log("killing worker", id);
      cluster.workers[id].kill();
    }
  };

  let currentRun = 1;

  let collectedData = [];
  const resetCollectedData = () => {
    collectedData = [];
  };

  cluster.on("exit", (worker, code, signal) => {
    //console.log(`worker ${worker.process.pid} died`);
    workerCount -= 1;

    if (workerCount === 0) {
      // Time to convert data to some csv
      let fields;
      if(program.type === "et"){
        fields = [
          {label: "Query Template", value: "queryT"},
          {label: "Query Number", value: "index" },
          {label:"Execution time", value: "executionT"},
          { label: "Error", value: "error" }
        ];
      }

      const json2csvParser = new Parser({fields});
      const csv = json2csvParser.parse(collectedData);
      const csvValue = csv.split("\n");
      const csv1 = csvValue[1]+"\n";
      // Create output dir if it doesn't exist
      if (!fs.existsSync("output")) fs.mkdirSync("output");
      // Write to file
      if(program.name == "0"){
        program.name = `${program.type}`;
      }else{
        program.name = program.name;
      }
      const outputFileName = program.name;
      fs.appendFile(
        `output/${outputFileName}.csv`,
        csv1,
        { encoding: "utf-8" },
        err => {
          if (err) throw err;
          console.log("Execution time has been recorded.\n");
        }
      );
      reset();
    }
  });

  let totalCount = 0;
  let errorCount = 0;
  // Handle log-data from the workers
  cluster.on("message", (worker, { command, data }) => {
    switch (command) {
      case "LOGDATA":
        console.log(`worker ${worker.id}:`, data);
        if(data.error == 0){
          totalCount +=1;
        }else{
          errorCount +=1;
        }
        killWorkers();
        collectedData.push(data);
    }
  });

  const qtsFuc = queryT => {
    const qts_value = queryTemplates.find(
      qt => qt.queryTemplate === parseInt(queryT)
    );
    return {
      qts_value
    };
  };

  const distributeQueries = (queryT) => {
    let index = 1;
    let qts = qtsFuc(queryT).qts_value;
    _.forEach(cluster.workers, worker => {
      const slice = Math.floor(
        (qts.queries.length / program.clients) * (index - 1)
      );
      worker.send({
        command: "QUERIES",
        data: qts.queries,
        template: qts.queryTemplate,
        slice,
        currentExe: currentRun
      });
      index += 1;
    });
  };

  const startWorkers = () => {
    for (const id in cluster.workers) {
      cluster.workers[id].send({
        command: "START",
        data: { url: SERVER_URL, type: program.type }
      });
    }
  };

  const start = (queryT) => {
    createWorkers();
    distributeQueries(queryT);
    startWorkers();
  };

  const repeatExe = queryT =>{
    if (currentRun < program.numberQET) {
      currentRun += 1;
      //reset(queryT);
      if(program.queryTP == 0){
        reset(queryT);
      } else{
        setTimeout(() => {
          start(queryT);
        }, 1000);
      }
    }else{
      killWorkers();
      console.log("All test have been completed");
    }
  }
  
  const reset = () => {
    totalCount = 0;
    resetCollectedData();
    if(program.queryTP == 0){
      query +=1;
      testForNextQT(query);
    } else{
      console.log("test");
      repeatExe(program.queryTP);
    }
  };
  const testForNextQT = (queryT) => {
    if(queryT<queryTemplatesDirs.length+1){
      setTimeout(() => {
        start(queryT);
      }, 1000);
    }else{
      console.log("QETs for "+currentRun+" queries per template have been recorded");
      query = 0;
      repeatExe(query);
    }
  };

  //query: specify the nr of query template
  let query = 1;
  if(program.queryTP == 0){
    query = 1;
    start(query);
  }else{
    query = program.queryTP;
    start(query);
  } 
};