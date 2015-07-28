#!/usr/bin/python

import sys
def get_pivot1(arr, st, end):
    return st

def get_pivot2(arr, st, end):
    return end

def get_largest(arr, a, b):
    if arr[a] > arr[b]:
        return a
    return b

def get_pivot3(arr, st, end):
    mid = st + (end - st)/2

    h = get_largest(arr, get_largest(arr, st, mid), end)

    if h == end:
        return get_largest(arr, st, mid)

    if h == mid:
        return get_largest(arr, st, end)

    if h == st:
        return get_largest(arr, mid, end)

def print_data(arr, pivot):
    return
    print "pivot: ", pivot
    print arr

def partition(arr, p, st, end):
    arr[p], arr[st] = arr[st], arr[p]
    arr_p = arr[st]
    i = j = st+1

    while j <= end:
        if arr[j ] < arr_p:
            arr[i], arr[j] = arr[j], arr[i]
            i += 1
        j += 1
    i = i - 1
    arr[st], arr[i] = arr[i], arr[st]
    return i

def qsort(array, st, end):
    if end <= st:
        return 0


    pivot = get_pivot3(array, st, end)
    pivot = partition(array, pivot, st, end)
    lt = qsort(array, st, pivot-1)
    rt = qsort(array, pivot+1, end)
    cmp = end - st # lengof array = (end - st + 1) and subtract 1

#   print "comparisons:", st, end, cmp
    return lt + rt + cmp




f = open('input.txt', 'r')

N = int(f.readline().rstrip());

array = [int(x.rstrip('\n\r')) for x in f]

inversions = 0
comp = qsort(array, 0, N-1)

print comp

