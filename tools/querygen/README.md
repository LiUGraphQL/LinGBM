# Linköping GraphQL Benchmark (LinGBM) <br/> Query Generator
This directory contains the source code of a program to generate query instances from the query templates of the benchmark.

The input to this query generator program consists of the following two parts:

1. **Query templates**: The benchmark comes with a set of [query templates](https://github.com/LiUGraphQL/LinGBM/wiki/Query-Templates-of-the-Benchmark) that cover the [chokepoints based on which the benchmark has been designed](https://github.com/LiUGraphQL/LinGBM/wiki/Choke-Points-for-a-GraphQL-Performance-Benchmark). The templates can be found in the directory `./artifacts/queryTemplates/` of the LinGBM git repo.

2. **Values for placeholders**: these values are available in an extra directory that the [BSBM data generator](http://wifo5-03.informatik.uni-mannheim.de/bizer/berlinsparqlbenchmark/spec/BenchmarkRules/index.html#datagenerator) 
produces, default "/td_data". The values in this directory will be different for the different datasets that the dataset generator generates.

The query generator tool turns the above-mentioned query templates into a group of actual queries, where the placeholders are replaced by actual values, which are randomly selected from valid values for a particular dataset. For the same dataset, the generated query instances are deterministic and duplicate-free, however, for a different dataset, the query generator has to be run again to obtain the query instances that are guaranteed to work for that dataset.

### Setup

You can clone this repo and build the query generator using Maven:

```
git clone git@github.com:LiUGraphQL/LinGBM.git
cd LinGBM/querygen
mvn package
```

### Usage

Start generating query instances by running the following example command, which default generate 20 instances for each query template, and output these queries to the directory: "/actualQueries"

```
java -cp target/querygen-1.0-SNAPSHOT.jar se.liu.ida.querygen.generator
```

Configuration options:

| Option | Description |
| ------ | ------|
|-nm \<number of instances for each template> |The number of query instances for each template. Default: 20. <br> The generated instances is random but not duplicate. If the total number of possible instances is less than the specified value, then generate all possible query instances.| 
|-values \<Absolute path to placeholder values> |The input values for placeholders, which was created by the Dataset Generator. Default:"/LinGBM/tools/datasetgen/td_data"|
|-templates \<Absolute path to query template> |The input query templates. Default: "LinGBM/artifacts/queryTemplates/main"| 
|-outdirQ \<Absolute path to output directory: query instances> |The output directory for storing the generated queries. Default: "/LinGBM/tools/querygen/actualQueries"|
|-outdirV \<Absolute path to output directory: values for variables> |The output directory for storing the variable values for query templates. Default: "/LinGBM/tools/querygen/queryVariables"|

The following example specifies 10 queries for each query template.

```
java -cp target/querygen-1.0-SNAPSHOT.jar se.liu.ida.querygen.generator -nm 10
```
