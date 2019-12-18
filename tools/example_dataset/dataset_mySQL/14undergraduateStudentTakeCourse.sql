CREATE DATABASE IF NOT EXISTS `LinBenchmark` DEFAULT CHARACTER SET utf8;

USE `LinBenchmark`;

DROP TABLE IF EXISTS `undergraduateStudentTakeCourse`;
CREATE TABLE `undergraduateStudentTakeCourse` (
  `undergraduateStudentID` int(11),
  `undergraduateCourseID` int(11),
  primary key (undergraduateStudentID, undergraduateCourseID),
  foreign key (undergraduateStudentID) references undergraduateStudent(nr),
  foreign key (undergraduateCourseID) references undergraduateCourse(nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `undergraduateStudentTakeCourse` WRITE;

ALTER TABLE `undergraduateStudentTakeCourse` DISABLE KEYS;
INSERT INTO `undergraduateStudentTakeCourse` VALUES (1, 2), (2, 4),(3, 1),(3, 2);
ALTER TABLE `undergraduateStudentTakeCourse` ENABLE KEYS;

UNLOCK TABLES;