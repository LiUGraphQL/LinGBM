This document provides an informal definition of how the elements of the [LinGBM GraphQL schema](LinGBM-GraphQL-Schema) map to the [database schema of the benchmark dataset](Datasets#relational-schema).

When designing the LinGBM GraphQL schema, we first have created an initial sketch of this schema that closely resembles the database schema. Thereafter, we have extended this initial version with additional fields and types that we needed to define [the query workloads of the benchmark](Query-Workloads-of-the-Benchmark).

In the following we first outline the general approach adopted to create the initial sketch of the LinGBM GraphQL schema. Thereafter, we define the mapping in detail by focusing on the object types of the schema, one after another.

***
#### Table of Contents
1. [**General Idea**](#general-idea)
2. [**The Mapping in Detail**](#the-mapping-in-detail)
  * [University](#mapping-of-university-objects)
  * [Department](#mapping-of-department-objects)
  * [ResearchGroup](#mapping-of-researchGroup-objects)
  * [Faculty](#mapping-of-faculty-objects)
  * [Professor](#mapping-of-professor-objects)
  * [Lecturer](#mapping-of-lecturer-objects)
  * [GraduateStudent](#mapping-of-graduateStudent-objects)
  * [UndergraduateCourse](#mapping-of-undergraduateCourse-objects)
  * [Publication](#mapping-of-publication-objects)
  * [GraduateCourse](#mapping-of-graduateCourse-objects)
  * [UndergraduateStudent](#mapping-of-undergraduateStudent-objects)
***

## General Idea
The general approach adopted to create the initial sketch of the LinGBM GraphQL schema is based on the following four rules.
1. For every table in the database schema (except for the tables CoAuthorOfPublication,  GraduateStudentTakeCourse and UndergraduateStudentTakeCourse), create an object type in the GraphQL schema; the name of this object type is the same as the name of the table.
2. For every attribute in these tables, create a field in the corresponding object type; the name of this field is the same as the name of the attribute and the type of the field resembles the domain (datatype) of the attribute.
3. For an attribute that is a foreign key into some other table, the type of the corresponding field has to be the object type for the table referenced by the foreign key. Additionally, that object type (for the table referenced by the foreign key) is extended with another field to follow the foreign key in the reverse direction (i.e., back to the object type for the table that contains the foreign key); the type of this field is a list type that wraps the object type for the table containing the foreign key.
4. The many-to-many relationship tables CoAuthorOfPublication GraduateStudentTakeCourse and UndergraduateStudentTakeCourse are captured in a similar manner by adding cross-reference fields in the corresponding object types.


## The Mapping in Detail

### Mapping of _University_ Objects
#### Relevant part of the GraphQL schema:
```graphql
type University  
{ 
  id: ID! 
  undergraduateDegreeFromObtainedByFaculty: [Faculty] 
  mastergraduateDegreeObtainers: [Faculty]
  doctoralDegreeObtainers: [Faculty]
  undergraduateDegreeFromObtainedBystudent: [GraduateStudent]
  departments: [Department] 
} 

```
#### Relevant parts of the relational schema:
**university** (<u>nr</u>)<br>
**faculty** (<u>nr</u>, telephone, emailAddress, undergraduateDegreeFrom, masterDegreeFrom, doctoralDegreeFrom, worksFor)<br>
**graduateStudent** (<u>nr</u>, telephone, emailAddress, age, undergraduateDegreeFrom, advisor, memberOf)<br>
**department** (<u>nr</u>, subOrganizationOf)<br>

#### Mapping:

The object type *University* in the GraphQL schema corresponds to the table *university* in the relational model. That is, for every row in the *university* table, there exists an object of type *University*. For this object, the values of its fields are determined as follows:

* The value of the field ***id*** is the value of the attribute *nr* that the corresponding row has in the table *university*;<br>

* The value of the ***undergraduateDegreeFromObtainedByFaculty*** field is an array containing each *Faculty* object ([created as described for the ***Faculty*** type](#mapping-of-faculty-objects)) whose corresponding row in the ***faculty*** table has the *nr* attribute of this University as its 'undergraduateDegreeFrom' attribute value.<br>
* The value of the ***mastergraduateDegreeObtainers*** field is an array containing each *Faculty* object ([created as described for the ***Faculty*** type](#mapping-of-faculty-objects)) whose corresponding row in the ***faculty*** table has the *nr* attribute of this University as its 'masterDegreeFrom' attribute value.<br>
* The value of the ***doctoralDegreeObtainers*** field is an array containing each *Faculty* object ([created as described for the ***Faculty*** type](#mapping-of-faculty-objects)) whose corresponding row in the ***faculty*** table has the *nr* attribute of this University as its 'doctoralDegreeFrom' attribute value.<br>
* The value of the ***undergraduateDegreeFromObtainedBystudent*** field is an array containing each *GraduateStudent* object ([created as described for the ***GraduateStudent*** type](#mapping-of-graduateStudent-objects)) whose corresponding row in the ***graduateStudent*** table has the *nr* attribute of this University as its 'undergraduateDegreeFrom' attribute value.<br>
* The value of the ***departments*** field is an array containing each *Department* object ([created as described for the ***Department*** type](#mapping-of-department-objects)) whose corresponding row in the ***department*** table has the *nr* attribute of this University as its 'subOrganizationOf' attribute value.<br>


### Mapping of _Faculty_ Objects
#### Relevant part of the GraphQL schema:
```graphql
interface Faculty  
{ 
  id: ID! 
  telephone: String 
  emailAddress: String 
  undergraduteDegreeFrom: University 
  masterDegreeFrom: University 
  doctoralDegreeFrom: University 
  worksFor: Department 
  teacherOfGraduateCourses: [GraduateCourse] 
  teacherOfUndergraduateCourses: [UndergraduateCourse] 
  publications: [Publication] 
} 
```
#### Relevant parts of the relational schema:
**faculty** (<u>nr</u>, telephone, emailAddress, undergraduateDegreeFrom, masterDegreeFrom, doctoralDegreeFrom, worksFor)<br>
**university** (<u>nr</u>)<br>
**department** (<u>nr</u>, subOrganizationOf)<br>
**undergraduateCourse** (<u>nr</u>, teacher, teachingAssistant)<br>
**graduateCourse** (<u>nr</u>, teacher)<br>
**publication** (<u>nr</u>, title, abstract, mainAuthor)<br>

#### Mapping:

The interface *Faculty* in the GraphQL schema corresponds to the table *Faculty* in the relational model. That is, for every row in the *Faculty* table, there exists a *Faculty*. For this object, the values of its fields are determined as follows:

* The value of the field ***id*** is the value of the attribute *nr* that the corresponding row has in the table *faculty*;<br>
* The value of the field ***telephone*** is the value of the attribute *telephone* that the corresponding row has in the table *faculty*;<br>
* The value of the field ***emailAddress*** is the value of the attribute *emailAddress* that the corresponding row has in the table *faculty*;<br>


* The value of the field ***undergraduteDegreeFrom*** is the value of the *University* object ([created as described for the ***University*** type](#mapping-of-university-objects)) whose corresponding row in the *university* table has the *nr* attribute, whose value is the same as the 'undergraduteDegreeFrom' attribute value of this *Faculty*.<br>
* The value of the field ***masterDegreeFrom*** is the value of the *University* object ([created as described for the ***University*** type](#mapping-of-university-objects)) whose corresponding row in the *university* table has the *nr* attribute, whose value is the same as the 'masterDegreeFrom' attribute value of this *Faculty*.<br>
* The value of the field ***doctoralDegreeFrom*** is the value of the *University* object ([created as described for the ***University*** type](#mapping-of-university-objects)) whose corresponding row in the *university* table has the *nr* attribute, whose value is the same as the 'doctoralDegreeFrom' attribute value of this *Faculty*.<br>
* The value of the field ***worksFor*** is the value of the *Department* object ([created as described for the ***Department*** type](#mapping-of-department-objects)) whose corresponding row in the *department* table has the *nr* attribute, whose value is the same as the 'worksFor' attribute value of this *Faculty*.<br>

* The value of the ***teacherOfGraduateCourses*** field is an array containing each *GraduateCourse* object ([created as described for the ***GraduateCourse*** type](#mapping-of-graduateCourse-objects)) whose corresponding row in the ***graduateCourse*** table has the *nr* attribute of this University as its 'teacher' attribute value.<br>
* The value of the ***teacherOfUndergraduateCourses*** field is an array containing each *UndergraduateCourse* object ([created as described for the ***UndergraduateCourse*** type](#mapping-of-undergraduateCourse-objects)) whose corresponding row in the ***undergraduateCourse*** table has the *nr* attribute of this University as its 'teacher' attribute value.<br>
* The value of the ***publications*** field is an array containing each *Publication* object ([created as described for the ***Publication*** type](#mapping-of-publication-objects)) whose corresponding row in the ***publication*** table has the *nr* attribute of this University as its 'mainAuthor' attribute value.<br>

### Mapping of _Department_ Objects
#### Relevant part of the GraphQL schema:
```graphql
type Department  
{ 
  id: ID! 
  subOrganizationOf: University 
  head: Professor 
  researchGroups: [ResearchGroup] 
  faculties: [Faculty] 
  professors: [Professor]
  lecturers: [Lecturer]
  graduateStudents: [GraduateStudent] 
  undergraduateStudents: [UndergraduateStudent] 
}
```
#### Relevant parts of the relational schema:
**department** (<u>nr</u>, subOrganizationOf)<br>
**university** (<u>nr</u>)<br>
**researchGroup** (<u>nr</u>, subOrganizationOf)<br>
**faculty** (<u>nr</u>, telephone, emailAddress, undergraduateDegreeFrom, masterDegreeFrom, doctoralDegreeFrom, worksFor)<br>
**professor** (<u>nr</u>, professorType, researchInterest, headOf)<br>
**lecturer** (<u>nr</u>)<br>
**graduateStudent** (<u>nr</u>, telephone, emailAddress, age, undergraduateDegreeFrom, advisor, memberOf)<br>
**undergraduateStudent** (<u>nr</u>, telephone, emailAddress, age, advisor, memberOf)<br>

#### Mapping:

The object type *Department* in the GraphQL schema corresponds to the table *department* in the relational model. That is, for every row in the *department* table, there exists an object of type *Department*. For this object, the values of its fields are determined as follows:

* The value of the field ***id*** is the value of the attribute *nr* that the corresponding row has in the table *department*;<br>

* The value of the field ***subOrganizationOf*** is the value of the *University* object ([created as described for the ***University*** type](#mapping-of-university-objects)) whose corresponding row in the *university* table has the *nr* attribute, whose values is the same as the 'subOrganizationOf' attribute value of this *Department*.<br>
* The value of the field ***head*** is the value of the *Professor* object ([created as described for the ***University*** type](#mapping-of-professor-objects)) whose corresponding row in the *professor* table has the *headOf* attribute, whose values is the same as the 'nr' attribute value of this *Department*.<br>

* The value of the ***researchGroups*** field is an array containing each *ResearchGroup* object ([created as described for the ***ResearchGroup*** type](#mapping-of-researchGroup-objects)) whose corresponding row in the *researchGroup* table has the *nr* attribute of this Department as its 'subOrganizationOf' attribute values.<br>
* The value of the ***faculties*** field is an array containing each *Faculty* object ([created as described for the ***Faculty*** type](#mapping-of-faculty-objects)) whose corresponding row in the *faculty* table has the *nr* attribute of this Department as its 'worksFor' attribute values.<br>
* The value of the ***professors*** field is an array containing each *Professor* object ([created as described for the ***Professor*** type](#mapping-of-professor-objects)) whose corresponding row in the *professor* table has the same *nr* attribute value as corresponding *faculty* table, where the corresponding *faculty* has the *nr* attribute of this Department as its 'worksFor' attribute values.<br>
* The value of the ***lecturers*** field is an array containing each *Lecturer* object ([created as described for the ***Lecturer*** type](#mapping-of-lecturer-objects)) whose corresponding row in the *lecturer* table has the same *nr* attribute value as corresponding *faculty* table, where the corresponding *faculty* has the *nr* attribute of this Department as its 'worksFor' attribute values.<br>
* The value of the ***graduateStudents*** field is an array containing each *GraduateStudent* object ([created as described for the ***GraduateStudent*** type](#mapping-of-graduateStudent-objects)) whose corresponding row in the *graduateStudent* table has the *nr* attribute of this Department as its 'memberOf' attribute values.<br>
* The value of the ***undergraduateStudents*** field is an array containing each *UndergraduateStudent* object ([created as described for the ***UndergraduateStudent*** type](#mapping-of-undergraduateStudent-objects)) whose corresponding row in the *undergraduateStudent* table has the *nr* attribute of this Department as its 'memberOf' attribute values.<br>

### Mapping of _ResearchGroup_ Objects
#### Relevant part of the GraphQL schema:
```graphql
type ResearchGroup  
{ 
  id: ID! 
  subOrgnizationOf: Department 
} 
```
#### Relevant parts of the relational schema:
**researchGroup** (<u>nr</u>, subOrganizationOf)<br>
**department** (<u>nr</u>, subOrganizationOf)<br>

#### Mapping:

The object type *ResearchGroup* in the GraphQL schema corresponds to the table *researchGroup* in the relational model. That is, for every row in the *researchGroup* table, there exists an object of type *ResearchGroup*. For this object, the values of its fields are determined as follows:

* The value of the field ***id*** is the value of the attribute *nr* that the corresponding row has in the table *researchGroup*;<br>

* The value of the field ***subOrganizationOf*** is the value of the *Department* object ([created as described for the ***Department*** type](#mapping-of-department-objects)) whose corresponding row in the *department* table has the *nr* attribute, whose values is the same as the 'subOrganizationOf' attribute value of this *ResearchGroup*.<br>

### Mapping of _Author_ Objects
#### Relevant part of the GraphQL schema:
```graphql
interface Author  
{ 
  id: ID! 
  telephone: String 
  emailAddress: String
} 
```
#### Relevant parts of the relational schema:
**faculty** (<u>nr</u>, telephone, emailAddress, undergraduateDegreeFrom, masterDegreeFrom, doctoralDegreeFrom, worksFor)<br>
**graduateStudent** (<u>nr</u>, telephone, emailAddress, age, undergraduateDegreeFrom, advisor, memberOf)<br>
**publication** (<u>nr</u>, title, abstract, mainAuthor)<br>

#### Mapping:
<!--- TODO --->

-----------
### Mapping of _Professor_ Objects
#### Relevant part of the GraphQL schema: 
```graphql
type Professor implements Faculty & Author
{ 
  id: ID! 
  telephone: String 
  emailAddress: String 
  researchInterest: String 
  profType: String
  undergraduateDegreeFrom: University 
  masterDegreeFrom: University 
  doctoralDegreeFrom: University 
  worksFor: Department 
  teacherOfGraduateCourses: [GraduateCourse] 
  teacherOfUndergraduateCourses: [UndergraduateCourse]
  publications(order: PublicationSortCriterion): [Publication]
  supervisedUndergraduateStudents: [UndergraduateStudent] 
  supervisedGraduateStudents: [GraduateStudent]
} 
```
#### Relevant parts of the relational schema:
**professor** (<u>nr</u>, professorType, researchInterest, headOf)<br>
**faculty** (<u>nr</u>, telephone, emailAddress, undergraduateDegreeFrom, masterDegreeFrom, doctoralDegreeFrom, worksFor)<br>
**university** (<u>nr</u>)<br>
**department** (<u>nr</u>, subOrganizationOf)<br>
**undergraduateCourse** (<u>nr</u>, teacher, teachingAssistant)<br>
**graduateCourse** (<u>nr</u>, teacher)<br>
**graduateStudent** (<u>nr</u>, telephone, emailAddress, age, undergraduateDegreeFrom, advisor, memberOf)<br>
**undergraduateStudent** (<u>nr</u>, telephone, emailAddress, age, advisor, memberOf)<br>
**publication** (<u>nr</u>, title, abstract, mainAuthor)<br>

#### Mapping:

--------------
### Mapping of _Lecturer_ Objects
#### Relevant part of the GraphQL schema:
```graphql
type Lecturer implements Faculty & Author 
{ 
  id: ID! 
  telephone: String 
  emailAddress: String 
  position: String
  undergraduateDegreeFrom: University 
  masterDegreeFrom: University 
  doctoralDegreeFrom: University 
  worksFor: Department 
  teacherOfGraduateCourses: [GraduateCourse] 
  teacherOfUndergraduateCourses: [UndergraduateCourse] 
  publications: [Publication] 
}
```
#### Relevant parts of the relational schema:
**lecturer** (<u>nr</u>)<br>
**faculty** (<u>nr</u>, telephone, emailAddress, undergraduateDegreeFrom, masterDegreeFrom, doctoralDegreeFrom, worksFor)<br>
**university** (<u>nr</u>)<br>
**department** (<u>nr</u>, subOrganizationOf)<br>
**undergraduateCourse** (<u>nr</u>, teacher, teachingAssistant)<br>
**graduateCourse** (<u>nr</u>, teacher)<br>
**publication** (<u>nr</u>, title, abstract, mainAuthor)<br>

#### Mapping:

--------------
### Mapping of _Publication_ Objects
#### Relevant part of the GraphQL schema:
```graphql
type Publication  
{ 
  id: ID! 
  title: String 
  abstract: String 
  authors: [Author] 
} 
```
#### Relevant parts of the relational schema:
**publication** (<u>nr</u>, title, abstract, mainAuthor)<br>
**faculty** (<u>nr</u>, telephone, emailAddress, undergraduateDegreeFrom, masterDegreeFrom, doctoralDegreeFrom, worksFor)<br>
**graduateStudent** (<u>nr</u>, telephone, emailAddress, age, undergraduateDegreeFrom, advisor, memberOf)<br>

#### Mapping:

--------------
### Mapping of _GraduateStudent_ Objects
#### Relevant part of the GraphQL schema:
```graphql
type GraduateStudent implements Author 
{ 
  id: ID! 
  telephone: String 
  emailAddress: String 
  age: Int
  memberOf: Department 
  undergraduateDegreeFrom: University 
  advisor: Professor 
  takeGraduateCourses: [GraduateCourse] 
  assistCourses: [UndergraduateCourse] 
}  
```
#### Relevant parts of the relational schema:
**graduateStudent** (<u>nr</u>, telephone, emailAddress, age, undergraduateDegreeFrom, advisor, memberOf)<br>
**department** (<u>nr</u>, subOrganizationOf)<br>
**university** (<u>nr</u>)<br>
**professor** (<u>nr</u>, professorType, researchInterest, headOf)<br>
**undergraduateCourse** (<u>nr</u>, teacher, teachingAssistant)<br>
**graduateCourse** (<u>nr</u>, teacher)<br>

#### Mapping:

-------------------
### Mapping of _UndergraduateStudent_ Objects
#### Relevant part of the GraphQL schema:
```graphql
type UndergraduateStudent  
{ 
  id: ID! 
  telephone: String 
  emailAddress: String 
  age: Int
  memberOf: Department 
  advisor: Professor 
  takeCourses: [UndergraduateCourse] 
}  
```
#### Relevant parts of the relational schema:
**undergraduateStudent** (<u>nr</u>, telephone, emailAddress, age, advisor, memberOf)<br>
**department** (<u>nr</u>, subOrganizationOf)<br>
**professor** (<u>nr</u>, professorType, researchInterest, headOf)<br>
**undergraduateCourse** (<u>nr</u>, teacher, teachingAssistant)<br>

#### Mapping:


------------------
### Mapping of _GraduateCourse_ Objects
#### Relevant part of the GraphQL schema:
```graphql
type GraduateCourse  
{ 
  id: ID! 
  teachedby: Faculty 
  graduateStudents: [GraduateStudent] 
} 
```
#### Relevant parts of the relational schema:
**graduateCourse** (<u>nr</u>, teacher)<br>
**faculty** (<u>nr</u>, telephone, emailAddress, undergraduateDegreeFrom, masterDegreeFrom, doctoralDegreeFrom, worksFor)<br>
**graduateStudent** (<u>nr</u>, telephone, emailAddress, age, undergraduateDegreeFrom, advisor, memberOf)<br>

#### Mapping:

-------------------
### Mapping of _UndergraduateCourse_ Objects
#### Relevant part of the GraphQL schema:
```graphql
type UndergraduateCourse  
{ 
  id: ID! 
  teachedby: Faculty 
  undergraduateStudents: [UndergraduateStudent] 
  teachingAssistants: GraduateStudent 
} 
```
#### Relevant parts of the relational schema:
**undergraduateCourse** (<u>nr</u>, teacher, teachingAssistant)<br>
**faculty** (<u>nr</u>, telephone, emailAddress, undergraduateDegreeFrom, masterDegreeFrom, doctoralDegreeFrom, worksFor)<br>
**undergraduateStudent** (<u>nr</u>, telephone, emailAddress, age, advisor, memberOf)<br>
**graduateStudent** (<u>nr</u>, telephone, emailAddress, age, undergraduateDegreeFrom, advisor, memberOf)<br>

#### Mapping:
<!--- continue here --->

<!---
### Mapping of _ProductType_ Objects
#### Relevant part of the GraphQL schema:
```graphql
type ProductType {
  nr: ID!
  label: String
  comment: String
  parent: ProductType
  products: [Product]
}
```
#### Relevant parts of the relational schema:
**ProductType** (<span style="border-bottom:1px solid black;">nr</span>, label, comment, parent, publisher, publishDate)

**Product** (<span style="border-bottom:1px solid black;">nr</span>, label, comment, <span style="border-bottom:1px dashed black;">producer</span>, propertyNum1, propertyNum2, propertyNum3, propertyNum4, propertyNum5, propertyNum6, propertyTex1, propertyTex2, propertyTex3, propertyTex4, propertyTex5, propertyTex6, publisher, publishDate)<br>
**ProductTypeProduct** (<span style="border-bottom:1px solid black;">product</span>, <span style="border-bottom:1px solid black;">productType</span>)

#### Mapping:
The object type *ProductType* in the GraphQL schema corresponds to the table *ProductType* in the relational model. That is, for every row in the *ProductType* table, there exists an object of type *ProductType*. For this object, the values of its fields are determined as follows:

* The value of the field ***nr*** is the value of the attribute *nr* that the corresponding row has in the table *ProductType*;<br>
* The value of the field ***label*** is the value of the attribute *label* that the corresponding row has in the table *ProductType*;<br>
* The value of the field ***comment*** is the value of the attribute *comment* that the corresponding row has in the table *ProductType*;<br>

* The value of the field ***parent*** is the value of the *ProductType* object ([created as described for the parent type](#mapping-of-producttype-objects)) whose corresponding row in the *ProductType* table has *nr* attribute, whose value is the same as the 'parent' attribute value of this *ProductType*.<br>
* The value of the field ***products*** is the value of the *Product* object ([created as described for the Product type](#mapping-of-product-objects)) whose corresponding row in the *Product* table has the *nr* attribute, whose value is the value of the attribute 'product' that the corresponding row has in the table *ProductTypeProduct*, where the corresponding row in the *ProductTypeProduct* table has the *nr* attribute of this *ProductType* as its 'ProductType' attribute value.<br>


### Mapping of _ProductFeature_ Objects
#### Relevant part of the GraphQL schema:
```graphql
type ProductFeature {
  nr: ID!
  label: String
  comment: String
  products: [Product]
}
```
#### Relevant parts of the relational schema:
**ProductFeature** (<span style="border-bottom:1px solid black;">nr</span>, label, comment, publisher, publishDate)<br>
**Product** (<span style="border-bottom:1px solid black;">nr</span>, label, comment, <span style="border-bottom:1px dashed black;">producer</span>, propertyNum1, propertyNum2, propertyNum3, propertyNum4, propertyNum5, propertyNum6, propertyTex1, propertyTex2, propertyTex3, propertyTex4, propertyTex5, propertyTex6, publisher, publishDate)<br>
**ProductFeatureProduct** (<span style="border-bottom:1px solid black;">product</span>, <span style="border-bottom:1px solid black;">productFeature</span>)

#### Mapping:
The object type *ProductFeature* in the GraphQL schema corresponds to the table *ProductFeature* in the relational model. That is, for every row in the *ProductFeature* table, there exists an object of type *ProductFeature*. For this object, the values of its fields are determined as follows:

* The value of the field ***nr*** is the value of the attribute *nr* that the corresponding row has in the table *ProductFeature*;<br>
* The value of the field ***label*** is the value of the attribute *label* that the corresponding row has in the table *ProductFeature*;<br>
* The value of the field ***comment*** is the value of the attribute *comment* that the corresponding row has in the table *ProductFeature*;<br>

* The value of the field ***products*** is the value of the *Product* object ([created as described for the Product type](#mapping-of-product-objects)) whose corresponding row in the *Product* table has the *nr* attribute, whose value is the value of the attribute 'product' that the corresponding row has in the table *ProductFeatureProduct*, where the corresponding row in the *ProductFeatureProduct* table has the *nr* attribute of this *ProductFeature* as its 'productFeature' attribute value.<br>


### Mapping of _Producer_ Objects
#### Relevant part of the GraphQL schema:
```graphql
type Producer {
  nr: ID!
  label: String
  comment: String
  homepage: String
  country: Country
  products: [Product]
}
```
#### Relevant parts of the relational schema:
**Producer** (<span style="border-bottom:1px solid black;">nr</span>, label, comment, homepage, country, publisher, publishDate)<br>
**Product** (<span style="border-bottom:1px solid black;">nr</span>, label, comment, <span style="border-bottom:1px dashed black;">producer</span>, propertyNum1, propertyNum2, propertyNum3, propertyNum4, propertyNum5, propertyNum6, propertyTex1, propertyTex2, propertyTex3, propertyTex4, propertyTex5, propertyTex6, publisher, publishDate)

#### Mapping:

The object type *Producer* in the GraphQL schema corresponds to the table *Producer* in the relational model. That is, for every row in the *Producer* table, there exists an object of type *Producer*. For this object, the values of its fields are determined as follows:

* The value of the field ***nr*** is the value of the attribute *nr* that the corresponding row has in the table *Producer*;<br>
* The value of the field ***label*** is the value of the attribute *label* that the corresponding row has in the table *Producer*;<br>
* The value of the field ***comment*** is the value of the attribute *comment* that the corresponding row has in the table *Producer*;<br>
* The value of the field ***homepage*** is the value of the attribute *homepage* that the corresponding row has in the table *Producer*;<br>
* The value of the field ***country*** is the value of the attribute *country* that the corresponding row has in the table *Producer*;<br>

* The value of the ***products*** field is an array containing each Product object ([created as described for the Product type](#mapping-of-product-objects)) whose corresponding row in the *Product* table has the *nr* attribute of this producer as its 'producer' attribute value.<br>


### Mapping of _Review_ Objects
#### Relevant part of the GraphQL schema:
```graphql
type Review{
  nr: ID!
  title: String
  text: String
  reviewDate: Date
  rating1: Int
  rating2: Int
  rating3: Int
  rating4: Int
  publishDate: Date
  reviewFor: Product
  reviewer: Person
}
```
#### Relevant parts of the relational schema:
**Review** (<span style="border-bottom:1px solid black;">nr</span>, <span style="border-bottom:1px dashed black;">product</span>, producer, <span style="border-bottom:1px dashed black;">person</span>, reviewDate, title, text, language, rating1, rating2, rating3, rating4, publisher, publishDate, ratingSite)<br>
**Product** (<span style="border-bottom:1px solid black;">nr</span>, label, comment, <span style="border-bottom:1px dashed black;">producer</span>, propertyNum1, propertyNum2, propertyNum3, propertyNum4, propertyNum5, propertyNum6, propertyTex1, propertyTex2, propertyTex3, propertyTex4, propertyTex5, propertyTex6, publisher, publishDate)<br>
**Person** (<span style="border-bottom:1px solid black;">nr</span>, name, mbox_sha1sum, country, publisher, publishDate)

#### Mapping:
The object type *Review* in the GraphQL schema corresponds to the table *Review* in the relational model. That is, for every row in the *Review* table, there exists an object of type *Review*. For this object, the values of its fields are determined as follows:

* The value of the field ***nr*** is the value of the attribute *nr* that the corresponding row has in the table *Review*;<br>
* The value of the field ***title*** is the value of the attribute *title* that the corresponding row has in the table *Review*;<br>
* The value of the field ***text*** is the value of the attribute *text* that the corresponding row has in the table *Review*;<br>
* The value of the field ***reviewDate*** is the value of the attribute *reviewDate* that the corresponding row has in the table *Review*;<br>
* The value of the field ***rating1*** is the value of the attribute *rating1* that the corresponding row has in the table *Review*;<br>
* The value of the field ***rating2*** is the value of the attribute *rating2* that the corresponding row has in the table *Review*;<br>
* The value of the field ***rating3*** is the value of the attribute *rating3* that the corresponding row has in the table *Review*;<br>
* The value of the field ***rating4*** is the value of the attribute *rating4* that the corresponding row has in the table *Review*;<br>
* The value of the field ***publishDate*** is the value of the attribute *publishDate* that the corresponding row has in the table *Review*;<br>

* The value of the field ***reviewFor*** is the value of the *Product* object ([created as described for the Product type](#mapping-of-product-objects)) whose corresponding row in the *Product* table has *nr* attribute, whose value is the same as the 'product' attribute value of this review.<br>
* The value of the field ***reviewer*** is the value of the *Person* object ([created as described for the Person type](#mapping-of-person-objects)) whose corresponding row in the *Person* table has *nr* attribute, whose value is the same as the 'person' attribute value of this review.<br>


### Mapping of _Person_ Objects
#### Relevant part of the GraphQL schema:
```graphql
type Person {
  nr: ID!
  name: String
  mbox_sha1sum: String
  country: Country
  reviews: [Review]
}
```
#### Relevant parts of the relational schema:
**Review** (<span style="border-bottom:1px solid black;">nr</span>, <span style="border-bottom:1px dashed black;">product</span>, producer, <span style="border-bottom:1px dashed black;">person</span>, reviewDate, title, text, language, rating1, rating2, rating3, rating4, publisher, publishDate, ratingSite)<br>
**Person** (<span style="border-bottom:1px solid black;">nr</span>, name, mbox_sha1sum, country, publisher, publishDate)

#### Mapping:
The object type *Person* in the GraphQL schema corresponds to the table *Person* in the relational model. That is, for every row in the *Person* table, there exists an object of type *Person*. For this object, the values of its fields are determined as follows:

* The value of the field ***nr*** is the value of the attribute *nr* that the corresponding row has in the table *Person*;<br>
* The value of the field ***name*** is the value of the attribute *name* that the corresponding row has in the table *Person*;<br>
* The value of the field ***mbox_sha1sum*** is the value of the attribute *mbox_sha1sum* that the corresponding row has in the table *Person*;<br>
* The value of the field ***country*** is the value of the attribute *country* that the corresponding row has in the table *Person*;<br>

* The value of the ***reviews*** field is an array containing each Review object ([created as described for the Review type](#mapping-of-review-objects)) whose corresponding row in the *Review* table has the *nr* attribute of this person as its 'person' attribute value.<br>

--->