The basis of the LinGBM query workloads are [16 templates of GraphQL queries](Query-Templates-of-the-Benchmark). This mix of query templates has been hand-crafted to cover all the [choke points](Choke-Points) that have been identified in the initial [design phase of the benchmark](Introduction-to-the-LinGBM-Project#methodology).

The query templates contain one or more placeholders for values that exist in the generated [benchmark datasets](Datasets). Hence, to instantiate any of the templates into an actual query, every placeholder has to be substituted by one of the possible values. The query workloads consist of such queries obtained by instantiating the templates.

In addition to the [textual definition of the query templates](Query-Templates-of-the-Benchmark), the templates are defined in the GraphQL query language. That is, every template is defined as a GraphQL query with variables that represent the placeholders of the template. These template GraphQL queries are expressed in terms of the [LinGBM GraphQL schema](LinGBM-GraphQL-Schema) and can be found in the directory [./artifacts/queryTemplates/main/](https://github.com/LiUGraphQL/LinGBM/tree/master/artifacts/queryTemplates/main) of the LinGBM github repository. The [LinGBM Query Generator](https://github.com/LiUGraphQL/LinGBM/tree/master/tools/querygen) can be used to generate actual GraphQL queries as instances of this, and any other, GraphQL-based representation of the query templates.

### Query Workloads for Scenario 1
As mentioned above, the GraphQL-based representation of the query templates uses the [LinGBM GraphQL schema](LinGBM-GraphQL-Schema). This schema has been defined specifically for the first of the [two scenarios captured by LinGBM](Introduction-to-the-LinGBM-Project#scenarios). Hence, instantiating this GraphQL-based representation of the query templates gives us query workloads for tests in the context of this first scenario. We consider two classes of such workloads:
1. **Workloads for experiments that focus on a fine-grain analysis of _response times_ achieved by GraphQL servers:** such workloads consist of one (or very few) instances per query template.
	* A concrete version of such a workload that can be used in combination with any benchmark dataset of scale factor _100_ or greater can be found at: **TODO**  This workload consists of one instance of each of the 16 query templates.
2. **Workloads for experiments to analyze _throughput_ of GraphQL-based client-server systems:** such workloads consist of many instances per query template.
	* A concrete version of such a workload that can be used in combination with any benchmark dataset of scale factor _1000_ or greater can be found at: **TODO**  This workload consists of 100 instances of each of the 16 query templates except for the following instances, for which there does not exist at least 100 instances at scale factor 1000: templates QT2 (22 instances), QT6 (12 instances), QT11 (12 instances), QT15 (12 instances), and QT16 (12 instances).


### Query Workloads for Scenario 2
Systems that may be tested in the context of the second scenario are systems that can auto-generate a GraphQL schema for the underlying data source (which, typically, is an SQL database in this case). The scenario 2 benchmark tests are based on such generated schemas. That is, instead of using the aforementioned GraphQL-based representation of the LinGBM query templates (which is based on the LinGBM GraphQL schema), for the second scenario we use other GraphQL-based representations that express the templates in terms of the particular GraphQL schemas generated by the systems under test. We have created such representations for the following systems:
* **TODO**
* **TODO**

Further representations of the LinGBM query templates for other systems may be created. You are welcome to contact us if you are planning to do so, and if you have created representations to cover another system, please let us know and we may include a pointer to them in the list above.  

Like for the first scenario, instantiating any of these system-specific representations of the query templates gives us query workloads for scenario 2 benchmark tests of the corresponding systems. These workloads may be classified by using the same two classes as introduced above for the scenario 1 workloads:
1. **Workloads for experiments that focus on a fine-grain analysis of _response times_ achieved by GraphQL servers:**
	* Concrete versions of such a workload for **System 1 (TODO)** and for **System 2 (TODO)** that can be used in combination with any benchmark dataset of scale factor _100_ or greater can be found at: **TODO** and **TODO** respectively. These workloads correspond exactly to the corresponding scenario 1 workload mentioned above; that is, for each query template, the instances in these workloads are created based on the same value(s) for the placeholder(s) of that template.
2. **Workloads for experiments to analyze _throughput_ of GraphQL-based client-server systems:**
	* Concrete versions of such a workload for **System 1 (TODO)** and for **System 2 (TODO)** that can be used in combination with any benchmark dataset of scale factor _1000_ or greater can be found at: **TODO** and **TODO** respectively. These workloads correspond exactly to the corresponding scenario 1 workload mentioned above.