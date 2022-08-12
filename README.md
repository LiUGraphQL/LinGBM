# Linköping GraphQL Benchmark (LinGBM)
LinGBM is a performance benchmark for GraphQL server implementations.
The [wiki of this repo](https://github.com/LiUGraphQL/LinGBM/wiki) provides an [introduction to the project](https://github.com/LiUGraphQL/LinGBM/wiki/Introduction-to-the-LinGBM-Project), the [specification of the benchmark](https://github.com/LiUGraphQL/LinGBM/wiki/Specification-of-the-Benchmark), and [design artifacts](https://github.com/LiUGraphQL/LinGBM/wiki/Choke-Points-for-a-GraphQL-Performance-Benchmark).
This repo contains artifacts created for the benchmark (such as GraphQL schemas, query templates, query workloads) and the following benchmark software:
* [Dataset generator](https://github.com/LiUGraphQL/LinGBM/tree/master/tools/datasetgen)
* [Query generator](https://github.com/LiUGraphQL/LinGBM/tree/master/tools/querygen)
* [Report generator](https://github.com/LiUGraphQL/LinGBM/tree/master/tools/reportgen) (TODO)
* [Test driver-throughput](https://github.com/LiUGraphQL/LinGBM/tree/master/tools/testdriver_TP)
* [Test driver-QET&QRT](https://github.com/LiUGraphQL/LinGBM/tree/master/tools/testdriver_QET_QRT)

## Publications related to LinGBM
* Sijin Cheng and Olaf Hartig: [LinGBM: A Performance Benchmark for Approaches to Build GraphQL Servers](http://olafhartig.de/files/ChengHartig_LinGBM_WISE2022_AcceptedManuscript.pdf). In Proceedings of the 23rd International Conference on Web Information Systems Engineering (WISE), 2022.
*A significantly extended version of this manuscript is available on arXiv: [abs/2208.04784](https://arxiv.org/abs/2208.04784), and we have a [repo with the material for the experiments](https://github.com/LiUGraphQL/LinGBM-OptimizationTechniquesExperiments) in that paper.*
