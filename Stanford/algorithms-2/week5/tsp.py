#!/usr/bin/python

import sys
import math
import random
def get_distance(l, m):
    return math.sqrt((l[0] - m[0])**2 + (l[1] - m[1])**2)



def insert_node(i, tour, dist_matrix):
    min_incr = 1 << 32
    max_nodes = len(tour)-1
    insert_pos = -1
    for p in range(max_nodes):
        u = tour[p]
        v = tour[p+1]
        inc =  dist_matrix[u][i] + dist_matrix[i][v] - dist_matrix[u][v]
        if (inc < min_incr):
            min_incr = inc
            insert_pos = p+1
    
    tour.insert(insert_pos, i)
    return min_incr

f = open(sys.argv[1], "r")
n = int(f.readline().rstrip())

dist_arr=[[0, 0]]
for i in range(n):
    dist_arr.append(map(float, f.readline().rstrip().split()))

random.shuffle(dist_arr)

dist_matrix=[[0 for i in range(n+1)] for j in range(n+1)]
for i in range(1, n+1):
    for j in range(i, n+1):
        dist_matrix[i][j] = dist_matrix[j][i]= get_distance(dist_arr[i], dist_arr[j])

#print dist_matrix
# tsp, randomized greedy tour

tour=[1, 1]
tour_cost = 0
#tour.insert(pos, val)
for node in range(2, n+1):
    tour_cost += insert_node(node, tour, dist_matrix)

print tour_cost
