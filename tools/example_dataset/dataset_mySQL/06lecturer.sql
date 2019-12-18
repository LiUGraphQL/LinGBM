CREATE DATABASE IF NOT EXISTS `LinBenchmark` DEFAULT CHARACTER SET utf8;

USE `LinBenchmark`;

DROP TABLE IF EXISTS `lecturer`;
CREATE TABLE `lecturer` (
  `nr` int(11),
  primary key (nr),
  foreign key (nr) references faculty(nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `lecturer` WRITE;

ALTER TABLE `lecturer` DISABLE KEYS;
INSERT INTO `lecturer` VALUES (4);
ALTER TABLE `lecturer` ENABLE KEYS;
UNLOCK TABLES;