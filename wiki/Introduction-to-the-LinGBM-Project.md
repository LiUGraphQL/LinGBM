We--researchers of the [Database and Web Information Systems Group](https://www.ida.liu.se/divisions/adit/dwis/) at [Linköping University](https://liu.se/)--are developing a performance benchmark for GraphQL server implementations. In the following we provide a few words about the motivation, describe the scenarios covered by the benchmark, and highlight the benchmark design methodology that we are applying.

## Motivation
We observe that some of the vendors of GraphQL server implementations have a performance-related test suite. However, these test suites are very limited, which makes them unsuitable as a general performance benchmark. Important limitations include: a single fixed dataset that cannot be scaled to different sizes, a fixed set of queries (often only a few) that have been designed to test features of the respective implementation, only a single type of metric that can be measured (typically, some form of throughput), single-machine setup (i.e., the benchmark driver that issues concurrent GraphQL requests runs on the same machine as the system under test). In addition to these limitations, the dataset and the queries of these test suites have to be customized for every system that is tested, which makes experimental comparisons of systems unreliable.

Our aim for the benchmark that we are developing is to address these limitations in order to enable users to compare the performance of different GraphQL implementations and setups, and also to provide developers of GraphQL server implementations with a well-designed and balanced test suite.

## Scenarios
Our benchmark focuses on the following two scenarios.

**The first scenario represents cases in which data from a _legacy database_ has to be exposed as a _read-only_ GraphQL API with a _user-specified GraphQL schema_.**

Systems that can be used to implement GraphQL servers but that are not designed to support this scenario out of the box can also be tested in terms of this scenario. To this end, they have to be extended with an integration component such as a schema delegation layer. In such a case, from the perspective of the benchmark, the combination of the system and the integration component are treated as a black box.

**The second scenario represents cases in which data from a _legacy database_ has to be exposed as a _read-only_ GraphQL API provided by an _auto-generated GraphQL server implementation_.**

This scenario focuses on systems that auto-generate all the artifacts necessary to set up a GraphQL server that provides access to a legacy database. Notice that such systems are examples of systems that do not support the first scenario out of the box because any GraphQL server created by such a system is based on a system-generated GraphQL schema (rather than a user-specified one).

The benchmark uses the same datasets for both scenarios and the query workloads created for the second scenario resemble the workloads created for the first scenario.

## Methodology
We are creating the benchmark based on the [design methodology for benchmark development](http://ldbcouncil.org/blog/choke-point-based-benchmark-design) as introduced by the [Linked Data Benchmark Council](http://ldbcouncil.org/). The main artifacts created by the process of applying this methodology are

* a [data schema](Data-Schema-of-the-Benchmark),
* a [workload](Query-Workloads-of-the-Benchmark) of operations to be performed by the system under test,
* [performance metrics](Performance-Metrics), and
* [benchmark execution rules](Benchmark-Execution-Rules).

A crucial aspect of the methodology is to identify key technical challenges, so-called "choke points",  for the types of systems for which the benchmark is designed. These choke points then inform the creation of the aforementioned artifacts. Hence, we have created such [choke points for our benchmark](Choke-Points).