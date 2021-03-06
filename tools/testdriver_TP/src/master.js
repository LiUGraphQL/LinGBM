import cluster from "cluster";
import os from "os";
import { join } from "path";
import fs, { writeFile, lstatSync, readdirSync, readFileSync } from "fs";
import _ from "lodash";
import program from "commander";
import { Parser } from "json2csv";

// MASTER
export default () => {
  program
    .version("0.0.1", "-v, --version")
    .option("-a, --actual-queries <path>", "Path to actualQueries folder.")
    .option("-c, --clients <clients>", "Number of clients", 1)
    .option(
      "-s --server <url>",
      "complete URL to the GraphQL server under test, including port (if any) and local path (if any)",
      "http://localhost:4000"  // default value
    )
    .option(
      "-t --type <type>",
      "Type of test to run, tp (throughput).",
      "tp"
    )
    .option(
      "-i --interval <interval>",
      "How long the test should run for, default 60 sec",
      60
    )
    .option("-o --name <name>", "Set the name of the output file", "0")
    .option("-q, --queryTP <queryTP>", "Set the queryTemplate to test", 0)
    .option(
      "-n --repeatNr <repeatNr>",
      "Set the number of running the test, 1 refers to the first time",
      1
    )
    .option(
      "-r, --repeat <repeat>",
      "Set the number of times to repeat the test",
      1
    )
    .parse(process.argv);
  
  let sendKeyValue = {};
  let successKeyValue = {};
  let errorKeyValue = {};
  const numCPUs = os.cpus().length;
  const actualQueriesPath = program.actualQueries;
  const SERVER_URL = program.server;

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
  let sendCount = 0;
  let successCount = 0;
  let errorCount = 0;
  
  let collectedData = [];
  const resetCollectedData = () => {
    collectedData = [];
    sendCount = 0;
    successCount = 0;
    errorCount = 0;
    sendKeyValue = {};
    successKeyValue = {};
    errorKeyValue = {};
  };

  cluster.on("exit", (worker, code, signal) => {
    workerCount -= 1;
    if (signal) {
      //console.log(`worker ${worker.process.pid} was killed by signal: ${signal}`);
    } else if (code !== 0) {
      console.log(`worker exited with error code: ${code}`);
    } else {
      console.log('worker success!');
    }
    if (workerCount === 0) {
      // Time to convert data to some csv
      console.log("All workers are done.");
      for(var i in sendKeyValue) {
          if(sendKeyValue[i]){
            sendCount += sendKeyValue[i];
          }else{
            console.log("client",worker.id,"send out 0 query.");
          }
          if(successKeyValue[i]){
            successCount += successKeyValue[i];
          }else{
            console.log("client",worker.id,"successfully executed 0 query.");
          }
          if(errorKeyValue[i]){
            errorCount += errorKeyValue[i];
          }else{
            //console.log("client",worker.id," have 0 query returned error message.");
          }
      }
      //console.log("Within", program.interval, "seconds, the number of total send out queries are:", sendCount);
      console.log("the number of successfully executed queries are:", successCount);
      console.log("the number of queries output an error message:", errorCount);
      let fields;
      fields = [
        { label: "Client Nr", value: "clientID" },
        { label: "Query Number", value: "index" },
        { label:"Execution time", value: "executionT"},
        //{ label:"Start time", value: "startTime"},
        //{ label:"End time", value: "endTime"},
        { label: "Error", value: "error" }
      ];
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
        `${outputFileName}/QT${query}_${currentRun}_${program.repeatNr}.csv`,
        csv,
        { encoding: "utf-8" },
        err => {
          if (err) throw err;
          //console.log("Output has been saved.\n");
        }
      );
      //Append to statistic file
      const current_statistic = `QT${query}, ${sendCount}, ${successCount}, ${errorCount}, ${currentRun}\n`;
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

  // Handle log-data from the workers
  cluster.on("message", (worker, { command, data }) => {
    switch (command) {
      case "LOGDATA":
        console.log(`worker ${worker.id}:`, data);
        if(data.error == 0){
          successKeyValue[worker.id] = data.count; 
        }
        else{
          errorKeyValue[worker.id] = data.count; 
        }
        collectedData.push(data);
        //console.log("totalSuccessuery of :", worker.id, "is: ",successKeyValue[worker.id]);
        //console.log("totalErrorquery of :", worker.id, "is: ",errorKeyValue[worker.id]);
      case "COUNT":
        sendKeyValue[worker.id] = data.count;
        //console.log("client",worker.id,"send out", sendKeyValue[i], " queries.");
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
    let sliceStart = 0;
    let sliceStartNext = 0;
    let qts = qtsFuc(queryT).qts_value;
    _.forEach(cluster.workers, worker => {
      if((qts.queries.length / program.clients) < 1){
        sliceStart = 0;
        sliceStartNext = 0;
      }else{
        sliceStart = Math.floor(
          (qts.queries.length / program.clients) * (index - 1)
        );
        sliceStartNext = Math.floor(
          (qts.queries.length / program.clients) * (index)
        );
      }
      worker.send({
        command: "QUERIES",
        data: qts.queries,
        workerID: worker.id,
        sliceStart,
        sliceStartNext
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
    setTimeout(() => {
      console.log("timeout");
      killWorkers();
    }, program.interval * 1000);
  };

  const reset = () => {
    if (currentRun < program.repeat) {
      currentRun += 1;
      resetCollectedData();
      console.log("Repeat the throughput test for query template",query,". The", currentRun ,"time starting:\n");
      setTimeout(() => {
        start(query);
      }, 1000);
    } else {
      if(program.queryTP == 0){
        console.log("The throughput test for query template", query, "is completed");
        T_index +=1;
        currentRun = 1;
        testForNextQT(T_index);
      }else{
        console.log("The throughput test for query template", program.queryTP, "is completed");
      } 
    }
  };

  const testForNextQT = index => {
    if(index<queryTemplatesDirs.length){
      resetCollectedData();
      query = queryT_list[index]
      setTimeout(() => {
        start(query);
      }, 1000);
    }else{
      console.log("The Throughput test is completed");
    }
  };

  let queryT_list = []
  for (const QT_item of queryTemplates) {
    queryT_list.push(QT_item.queryTemplate);
  }

  //query: specify the nr of query template
  let query
  let T_index = 0;
  if(program.queryTP == 0){
    query = queryT_list[T_index];
    start(query);
  }else{
    query = parseInt(program.queryTP, 10)
    if(queryT_list.includes(query)){
      start(query);
    }else{
      console.log("Query instances for QT",program.queryTP ,"are not available.");
    }
  } 
};
