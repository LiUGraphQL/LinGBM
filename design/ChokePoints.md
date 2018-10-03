# Choke Points Covered by the Benchmark
This document specifies the choke points to be covered by the benchmark. 

**What do we mean by "choke points"?**
As per the [design methodology for benchmark development](http://ldbcouncil.org/blog/choke-point-based-benchmark-design) introduced by the Linked Data Benchmark Council, a so-called choke point is a key technical challenge for the types of systems for which a benchmark is being designed. These choke points then inform the creation of the artifacts of the benchmark, where these artifacts include i) a data schema of the benchmark, ii) a workload of operations to be performed by the system under test, iii) performance metrics, and iv) benchmark execution rules.

We have grouped the choke points of the our benchmark into the following five different classes. For the time being, the descriptions of the actual choke points will be maintained in separate branches of this document in [our Github repo](https://github.com/LiUGraphQL/LinGBM/tree/master/design) in order to enable a discussion of each of them in a corresponding [pull request](https://github.com/LiUGraphQL/LinGBM/pulls).

## Choke Points Related to Attribute Retrieval
Queries may request the retrieval of multiple attributes (scalar fields) of the data objects selected by the queries. A naive implementation may perform multiple, separate operations to fetch these attributes from the underlying data source. An approach that is commonly used to improve the performance in this case is called _batching_; the idea of this approach is to combine the retrieval of all requested attributes into a single query over the underlying data source.

### CP 1.1: 

## Choke Points Related to Relationship Traversal 
One of the main innovations of GraphQL in comparison to REST APIs is that it allows users to traverse the relationships between data objects in a single request. Supporting such a traversal in a GraphQL server may pose different challenges. The following choke points capture these challenges.

### CP 2.1: 

### CP 2.2: 

### CP 2.3: 

### CP 2.4: 

### CP 2.5: 

## Choke Points Related to Ordering and Paging
Given that an exhaustive traversal of a sequence of 1:N relationships may easily result in reaching a prohibitively large number of data objects, providers of public GraphQL APIs aim to protect their servers from queries that require such resource-intensive traversals. A common approach used in this context is to enforce clients to use paging when accessing 1:N relationships, which essentially establishes an upper bound on the maximum possible fan-out at every level of the traversal. As an example, consider the following query which retrieves only the last ten books reviewed by each of the first ten persons known by Alice (rather than all the books reviewed by all the persons known by Alice).

```
query {
  person(name:″Alice″) {
    knows (first:10) {
      reviewedBooks (last:10) { title }
    }
  }
}
```

A feature related to paging is to allow users to specify a particular order over the objects visited by traversing a 1:N relationship. This feature may be used in combination with paging, but also to simply request a particular order in which objects have have to appear in the result.

### CP 3.1: 

### CP 3.2: 

### CP 3.3: Ordering
The challenge captured by this choke point is to efficiently apply a user-specified order over the objects visited by traversing a 1:N relationship.

## Choke Points Related to Searching and Filtering
Field arguments in GraphQL queries are powerful not only because they can be used as a flexible approach to expose paging and ordering features. Another use case, which is perhaps even more interesting from a data retrieval point of view, is to expose arbitrarily complex search and filtering functionality. The following choke points capture different challenges related to this use case. 

### CP 4.1: 

### CP 4.2: 

### CP 4.3: 

### CP 4.4: 

### CP 4.5: 

## Choke Points Related to Aggregation
Another advanced feature that GraphQL APIs may provide is to execute aggregation functions over the queried data. We identify the following two choke points related to aggregation. 

### CP 5.1: 

### CP 5.2: 
