CREATE DATABASE IF NOT EXISTS `LinBenchmark` DEFAULT CHARACTER SET utf8;

USE `LinBenchmark`;

DROP TABLE IF EXISTS `coAuthorOfPublication`;
CREATE TABLE `coAuthorOfPublication` (
  `publicationID` int(11),
  `graduateStudentID` int(11),
  primary key (publicationID, graduateStudentID),
  foreign key (publicationID) references publication(nr),
  foreign key (graduateStudentID) references graduateStudent(nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `coAuthorOfPublication` WRITE;

ALTER TABLE `coAuthorOfPublication` DISABLE KEYS;
INSERT INTO `coAuthorOfPublication` VALUES (1, 4), (2, 2),(3, 1),(3, 3);
ALTER TABLE `coAuthorOfPublication` ENABLE KEYS;

UNLOCK TABLES;