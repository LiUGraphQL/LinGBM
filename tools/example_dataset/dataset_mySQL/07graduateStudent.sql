CREATE DATABASE IF NOT EXISTS `LinBenchmark` DEFAULT CHARACTER SET utf8;

USE `LinBenchmark`;

DROP TABLE IF EXISTS `graduateStudent`;
CREATE TABLE `graduateStudent` (
  `nr` int(11),
  `telephone` varchar(100) character set utf8 collate utf8_bin default NULL,
  `emailAddress` varchar(2000) character set utf8 collate utf8_bin default NULL,
  `age` int(11),
  `undergraduateDegreeFrom` int(11),
  `advisor` int(11),
  `memberOf` int(11),
  primary key (nr),
  foreign key (undergraduateDegreeFrom) references university(nr),
  foreign key (advisor) references professor(nr),
  foreign key (memberOf) references department(nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `graduateStudent` WRITE;

ALTER TABLE `graduateStudent` DISABLE KEYS;
INSERT INTO `graduateStudent` VALUES (1,'xxxxxxxx', 'xxxxxxxx', 18, 1, 1, 2), (2,'xxxxxxxx', 'xxxxxxxx', 20, 2, 2, 1),(3,'xxxxxxxx', 'xxxxxxxx', 16, 2, 2, 1),(4,'xxxxxxxx', 'xxxxxxxx', 21, 2, 2, 4);
ALTER TABLE `graduateStudent` ENABLE KEYS;

UNLOCK TABLES;