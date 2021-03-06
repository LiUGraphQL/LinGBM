# Dataset Generator <br/> of the Linköping GraphQL Benchmark (LinGBM)
LinGBM uses the [datasets of the Lehigh University Benchmark](http://swat.cse.lehigh.edu/projects/lubm/) (LUBM), which can be generated by the Univ-Bench Artificial Data Generator (UBA):. This directory contains an extended version of this data generator. The original source code of the UBA data generator can be found at [https://github.com/rvesse/lubm-uba](https://github.com/rvesse/lubm-uba).

We had to extend the original UBA data generator to support generate SQL data dump, and we also add some  metadata when generating a dataset. These metadata is needed for our query generator.

### Setup

To generate a dataset for the benchmark, first clone this repo and change to the directory `./LinGBM/tools/datasetgen`.

```
git clone git@github.com:LiUGraphQL/LinGBM.git
cd LinGBM/tools/datasetgen
```

### Usage

For the instructions and command options for using the data generator refer to the [README.md of the LUBM data generator](https://github.com/rvesse/lubm-uba/blob/improved/ReadMe.md). The data generator supports different output formats such as OWL, DAML, NTRIPLES, TURTLE, GRAPHML, GRAPHML_NODESFIRST, NEO4J_GRAPHML and JSON. In addition, our extended generator also support outputing SQL and PostgreSQL.

As an example, the following command generates the dataset with scale factor 2 and writes all the data as an SQL dump: Universities.sql:

```
mvn clean install

./generate.sh --format SQL --consolidate Maximal -u 2
or
./generate.sh --format PostgreSQL --consolidate Maximal -u 2
```
To set up the dataset with the script, we also supply a script named schema_university.sql, which can be used to created tables. Then, you can load data into the database by simply executing the script:Universities.sql. 

In this directory, we have an example script that was generated on setting the factor as 1, you could work with it by simply writing the bold text below at the mysql> prompt:
```
mysql> source /XXX/path/to/schema_university.sql

mysql> source /XXX/path/to/Universities.sql
```

**Note:** this data generator generates dataset in a deterministic way. When setting different scale factors, the generated dataset on the smaller factor is certainly be included in the larger dataset.
