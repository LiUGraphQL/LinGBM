DROP TABLE IF EXISTS undergraduateStudent;
CREATE TABLE undergraduateStudent (
  nr integer,
  telephone varchar(100) default NULL,
  emailAddress varchar(100) default NULL,
  age integer,
  advisor integer,
  memberOf integer,
  primary key (nr),
  foreign key (advisor) references professor(nr),
  foreign key (memberOf) references department(nr)
);

INSERT INTO undergraduateStudent VALUES (1,'xxxxxxxx', 'xxxxxxxx', 18, 1, 1), (2,'xxxxxxxx', 'xxxxxxxx', 20, 2, 2),(3,'xxxxxxxx', 'xxxxxxxx', 16, 2, 2),(4,'xxxxxxxx', 'xxxxxxxx', 21, 2, 2);