#!/usr/bin/python
import sys



def knapsack(sack, size, n):
    M = [[0 for i in range(size+1)] for i in range(n+1)]
    for w in range(size+1):
        for i in range(1, n+1):
            v_i = sack[i][0]
            w_i = sack[i][1]
            v = M[i-1][w]
            if w_i <= w:
                v = max(M[i-1][w], M[i-1][w-w_i] + v_i)
            M[i][w] = v
    print M[n][size]





f = open(sys.argv[1], "r")
size, nitems =  map(int, next(f).rstrip("\r\n").split())
print size, nitems

ksack=[(0,0) for i in range(nitems+1)]
for i in range(1, nitems+1):
    ksack[i] = map(int, next(f).rstrip("\r\n").split())
#print ksack

knapsack(ksack, size, nitems)
