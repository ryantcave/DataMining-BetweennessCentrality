# DataMining-BetweennessCentrality

An assignment for CSI 4352 Data Mining - Graph Clustering by Hierarchical Approach. An implementation of Brandes' algorithm for faster computation of betweenness centrality, seen here: http://www.algo.uni-konstanz.de/publications/b-fabc-01.pdf

The input file provided is tab delimited and represents edge pairings. The program accepts all input in this form and iteratively removes edges of the highest betweenness until the graph is separated. At this point, we check to see if subgraphs meet a given density threshold. If not, we continue iteratively cutting edges.

Debugging output can be read as follows:
"Graph List Size: [number of subgraphs not meeting removal/cluster criteria] | vertices in graph 1 | vertices in graph 2 | etc"

Final output lists the size of the cluster followed by its member vertices.
