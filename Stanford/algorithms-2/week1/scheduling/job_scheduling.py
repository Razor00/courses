#!/usr/bin/python

import sys

def compare(x, y):
    a = (x[0])-(x[1])
    b = (y[0])-(y[1])
    if a == b:
        return (y[0]) - (x[0])
    return b-a

def compare1(x, y):
    a = (x[0])*1.0/(x[1])
    b = (y[0])*1.0/(y[1])
    if b > a:
        return 1
    return -1


def schedule(jobs):
    pj   = 0
    ctime = 0
    csum  = 0
    for e in jobs:
        ctime = e[1] + pj
        csum  += e[0] * ctime
        pj    += e[1]
    return csum

f = open(sys.argv[1], 'r')
n = int(f.readline().rstrip('\n\r'));
jobs_profile=[(0,0) for i in range(n)]
for i in range(n):
    l = f.readline().strip('\n\r\t').split()
    jobs_profile[i] = (int(l[0]), int(l[1]))

jobs_profile.sort(cmp = compare)
print schedule(jobs_profile)

jobs_profile.sort(cmp = compare1)
print schedule(jobs_profile)
