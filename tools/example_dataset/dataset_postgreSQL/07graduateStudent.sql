CREATE DATABASE IF NOT EXISTS LinBenchmark DEFAULT CHARACTER SET utf8;

USE LinBenchmark;

DROP TABLE IF EXISTS graduateStudent;
CREATE TABLE graduateStudent (
  nr integer primary key,
  telephone varchar(100) default NULL,
  emailAddress varchar(100) default NULL,
  age integer,
  undergraduateDegreeFrom integer,
  advisor integer,
  memberOf integer,
  foreign key (undergraduateDegreeFrom) references university(nr),
  foreign key (advisor) references professor(nr),
  foreign key (memberOf) references department(nr)
);

INSERT INTO graduateStudent VALUES (1,'xxxxxxxx', 'xxxxxxxx', 18, 1, 1, 2), (2,'xxxxxxxx', 'xxxxxxxx', 20, 2, 2, 1),(3,'xxxxxxxx', 'xxxxxxxx', 16, 2, 2, 1),(4,'xxxxxxxx', 'xxxxxxxx', 21, 2, 2, 4);