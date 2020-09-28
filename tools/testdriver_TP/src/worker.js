import { request } from "graphql-request";
import prettyjson from "prettyjson";

export default () => {
  console.log(`Worker ${process.pid} started`);

  let queries;
  let cursor = 0;
  let workerid;
  let sendQueryCount = 0;
  let errorCount = 0;
  let successCount = 0; 
  let executionT = 0;

  const errorHandler = ({ query, error }) => {
    console.log(cursor);
    console.log("QUERY:", query);
    console.log(prettyjson.render(error));

    errorCount += 1;
    process.send({
      command: "LOGDATA",
      data: {
        clientID: workerid,
        index: query.index,
        executionT,
        error: 1,
        count: errorCount
      }
    });
  };

  const throughputTest = async ({ url }) => {
    const runRequest = async (url, query) => {
      sendQueryCount += 1;
      //console.log("workerid:", workerid, "; sendQueryCount:", sendQueryCount);
      process.send({
        command: "COUNT",
        data: {
          count: sendQueryCount
        }
      });
      try {
        const startTime = Date.now();
        await request(url, query.data);
        const endTime = Date.now();
        executionT = endTime - startTime;
        successCount += 1;
        process.send({
          command: "LOGDATA",
          data: {
            clientID: workerid,
            index: query.index,
            executionT,
            //startTime,
            //endTime,
            error: 0,
            count: successCount
          }
        });
      } catch (error) {
        if(error.code == "ERR_IPC_CHANNEL_CLOSED"){
          console.log("exist due to ERROR of CHANNEL CLOSED");
          process.exit(1);
        }else{
          errorHandler({ query, error });
        }
      }
    };

    while (true) {
      await runRequest(url, queries[cursor]);
      cursor = cursor === queries.length - 1 ? 0 : cursor + 1;
    };
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