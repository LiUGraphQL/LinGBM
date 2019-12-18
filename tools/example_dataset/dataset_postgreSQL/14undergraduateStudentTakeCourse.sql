DROP TABLE IF EXISTS undergraduateStudentTakeCourse;
CREATE TABLE undergraduateStudentTakeCourse (
  undergraduateStudentID integer,
  undergraduateCourseID integer,
  primary key (undergraduateStudentID, undergraduateCourseID),
  foreign key (undergraduateStudentID) references undergraduateStudent(nr),
  foreign key (undergraduateCourseID) references undergraduateCourse(nr)
);

INSERT INTO undergraduateStudentTakeCourse VALUES (1, 2), (2, 4),(3, 1),(3, 2);