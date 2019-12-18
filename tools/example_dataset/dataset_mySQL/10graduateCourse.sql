CREATE DATABASE IF NOT EXISTS `LinBenchmark` DEFAULT CHARACTER SET utf8;

USE `LinBenchmark`;

DROP TABLE IF EXISTS `graduateCourse`;
CREATE TABLE `graduateCourse` (
  `nr` int(11),
  `teacher` int(11),
  primary key (nr),
  foreign key (teacher) references faculty(nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `graduateCourse` WRITE;

ALTER TABLE `graduateCourse` DISABLE KEYS;
INSERT INTO `graduateCourse` VALUES (1, 1), (2, 2),(3, 2),(4, 3);
ALTER TABLE `graduateCourse` ENABLE KEYS;

UNLOCK TABLES;