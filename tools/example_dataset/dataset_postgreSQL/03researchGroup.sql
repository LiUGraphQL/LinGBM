DROP TABLE IF EXISTS researchGroup;
CREATE TABLE researchGroup (
  nr integer,
  subOrganizationOf integer,
  primary key (nr),
  foreign key (subOrganizationOf) references department(nr)
);

INSERT INTO researchGroup VALUES (1, 1),(2, 2),(3, 3),(4, 4),(5, 2),(6, 1), (7, 6);