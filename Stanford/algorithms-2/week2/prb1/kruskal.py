#!/usr/bin/python
import sys

def set_parent(parent, l, p):
    for v in l:
        parent[v] = p

def kruskal(parent, children, edges):
    final_edges = []
    for e in edges:
        l, s, d = e
        ps, pd = parent[s], parent[d]
        if ps != pd: 
            children[parent[s]].extend(children[parent[d]])
            children[parent[d]] = []
            set_parent(parent, children[parent[s]], parent[s])
            final_edges.append(e)
        else:
            print "Ignoring"
#        print e, ps, pd, parent, children
#        if len(final_edges) == len(parent) - 2:
#            break
#    print final_edges
    return final_edges


def compare(x, y):
    return x[0] - y[0]

f = open(sys.argv[1], 'r')
nnodes, nedges = map(int, f.readline().rstrip('\n\r').split())
print nnodes, nedges

edges=[(0,0,0) for i in range(nedges)]
parent=[i for i in range(nnodes+1)]
children={i:[i] for i in range(nnodes+1)}
for i in range(nedges):
    l = f.readline().strip('\n\r\t').split()
    edges[i] = (int(l[2]), int(l[0]), int(l[1]))

edges.sort(cmp=compare)
l=[e[0] for e in kruskal(parent, children, edges)]
print len(l), sum(l)

