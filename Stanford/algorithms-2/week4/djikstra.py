#!/usr/bin/python

import sys

def heapify_up(h, dist, pos):
    if pos > 1:
        j = pos/2
        if h[pos][1] < h[j][1]:
            h[pos], h[j] = h[j], h[pos]
            dist[pos], dist[j] = dist[j], dist[pos]
            heapify_up(h, dist, j)


def heapify_down(h, dist, pos, size):

    j     = 2 * pos
    l, r  = j, j+1
    mpos  = l

    if j > size:
        return
    elif (j < size and h[mpos][1] >  h[r][1]):
        mpos  = r
    if (h[mpos][1] < h[pos][1]):
        h[mpos], h[pos] = h[pos], h[mpos]
        dist[mpos], dist[pos] = dist[pos], dist[mpos]
        heapify_down(h, dist, mpos, size)


def min_vertex(heap, dist, n):
    v = heap[1][0]
    heap[n], heap[1] = heap[1], heap[n]
    dist[n], dist[1] = dist[1], dist[n]
    heapify_down(heap, dist, 1, n-1)
    return v

def min_vertex1(Q, dist):
    min_v = 1000000
    ver   = -1
    for q in Q:
        if  dist[q] < min_v:
            min_v = dist[q]
            ver   = q
    return ver

def create_heap(pq_dist, dist, qlen):
    for i in range(qlen, 0, -1):
        print i
        heapify_up(pq_dist, dist, i)



def dijkstra(s, adj, pq_dist, dist, Q):
    pq_dist[dist[s]][1] = 0
    qlen = len(dist)-1
    create_heap(pq_dist, dist, qlen)
    while Q:
        u = min_vertex(pq_dist, dist, qlen)
        qlen -= 1
        Q.remove(u)
        if pq_dist[dist[u]][1] == 1000000:
            break
        for edge in adj[u]:
            v = edge[0]
            d = edge[1]
            alt = pq_dist[dist[u]][1] + d 
            if pq_dist[dist[v]][1] > alt:
                pq_dist[dist[v]][1] = alt
                heapify_up(pq_dist, dist, dist[v])
    print Q


f = open(sys.argv[1], 'r')
max_v = int(f.readline().rstrip('\n\r'));

adj_l={}
dist=[x for x in range(max_v+1)]
pq_dist=[[x, 100000] for x in range(max_v+1)]
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
dijkstra(source, adj_l, pq_dist, dist, Q)
ans_v = [7, 37, 59, 82, 99, 115, 133, 165, 188, 197]
for v in ans_v:
    print pq_dist[dist[v]][1]

for i in range(0, len(dist)):
    if pq_dist[dist[i]][0] != i:
        print "mismatch ", i

print dist
print pq_dist

#print dist
