#!/usr/bin/python


import math
import sys
import random
from random import randint
""" 1. Find the variable which is only positive or only negative in the clauses 
    2. Assign a truth value [true/false for positive/negative] and can
    3. Remove the clause entirely in which this variable is part of
    4. Repeat the process iterative until cannot be reduced further"""

def reduce(a, b, c, flag):
    e = b 
    f = a
    if flag < 0:
        e,f = a, b
    for k in a.keys():
        if a[k][0] == -1 and b[k][1] == 0:
            a[k][1] = 0
            a[k][0] = 1
            b[k][0] = 0 
            for c in a[k][1:]:
                if c in clauses:
                    nh = clauses[c][0]
                    if nh == k*flag:
                        nh = clauses[c][1]
                    del (clauses[c])

                    if nh < 0:
                        nh = abs(nh)
                        e[nh][1] -= 1
                    else:
                        f[nh][1] -= 1
#        print len(clauses)
#    print clauses

def clean(a):
    for i in a.keys():
        if a[i][0] != -1:
            assert(a[i][1] == 0)
            del(a[i])

def get_value(pos, neg, v, assign):
    if v > 0:
        return assign[pos[v][1]]
# the assignment is valid for postive values, for neg values, return the toggled value
    return assign[neg[abs(v)][1]]^1

def two_sat(pos, neg, clauses):
    print "Two sat algorithm "
    n = len(pos)
    assert(n == len(neg))
    for i, k in enumerate(pos):
        pos[k][1] = i 
        neg[k][1] = i
   
    print n
    for l in range(int(math.log(n, 2))+1):
        assign = [randint(0,1) for i in range(n)]

        for j in range(2*n*n):
            print l,j
            failed_clauses = []
            for k, c in clauses.items():
                v1 = get_value(pos, neg, c[0], assign)
                v2 = get_value(pos, neg, c[1], assign)

                if v1 == 0  and v2 == 0:
                    failed_clauses.append(k)

            nfcl = len(failed_clauses)
            if nfcl > 0:
               #find random index in failed clauses
                k = failed_clauses[randint(0, nfcl-1)]
                
                #find the corresponding clause
                clause =  clauses[k]
                
                #randomly choose one of the two variables
                var = clauses[k][randint(0,1)]
                if var > 0:
                    a_i = pos[var][1] 
                else:
                    a_i = neg[abs(var)][1]
                assign[a_i] ^= 1
            else:
                print "Found assignment\n", assign
                print "pos\n", pos
                print "neg\n", neg
                print "clauses", clauses
                return
    print "No assignment found"

                

f = open(sys.argv[1], "r")
max_clause = max_var = int(f.readline().rstrip())
# position 0: assignment
# position 1: number of elements, later we will use it store for another purpose
positive_var  = {i:[-1, 0] for i in range(1, max_var+1)}
negative_var  = {i:[-1, 0] for i in range(1, max_var+1)}
clauses       = {i:[-1] for i in range(1, max_var+1)}
for i, line in enumerate(f):
    clause = map(int, line.rstrip().split())
    clauses[i+1] = clause
    for v in clause:
        if v < 0:
            negative_var[abs(v)][1] += 1
            negative_var[abs(v)].append(i+1)
        if v >= 0:
            positive_var[v][1] += 1
            positive_var[v].append(i+1)

prev = len(clauses)
while True:
#    print positive_var
#    print negative_var
#    print clauses
    reduce(positive_var, negative_var, clauses, 1)
    reduce(negative_var, positive_var, clauses, -1)
   
    if prev == len(clauses):
    #    print positive_var
    #    print negative_var
        clean(positive_var)
        clean(negative_var)

        print prev, len(positive_var), len(negative_var)
        print "pos:", positive_var
        print "neg:", negative_var
        print "clauses:", clauses
        break
    
    prev = len(clauses)


if len(positive_var) != 0:
    two_sat(positive_var, negative_var, clauses)
