DROP TABLE IF EXISTS faculty;
CREATE TABLE faculty (
  nr integer primary key,
  telephone varchar(100) default NULL,
  emailAddress varchar(100) default NULL,
  undergraduateDegreeFrom integer default NULL,
  masterDegreeFrom integer default NULL,
  doctoralDegreeFrom integer default NULL,
  worksFor integer,
  foreign key (undergraduateDegreeFrom) references university(nr),
  foreign key (masterDegreeFrom) references university(nr),
  foreign key (doctoralDegreeFrom) references university(nr),
  foreign key (worksFor) references department(nr)
);

INSERT INTO faculty VALUES (1, 'xxxxxxxx', 'xxxxxxxx', 1, 1, 1, 1), (2,'xxxxxxxx', 'xxxxxxxx', 1, 2, 2, 2),(3,'xxxxxxxx', 'xxxxxxxx', 1, 2, 2, 2),(4,'xxxxxxxx', 'xxxxxxxx', 1, 2, 2, 2);