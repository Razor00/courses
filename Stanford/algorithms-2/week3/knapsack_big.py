#!/usr/bin/python
import sys

sys.setrecursionlimit(20000)
def gcd(a, b):
    if b == 0:
        return a
    else:
        return gcd(b, a % b)

def compare(a, b):
    return b[1] - a[1]


'''If weights are sorted in reverse order, when we approach 
a weight which is greater then remaining size, then no need 
to descend further'''

def knapsack_rec1(sack, size, n, h):

    if (n, size) in h:
        return h[(n, size)]

    if n <= 0 or size <= 0:
        return 0

    if sack[n][1] > size: #works only when weights are sorted in reverse
        return 0
    
    h[(n, size)] = max(knapsack_rec1(sack, size, n-1, h), knapsack_rec1(sack, size - sack[n][1], n - 1, h) + sack[n][0])
    return h[(n, size)]

"Best when space is not permium, straightforward"
def knapsack_rec2(sack, size, n, h):

    if (n, size) in h:
        return h[(n, size)]

    if n <= 0 or size <= 0:
        return 0

    if sack[n][1] > size:
        h[(n, size)] = knapsack_rec2(sack, size, n-1, h)
    else:
        h[(n, size)] = max(knapsack_rec2(sack, size, n-1, h), knapsack_rec2(sack, size - sack[n][1], n - 1, h) + sack[n][0])
    return h[(n, size)]


"Iterative version, performs worst when size is large"

def knapsack(sack, size, n):
    M = [0 for i in range(size+1)]
#    gcd_all = size
#    for i in range(1, n+1):
#        gcd_all = gcd(gcd_all, sack[i][1])
#    for i in range(1, n+1):
#        sack[i][1] = sack[i][1]/gcd_all

#    size = size/gcd_all
#    print gcd_all
    for i in range(1, n+1):
        for w in xrange(size, 1, -1):
            v_i = sack[i][0]
            w_i = sack[i][1]
            v = M[w]
            if w_i <= w:
                v = max(M[w], M[w-w_i] + v_i)
            M[w] = v
#        print i
#    print M[size]*gcd_all
    return M[size]

f = open(sys.argv[1], "r")
size, nitems =  map(int, next(f).rstrip("\r\n").split())

print size, nitems

ksack=[(0,0) for i in range(nitems+1)]
for i in range(1, nitems+1):
    ksack[i] = map(int, next(f).rstrip("\r\n").split())

#ksack.sort(cmp=compare)

h={}
print knapsack_rec2(ksack, size, nitems, h)
