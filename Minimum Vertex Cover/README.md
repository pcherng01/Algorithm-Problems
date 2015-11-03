# Algorithm-Problems

One of the challenging assignment from Advanced Algorithm. This assignment covers NP-Complete Minimum Vertex Cover.
Here is the problem description:
Almost-tree vertex cover
Assume that you are looking for a vertex cover for an undirected graph G =
(V, E) that “from a distance” looks like a tree. However, upon closer examina-
tion you find that it is not a tree, but would be a tree if at most 2/3 of its edges
are removed. In other words, you can think of your input as being created via
the generation of a large random tree to which random edges are added. Your
goal is to find the size of the smallest possible vertex cover for this graph.
The input to your program will be a graph that has the “almost-tree” property
described above in edge list form. The vertices will be labelled by integers.
So the edges connected to vertex i will come after i followed by a colon. An x
will indicate the end of an edge list. A triangle might then be described by
0 : 1, 2x1 : 0, 2x2 : 0, 1x
The output of your program will be a file with a list of vertex names followed
by x. This will indicate your minimal vertex cover. So, for example, your
program may return
0x1x
for the triangle above.
Once you are finished coding, you will email me and I will respond with a
graph. You will then email the results of your calculation. Assuming that your
file does in fact describe a vertex cover, your grade will be based on the size of
your vertex cover as compared with my simple algorithm’s solution.
