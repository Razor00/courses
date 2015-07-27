#!/usr/bin/python

import sys

def set_parent(parent, l, p):
    for v in l:
        parent[v] = p

def get_connected_components(edges, parent, children):
    print len(edges)
    raw_input()
    for i, e in enumerate(edges):
        l, s, d = e
        ps, pd = parent[s], parent[d]
        if ps != pd:
            children[parent[s]].extend(children[parent[d]])
            children[parent[d]] = []
            set_parent(parent, children[parent[s]], parent[s])
        print i 
    print "finished the children relationship"
    no_components = -1 #first object has [0] which we should not count
    for key, value in children.iteritems():
        if len(value) > 0:
            no_components += 1

    return no_components


def get_neighbours(v, neighbour, bits):
# get 1 bit
    i = 0
    for b in range(bits):
        neighbour[i] = v ^ (1<<b)
        i += 1
    
#get 2 bits
    for b1 in range(bits):
        for b2 in range(b1+1, bits):
            neighbour[i] = v ^ (1<<b1|1<<b2)
            i += 1
#    print v, neighbour
    return neighbour
    

f=open(sys.argv[1], 'r')
nodes, bits = map(int, f.readline().rstrip('\n\r').split())
print nodes, bits

hash_tbl={0 for i in range(1<<24)}
vertices = [0 for i in range(nodes)]

#handle duplicates, i.e do not count them as vertices
n_vertices = 0
for i in range(nodes):
    v = int("".join(f.readline().rstrip('\n\r').split()), 2)
    if v not in hash_tbl:
        hash_tbl[v] = n_vertices
        vertices[n_vertices] = v
        n_vertices += 1

#update nodes with actual nodes
nodes = n_vertices

bit_1_nbrs = bits
bit_2_nbrs = (bits * (bits - 1))/2
neighbours = [0 for i in range(bit_1_nbrs + bit_2_nbrs)]
#print len(neighbours)

edges = []
print "nodes :", nodes
for i in range(nodes):
    v = vertices[i]
    neighbours = get_neighbours(v, neighbours, bits)
    for j in range(bit_1_nbrs):
        nbr = neighbours[j]
        if nbr in hash_tbl and hash_tbl[nbr] > i:
            edges.append((1, i+1, hash_tbl[nbr]+1))   

    for j in range(bit_1_nbrs, bit_1_nbrs+bit_2_nbrs):
        nbr = neighbours[j]
        if nbr in hash_tbl and hash_tbl[nbr] > i:
            edges.append((2, i+1, hash_tbl[nbr]+1))   



print "finding connected components"
parent=[i for i in range(nodes+1)]
children={i:[i] for i in range(nodes+1)}
print get_connected_components(edges, parent, children)
#print edges, len(edges)
