DROP TABLE IF EXISTS coAuthorOfPublication;
CREATE TABLE coAuthorOfPublication (
  publicationID integer,
  graduateStudentID integer,
  primary key (publicationID, graduateStudentID),
  foreign key (publicationID) references publication(nr),
  foreign key (graduateStudentID) references graduateStudent(nr)
);

INSERT INTO coAuthorOfPublication VALUES (1, 4), (2, 2),(3, 1),(3, 3);
