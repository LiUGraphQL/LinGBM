This document describes the data used by the Linköping GraphQL Benchmark (LinGBM).

LinGBM is based on a scalable synthetic dataset that can be generated in an unlimited number of different sizes. Instead of designing a new dataset generator from scratch, LinGBM uses the dataset generator of the [the Lehigh University Benchmark (LUBM)][1]. However, for LinGBM it was necessary to develop a [slightly extended version of this dataset generator](https://github.com/LiUGraphQL/LinGBM/tree/master/tools/datasetgen).

The generated data can be created in the form of an SQL database or an RDF graph. While the RDF versions can be written in several RDF serialization formats, the SQL-database versions are written as an SQL dump file that can be imported by a MySQL server.

In the remainder of this document we provide:
* an [Entity-Relationship diagram](#entity-relationship-diagram) that models the scenario captured by the benchmark datasets,
* the corresponding [relational schema](#relational-schema) of the SQL-database version of the benchmark datasets, and
* an [overview of the average cardinalities](#cardinalities-of-relationships) of the relationships in the generated data.

For more details regarding the datasets and the dataset generator we refer to the [LUBM DATA PROFILE](http://swat.cse.lehigh.edu/projects/lubm/profile.htm).

## Entity-Relationship Diagram [To be reviewed]
The generated benchmark datasets capture a fictitious University scenario with the department that has different types of faculties and students, who teach/take courses and have some publications. Moreover, for each university, there are many graduates (including an undergraduate degree, master's degree, and doctoral degree). The faculty who get his/her degree from one university could work for the department in the same or another university. The graduate student that is studying in one University could got his/her undergraduate degree from the same or another university. Overall, the captured scenario consists off 12 entities and 18 types of relationships between such entities. The following Entity-Relationship diagram illustrates these entity types and relationship types.

![](https://raw.githubusercontent.com/wiki/LiUGraphQL/LinGBM/ER_lubm.png)


## Relational Schema

The relational schema of the SQL-database version of the benchmark datasets consists of the following 14 relations/tables (where the underlined attributes belong to the primary key of the corresponding relation).

**University** (<u>nr</u>)<br/>
**Department** (<u>nr</u>, subOrganizationOf)<br/>
**ResearchGroup** (<u>nr</u>, subOrganizationOf)<br/>
**Faculty** (<u>nr</u>, telephone, emailAddress, undergraduateDegreeFrom, masterDegreeFrom, doctoralDegreeFrom, worksFor)<br/>
**Professor** (<u>nr</u>, professorType, researchInterest, headOf)<br/>
**Lecturer** (<u>nr</u>)<br/>
**GraduateStudent** (<u>nr</u>, telephone, emailAddress, age, undergraduateDegreeFrom, advisor, memberOf)<br/>
**UndergraduateCourse** (<u>nr</u>, teacher, teachingAssistant)<br/>
**Publication** (<u>nr</u>, title, abstract, mainAuthor)<br/>
**GraduateCourse** (<u>nr</u>, teacher)<br/>
**UndergraduateStudent** (<u>nr</u>, telephone, emailAddress, age, advisor, memberOf)<br/>
**CoAuthorOfPublication** (<u>publicationID</u>, <u>graduateStudentID</u>)<br/>
**GraduateStudentTakeCourse** (<u>graduateStudentID</u>, <u>graduateCourseID</u>)<br/>
**UndergraduateStudentTakeCourse** (<u>undergraduateStudentID</u>, <u>undergraduateCourseID</u>)<br/>

The following diagram provides a visual illustration of the relational schema, including the referential integrity constraints (foreign keys) between the tables.
![](https://raw.githubusercontent.com/wiki/LiUGraphQL/LinGBM/RM_lubm.png)

## Cardinalities of Relationships
The following table provides an overview of the average cardinalities of the relationships in the generated data.

|Code| RelationName | Relationship | Cardinalities |
|------|------|------|------|
|R1|Bachelor's DegreeFrom|University:Faculty|1:(0~4)*N|
|R2|MasterDegreeFrom|University:Faculty|1:(0~4)*N|
|R3|DoctorDegreeFrom|University:Faculty|1:(0~5)*N|
|R4|UndergraduateDegreeFrom|University:GraduateStudents|1:(0~7)*N|
|R5|SubOrganizationOf|University:Department|1:15~25|
|R6|SubOrganizationOf|Department:ResearchGroup|1:10~20|
|R7|worksFor|Department:FullProfessor|1:7~10|
|R8|worksFor|Department:AssociateProfessor|1:10~14|
|R9|worksFor|Department:AssistantProfessor|1:8~11|
|R10|worksFor|Department:Lecturers|1:5~7|
|R11|worksFor|Department:Faculty|1:30~42|
|R12|memberOf|Department:UndergraduateStudent|1:240~588|
|R13|memberOf|Department:GraduateStudent|1:90~168|
|R14|memberOf|Department:Student|1:330~756|
|R15|headOf|Department:FullProfessor|0~1:1|
|R16|takeCourse|GraduateStudent:GraduateCourse|0\~14:1~3|
|R17|takeCourse|UndergraduateStudent:Courese|7\~38:2~4|
|R18|assist|GraduateStudent:Course|0\~1:0~1|
|R19|advisor|GraduateStudent:Professor|0~11:1|
|R20|advisor|UndergraduateStudent:Professor|0\~10:0~1|
|R21|publicationAuthor|GraduateStudent:Publication|0\~6:0~5|
|R22|publicationAuthor|FullProfessor:Publication|0\~1:15~20|
|R23|publicationAuthor|AssociateProfessor:Publication|0\~1:10~18|
|R24|publicationAuthor|AssistantProfessor:Publication|0\~1:5~10|
|R25|publicationAuthor|Lecturer:Publication|0\~1:0~5|
|R26|publicationAuthor|Faculty:Publication|1:0~20|
|R27|publicationAuthor|Author:Publication|1\~7:0~20|
|R28|teacherOf|Faculty:GraduateCourse|1:1~2|
|R29|teacherOf|Faculty:Course|1:1~2|


  [1]: http://swat.cse.lehigh.edu/projects/lubm/index.htm
  [2]: http://wifo5-03.informatik.uni-mannheim.de/bizer/berlinsparqlbenchmark/spec/Dataset/index.html#triplenamedgraph
  [3]: http://wifo5-03.informatik.uni-mannheim.de/bizer/berlinsparqlbenchmark/spec/BenchmarkRules/index.html#datagenerator