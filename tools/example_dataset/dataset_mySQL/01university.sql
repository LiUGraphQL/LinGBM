CREATE DATABASE IF NOT EXISTS `LinBenchmark` DEFAULT CHARACTER SET utf8;

USE `LinBenchmark`;

DROP TABLE IF EXISTS `university`;
CREATE TABLE `university` (
  `nr` int(11),
  primary key (nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `university` WRITE;

ALTER TABLE `university` DISABLE KEYS;

INSERT INTO `university` VALUES (1),(2);

ALTER TABLE `university` ENABLE KEYS;

UNLOCK TABLES;