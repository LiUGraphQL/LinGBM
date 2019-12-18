CREATE DATABASE IF NOT EXISTS `LinBenchmark` DEFAULT CHARACTER SET utf8;

USE `LinBenchmark`;

DROP TABLE IF EXISTS `undergraduateStudent`;
CREATE TABLE `undergraduateStudent` (
  `nr` int(11),
  `telephone` varchar(100) character set utf8 collate utf8_bin default NULL,
  `emailAddress` varchar(2000) character set utf8 collate utf8_bin default NULL,
  `age` int(11),
  `advisor` int(11),
  `memberOf` int(11),
  primary key (nr),
  foreign key (advisor) references professor(nr),
  foreign key (memberOf) references department(nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `undergraduateStudent` WRITE;

ALTER TABLE `undergraduateStudent` DISABLE KEYS;
INSERT INTO `undergraduateStudent` VALUES (1,'xxxxxxxx', 'xxxxxxxx', 18, 1, 1), (2,'xxxxxxxx', 'xxxxxxxx', 20, 2, 2),(3,'xxxxxxxx', 'xxxxxxxx', 16, 2, 2),(4,'xxxxxxxx', 'xxxxxxxx', 21, 2, 2);
ALTER TABLE `undergraduateStudent` ENABLE KEYS;

UNLOCK TABLES;