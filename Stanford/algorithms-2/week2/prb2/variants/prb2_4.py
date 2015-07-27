#!/usr/bin/python
import sys

def init_rank(parent, x):
    parent[x] = [x, 0]

def find(parent, x):
    p = parent[x][0]
    if p != parent[p][0]:
        p = find(parent, p)
    parent[x][0] = p
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

def get_connected_components(edges, nodes, parent):
    
    for i, e in enumerate(edges):
        l, s, d = e
        ps = find(parent, s)
        pd = find(parent, d)
        union(parent, ps, pd)

    count={} 
    #skipping zero here
    for v in range(1, nodes):
        count[find(parent, v)] = 1
    return len(count)


def get_neighbours(v, neighbour, bits):
# get 1 bit
    i = 0
    for b1 in range(bits):
        neighbour[i] = v ^ (1<<b1)
        i += 1
    
#get 2 bits
    for b1 in range(bits):
        for b2 in range(b1+1, bits):
            neighbour[i] = v ^ (1<<b1|1<<b2)
            i += 1
    return neighbour
    

f=open(sys.argv[1], 'r')
nodes, bits = map(int, f.readline().rstrip('\n\r').split())
print nodes, bits

hash_tbl={}
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

bit_1_nbrs = bits
bit_2_nbrs = (bits * (bits - 1))/2
neighbours = [0 for i in range(bit_1_nbrs + bit_2_nbrs)]

edges = []
for i in range(1, nodes):
    v = vertices[i]
    neighbours = get_neighbours(v, neighbours, bits)
    for j in range(bit_1_nbrs):
        nbr = neighbours[j]
        if nbr in hash_tbl and hash_tbl[nbr] > i:
            edges.append((1, i, hash_tbl[nbr]))   

    for j in range(bit_1_nbrs, bit_1_nbrs+bit_2_nbrs):
        nbr = neighbours[j]
        if nbr in hash_tbl and hash_tbl[nbr] > i:
            edges.append((2, i, hash_tbl[nbr]))   


parent=[[i, 0] for i in range(nodes)]
print get_connected_components(edges, nodes, parent)
