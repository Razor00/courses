#!/usr/bin/python
import sys




f = open(sys.argv[1], "r")
nver, nedges =  map(int, next(f).rstrip("\r\n").split())
print nver, nedges

sdist=[[z for z in range(nver+1)][y for y in range(nver+1)][x for x in range(nver+1)]]
for l in f:
    u, v, cost = map(int, l.rstrip().split())
    
