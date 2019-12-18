DROP TABLE IF EXISTS department;
CREATE TABLE department (
  nr integer primary key,
  subOrganizationOf integer,
  foreign key (subOrganizationOf) references university(nr)
);


INSERT INTO department VALUES (1, 1),(2, 1),(3, 1),(4, 2),(5, 2),(6, 2);