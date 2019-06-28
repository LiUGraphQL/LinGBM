# Query Generator <br/> of the Linköping GraphQL Benchmark (LinGBM)
This directory contains the source code of a program to generate query instances from the query templates of the benchmark. More specifically, this program turns a set of query templates into groups of actual queries (query instances), where the placeholders in the query templates are replaced by actual values. These values are selected uniformly at random from valid values for a particular benchmark dataset. Hence, for every dataset, the query generator has to be run again to obtain the query instances that are guaranteed to work for that dataset. Then, for each dataset, the set of generated query instances is deterministic and duplicate-free. 

The input to this query generator program consists of the following two parts:

1. **Query templates**: The benchmark comes with a set of [query templates](https://github.com/LiUGraphQL/LinGBM/wiki/Query-Templates-of-the-Benchmark) that cover the [chokepoints based on which the benchmark has been designed](https://github.com/LiUGraphQL/LinGBM/wiki/Choke-Points-for-a-GraphQL-Performance-Benchmark). The templates can be found in the directory `./artifacts/queryTemplates/` of the LinGBM git repo.

2. **Values for placeholders in the templates**: The possible values to replace the placeholders in the query templates are available in an extra directory created by the [dataset generator](https://github.com/LiUGraphQL/LinGBM/tree/master/tools/datasetgen). Per default, this directory is called `td_data/`. The values in this directory depend on the generated dataset; hence, they will be different for each dataset. Consequently, the query instances may be different as well. In other words, for each dataset it is necessary to generate a corresponding set of query instances.

### Setup

To use the query generator you have to clone this repository and build the query generator using Maven:

```
git clone git@github.com:LiUGraphQL/LinGBM.git
cd LinGBM/tools/querygen/
mvn package
```

### Usage

To have the program generate query instances execute it as a Java program using the following example command.

```
java -cp target/lingbm-querygen-1.0-SNAPSHOT.jar se.liu.ida.lingbm.querygen.generator
```

By default, the program generates 20 instances for each query template and writes these queries in separate files into the directory `./actualQueries/`

The following arguments can be used to change the behavior of the program:

| Argument | Description |
| ------ | ------|
|-nm \<number of instances for each template> |The number of query instances for each template. Default: 20. <br> If the specified value is less than the total number of instances possible for some query template (because there are not enough possible values for the placeholder(s) in that query template), then all possible query instances are generated.| 
|-templates \<Absolute path to the directory with the query templates> |The query templates used as input. Default: /LinGBM/artifacts/queryTemplates/main| 
|-values \<Absolute path to the directory with the placeholder values> |The directory with the input values for placeholders, which was created by the dataset generator. Default: /LinGBM/tools/datasetgen/td_data |
|-outdirQ \<Absolute path to output directory: query instances> |The output directory for writing the files with the generated query instances. Default: /LinGBM/tools/querygen/actualQueries|
|-outdirV \<Absolute path to output directory: values for variables> |The output directory for writing the files with the variable assignments to be used together with the query templates. Default: /LinGBM/tools/querygen/queryVariables |

For instance, the following example command generates only 10 queries for each query template.

```
java -cp target/lingbm-querygen-1.0-SNAPSHOT.jar se.liu.ida.lingbm.querygen.generator -nm 10
```
