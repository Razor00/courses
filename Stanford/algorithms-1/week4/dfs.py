#!/usr/bin/python

def dfs1(G, v):
    non_visited=[v]
    visited=[]
    while non_visited:
        v = non_visited.pop(0);
        if v not in visited:
            visited.extend([v])
            non_visited.extend(G[v])
    print visited 

def dfs(G, visited, non_visited, order, number):

    while non_visited:
        v = non_visited.pop(len(non_visited)-1)
        if v not in visited:
            visited.extend([v])
            non_visited.extend(G[v])
            number = dfs(G, visited, non_visited, order, number) + 1;
            order[v] = number
            print v, number
    return number

def dfs_num(G, visited, non_visited, order, number):
    for i in range(

G={}
f = open('input.txt', 'r')
N = int(f.readline().rstrip('\n\r'));
print N

Grev={}
for i in range(N):
    v = map(int, f.readline().split())
    s = v.pop(0)
    G[s] = v;
    for i in v:
        Grev[i] = []
        Grev[i].extend([s])

v=7
non_visited=[v]
visited=[]
order={}
number = 0
dfs(G, visited, non_visited, order, number)
print order
v=7
non_visited=[v]
visited=[]
order={}
number = 0
dfs_number(Grev, visited, non_visited, order, number)
print order

