#!/usr/bin/python

import random

def min_cut(ver, edges):
    iters = len(ver) - 2;
    c_edges = []
    while iters > 0:
        e = random.randint(0, len(edges)-1)
#       print "enter edge to remove: "
#       e = int(raw_input())
        edge = edges.pop(e)
        v1 = edge[0]
        v2 = edge[1]
        c_edges.append([v1, v2])
        for i, e in enumerate(edges):
            if e[0] == v2:
                if e[1] == v1:
                    edges[i] = [-1, -1]
                else:
                    edges[i] = [min(v1, e[1]), max(v1, e[1])]
            elif e[1] == v2:
                if e[0] == v1:
                    edges[i] = [-1,-1]
                else:
                    edges[i] = [min(v1, e[0]), max(v1, e[0])]
        while True:
            try:
                edges.remove([-1, -1])
            except:
                break
        iters -= 1 
#        print edges
#    print "edge len ", len(edges), "c_edges", c_edges
    return len(edges),c_edges



edges=[]
vertices={}
f = open('input.txt', 'r')
N = int(f.readline().rstrip('\n\r'));
print N
dup_count = 0
count = 0
for i in range(N):
    v = map(int, f.readline().split())
    s = v.pop(0)
    vertices[s] = v;
    for vi in v:
        l1 = [vi, s] 
        if vi > s:
            l1 = [s, vi]
        if l1 not in edges:
            edges.append(l1)
        else:
            #print "(%d, %d) is duplicate" % (s, vi)
            dup_count += 1
print vertices
print edges
print len(edges), len(vertices), dup_count

min_value = len(edges);
total_iters = N * N * N 
cedges=[]
t_cedges=[]
while total_iters > 0:
    edges1 = list(edges)
#    print "edges:", edges1
    value,t_edges = min_cut(vertices, edges1)
    if (value < min_value):
        min_value = value
        cedges = t_edges
        print "total_iters: %d , min_value : %d " % (total_iters, min_value)
    total_iters -= 1
#    raw_input()
print min_value, cedges;

