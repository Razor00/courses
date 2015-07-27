#! /usr/bin/python

import sys	

sys.setrecursionlimit(20000)
def Max(a, b):
	if (a > b):
		return a
	return b	

def knapsack(K, W, n, weight):
    if (n, weight) in K:
        return K[(n, weight)]

    if n <= 0:
        return 0

    if weight <= 0:
        return 0
	
    ks_1 = knapsack(K, W, n-1, weight)

    ks_2 = 0
    if weight >= W[n][1]:
        ks_2 = knapsack(K, W, n-1, weight-W[n][1]) + W[n][0]

    K[(n, weight)] = max(ks_1, ks_2)
    return K[(n, weight)]

f = open(sys.argv[1], 'r')
l = f.readline().strip('\n\r\t').split()
ks_size, items = (int(l[0]), int((l[1])))
#print ks_size,items
W=[(0,0) for i in range(items)]
for i in range(items):
	l = f.readline().strip('\n\r\t').split()
	W[i]=(int(l[0]), int(l[1]))

K = {}
x = knapsack(K, W, items-1, ks_size)
print x






