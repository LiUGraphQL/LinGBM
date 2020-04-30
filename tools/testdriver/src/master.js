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
      "Type of test to run, tp (throughput) or dbc (database-count).",
      "tp"
    )
    .option(
      "-i --interval <interval>",
      "How long the test should run for, default 30 sec",
      30
    )
    .option("-n --name <name>", "Set the name of the output file")
    .option("-q, --query <query>", "Set the queryTemplate to test", 1)
    .option(
      "-r, --repeat <repeat>",
      "Set the number of times to repeat the test",
      1
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
      console.log("killing worker", id);
      cluster.workers[id].kill();
    }
  };

  let currentRun = 1;

  let collectedData = [];
  const resetCollectedData = () => {
    collectedData = [];
  };

  cluster.on("exit", (worker, code, signal) => {
    console.log(`worker ${worker.process.pid} died`);
    workerCount -= 1;

    if (workerCount === 0) {
      // Time to convert data to some csv
      console.log("All workers are done.");
      let fields;
      if (program.type === "tp") {
        fields = [
          { label: "Query Number", value: "index" },
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
      // Create output dir if it doesn't exist
      if (!fs.existsSync("output")) fs.mkdirSync("output");
      // Write to file
      const outputFileName = program.name
        ? program.name
        : `${program.type}-test-${new Date().toISOString()}`;

      writeFile(
        `output/${outputFileName}-${currentRun}.csv`,
        csv,
        { encoding: "utf-8" },
        err => {
          if (err) throw err;
          console.log("Output has been saved.");
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
        collectedData.push(data);
    }
  });

  const qts = queryTemplates.find(
    qt => qt.queryTemplate === parseInt(program.query)
  );
  const distributeQueries = () => {
    let index = 1;
    _.forEach(cluster.workers, worker => {
      const slice = Math.floor(
        (qts.queries.length / program.clients) * (index - 1)
      );
      worker.send({
        command: "QUERIES",
        data: qts.queries,
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

  const start = () => {
    createWorkers();
    distributeQueries();
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
      resetCollectedData();
      setTimeout(start, 1000);
    } else {
      console.log("Test is complete, exiting.");
    }
  };
  start();
};
