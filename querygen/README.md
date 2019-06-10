# Linköping GraphQL Benchmark (LinGBM) - query Generator
This repo contains the source code of the generator that we used for generating query instances from the query templates.

The input to the query generator tool contains 2 parts:

1. **Query templates**: we defined 16 [query templates](https://docs.google.com/document/d/1-t2RxAcZMo47JPXBh9nZBlGhKKmH433ZuCCPg9W55ZE/edit?usp=sharing) to cover the [chokepoints](https://github.com/LiUGraphQL/LinGBM/wiki/Choke-Points-for-a-GraphQL-Performance-Benchmark) in GraphQL implementation. You could find these tempaltes in the "queryTemplate" subdirectory.

2. **Values for placeholders**: these values are available in an extra directory that the [BSBM data generator](http://wifo5-03.informatik.uni-mannheim.de/bizer/berlinsparqlbenchmark/spec/BenchmarkRules/index.html#datagenerator) 
produces, default "/td_data". The values in this directory will be different for the different datasets that the dataset generator generates.

The query generator tool turns the above mentioned query templates into a group of actual queries, where the placeholders are replaced by actual values, which are randomly selected from valid values for a particular dataset. For every dataset, the query generator has to be run again to obtain the query instances that are guaranteed to work for that dataset.

### Setup

You can clone this repo and build the query generator using Maven:

```
git clone git@github.com:LiUGraphQL/LinGBM.git
cd LinGBM/querygen
mvn package
```

### Usage

Start generating query instances by running the following example command, which defaultly generate 20 instances for each query template, and output these queries to the directory: "/actualQueries"

```
java -cp target/querygen-1.0-SNAPSHOT.jar se.liu.ida.querygen.generator
```

Configuration options:

| Option | Description |
| ------ | ------|
|-nm \<number of instances for each template> |The number of query instances for each template. Default: 20. <br> The generated instances is random but not duplicate. If the total number of possible instances is less than the specified value, then generate all possible query instances.| 
|-values \<path to directory> |The input values for placeholders, which was created by the Dataset Generator. Default:"td_data"|
|-tempplates \<path to query template> |The input query templates. Default: "queryTemplate"| 
|-outdir \<path to output directory> |The output directory for storing the generated queries. Default: "actualQueries"|

The following example specifies 10 queries for each query template.

```
java -cp target/querygen-1.0-SNAPSHOT.jar se.liu.ida.querygen.generator -nm 10
```
