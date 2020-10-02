import { request } from "graphql-request";
import prettyjson from "prettyjson";
import pg from "pg";

export default () => {
  console.log(`Worker ${process.pid} started`);

  let db;
  let clear;
  let queries;
  let cursor = 0;
  let workerid;
  let statAfter;

  const pool = new pg.Pool({
    host: "localhost",
    port: 5432,
    user: "postgres",
    password: "postgres",
    database: "linbenchmark"
  })

  async function setupDatabase(){
    const client = await pool.connect();
    return client;
  }

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
    //db = await setupDatabase();
    db = await setupDatabase();
    const runRequest = async (url, query) => {
      try {
        clear = await db.query('SELECT pg_stat_reset()');
        await delay(1000);

        await request(url, query.data)
          .catch(console.error)
          .then(console.log);

        await delay(1000);

        statAfter = await db.query('SELECT * FROM pg_stat_database WHERE datname= $1',['linbenchmark']);
        const { numbackends: numbackends } = statAfter.rows[0];
        //const { xact_commit: xact_commit } = statAfter.rows[0];
        //const { xact_rollback: xact_rollback } = statAfter.rows[0];
        //const { blks_read: afterdiskRead } = statAfter.rows[0];
        //const { blks_hit: aftercacheHit } = statAfter.rows[0];
        const { tup_returned: afterReturnTuple } = statAfter.rows[0];
        const { tup_fetched: afterFetchTuple } = statAfter.rows[0];
       // const { blk_read_time: blk_read_time } = statAfter.rows[0];
        console.log(JSON.stringify(statAfter.rows))

        // Send data about the sent query up to the master.
        const disnumbackends = parseInt(numbackends)
        //const disxact_commit = parseInt(xact_commit)
        //const disxact_rollback = parseInt(xact_rollback)
        //const diskRead = parseInt(afterdiskRead);
        //const cacheHit = parseInt(aftercacheHit);
        const returnTuple = parseInt(afterReturnTuple);
        const fetchTuple = parseInt(afterFetchTuple);
        //const disblk_read_time = parseInt(blk_read_time);
       
        process.send({
          command: "LOGDATA",
          data: {
            index: query.index,
            disnumbackends,
            //disxact_commit,
            //disxact_rollback,
            //diskRead,
            //cacheHit,
            returnTuple,
            fetchTuple,
            //disblk_read_time,
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