#!/usr/bin/python


def min_vertex(Q, dist):
    min_v = 1000000
    ver = -1
    for q in Q:
        if  dist[q] < min_v:
            min_v = dist[q]
            ver = q
    return ver


def dijkstra(s, adj, dist, Q):
    dist[s] = 0
    while Q:
        u = min_vertex(Q, dist)
        Q.remove(u)
        if dist[u] == 1000000:
            break
        for edge in adj[u]:
            v = edge[0]
            d = edge[1]
            alt = dist[u] + d 
            if dist[v] > alt:
                dist[v] = alt
    print Q
import sys
f = open(sys.argv[1], 'r')
max_v = int(f.readline().rstrip('\n\r'));

adj_l={}
dist=[1000000 for x in range(max_v+1)]
Q=[0 for x in range(max_v)]

for i in range(max_v):
    line = f.readline().strip('\n\r\t').split()
    v = int(line[0])
    Q[i] = v
    if v not in adj_l:
        adj_l[v] = []
    for e in line[1:]:
        edge = []
        for i in  e.split(','):
            edge.append(int(i))
        adj_l[v].append(edge)

print adj_l
print dist
print Q
source = 1
dijkstra(source, adj_l, dist, Q)
ans_v = [7, 37, 59, 82, 99, 115, 133, 165, 188, 197]
for v in ans_v:
    print dist[v]
#print dist
