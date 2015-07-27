#!/usr/bin/python
import sys

def get_2_sum(key, hash):
    count = 0
    for sum in range(-10000, 10001):
        if (sum-key) in hash: count += 1
    return count
        


hash={}
f = open(sys.argv[1], 'r')
n = int(f.readline().rstrip('\n\r'));
for i in range(n):
    value = int(f.readline().rstrip('\n\r'))
    hash[value] = 1
total_sum = 0
for key in hash:
    total_sum += get_2_sum(key, hash)

print total_sum

