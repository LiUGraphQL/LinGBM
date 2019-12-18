DROP TABLE IF EXISTS graduateStudentTakeCourse;
CREATE TABLE graduateStudentTakeCourse (
  graduateStudentID integer,
  graduateCourseID integer,
  primary key (graduateStudentID, graduateCourseID),
  foreign key (graduateCourseID) references graduateCourse(nr),
  foreign key (graduateStudentID) references graduateStudent(nr)
);


INSERT INTO graduateStudentTakeCourse VALUES (1, 4), (2, 2),(3, 1),(1, 3);