#!/usr/bin/python

import sys

def heapify_up(h, pos):
    if pos > 1:
        j=pos/2
        if h[pos] < h[j]:
            print "swapping"
            h[pos], h[j] = h[j], h[pos]
            heapify_up(h, j)


def heapify_down(h, pos, size):
    j     = 2 * pos
    l,r   = j, j+1
    mpos  = l
    if j > size:
        return
    elif (j < size and h[mpos] >  h[r]):
            mpos  = r
    if (h[mpos] < h[pos]):
        h[mpos], h[pos] = h[pos], h[mpos]
        heapify_down(h, mpos, size)
    

f = open(sys.argv[1], "r")

n = int(f.readline().rstrip("\r\n"))

heap = [g]

for l in f:
    heap.append(int(l.rstrip("\r\n")))
print heap
    
for i in range(n, 0, -1):
    heapify_up(heap, i)

print heap

print "Printing elements in heap"
for i in range(1,n+1):
    print heap[1]
    heap[n-i+1], heap[1] = heap[1], heap[n-i+1]
    heapify_down(heap, 1, n-i)
