#!/usr/bin/python

import sys

def compare(x, y):
    return x[0]-y[0]


def prim(edges, nodes, max_edge):
    X = [1]
    min_edges=[]
    cost = 0
    while len(X) < nodes:
        for i, e in enumerate(edges):
            if e[1] in X and e[2] not in X:
                cost += e[0]
                X.append(e[2])
                min_edges.append(e)
                print edges[i]

                del edges[i]
                break
            if e[2] in X and e[1] not in X:
                cost += e[0]
                X.append(e[1])
                print edges[i]
                min_edges.append(e)
                del edges[i]
                break
    print X
    print min_edges
    print cost


f = open(sys.argv[1], 'r')
nodes, nedges = map(int, f.readline().rstrip('\n\r').split())
print nodes, nedges

edges=[(0,0,0) for i in range(nedges)]
for i in range(nedges):
    l = f.readline().strip('\n\r\t').split()
    edges[i] = (int(l[2]), int(l[0]), int(l[1]))


edges.sort(cmp=compare)

max_edge = edges[-1][0] + 10000

prim(edges, nodes, max_edge)
