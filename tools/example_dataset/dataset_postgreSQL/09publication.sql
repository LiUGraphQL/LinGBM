DROP TABLE IF EXISTS publication;
CREATE TABLE publication (
  nr integer,
  title varchar(100) default NULL,
  abstract varchar(100) default NULL,
  mainAuthor integer,
  primary key (nr),
  foreign key (mainAuthor) references faculty(nr)
);

INSERT INTO publication VALUES (1,'xxxxxxxx', 'xxxxxxxx', 4), (2,'xxxxxxxx', 'xxxxxxxx', 4),(3,'xxxxxxxx', 'xxxxxxxx', 1),(4,'xxxxxxxx', 'xxxxxxxx', 2);