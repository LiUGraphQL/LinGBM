CREATE DATABASE IF NOT EXISTS `LinBenchmark` DEFAULT CHARACTER SET utf8;

USE `LinBenchmark`;

DROP TABLE IF EXISTS `professor`;
CREATE TABLE `professor` (
  `nr` int(11),
  `professorType` varchar(100) character set utf8 collate utf8_bin default NULL,
  `researchInterest` varchar(100) character set utf8 collate utf8_bin default NULL,
  `headOf` int(11) default NULL,
  primary key (nr),
  foreign key (nr) references faculty(nr),
  foreign key (headOf) references department(nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `professor` WRITE;

ALTER TABLE `professor` DISABLE KEYS;
INSERT INTO `professor` VALUES (1, 'fullProfessor', 'xxxxxxxx', 1), (2, 'associateProfessor', 'xxxxxxxx', NULL),(3, 'assistantProfessor', 'xxxxxxxx',NULL);
ALTER TABLE `professor` ENABLE KEYS;

UNLOCK TABLES;