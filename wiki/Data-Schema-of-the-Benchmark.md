The data schema of the benchmark consists of the following artifacts.
* [Datasets](Datasets): a synthetic dataset that can be generated in different sizes, in the form of an SQL database or an RDF graph;
* [GraphQL schema](LinGBM-GraphQL-Schema): a schema for a GraphQL API that may provide access to any version of the benchmark dataset;
* [Schema mapping](Schema-Mapping): a definition of how the elements of the GraphQL schema map to the database schema of the benchmark dataset ([for the relational database schema](Mapping-of-GraphQL-Schema-to-Relational-Schema) and for the RDF schema).