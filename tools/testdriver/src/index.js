import cluster from "cluster";
import master from "./master";
import worker from "./worker";
const fs = require('fs');
const path = require('path');


if (cluster.isMaster) {
  master();
  if (!fs.existsSync("output")) fs.mkdirSync("output");
  const fsExtra = require('fs-extra')
  fsExtra.emptyDirSync("output")
} else {
  worker();
}