import { request } from "graphql-request";
import prettyjson from "prettyjson";
import pgPromise from 'pg-promise';

export default () => {
  console.log(`Worker ${process.pid} started`);

  let db;
  let clear;
  let queries;
  let cursor = 0;
  let workerid;
  let statAfter;

  const pgp = pgPromise({});
  const setupDatabase = pgp('postgres://postgres:postgres@localhost:5432/linbenchmark');

  const errorHandler = ({ query, error }) => {
    console.log(cursor);
    console.log("QUERY:", query);
    console.log(prettyjson.render(error));

    process.send({
      command: "LOGDATA",
      data: {
        index: query.index,
        error: 1
      }
    });
  };
  const delay = ms => new Promise(res => setTimeout(res, ms));
  const databaseRequestTest = async ({ url }) => {
    const runRequest = async (url, query) => {
      try {
        clear = await setupDatabase.query('SELECT pg_stat_reset()');
        await delay(1000);

        await request(url, query.data)
          .catch(console.error)
          .then(console.log);

        await delay(1000);

        statAfter = await setupDatabase.query('SELECT * FROM pg_stat_database WHERE datname= $1',['linbenchmark']);
        const { numbackends: numbackends } = statAfter[0];
        const { tup_returned: afterReturnTuple } = statAfter[0];
        const { tup_fetched: afterFetchTuple } = statAfter[0];
        console.log(JSON.stringify(statAfter))

        // Send data about the sent query up to the master.
        const disnumbackends = parseInt(numbackends)
        const returnTuple = parseInt(afterReturnTuple);
        const fetchTuple = parseInt(afterFetchTuple);
       
        process.send({
          command: "LOGDATA",
          data: {
            index: query.index,
            disnumbackends,
            returnTuple,
            fetchTuple,
            error: 0
          }
        });
      } catch (error) {
        errorHandler({ query, error });
      }
    };

    while (cursor !== queries.length) {
      await runRequest(url, queries[cursor]);
      cursor += 1;
    }
    if (cursor === queries.length) {
      process.exit(0);
    }
  };

  const start = async ({ url, type }) => {
    switch (type) {
      case "dbc":
        databaseRequestTest({ url });
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