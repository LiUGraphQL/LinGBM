import cluster from "cluster";
import master from "./master";
import worker from "./worker";
const fs = require('fs');
const path = require('path');


if (cluster.isMaster) {
  master();
} else {
  worker();
}