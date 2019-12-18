CREATE DATABASE IF NOT EXISTS `LinBenchmark` DEFAULT CHARACTER SET utf8;

USE `LinBenchmark`;

DROP TABLE IF EXISTS `publication`;
CREATE TABLE `publication` (
  `nr` int(11),
  `title` varchar(100) character set utf8 collate utf8_bin default NULL,
  `abstract` varchar(100) character set utf8 collate utf8_bin default NULL,
  `mainAuthor` int(11),
  primary key (nr),
  foreign key (mainAuthor) references faculty(nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `publication` WRITE;

ALTER TABLE `publication` DISABLE KEYS;
INSERT INTO `publication` VALUES (1,'xxxxxxxx', 'xxxxxxxx', 4), (2,'xxxxxxxx', 'xxxxxxxx', 4),(3,'xxxxxxxx', 'xxxxxxxx', 1),(4,'xxxxxxxx', 'xxxxxxxx', 2);
ALTER TABLE `publication` ENABLE KEYS;

UNLOCK TABLES;