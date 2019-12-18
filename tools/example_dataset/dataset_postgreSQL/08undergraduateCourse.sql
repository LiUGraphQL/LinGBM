DROP TABLE IF EXISTS undergraduateCourse;
CREATE TABLE undergraduateCourse (
  nr integer,
  teacher integer,
  teachingAssistant integer,
  primary key (nr),
  foreign key (teacher) references faculty(nr),
  foreign key (teachingAssistant) references graduateStudent(nr)
);

INSERT INTO undergraduateCourse VALUES (1, 2, 1), (2, 1, 3),(3, 3, 4),(4, 2, 2);