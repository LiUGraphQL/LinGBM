DROP DATABASE IF EXISTS linbenchmark;
CREATE DATABASE linbenchmark;

\c linbenchmark;


DROP TABLE IF EXISTS university;
DROP TABLE IF EXISTS department;
DROP TABLE IF EXISTS researchGroup;
DROP TABLE IF EXISTS faculty;
DROP TABLE IF EXISTS professor;
DROP TABLE IF EXISTS lecturer;
DROP TABLE IF EXISTS graduateStudent;
DROP TABLE IF EXISTS undergraduateCourse;
DROP TABLE IF EXISTS publication;
DROP TABLE IF EXISTS graduateCourse;
DROP TABLE IF EXISTS coAuthorOfPublication;
DROP TABLE IF EXISTS undergraduateStudent;
DROP TABLE IF EXISTS graduateStudentTakeCourse;
DROP TABLE IF EXISTS undergraduateStudentTakeCourse;


CREATE TABLE university (
  nr integer primary key,
  name varchar(25)
);

CREATE TABLE department (
  nr integer,
  name varchar(25),
  subOrganizationOf integer,
  primary key (nr)
);

CREATE TABLE researchGroup (
  nr integer,
  subOrganizationOf integer,
  primary key (nr)
);

CREATE TABLE faculty (
  nr integer,
  name varchar(25),
  telephone varchar(100),
  emailAddress varchar(2000),
  undergraduateDegreeFrom integer,
  masterDegreeFrom integer,
  doctoralDegreeFrom integer,
  worksFor integer,
  primary key (nr)
);

CREATE TABLE professor (
  nr integer,
  professorType varchar(100),
  researchInterest varchar(100),
  headOf integer,
  primary key (nr),
  foreign key (nr) references faculty(nr)
);


CREATE TABLE lecturer (
  nr integer,
  primary key (nr),
  foreign key (nr) references faculty(nr)
);


CREATE TABLE graduateStudent (
  nr integer,
  name varchar(25),
  telephone varchar(100),
  emailAddress varchar(2000),
  age integer,
  undergraduateDegreeFrom integer,
  advisor integer,
  memberOf integer,
  primary key (nr)
);

CREATE TABLE undergraduateCourse (
  nr integer,
  name varchar(25),
  teacher integer,
  teachingAssistant integer,
  primary key (nr)
);


CREATE TABLE publication (
  nr integer,
  name varchar(25),
  title varchar(100),
  abstract varchar(250),
  mainAuthor integer,
  primary key (nr)
);


CREATE TABLE graduateCourse (
  nr integer,
  name varchar(25),
  teacher integer,
  primary key (nr)
);


CREATE TABLE coAuthorOfPublication (
  publicationID integer,
  graduateStudentID integer,
  primary key (publicationID, graduateStudentID),
  foreign key (publicationID) references publication(nr),
  foreign key (graduateStudentID) references graduateStudent(nr)
);


CREATE TABLE undergraduateStudent (
  nr integer,
  name varchar(25),
  telephone varchar(100),
  emailAddress varchar(2000),
  age integer,
  advisor integer,
  memberOf integer,
  primary key (nr)
);


CREATE TABLE graduateStudentTakeCourse (
  graduateStudentID integer,
  graduateCourseID integer,
  primary key (graduateStudentID, graduateCourseID),
  foreign key (graduateCourseID) references graduateCourse(nr),
  foreign key (graduateStudentID) references graduateStudent(nr)
);


CREATE TABLE undergraduateStudentTakeCourse (
  undergraduateStudentID integer,
  undergraduateCourseID integer,
  primary key (undergraduateStudentID, undergraduateCourseID),
  foreign key (undergraduateStudentID) references undergraduateStudent(nr),
  foreign key (undergraduateCourseID) references undergraduateCourse(nr)
);

ALTER TABLE department ADD CONSTRAINT fk_dep_uni FOREIGN KEY (subOrganizationOf) REFERENCES university(nr) ON DELETE SET NULL;

ALTER TABLE researchGroup ADD CONSTRAINT fk_res_dep FOREIGN KEY (subOrganizationOf) REFERENCES department(nr) ON DELETE SET NULL;

ALTER TABLE faculty ADD CONSTRAINT fk_fal_u_uni FOREIGN KEY (undergraduateDegreeFrom) REFERENCES university(nr) ON DELETE SET NULL;
ALTER TABLE faculty ADD CONSTRAINT fk_fal_m_uni FOREIGN KEY (masterDegreeFrom) REFERENCES university(nr) ON DELETE SET NULL;
ALTER TABLE faculty ADD CONSTRAINT fk_fal_d_uni FOREIGN KEY (doctoralDegreeFrom) REFERENCES university(nr) ON DELETE SET NULL;
ALTER TABLE faculty ADD CONSTRAINT fk_fal_dep FOREIGN KEY (worksFor) REFERENCES department(nr) ON DELETE SET NULL;


ALTER TABLE professor ADD CONSTRAINT fk_p_dep FOREIGN KEY (headOf) REFERENCES department(nr) ON DELETE SET NULL;
ALTER TABLE graduateStudent ADD CONSTRAINT fk_gs_uni FOREIGN KEY (undergraduateDegreeFrom) REFERENCES university(nr) ON DELETE SET NULL;
ALTER TABLE graduateStudent ADD CONSTRAINT fk_gs_fac FOREIGN KEY (advisor) REFERENCES professor(nr) ON DELETE SET NULL;
ALTER TABLE graduateStudent ADD CONSTRAINT fk_gs_dep FOREIGN KEY (memberOf) REFERENCES department(nr) ON DELETE SET NULL;

ALTER TABLE undergraduateCourse ADD CONSTRAINT fk_udg_stu_cour_fac FOREIGN KEY (teacher) REFERENCES faculty(nr) ON DELETE SET NULL;
ALTER TABLE undergraduateCourse ADD CONSTRAINT fk_udg_stu_cour_gStu FOREIGN KEY (teachingAssistant) REFERENCES graduateStudent(nr) ON DELETE SET NULL;

ALTER TABLE publication ADD CONSTRAINT fk_pub_aut FOREIGN KEY (mainAuthor) REFERENCES faculty(nr) ON DELETE SET NULL;

ALTER TABLE graduateCourse ADD CONSTRAINT fk_graCour_fac FOREIGN KEY (teacher) REFERENCES faculty(nr) ON DELETE SET NULL;

ALTER TABLE undergraduateStudent ADD CONSTRAINT fk_undergra_Stu_prof FOREIGN KEY (advisor) REFERENCES professor(nr) ON DELETE SET NULL;
ALTER TABLE undergraduateStudent ADD CONSTRAINT fk_undergra_Stu_dep FOREIGN KEY (memberOf) REFERENCES department(nr) ON DELETE SET NULL;
