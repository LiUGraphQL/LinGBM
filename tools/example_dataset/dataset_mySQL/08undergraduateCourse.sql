CREATE DATABASE IF NOT EXISTS `LinBenchmark` DEFAULT CHARACTER SET utf8;

USE `LinBenchmark`;

DROP TABLE IF EXISTS `undergraduateCourse`;
CREATE TABLE `undergraduateCourse` (
  `nr` int(11),
  `teacher` int(11),
  `teachingAssistant` int(11),
  primary key (nr),
  foreign key (teacher) references faculty(nr),
  foreign key (teacheingAssistant) references graduateStudent(nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `undergraduateCourse` WRITE;

ALTER TABLE `undergraduateCourse` DISABLE KEYS;
INSERT INTO `undergraduateCourse` VALUES (1, 2, 1), (2, 1, 3),(3, 3, 4),(4, 2, 2);
ALTER TABLE `undergraduateCourse` ENABLE KEYS;

UNLOCK TABLES;