#!/usr/bin/python

""" In this question your task is again to run the clustering algorithm from lecture, 
    but on a MUCH bigger graph. So big, in fact, that the distances (i.e., edge costs) 
    are only defined implicitly, rather than being provided as an explicit list.
    The data set is here. The format is:
        [# of nodes] [# of bits for each node's label]
        [first bit of node 1] ... [last bit of node 1]
        [first bit of node 2] ... [last bit of node 2]
        ...
     For example, the third line of the file "0 1 1 0 0 1 1 0 0 1 0 1 1 1 1 1 1 0 1 0 1 1 0 1" 
     denotes the 24 bits associated with node #2.

     The distance between two nodes u and v in this problem is defined as the Hamming distance--- 
     the number of differing bits --- between the two nodes' labels. For example, the Hamming distance 
     between the 24-bit label of node #2 above and the label "0 1 0 0 0 1 0 0 0 1 0 1 1 1 1 1 1 0 1 0 0 1 0 1" 
     is 3 (since they differ in the 3rd, 7th, and 21st bits).

     The question is: what is the largest value of k such that there is a k-clustering with spacing at least 3? 
     That is, how many clusters are needed to ensure that no pair of nodes with all but 2 bits in common get split 
     into different clusters?

     NOTE: The graph implicitly defined by the data file is so big that you probably can't write it out explicitly,      let alone sort the edges by cost. So you will have to be a little creative to comp"""

# As the question is to find the total clustering which are min-apart by distance 3
# 1. Store all vertices in a hash
# 2. Generate all possible neighbours of a given vertex with distance 1 or 2 only i.e with 24 bits
#  24 + 12 * 23 neighbours of each vertex
# 3. Check wether that neighbour in hash table or not
# 4. Maintain edge
# 5. Find the components using union-find algorithm, with path-compress and parent update
import sys
import time

def init_rank(parent, x):
    parent[x] = [x, 0]

def find(parent, x):
    p = parent[x][0]
    if p != parent[p][0]:
        p = find(parent, p)
    parent[x][0] = p #update the parent, which drecreases further lookups
    return p

def union(parent, x, y):
    px = find(parent, x)
    py = find(parent, y)

    if px == py:
        return 

    px_rank = parent[px][1]
    py_rank = parent[py][1]

    if px_rank < py_rank:
        parent[px][0] = parent[py][0]
    elif px_rank > py_rank:
        parent[py][0] = parent[px][0]
    else:
        parent[py][0] = parent[px][0]
        parent[py][1] = parent[px][1] + 1

def get_neighbours(bits):

    neighbour = [0 for i in range(bits + (bits*(bits-1))/2)]
# get 1 bit
    i = 0
    for b1 in range(bits):
        neighbour[i] = (1<<b1)
        i += 1
#get 2 bits
    for b1 in range(bits):
        for b2 in range(b1+1, bits):
            neighbour[i] = (1<<b1|1<<b2)
            i += 1
    return neighbour
    

start_time = time.time() 
f=open(sys.argv[1], 'r')
nodes, bits = map(int, f.readline().rstrip('\n\r').split())
print nodes, bits

hash_tbl=dict()
vertices = [0 for i in range(nodes+1)]

#handle duplicates, i.e do not count them as vertices
n_vertices = 1 #start with first vertex
for i in range(nodes):
    v = int("".join(f.readline().rstrip('\n\r').split()), 2)
    if v not in hash_tbl:
        hash_tbl[v] = n_vertices
        vertices[n_vertices] = v
        n_vertices += 1

#update nodes with actual nodes i.e after removing duplicates
nodes = n_vertices

neighbour = get_neighbours(bits)
end_time = time.time()
print('Elaspsed time was {0:f}'.format(end_time - start_time))
start_time = time.time() 

max_nbr = bits + (bits*(bits-1))/2
edges = []
parent=[[i, 0] for i in range(nodes)]
for i in range(1, nodes):
    v = vertices[i]
    for j in range(max_nbr):
        nbr = neighbour[j] ^ v
        if nbr in hash_tbl and hash_tbl[nbr] > i:
            ps = find(parent, i)
            pd = find(parent, hash_tbl[nbr])
            union(parent, ps, pd)

count={} 
#skipping zero here
for v in range(1, nodes):
    count[find(parent, v)] = 1
print len(count)
end_time = time.time()
print('Elaspsed time was {0:f}'.format(end_time - start_time))

#print get_connected_components(edges, nodes, parent)
