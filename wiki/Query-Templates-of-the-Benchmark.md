#### Table of Contents
* [Overview of Choke Point Coverage by the Query Templates](#overview-of-choke-point-coverage-by-the-query-templates)
* [QT1](#qt1)
* [QT2](#qt2)
* [QT3](#qt3)
* [QT4](#qt4)
* [QT5](#qt5)
* [QT6](#qt6)
* [QT7](#qt7)
* [QT8](#qt8)
* [QT9](#qt9)
* [QT10](#qt10)
* [QT11](#qt11)
* [QT12](#qt12)
* [QT13](#qt13)
* [QT14](#qt14)
* [QT15](#qt15)
* [QT16](#qt16)
***

## Overview of Choke Point Coverage by the Query Templates
|           |[1.1][1]|[2.1][2]|[2.2][3]|[2.3][4]|[2.4][5]|[2.5][6]|[3.1][7]|[3.2][8]|[3.3][9]|[4.1][10]|[4.2][11]|[4.3][12]|[4.4][13]|[4.5][14]|[5.1][15]|[5.2][16]|
|------     |:------:|:------:|:------:|:------:|:------:|:------:|:------:|:------:|:------:|:-------:|:-------:|:-------:|:-------:|:-------:|:-------:|:-------:|
|[QT1](#qt1)| X      | x      | x      |        |        |        |        |        |        |         |         |         |         |         |         |         |
|[QT2](#qt2)|       | X      |       |        |        |        |        |        |        |         |         |         |         |         |         |         |
|[QT3](#qt3)| X     |       |  X     | X       |        |        |        |        |        |         |         |         |         |         |         |         |
|[QT4](#qt4)|       |       |  X     | X      |        | x       |        |        |        |         |         |         |         |         |         |         |
|[QT5](#qt5)|       | x      | x      |  X      |  X      |        |        |        |        |         |         |         |         |         |         |         |
|[QT6](#qt6)|       | x      | X     |        |        |  X      |        |        |        |         |         |         |         |         |         |         |
|[QT7](#qt7)| X      |       |       | X     |        | x       |        | X       |        |         |         |         |         |         |         |         |
|[QT8](#qt8)| X      |       |       |        |        |        | X       |        | X       |         |         |         |         |         |         |         |
|[QT9](#qt9)| X      | x     | x      |        |        | X       | X       |        | X       |         |         |         |         |         |         |         |
|[QT10](#qt10)|X       |       |       |        |        |        |        |        |        | X        |         |         |         |         |         |         |
|[QT11](#qt11)| X      |       |       |        |        | X       |        |        |        |         |         |         | X        |         |         |         |
|[QT12](#qt12)|X       |X       |       |        |        |        |        |        |        |         |         |X         |         |         |         |         |
|[QT13](#qt13)|X       |X       |       |        |        |        |        |        |        |         |X        | X        |         |         |         |         |
|[QT14](#qt14)|X       |X       |       |        |        |        |        |        |        |X         |X         |X         |         |X         |         |         |
|[QT15](#qt15)|       |       |       |        |        |        |        |        |        |         |         |         |         |         |         |X         |
|[QT16](#qt16)|       |       |       |        |        |        |        |        |        |         |         |         |         |         |X         |         |

[1]: Choke-Points#cp-1-1

[2]: Choke-Points#cp-2-1
[3]: Choke-Points#cp-2-2
[4]: Choke-Points#cp-2-3
[5]: Choke-Points#cp-2-4
[6]: Choke-Points#cp-2-5

[7]: Choke-Points#cp-3-1
[8]: Choke-Points#cp-3-2
[9]: Choke-Points#cp-3-3

[10]: Choke-Points#cp-4-1
[11]: Choke-Points#cp-4-2
[12]: Choke-Points#cp-4-3
[13]: Choke-Points#cp-4-4
[14]: Choke-Points#cp-4-5

[15]: Choke-Points#cp-5-1
[16]: Choke-Points#cp-5-2

***

## QT1

**Template file:** [./artifacts/queryTemplates/main/QT1.txt](https://github.com/LiUGraphQL/LinGBM/blob/master/artifacts/queryTemplates/main/QT1.txt)

**Description:** <br/>
Queries of this template retrieve several attributes of the graduate student that get bachelor’s degree from the university that grant the doctor degree to the given faculty. 

By requesting several attributes of the leaf nodes of the result (i.e., the graduateStudent) this query template covers chokepoint CP 1.1. Additionally, to a lesser degree, the template also covers CP 2.2 (because of the traversal from university to its graduate students) and CP 2.1 (because of the traversal from a faculty to the university that he/she gets doctoral degree from) 

**Choke points covered by this template:** 
* [CP 1.1: Multi-attribute retrieval](Choke-Points#cp-11-multi-attribute-retrieval)<br/>
* ( [CP 2.1 Traversal of different 1:N relationship types](Choke-Points#cp-21-traversal-of-different-1n-relationship-types) )  (only one 1:N, not multiple different ones)<br/>
* ( [CP 2.2: Efficient traversal of 1:1 relationship types](Choke-Points#cp-22-efficient-traversal-of-11-relationship-types) )  (only one N:1, not multiple different ones)<br/>

**Placeholder:** *$facultyID*<br/>
**Placeholder type:**: ID<br/>
**Placeholder meaning:**: *$facultyID* is the *nr* attribute of some faculties<br/>
**Possible values of the placeholder:** All the possible faculty IDs can be used for this query template. <br/>
**Number of possible instances:** For a scale factor of *N*, we can generate *(450\~1050)\*N* instances of this query template, because per university, there are 15~25 departments generated, and for each department, there are 30~42 faculties.<br/>
**Number of leaf nodes:**<br/>
Every faculty gets doctoral degree from a single university, and each university grant bachelor’s degree to *(0\~7)\*N * graduate students. Hence, the result tree of each query of this template has (0\~7)*N leaf node that are all different from one another.

## QT2. 

**Template file:** [./artifacts/queryTemplates/main/QT2.txt](https://github.com/LiUGraphQL/LinGBM/blob/master/artifacts/queryTemplates/main/QT2.txt)

**Description:**<br/>
Queries of this template retrieve all the publications about all faculties that get doctoral degree from a given university. 

Due to the traversal along a sequence of 1: N relationships, this template covers chokepoint CP 2.1.<br/>

**Choke points covered by this template:**
* [CP 2.1 (Traversal of different 1: N relationship types)](Choke-Points#cp-21-traversal-of-different-1n-relationship-types)<br/>

**Placeholder:** *$universityID*<br/>
**Placeholder type:** ID<br/>
**Placeholder meaning:** *$universityID* is the *nr* attribute of some universities<br/>
**Possible values for the placeholder:** All the possible university IDs can be used for this query template.<br/>
**Number of possible instances:** We can generate about 1000 instances of this query template<br/>
**Number of leaf nodes:** These queries traverse along a sequence of two 1:N relationships. The first one (from the given university to the faculty that get doctoral degree from the university) has an out-degree range of *(0\~4)\* N*, and the second one (from the faculty to their publications) has an out-degree range of 0\~20. Consequently, the result tree of such a query has about *(0\~80)\*N* leaf nodes. 

## QT3

**Template file:** [./artifacts/queryTemplates/main/QT3.txt](https://github.com/LiUGraphQL/LinGBM/blob/master/artifacts/queryTemplates/main/QT3.txt)

**Description:** <br/>
Given a research group that belongs to a department, queries of this template retrieve the University that granted the doctoral degree to the head of this department. 

Due to the traversal along a sequence of N: 1: 1: 1, queries of this template cover chokepoint CP 2.2. By requesting several attributes of some internal nodes of the result tree (namely, the head type nodes) these queries cover chokepoints CP 2.3.<br/>

**Choke points covered by this template:**
* [CP 2.2 Traversal of different 1:1 relationship types](Choke-Points#cp-22-efficient-traversal-of-11-relationship-types)<br/>
* [CP 2.3: Relationship traversal with and without retrieval of intermediate object data](Choke-Points#cp-23-relationship-traversal-with-and-without-retrieval-of-intermediate-object-data)<br/>

**Placeholder:** *$researchGroupID*<br/>
**Placeholder type:** ID<br/>
**Placeholder meaning:** *$researchGroupID* is the *nr* attribute of some researchGroups<br/>
**Possible values for the placeholder:** All the possible researchGroup IDs can be used for this query template.<br/>
**Number of possible instances:** For a scale factor of N, we can generate *(150\~ 500)\*N* instances of this query template, because per university, there are 15\~25 departments generated, and for each Department, there are 1\~20 research groups.<br/>
**Number of leaf nodes:**<br/>
These queries traverse along a sequence of N: 1: 1: 1, because every research group is a suborganization of one department, and every department has one full professor as head, which, in turn, got doctor degree from one university. Hence, given a research group, the result tree of such a query has only a single leaf node. 

## QT4

**Template file:** [./artifacts/queryTemplates/main/QT4.txt](https://github.com/LiUGraphQL/LinGBM/blob/master/artifacts/queryTemplates/main/QT4.txt)

**Description:** 
Queries of this template retrieve the details of the graduate student that get bachelor’s degree from the same university as the one that grant the doctor degree to the given lecture, including the apartment of the students’ supervisor.

By requesting several attributes of the intermediate object (i.e., the graduate students), this query template covers chokepoint CP 2.3. Additionally, the template also covers CP 2.2 (because of the traversal from faculty to university and from graduate student to supervisor then to apartment) and CP 2.5 (because different graduate students may be supervised by the same professor and different professors may work for the same department). 

**Choke points covered by this template:**
* [CP 2.2: Traversal of different 1:1 relationship types](Choke-Points#cp-22-efficient-traversal-of-11-relationship-types)<br/>
* [CP 2.3: Relationship traversal with and without retrieval of intermediate object data](Choke-Points#cp-23-relationship-traversal-with-and-without-retrieval-of-intermediate-object-data)<br/>
* ( [CP 2.5: Acyclic relationship traversal that visits data objects repeatedly](Choke-Points#cp-25-acyclic-relationship-traversal-that-visits-data-objects-repeatedly) )  (only to obtain the country code in the end)<br/>

**Placeholder:** *$lecturerID*<br/>
**Placeholder type:** ID<br/>
**Placeholder meaning:** *$lecturerID* is the *nr* attribute of some lecturers<br/>
**Possible values of the placeholder:** All the possible lecturer IDs can be used for this query template. <br/>
**Number of possible instances:**  For a scale factor of N, we can generate *(75\~175)\* N* instances of this query template, because per university, there are 15\~20 departments generated, and for each Department, there are 5\~7 lecturers.<br/>
**Number of leaf nodes:**<br/>
Each lecturer gets doctoral degree from a single university, from university to graduate students we have a 1: N relationship where the out-degree is 0\~7 (i.e., there are 0\~7 graduate students get their bachelor’s degree from the given university). Furthermore, every graduate student has a professor as supervisor, and each professor works for a single department. However, different graduate students may have the same advisor and different professors may work for the same department. Hence, the result tree of each query of this template has about *(0\~7)\*N* leaf nodes where some of them may be duplicates of one another.  

## QT5

**Template file:** [./artifacts/queryTemplates/main/QT5.txt](https://github.com/LiUGraphQL/LinGBM/blob/master/artifacts/queryTemplates/main/QT5.txt)

**Description:**<br/>
Queries of this template go from a given department to its university, then retrieve all graduate students who get the bachelor's degree from the university, then come back to department. Each query repeats this cycle two times and requests the students’ email addresses along the way. 
 
This query template is a typical example of queries the traverse relationships in cycles, coming back to the same nodes multiple times. Hence, this query templates covers chokepoint CP 2.4. Additionally, by requesting attributes of the intermediate object (i.e., the graduate students), this query template covers chokepoint CP 2.3. Furthermore, the template also covers CP 2.1 (because of the traversal from a university to graduate students) and CP2.2 (because of the traversal from department to university, from graduate student to the department).  
 
**Choke points covered by this template:**
* ( [CP 2.1: Traversal of different 1: N relationship types](Choke-Points#cp-21-traversal-of-different-1n-relationship-types) )  (only one 1: N, not multiple different ones)<br/>
* ( [CP 2.2: Traversal of different 1:1 relationship types](Choke-Points#cp-22-efficient-traversal-of-11-relationship-types) )  (only one N:1, not multiple different ones)<br/>
* [CP 2.3: Relationship traversal with and without retrieval of intermediate object data](Choke-Points#cp-23-relationship-traversal-with-and-without-retrieval-of-intermediate-object-data)<br/>
* [CP 2.4: Traversal of relationships that form cycles](Choke-Points#cp-24-traversal-of-relationships-that-form-cycles)<br/>

**Placeholder:** *$departmentID*<br/>
**Placeholder type:** ID<br/>
**Placeholder meaning:** *$departmentID* is the *nr* attribute of some departments<br/>
**Possible values for the placeholder:** All the possible department IDs can be used for this query template. <br/>
**Number of possible instances:** For a scale factor of N, we can generate *(15\~25)\* N* instances of this query template, because per university, there are 15\~20 departments generated.<br/>
**Number of leaf nodes:**<br/>
These queries traverse along two N: 1 relationship and one 1: N relationship. Every department is suborganization of a single university, each university grant bachelor’s degree to (0\~7)\*N graduate students. Furthermore, each graduate student belongs to a department. Hence, each query of this template may repeatedly visit the same department. Thus, the query result trees consist of (0\~49) \*N^2 leaf nodes that may contains the same department, and the trees contain many duplicate internal nodes (not only within each level of depth but also across levels). 

## QT6

**Template file:** [./artifacts/queryTemplates/main/QT6.txt](https://github.com/LiUGraphQL/LinGBM/blob/master/artifacts/queryTemplates/main/QT6.txt)

**Description:**<br/>
Queries of this template retrieve all graduate students that graduates from a given university, and then retrieve the professors that supervise these students and the department’s head of these professors.  

This template presents a typical example of queries that do not traverse in cycles and yet visit nodes multiple times. Hence, the chokepoint covered primarily by this template is CP 2.5. Additionally, the template covers CP 2.2 because of the sequence of N:1 relationships that the queries traverse (I.e., from a graduate student to the supervisor, from the supervisor to the department). 

**Choke points covered by this template:**
* [CP 2.2: Traversal of different 1:1 relationship types](Choke-Points#cp-22-efficient-traversal-of-11-relationship-types)<br/>
* [CP 2.5: Acyclic relationship traversal that visits data objects repeatedly](Choke-Points#cp-25-acyclic-relationship-traversal-that-visits-data-objects-repeatedly)<br/>

**Placeholder:** *$universityID*<br/>
**Placeholder type:** ID<br/>
**Placeholder meaning:** *$universityID* is the *nr* attribute of some uinversities<br/>
**Possible values for the placeholder:** All the possible university IDs can be used for this query template.<br/>
**Number of possible instances:** We can generate about 1000 instances of this query template <br/>
**Number of leaf nodes:**<br/>
From university to graduate students we have a 1: N relationship where the out-degree is 0\~7 (i.e., there are 0\~7 graduate students get their bachelor’s degree from the given university). Furthermore, every graduate student has a professor as supervisor, and each professor works for a single department. However, different graduate students may have the same advisor and different professors may work for the same department. Hence, the result tree of each query of this template has about (0\~7)\*N leaf nodes where some of them may be duplicates of one another. 


## QT7

**Template file:** [./artifacts/queryTemplates/main/QT7.txt](https://github.com/LiUGraphQL/LinGBM/blob/master/artifacts/queryTemplates/main/QT7.txt)

**Description:**<br/>
Queries of this template retrieve data about 10 graduate students where these students must be the first 10 at a given offset within the list of all graduate students that graduate from the given university. 

The chokepoint covered primarily by this template is CP 3.2. Additionally, the template covers chokepoints CP 2.2 (because the traversal from graduate student to advisor) and, to a lesser degree, CP 2.5 (because different graduate students may have the same advisor; however, chances are small that some of the selected 10 students are the same advisor). 

**Choke points covered by this template:**
* [CP 2.2: Traversal of different 1:1 relationship types](Choke-Points#cp-22-efficient-traversal-of-11-relationship-types)<br/>
* ( [CP 2.5: Acyclic relationship traversal that visits data objects repeatedly](Choke-Points#cp-25-acyclic-relationship-traversal-that-visits-data-objects-repeatedly) )  (only to obtain the product label in the end; chances are little that we end up at the same product multiple times)<br/>
* [CP 3.2: Paging with offset](Choke-Points#cp-32-paging-with-offset)<br/>

**Placeholders:** *$universityID, $offset* <br/>
**Placeholder types:** ID, Int <br/>
**Placeholder meaning:** *$universityID* is the nr attribute of some universities, and *$offset* specifies the offset <br/>
**Possible values for the placeholders:** All the possible university IDs can be used for this query template, the value of $offset is selected by random from the interval [1, 50].<br/>
**Number of possible instances:** We can generate about 1000 instances of this query template <br/>
**Number of leaf nodes:**<br/>
From a university to its graduate students, we have a 1: N relationship with the out-degree of (0\~7)\*N. In addition, every graduate student had a professor as advisor, but different students may have the same advisor. Hence, the result tree of each query of this template has about 10 leaf nodes that may be duplicates of one another. 

## QT8

**Template file:** [./artifacts/queryTemplates/main/QT8.txt](https://github.com/LiUGraphQL/LinGBM/blob/master/artifacts/queryTemplates/main/QT8.txt)

**Description:**<br/>
Queries of this template retrieve data about a given number of graduate students that are the first in the list of all graduate students sorted by a given ordering criterion. 

The main purpose of this query template is to cover chokepoints CP 3.1 and CP 3.3.  

**Choke points covered by this template:** 
* [CP 3.1: Paging without offset](Choke-Points#cp-31-paging-without-offset)<br/>
* [CP 3.3: Ordering](Choke-Points#cp-33-ordering)<br/>

**Placeholders:** *$cnt, $attrOffer1, $attrOffer2* <br/>
**Placeholder types:** Int, graduateStudentField, graduateStudentField <br/>
**Placeholder meaning:** *$cnt* is the number of offers to be retrieved, *$attrOffer1* specifies the main sort order, and *$attrOffer2* specifies the secondary sort order <br/>
**Possible values for the placeholders:** The value of *$cnt* is selected by random from the interval [500, 1000]; the value of *$attrOffer1* is selected by random from all fileds of graduateStudentField, and the same holds for *$attrOffer2*.<br/>
**Number of possible instances:** The number of instances for this query template is 500*3*3 <br/>
**Number of leaf nodes:**<br/>
The result tree of each query of this template has 500 to 100 different leaf nodes. 

## QT9

**Template file:** [./artifacts/queryTemplates/main/QT9.txt](https://github.com/LiUGraphQL/LinGBM/blob/master/artifacts/queryTemplates/main/QT9.txt)

**Description:**<br/>
Queries of this template retrieve data about the publications for the advisor in the first 50 graduate students by a given university. For each such advisor, the publication has to be sorted on a given field. Hence, in contrast to the previous query template, which is about sorting a whole set of things (graduate students in this specific case), this query template requires sorting in separate subsets (namely, sets of publications). 

This query template covers chokepoint CP 3.1 (because of adding paging without offset on graduate students) and chokepoint CP 3.3 (because of adding ordering on the leaf nodes of the result (i.e., the publications)). In addition, this query covers CP 2.1(because of traversal from a university to its graduate students, and from advisor to their publication) and CP2.2 (because of traversal from the graduate students to their advisor). 

**Choke points covered by this template:**
* ( [CP2.1: Traversal of different 1: N relationship types](Choke-Points#cp-21-traversal-of-different-1n-relationship-types) )  (only one 1:N, not multiple different ones)<br/>
* ( [CP 2.2: Traversal of different 1:1 relationship types](Choke-Points#cp-22-efficient-traversal-of-11-relationship-types) )  (only one N:1, not multiple different ones)<br/>
* [CP 3.1: Paging without offset](Choke-Points#cp-31-paging-without-offset)<br/>
* [CP 3.3: Ordering](Choke-Points#cp-33-ordering)<br/>

**Placeholders:** *$universityID* <br/>
**Placeholder types:** ID <br/>
**Placeholder meaning:** *$universityID* is the nr attribute of some universities <br/>
**Possible values for the placeholders:** All the possible university IDs can be used for this query template. <br/>
**Number of possible instances:** We can generate about 1000 instances of this query template <br/>
**Number of leaf nodes:**<br/>
Queries of this template traverse along a sequence of a 1: N relationship (from a university to its graduate students), an N:1 relationship (the graduate students to their advisor). and another 1: N relationship (from advisor to their publications). That is, from a university to its graduate students we have a 1: N relationship, which has an out-degree of (0\~7) \*N. In addition, every graduate student has a professor as advisor, there would be (0\~7) \*N advisors (with same professor multiple times), and every professor has 5\~20 publications. Hence, the result tree of each query of this template has about (0\~140)\*N leaf nodes that some publications may repeat multiple times. 

## QT10

**Template file:** [./artifacts/queryTemplates/main/QT10.txt](https://github.com/LiUGraphQL/LinGBM/blob/master/artifacts/queryTemplates/main/QT10.txt)

**Description:**<br/>
Queries of this template retrieve all publications that the title contains the given keyword. 

Primarily, this query template covers chokepoint CP 4.1. Additionally, by requesting several attributes of the leaf nodes of the resulting reviews, this query template also covers chokepoint CP 1.1. 
 
**Choke points covered by this template:**
* [CP 1.1 Multi-attribute retrieval](Choke-Points#cp-11-multi-attribute-retrieval)<br/>
* [CP 4.1 String matching](Choke-Points#cp-41-string-matching)<br/>

**Placeholder:** *$keyword*<br/>
**Placeholder type:** String <br/>
**Placeholder meaning:** *$keyword* is a string which is contained in the title of publications<br/>
**Possible values for the placeholder:** any word that appears in the titles <br/>
**Number of possible instances:** We can generate about XXX(depend on the number of words that is contained in the title of publications) instances of this query template.<br/>
**Number of leaf nodes:**<br/>
Titles of different publications may contain common substrings. Hence, the result of each query of this template consists of number of different publications, where this number depends on the selected search string. In general, this number may range from zero (none of the publication contains the given search string) to the number of all publications in the benchmark dataset (which is (0\~6300) \*N). 

## QT11

**Template file:** [./artifacts/queryTemplates/main/QT11.txt](https://github.com/LiUGraphQL/LinGBM/blob/master/artifacts/queryTemplates/main/QT11.txt)

**Description:**<br/>
Queries of this template search for all graduate students of that graduates from a given university by using a search condition (instead of starting the traversal from the given university as done in Q6). Then, for each graduate student, the advisor is requested. 

This template is another example of queries that do not traverse in cycles and yet visit nodes multiple times. Hence, the template covers chokepoint CP 2.5. Besides this property, the primary focus of this query is chokepoint CP 4.4 (Subquery-based search).  

**Choke points covered by this template:**
* [CP 2.5 Acyclic relationship traversal that visits data objects repeatedly](Choke-Points#cp-25-acyclic-relationship-traversal-that-visits-data-objects-repeatedly)<br/>
* [CP 4.4 Subquery-based search](Choke-Points#cp-44-subquery-based-search)<br/>

**Placeholder:** *$universityID*<br/>
**Placeholder type:** ID <br/>
**Placeholder meaning:** *$universityID* is the nr attribute of some universities <br/>
**Possible values for the placeholder:** All the possible university IDs can be used for this query template.<br/>
**Number of possible instances:** We can generate about 1000 instances <br/>
**Number of leaf nodes:**<br/>
From a university to its graduate students we have a 1: N relationship, which has an out-degree of (0\~7)\*N. Thereafter, every graduate student has a single advisor. Hence, the result trees of these queries have about (0\~7)\*N leaf nodes. However, multiple different graduate students may share the same advisor. Hence, there may be duplicates among the leaf nodes. 

## QT12

**Template file:** [./artifacts/queryTemplates/main/QT12.txt](https://github.com/LiUGraphQL/LinGBM/blob/master/artifacts/queryTemplates/main/QT12.txt)

**Description:**<br/>
Queries of this template retrieve faculties that get doctoral degree from a given university. These faculties are filtered based on a given department ID.  

Primarily, this query template covers chokepoint CP 4.3 (because it uses a subquery to filter the retrieved graduate students). Moreover, by traversing along two 1: N relationships, this query template covers CP 2.1. 

**Choke points covered by this template:**
* [CP 2.1 Traversal of different 1:N relationship types](Choke-Points#cp-21-traversal-of-different-1n-relationship-types)<br/>
* [CP 4.3 Subquery-based filtering](Choke-Points#cp-43-subquery-based-filtering)<br/>

**Placeholders:** *$universityID, $departmentID*<br/>
**Placeholder types:** ID, ID <br/>
**Placeholder meaning:** *$universityID* is the nr attribute of some universities, and *$departmentID* is the nr attribute of some departments<br/>
**Possible values for the placeholders:** All the possible university IDs can be used for this query template; All the possible departments IDs can be used for this query template.<br/>
**Number of possible instances:**  For a scale factor of N, we can generate *1000\*(15\~25)\*N* instances of this query template, because per university, there are 15\~25 departments generated. 
**Number of leaf nodes:**<br/>
These queries traverse along two 1: N relationships. The first one (from the given university to its faculty) has an out-degree of (0\~5)\*N, and the second one (from the faculty to the publications) has an out-degree of 0\~20. Consequently, there are about (0\~100)\*N leaf nodes totally for a given university. By filtering the retrieved graduate students based on the subquery with the department ID, the number of faculties could be less than 42, and each faculty have 0\~20 publications. Therefore, the number of leaf nodes of the result tree could be less than 840. 

## QT13

**Template file:** [./artifacts/queryTemplates/main/QT13.txt](https://github.com/LiUGraphQL/LinGBM/blob/master/artifacts/queryTemplates/main/QT13.txt)

**Description:**<br/>
Queries of this template retrieve data about graduate courses for graduate students that graduated from a given university. These graduate students are filtered based on a string associated with the advisor of the graduate students. By using a string matching, the filter condition in this query template is more complex than the one in the previous template. 

Primarily, this query template covers chokepoints CP 4.3 (because it uses a subquery to filter the retrieved graduate students) and CP 4.1 (because the filter condition considers string matching). Additionally, for the same reasons as for the previous template, this template also covers chokepoints CP 2.1. 

**Choke points covered by this template:**
* [CP 2.1 Traversal of different 1:N relationship types](Choke-Points#cp-21-traversal-of-different-1n-relationship-types)<br/>
* [CP 4.2 Date matching](Choke-Points#cp-42-date-matching)<br/>
* [CP 4.3 Subquery-based filtering](Choke-Points#cp-43-subquery-based-filtering)<br/>

**Placeholders:** *$universityID, $interestWord*<br/>
**Placeholder types:** ID, String<br/>
**Placeholder meaning:**  *$universityID* is the nr attributes of some universities, and *$interestWord * is the word which is contained in the research interest of professor<br/>
**Possible values for the placeholders:** All the possible university IDs can be used for this query template<br/>
**Number of possible instances:** For a scale factor of N, we can generate 1000* X (X depends on the number of words that is contained in the research interest of the professor) instances of this query template.<br/>
**Number of leaf nodes:**<br/>
The characteristics of query results for this template are the same as for the previous template. 

## QT14

**Template file:** [./artifacts/queryTemplates/main/QT14.txt](https://github.com/LiUGraphQL/LinGBM/blob/master/artifacts/queryTemplates/main/QT14.txt)

**Description:**<br/>
Queries of this template retrieve graduate students that graduated from a given university and then retrieve graduate courses that these graduate student takes. Then the queries filter these graduate students by conjunctions of multiple conditions in a subquery. 

This query template covers CP 2.1 because of traversing along two 1: N relationships. Additionally, the template also covers CP 4.1, CP 4.3 and CP 4.5 (because of using a conjunction of multiple conditions as a subquery to filter the retrieved result and containing string matching in the conditions). 
 
**Choke points covered by this template:**
* [CP 2.1 Traversal of different 1: N relationship types](Choke-Points#cp-21-traversal-of-different-1n-relationship-types)<br/>
* [CP 4.1 String matching](Choke-Points#cp-41-string-matching)<br/>
* [CP 4.3 Subquery-based filtering](Choke-Points#cp-43-subquery-based-filtering)<br/>
* [CP 4.5 Multiple filter conditions](Choke-Points#cp-45-multiple-filter-conditions)<br/>

**Placeholders:** *$universityID; $age; $interestKeyword*<br/>
**Placeholder types:** ID; Int; String<br/>
**Placeholder meaning:** *$universityID* is  the nr attribute of some universities; *$age* is the age of professors; *$interestKeyword* is the string which is contained in the research interest of professors <br/>
**Possible values for the placeholders:** All the possible department IDs can be used for this query template; the value of $age can be selected from the interval [28, 65]; all word that is contained in the research interest of professors. <br/>
**Number of possible instances:** We can generate are 1000* 37* X instances of this query template, where X depends on the number of words that is contained in the research interest of professors. <br/>
**Number of leaf nodes:**<br/>
These queries traverse along two 1: N relationships. The first one (from the given university to its graduate students) has an out-degree of (0\~7)\*N, and the second one (from the graduate student to the graduate courses) has an out-degree of 1\~3. Consequently, there are about (0\~21)\*N leaf nodes totally for a given department. Because the retrieved graduate students are filtered by a subquery with conjunctions of multiple conditions, the number of leaf nodes of the result tree is decreased very much, which may be the same from one another. 


## QT15

**Template file:** [./artifacts/queryTemplates/main/QT15.txt](https://github.com/LiUGraphQL/LinGBM/blob/master/artifacts/queryTemplates/main/QT15.txt)

**Description:**<br/>
Queries of this template return the number of graduate students that got bachelor’s degree from a given university.

This query template covers CP 5.2 (because of retrieve count of graudate students). 

**Choke points covered by this template:**
* [CP 5.2 Counting](Choke-Points#cp-52-counting)<br/>

**Placeholder:** *$universityID* <br/>
**Placeholder type:** ID<br/>
**Placeholder meaning:** *$universityID* is  the nr attribute of some universities<br/>
**Possible values for the placeholder:** All the possible university IDs can be used for this query template. <br/>
**Number of possible instances:** We can generate about 1000 instances of this query template <br/>
**Number of leaf nodes:**<br/>
These queries traverse a 1: N relationship from a university to their graduate students, which has an out-degree of (0\~7)\*N. Therefore, the result tree of such a query has 1 leaf node, the value of which between 0 to 7N. 


## QT16

**Template file:** [./artifacts/queryTemplates/main/QT16.txt](https://github.com/LiUGraphQL/LinGBM/blob/master/artifacts/queryTemplates/main/QT16.txt)

**Description:**<br/>
Queries of this template retrieve some aggregation values of age of undergraduate students that graduated from a given department.

This query template covers CP 5.1 (because of retrieve count, sum, avg, max and min of publication numbers). 
 
**Choke points covered by this template:**
* [CP 5.1 Calculation-based aggregation](Choke-Points#cp-51-calculation-based-aggregation)<br/>

**Placeholder:** *$universityID*<br/>
**Placeholder type:** ID<br/>
**Placeholder meaning:** *$universityID* is  the nr attribute of some universities<br/>
**Possible values for the placeholder:** All the possible universityIDs can be used for this query template.<br/>
**Number of possible instances:** We can generate about 1000 instances of this query template <br/>
**Number of leaf nodes:**<br/>
These queries traverse a 1: N relationship from a university to its undergraduate students, which has an out-degree of (0\~7)\*N. Therefore, the result tree of such a query has 1 leaf node, the value of which between 18 to 24. 
