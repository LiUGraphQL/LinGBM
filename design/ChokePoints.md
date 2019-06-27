# Choke Points Covered by the Benchmark
This document specifies the choke points to be covered by the benchmark. 

**What do we mean by "choke points"?**
As per the [design methodology for benchmark development](http://ldbcouncil.org/blog/choke-point-based-benchmark-design) introduced by the Linked Data Benchmark Council, a so-called choke point is a key technical challenge for the types of systems for which a benchmark is being designed. These choke points then inform the creation of the artifacts of the benchmark, where these artifacts include i) a data schema of the benchmark, ii) a workload of operations to be performed by the system under test, iii) performance metrics, and iv) benchmark execution rules.

We have grouped the choke points of the our benchmark into the following five different classes. For the time being, the descriptions of the actual choke points will be maintained in separate branches of this document in [our Github repo](https://github.com/LiUGraphQL/LinGBM/tree/master/design) in order to enable a discussion of each of them in a corresponding [pull request](https://github.com/LiUGraphQL/LinGBM/pulls).

## Choke Points Related to Attribute Retrieval
Queries may request the retrieval of multiple attributes (scalar fields) of the data objects selected by the queries. A naive implementation may perform multiple, separate operations to fetch these attributes from the underlying data source. An approach that is commonly used to improve the performance in this case is called _batching_; the idea of this approach is to combine the retrieval of all requested attributes into a single query over the underlying data source.

### CP 1.1: Multi-attribute retrieval
This choke point captures the challenge to efficiently support queries that request multiple attributes of the selected data objects.

## Choke Points Related to Relationship Traversal 
One of the main innovations of GraphQL in comparison to REST APIs is that it allows users to traverse the relationships between data objects in a single request. Supporting such a traversal in a GraphQL server may pose different challenges. The following choke points capture these challenges.

### CP 2.1: Traversal of different 1:N relationship types
Types of relationships between object types may be 1:1, 1:N, or M:N (note that the latter two are the same from the perspective of a given data object that has such relationships), and whether a data object has a specific relationship may be mandatory or optional. Supporting an efficient traversal of 1:N (or M:N) relationships may be challenging due to the fact that every such relationship may have a different fan-out (i.e., how big is N?). This challenge, together with the need to support traversals along multiple relationships (which may be of different types), is captured by this choke point.

### CP 2.2: Efficient traversal of 1:1 relationship types
While all 1:1 relationship types trivially have the same fan-out of 1, they may differ in whether they are mandatory or optional. Additionally, a traversal of these types of relationships may be supported more efficiently by using techniques that cannot be applied to 1:N relationships. Consequently, the challenge captured by this choke point is to employ suitable traversal techniques that are efficient for the different types of relationships.

### CP 2.3: Relationship traversal with and without retrieval of intermediate object data
In addition to traversing along multiple relationships, GraphQL allows users to retrieve attributes (scalar fields) of the objects that are visited during the traversal. As an example, consider the following GraphQL query.

```
query {
  person(name:"Alice") {
    knows {
      name
      reviewedBooks { title }
    }
  }
}
```

This query does not only retrieve the titles of the books that are reached via the traversal. Instead, the query also retrieves the names of the persons that are visited when traversing to the books. In more general terms, this query does not only traverse from some starting node (such as the Alice object in the example) to some target nodes (the books) and then retrieve some attributes of these target nodes (the book titles). Instead, the additional aspect of this query that is important for this choke point is that the query requests attributes of objects that are along the way of the traversal. Hence, the challenge here is not only to be able to traverse efficiently (without looking at the intermediate objects along the way)--which is something that is already captured by choke points CP 2.1 and CP 2.2--but also to be able to efficiently access the attributes of objects that we "pass by" during the traversal.

### CP 2.4: Traversal of relationships that form cycles
In cases in which relationships form directed cycles, traversing along these relationships may result in coming back to a data object that has been visited before on the same traversal path (which, formally, turns the path into a walk). This choke point captures the challenge of avoiding unnecessary operations in these cases. For instance, a naive implementation may end up requesting the same data multiple times from the underlying data source. Even a more sophisticated solution that caches and reuses the results of such requests may end up repeating the same operations over the cached data.

### CP 2.5: Acyclic relationship traversal that visits data objects repeatedly
Even without directed cycles, data objects may be visited multiple times during a traversal (via different paths). The challenge in this case is essentially the same as in the previous choke point. However, the techniques to address the challenge may be different, or they may not be equally effective in all cases. Consequently, we introduce a separate choke point related to avoiding unnecessary operations when data objects are visited repeatedly during the traversal of acyclic relationships.

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

### CP 3.1: Paging without offset
This choke point captures the general challenge of providing efficient support for the retrieval of a limited number of first objects or last objects (i.e., by avoiding unnecessary operations).

### CP 3.2: Paging with offset
This choke point captures the additional challenge of efficiently supporting paging also in cases in which a limited number of objects have to be retrieved starting from a user-specified offset.

### CP 3.3: Ordering
The challenge captured by this choke point is to efficiently apply a user-specified order over the objects visited by traversing a 1:N relationship.

## Choke Points Related to Searching and Filtering
Field arguments in GraphQL queries are powerful not only because they can be used as a flexible approach to expose paging and ordering features. Another use case, which is perhaps even more interesting from a data retrieval point of view, is to expose arbitrarily complex search and filtering functionality. The following choke points capture different challenges related to this use case. 

### CP 4.1: String matching
The challenge captured by this choke point is to efficiently support string matching as illustrated in the following query (which retrieves the birthdate of persons whose name begins with an A).

```
query {
  person(match:{attribute:″name″, startswith:″A″}) {
    birthdate
  }
}
```

Notice that the string matching features exposed by a GraphQL API can be more powerful than supporting only the notion of prefix matching that is illustrated in the example.

### CP 4.2: Date matching
While similar in spirit to the previous one, this choke point focuses on values that are dates. In addition to requiring exact matches for a given date, it is possible to match dates based on range conditions (e.g., everything before a specific date) or based on given periods (e.g., everything in some February).

### CP 4.3: Subquery-based filtering
Consider the following GraphQL query which retrieves the title of books reviewed by Alice and, for each of these books, the names of all those reviewers of the book who have also reviewed a movie that was reviewed by Alice. 

```
query {
 person(name:″Alice″) {
   reviewedBooks {
     title
     reviewedBy(where:{reviewedMovies:{reviewedBy:{name:″Alice″}}}) {
       name
     }
   }
 }
} 
```

The query represents a case in which candidate data objects selected by traversing a relationship are filtered based on some condition; this filter condition is expressed in some form of subquery that is embedded inside the corresponding field argument. As illustrated by the example query, such a filter condition may involve the traversal of relationships for each candidate data object. We notice that this feature bears similarities to the notion of correlated subqueries in relational query languages such as SQL, and supporting such a subquery-based filtering in GraphQL poses similar challenges as correlated subqueries in SQL.

### CP 4.4: Subquery-based search
While the previous choke point focuses explicitly on cases in which a subquery-based filter condition has to be applied somewhere along a relationship traversal, it is also possible to use such subqueries as field arguments of the main query object (like in the string matching example given above). For instance, the following query searches for persons who have reviewed a movie that was reviewed by Alice and, for each such person, retrieves the persons that they know.

```
query {
  person(where:{reviewedMovies:{reviewedBy:{name:″Alice″}}}) {
    knows {
      name
    }
  }
} 
```

Such a usage of subqueries captures more a notion of query-based search for candidate objects rather than filtering. While techniques to perform a subquery-based filtering (as captured by the previous choke point) may also be employed for subquery-based search, some systems may be equipped with techniques that focus especially on only one of the two variants of using subqueries in field arguments. Therefore, we consider subquery-based search as an additional choke point (separate from the previous one about subquery-based filtering).

### CP 4.5: Multiple filter conditions
In addition to single filter conditions, it is also possible to enable users to express conjunctions or disjunctions of multiple such conditions. The added challenge in this case is that the conditions may not be equally selective. Hence, pushing them together to the underlying data source, or at least choosing which of them to evaluate first, may have an impact on the performance (in particular, if they are subqueries).

## Choke Points Related to Aggregation
Another advanced feature that GraphQL APIs may provide is to execute aggregation functions over the queried data. We identify the following two choke points related to aggregation. 

### CP 5.1: Calculation-based aggregation
This choke point focuses on aggregation functions that calculate a single value from a set of values specified by the query. Examples of such functions include SUM, AVERAGE, and MAX. The challenge is to push the computation of such aggregation functions into the underlying data source or, if this is not possible, employ some other techniques to aggregate the specified values efficiently.

### CP 5.2: 
