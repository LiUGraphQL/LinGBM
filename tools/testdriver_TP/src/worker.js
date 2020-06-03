import { request } from "graphql-request";
import prettyjson from "prettyjson";

export default () => {
  console.log(`Worker ${process.pid} started`);

  let db;
  let queries;
  let cursor = 0;
  let workerid;

  const errorHandler = ({ query, error }) => {
    console.log("ERROR - EXITING");
    console.log(cursor);
    console.log("QUERY:", query);
    console.log(prettyjson.render(error));

    // Send up error
    process.send({
      command: "LOGDATA",
      data: {
        clientID: workerid,
        index: query.index,
        error: 1
      }
    });
  };

  const throughputTest = async ({ url }) => {
    const runRequest = async (url, query) => {
      try {
        const startTime = Date.now();
        await request(url, query.data);
        const endTime = Date.now();
        const executionT = endTime - startTime;
        process.send({
          command: "LOGDATA",
          data: {
            clientID: workerid,
            index: query.index,
            executionT,
            //startTime,
            //endTime,
            error: 0
          }
        });
      } catch (error) {
        errorHandler({ query, error });
      }
    };

    while (true) {
      await runRequest(url, queries[cursor]);
      cursor = cursor === queries.length - 1 ? 0 : cursor + 1;
    }
  };

  const start = async ({ url, type }) => {
    switch (type) {
      case "dbc":
        databaseRequestTest({ url });
        break;
      case "tp":
        throughputTest({ url });
        break;
    }
  };

  process.on("message", ({ command, data, workerID, slice }) => {
    switch (command) {
      case "ECHO":
        process.send({ command: "ECHO", data });
        break;
      case "QUERIES":
        queries = data;
        workerid = workerID;
        cursor = slice;
        break;
      case "START":
        start(data);
        break;
    }
  });
};
