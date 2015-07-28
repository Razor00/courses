#!/usr/bin/python
""" In this programming problem and the next you'll code up the clustering algorithm from lecture 
    for computing a max-spacing k-clustering. Download the text file here. This file describes a 
    distance function (equivalently, a complete graph with edge costs). It has the following format:
    [number_of_nodes]
    [edge 1 node 1] [edge 1 node 2] [edge 1 cost]
    [edge 2 node 1] [edge 2 node 2] [edge 2 cost]
    ...
    There is one edge (i,j) for each choice of 1≤i<j≤n, where n is the number of nodes. For example, 
    the third line of the file is "1 3 5250", indicating that the distance between nodes 1 and 3 
    (equivalently, the cost of the edge (1,3)) is 5250. You can assume that distances are positive, 
    but you should NOT assume that they are distinct.

    Your task in this problem is to run the clustering algorithm from lecture on this data set, where 
    the target number k of clusters is set to 4. What is the maximum spacing of a 4-clustering?

    ADVICE: If you're not getting the correct answer, try debugging your algorithm using some small 
    test cases. And then post them to the discussion forum!"""

import sys

def set_parent(parent, l, p):
    for v in l:
        parent[v] = p

def kruskal(parent, children, edges, cluster):
    nodes = len(parent)-2-cluster+1
    final_edges = []
    for e in edges:
        l, s, d = e
        ps, pd = parent[s], parent[d]
        if ps != pd: 
            if len(final_edges) == nodes:
                print e[0] 
                break
            children[parent[s]].extend(children[parent[d]])
            children[parent[d]] = []
            set_parent(parent, children[parent[s]], parent[s])
            final_edges.append(e)
#        else:
#            print "Ignoring"
#        print e, ps, pd, parent, children
#    print final_edges
    return final_edges


def compare(x, y):
    return x[0] - y[0]

f = open(sys.argv[1], 'r')
nnodes, nedges, cluster = map(int, f.readline().rstrip('\n\r').split())
#print nnodes, nedges

edges=[(0,0,0) for i in range(nedges)]
parent=[i for i in range(nnodes+1)]
children={i:[i] for i in range(nnodes+1)}
for i in range(nedges):
    l = f.readline().strip('\n\r\t').split()
    edges[i] = (int(l[2]), int(l[0]), int(l[1]))

edges.sort(cmp=compare)
l=[e[0] for e in kruskal(parent, children, edges, cluster)]
#print len(l), sum(l)

