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
      "127.0.0.1"
    )
    .option("-p --port <port>", "Port used by the GraphQL server", "4000")
    .option(
      "-t --type <type>",
      "Type of test to run, tp (throughput) or dbc (database-count).",
      "tp"
    )
    .option(
      "-i --interval <interval>",
      "How long the test should run for, default 30 sec",
      30
    )
    .option("-n --name <name>", "Set the name of the output file", "0")
    .option("-q, --queryTP <queryTP>", "Set the queryTemplate to test", 0)
    .option(
      "-r, --repeat <repeat>",
      "Set the number of times to repeat the test",
      1
    )
    .parse(process.argv);

  const numCPUs = os.cpus().length;
  //const maxClients = numCPUs - 1; // The extra one here runs the graphQL server
  //console.log("The max number of clients that this operation system can hold:", maxClients);
  const actualQueriesPath = program.actualQueries;
  const SERVER_URL = "http://" + program.server + ":" + program.port;
  //const numWorkers =
  //  program.type === "tp" ? Math.min(maxClients, program.clients) : 1;

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
    //for (let i = 0; i < numWorkers; i++) {
    for (let i = 0; i < program.clients; i++) { 
      cluster.fork();
      workerCount += 1;
    }
  };

  const killWorkers = () => {
    for (const id in cluster.workers) {
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
      console.log("All workers are done.");
      console.log("Within", program.interval, "seconds, the number of executed queries are:", totalCount);
      console.log("the number of timeout queries are:", errorCount);
      let fields;
      if (program.type === "tp") {
        fields = [
          { label: "Client Nr", value: "clientID" },
          { label: "Query Number", value: "index" },
          { label:"Execution time", value: "executionT"},
          //{ label:"Start time", value: "startTime"},
          //{ label:"End time", value: "endTime"},
          { label: "Error", value: "error" }
        ];
      } else {
        fields = [
          { label: "Query Number", value: "index" },
          { label: "Database Request", value: "dbRequests" },
          { label: "Error", value: "error" }
        ];
      }
      const json2csvParser = new Parser({ fields });
      const csv = json2csvParser.parse(collectedData);
      // Write to file
      if(program.name == "0"){
        program.name = `${program.type}_${program.interval}sec_${program.clients}clients`;
      }else{
        program.name = program.name;
      }
      const outputFileName = program.name;
      // Create output dir if it doesn't exist
      if (!fs.existsSync(outputFileName)) fs.mkdirSync(outputFileName);

      writeFile(
        `${outputFileName}/QT${query}_${currentRun}.csv`,
        csv,
        { encoding: "utf-8" },
        err => {
          if (err) throw err;
          //console.log("Output has been saved.\n");
        }
      );
      //Append to statistic file
      const current_statistic = `QT${query}, ${totalCount}, ${errorCount}, ${currentRun}\n`;
      fs.appendFile(
        `${outputFileName}/statistics.csv`,
        current_statistic,
        { encoding: "utf-8" },
        err => {
          if (err) throw err;
          //console.log("statistics has been saved.\n");
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
        }
        else{
          errorCount +=1;
        }
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

  const distributeQueries = queryT => {
    let index = 1;
    let qts = qtsFuc(queryT).qts_value;
    _.forEach(cluster.workers, worker => {
      const slice = Math.floor(
        (qts.queries.length / program.clients) * (index - 1)
      );
      worker.send({
        command: "QUERIES",
        data: qts.queries,
        workerID: worker.id,
        slice
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

  const start = queryT => {
    createWorkers();
    distributeQueries(queryT);
    startWorkers();
    // If a throughput test is started, stop it after 30s.
    if (program.type == "tp") {
      setTimeout(() => {
        killWorkers();
      }, program.interval * 1000);
    }
  };

  const reset = () => {
    if (currentRun < program.repeat) {
      currentRun += 1;
      totalCount = 0;
      errorCount = 0;
      resetCollectedData();
      console.log("Repeat the throughput test for query template",query,", the", currentRun ,"time starting:\n");
      setTimeout(() => {
        start(query);
      }, 1000);
    } else {
      if(program.queryTP == 0){
        console.log("The throughput test for query template", query, "is completed");
        query +=1;
        currentRun = 1;
        testForNextQT(query);
      }else{
        console.log("Test is completed, exiting.");
      } 
    }
  };

  const testForNextQT = queryT => {
    if(queryT<queryTemplatesDirs.length+1){
      totalCount = 0;
      errorCount = 0;
      resetCollectedData();
      setTimeout(() => {
        start(queryT);
      }, 1000);
    }else{
      console.log("All Throughput test are completed");
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
