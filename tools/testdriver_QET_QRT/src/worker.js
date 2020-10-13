import { request} from "graphql-request";
import prettyjson from "prettyjson";

export default () => {
  //console.log(`Worker ${process.pid} started`);

  let queries;
  let cursor = 0;
  let queryIndex;
  let queryTemp;

  const errorHandler = ({ query, error }) => {
    //console.log("ERROR - EXITING");
    //console.log(cursor);
    //console.log("QUERY:", query);
    //console.log(prettyjson.render(error));

    // Send up error
    process.send({
      command: "LOGDATA",
      data: {
        queryT: queryTemp,
        index: query.index,
        error: 1
      }
    });
    //process.exit(1);
  };

  const executionTimeTest = async ({ url }) => {
    const runRequest = async (url, query) => {
      try {
        const startTime = Date.now();
        const reponseRes = await request(url, query.data);
        const responseTime = Date.now();
        console.log(reponseRes);
        const endTime = Date.now();

        const executionT = endTime - startTime;
        const responseT = responseTime - startTime;       
        
        process.send({
          command: "LOGDATA",
          data: {
            queryT: queryTemp,
            index: query.index,
            executionT,
            responseT,
            error: 0
          }
        });
      } catch (error) {
        errorHandler({ query, error });
      }
    };

    await runRequest(url, queries[queryIndex-1]);
  };

  const start = async ({ url, type }) => {
    switch (type) {
      case "et":
        executionTimeTest({ url });
        break;
    }
  };

  process.on("message", ({ command, data, template, slice, currentExe}) => {
    switch (command) {
      case "ECHO":
        process.send({ command: "ECHO", data });
        break;
      case "QUERIES":
        queries = data;
        queryTemp = template;
        cursor = slice;
        queryIndex = currentExe;
        break;
      case "START":
        start(data);
        break;
    }
  });
};
