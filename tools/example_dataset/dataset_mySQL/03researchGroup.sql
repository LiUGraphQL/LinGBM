CREATE DATABASE IF NOT EXISTS `LinBenchmark` DEFAULT CHARACTER SET utf8;

USE `LinBenchmark`;

DROP TABLE IF EXISTS `researchGroup`;
CREATE TABLE `researchGroup` (
  `nr` int(11),
  `subOrganizationOf` int(11),
  primary key (nr),
  foreign key (subOrganizationOf) references department(nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `researchGroup` WRITE;

ALTER TABLE `researchGroup` DISABLE KEYS;
INSERT INTO `researchGroup` VALUES (1, 1),(2, 2),(3, 3),(4, 4),(5, 2),(6, 1), (7, 6);
ALTER TABLE `researchGroup` ENABLE KEYS;

UNLOCK TABLES;