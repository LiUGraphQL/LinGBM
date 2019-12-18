CREATE DATABASE IF NOT EXISTS `LinBenchmark` DEFAULT CHARACTER SET utf8;

USE `LinBenchmark`;

DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `nr` int(11),
  `subOrganizationOf` int(11),
  primary key (nr),
  foreign key (subOrganizationOf) references university(nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `department` WRITE;

ALTER TABLE `department` DISABLE KEYS;

INSERT INTO `department` VALUES (1, 1),(2, 1),(3, 1),(4, 2),(5, 2),(6, 2);

ALTER TABLE `department` ENABLE KEYS;

UNLOCK TABLES;