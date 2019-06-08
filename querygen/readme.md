# Linköping GraphQL Benchmark (LinGBM) - query Generator
This repo contains the source code of the generator that we used for generating query instances form defined query templates.

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
|-nm \<number of instances for each template> |The number of query instances for each template. Default: 50. <br> The generating process is random but not duplicate. If the total number of possible instances is less than the specified value, then generate all possible query instances.| 
|-idir \<path to directory> |The input values for placeholders, which was created by the Dataset Generator. Default:"td_data"|
|-temp \<path to query template> |The input query templates. Default: "queryTemplate"| 
|-oq \<path to output directory> |The output directory for storing the generated queries. Default: "actualQueries"|

The following example specifies 10 queries for each query template.

```
java -cp target/querygen-1.0-SNAPSHOT.jar se.liu.ida.querygen.generator -nm 15
```
