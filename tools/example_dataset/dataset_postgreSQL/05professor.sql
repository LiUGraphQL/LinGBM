DROP TABLE IF EXISTS professor;
CREATE TABLE professor (
  nr integer primary key,
  professorType varchar(100) default NULL,
  researchInterest varchar(100) default NULL,
  headOf integer default NULL,
  foreign key (nr) references faculty(nr),
  foreign key (headOf) references department(nr)
);

INSERT INTO professor VALUES (1, 'fullProfessor', 'xxxxxxxx', 1), (2, 'associateProfessor', 'xxxxxxxx', NULL),(3, 'assistantProfessor', 'xxxxxxxx',NULL);