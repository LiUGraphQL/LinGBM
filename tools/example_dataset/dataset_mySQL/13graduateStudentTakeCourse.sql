CREATE DATABASE IF NOT EXISTS `LinBenchmark` DEFAULT CHARACTER SET utf8;

USE `LinBenchmark`;

DROP TABLE IF EXISTS `graduateStudentTakeCourse`;
CREATE TABLE `graduateStudentTakeCourse` (
  `graduateStudentID` int(11),
  `graduateCourseID` int(11),
  primary key (graduateStudentID, graduateCourseID),
  foreign key (graduateCourseID) references graduateCourse(nr),
  foreign key (graduateStudentID) references graduateStudent(nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `graduateStudentTakeCourse` WRITE;

ALTER TABLE `graduateStudentTakeCourse` DISABLE KEYS;
INSERT INTO `graduateStudentTakeCourse` VALUES (1, 4), (2, 2),(3, 1),(1, 3);
ALTER TABLE `graduateStudentTakeCourse` ENABLE KEYS;

UNLOCK TABLES;