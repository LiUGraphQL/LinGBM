import { request } from "graphql-request";
import prettyjson from "prettyjson";
import mysql from "mysql2/promise";
import bluebird from "bluebird";

export default () => {
  console.log(`Worker ${process.pid} started`);

  let db;
  let queries;
  let cursor = 0;

  const setupDatabase = async () => {
    console.log("setting up database");
    const db = await mysql.createConnection({
      host: "localhost",
      user: "test",
      password: "pass",
      database: "benchmark",
      Promise: bluebird
    });
    return db;
  };

  const errorHandler = ({ query, error }) => {
    console.log("ERROR - EXITING");
    console.log(cursor);
    console.log("QUERY:", query);
    console.log(prettyjson.render(error));
    process.exit(1);

    // Send up error
    process.send({
      command: "LOGDATA",
      data: {
        index: query.index,
        error: 1
      }
    });
  };

  const databaseRequestTest = async ({ url }) => {
    db = await setupDatabase();
    const runRequest = async (url, query) => {
      try {
        const [rows] = await db.query("SHOW GLOBAL STATUS LIKE 'com_select'");
        const { Value: preQuery } = rows[0];
        await request(url, query.data);
        const [rowsAfter] = await db.query(
          "SHOW GLOBAL STATUS LIKE 'com_select'"
        );
        const { Value: afterQuery } = rowsAfter[0];
        // Send data about the sent query up to the master.
        const dbRequests = parseInt(afterQuery) - parseInt(preQuery);
        process.send({
          command: "LOGDATA",
          data: {
            index: query.index,
            dbRequests,
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

  const throughputTest = async ({ url }) => {
    const runRequest = async (url, query) => {
      try {
        await request(url, query.data);
        process.send({
          command: "LOGDATA",
          data: {
            index: query.index,
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

  process.on("message", ({ command, data, slice }) => {
    switch (command) {
      case "ECHO":
        process.send({ command: "ECHO", data });
        break;
      case "QUERIES":
        queries = data;
        cursor = slice;
        break;
      case "START":
        start(data);
        break;
    }
  });
};
