CREATE DATABASE IF NOT EXISTS `LinBenchmark` DEFAULT CHARACTER SET utf8;

USE `LinBenchmark`;

DROP TABLE IF EXISTS `faculty`;
CREATE TABLE `faculty` (
  `nr` int(11),
  `telephone` varchar(100) character set utf8 collate utf8_bin default NULL,
  `emailAddress` varchar(2000) character set utf8 collate utf8_bin default NULL,
  `undergraduateDegreeFrom` int(11) default NULL,
  `masterDegreeFrom` int(11) default NULL,
  `doctoralDegreeFrom` int(11) default NULL,
  `worksFor` int(11),
  primary key (nr),
  foreign key (undergraduateDegreeFrom) references university(nr),
  foreign key (masterDegreeFrom) references university(nr),
  foreign key (doctoralDegreeFrom) references university(nr),
  foreign key (worksFor) references department(nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `faculty` WRITE;

ALTER TABLE `faculty` DISABLE KEYS;
INSERT INTO `faculty` VALUES (1, 'xxxxxxxx', 'xxxxxxxx', 1, 1, 1, 1), (2,'xxxxxxxx', 'xxxxxxxx', 1, 2, 2, 2),(3,'xxxxxxxx', 'xxxxxxxx', 1, 2, 2, 2),(4,'xxxxxxxx', 'xxxxxxxx', 1, 2, 2, 2);
ALTER TABLE `faculty` ENABLE KEYS;

UNLOCK TABLES;