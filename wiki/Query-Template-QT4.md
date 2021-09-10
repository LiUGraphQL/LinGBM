**Template file:** [./artifacts/queryTemplates/main/QT4.txt](https://github.com/LiUGraphQL/LinGBM/blob/master/artifacts/queryTemplates/main/QT4.txt)

### Description
Queries of this template retrieve the details of the graduate student that get bachelor’s degree from the same university as the one that grant the doctor degree to the given lecture, including the apartment of the students’ supervisor.

By requesting several attributes of the intermediate object (i.e., the graduate students), this query template covers chokepoint CP 2.3. Additionally, the template also covers CP 2.2 (because of the traversal from faculty to university and from graduate student to supervisor then to apartment) and CP 2.5 (because different graduate students may be supervised by the same professor and different professors may work for the same department). 

### Choke points covered by this template
* [CP 2.2: Traversal of different 1:1 relationship types](Choke-Points-for-a-GraphQL-Performance-Benchmark#cp-22-efficient-traversal-of-11-relationship-types)<br/>
* [CP 2.3: Relationship traversal with and without retrieval of intermediate object data](Choke-Points-for-a-GraphQL-Performance-Benchmark#cp-23-relationship-traversal-with-and-without-retrieval-of-intermediate-object-data)<br/>
* ( [CP 2.5: Acyclic relationship traversal that visits data objects repeatedly](Choke-Points-for-a-GraphQL-Performance-Benchmark#cp-25-acyclic-relationship-traversal-that-visits-data-objects-repeatedly) )  (only to obtain the country code in the end)<br/>

### Placeholder(s)
**Placeholder:** *$lecturerID*<br/>
**Placeholder type:** ID<br/>
**Placeholder meaning:** *$lecturerID* is the *nr* attribute of some lecturers<br/>
**Possible values of the placeholder:** All the possible lecturer IDs can be used for this query template.
### Number of possible instances
For a scale factor of N, we can generate *(75\~175)\* N* instances of this query template, because per university, there are 15\~20 departments generated, and for each Department, there are 5\~7 lecturers.

|**scale factor**|**number of instances**|
|:------:|:------:|
|1  |93|
|5  |562|
|10 |1128|
|15 |1745|
|20 |2399|
### Number of leaf nodes
Each lecturer gets doctoral degree from a single university, from university to graduate students we have a 1: N relationship where the out-degree is 0\~7 (i.e., there are 0\~7 graduate students get their bachelor’s degree from the given university). Furthermore, every graduate student has a professor as supervisor, and each professor works for a single department. However, different graduate students may have the same advisor and different professors may work for the same department. Hence, the result tree of each query of this template has about *(0\~7)\*N* leaf nodes where some of them may be duplicates of one another.