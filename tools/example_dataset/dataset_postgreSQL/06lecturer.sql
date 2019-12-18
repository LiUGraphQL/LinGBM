DROP TABLE IF EXISTS lecturer;
CREATE TABLE lecturer (
  nr integer primary key,
  foreign key (nr) references faculty(nr)
);

INSERT INTO lecturer VALUES (4);