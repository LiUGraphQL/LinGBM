DROP TABLE IF EXISTS graduateCourse;
CREATE TABLE graduateCourse (
  nr integer,
  teacher integer,
  primary key (nr),
  foreign key (teacher) references faculty(nr)
);

INSERT INTO graduateCourse VALUES (1, 1), (2, 2),(3, 2),(4, 3);