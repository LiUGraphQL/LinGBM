DROP DATABASE IF EXISTS LinBenchmark;
CREATE DATABASE LinBenchmark;

USE LinBenchmark;

SET FOREIGN_KEY_CHECKS = 0;

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

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE university(
  nr int(11),
  name varchar(25),
  primary key (nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE department (
  nr int(11),
  name varchar(25),
  subOrganizationOf int(11),
  primary key (nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE researchGroup (
  nr int(11),
  subOrganizationOf int(11),
  primary key (nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE faculty (
  nr int(11),
  name varchar(25),
  telephone varchar(100),
  emailAddress varchar(2000),
  undergraduateDegreeFrom int(11),
  masterDegreeFrom int(11),
  doctoralDegreeFrom int(11),
  worksFor int(11),
  primary key (nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE professor (
  nr int(11),
  professorType varchar(100),
  researchInterest varchar(100),
  headOf int(11),
  primary key (nr),
  foreign key (nr) references faculty(nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE lecturer (
  nr int(11),
  primary key (nr),
  foreign key (nr) references faculty(nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE graduateStudent (
  nr int(11),
  name varchar(25),
  telephone varchar(100),
  emailAddress varchar(2000),
  age int(11),
  undergraduateDegreeFrom int(11),
  advisor int(11),
  memberOf int(11),
  primary key (nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE undergraduateCourse (
  nr int(11),
  name varchar(25),
  teacher int(11),
  teachingAssistant int(11),
  primary key (nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE publication (
  nr int(11),
  name varchar(25),
  title varchar(100),
  abstract varchar(250),
  mainAuthor int(11),
  primary key (nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE graduateCourse (
  nr int(11),
  name varchar(25),
  teacher int(11),
  primary key (nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE coAuthorOfPublication (
  publicationID int(11),
  graduateStudentID int(11),
  primary key (publicationID, graduateStudentID),
  foreign key (publicationID) references publication(nr),
  foreign key (graduateStudentID) references graduateStudent(nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE undergraduateStudent (
  nr int(11),
  name varchar(25),
  telephone varchar(100) character set utf8 collate utf8_bin default NULL,
  emailAddress varchar(2000) character set utf8 collate utf8_bin default NULL,
  age int(11),
  advisor int(11),
  memberOf int(11),
  primary key (nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE graduateStudentTakeCourse (
  graduateStudentID int(11),
  graduateCourseID int(11),
  primary key (graduateStudentID, graduateCourseID),
  foreign key (graduateCourseID) references graduateCourse(nr),
  foreign key (graduateStudentID) references graduateStudent(nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE undergraduateStudentTakeCourse (
  undergraduateStudentID int(11),
  undergraduateCourseID int(11),
  primary key (undergraduateStudentID, undergraduateCourseID),
  foreign key (undergraduateStudentID) references undergraduateStudent(nr),
  foreign key (undergraduateCourseID) references undergraduateCourse(nr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


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
