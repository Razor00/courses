#!/usr/bin/python


def merge(array, start, middle, end):
    inversions = 0
    l  = start
    h  = middle + 1;
    si = start

#   print l, middle, end
#   print array[start:end+1]

    while (l <= middle and h <= end):
        if (array[l] < array[h]):
            newarray[si] = array[l]
            l += 1
        else:
            newarray[si] = array[h]
            h += 1
            inversions += middle - l + 1
        si += 1

    while (l <= middle):
        newarray[si] = array[l]
        l  += 1
        si += 1


    while (h <= end):
        newarray[si] = array[h]
        h  += 1
        si += 1

#   print newarray
    for i in range(start, end+1):
        array[i] = newarray[i]

    return inversions

def mergesort(array, start, end):

    if (abs(end - start + 1) <= 1):
        return 0

    middle = (start + end)/2
    x = mergesort(array, start, middle)
    y = mergesort(array, middle+1, end)
    z = merge(array, start, middle, end)

    return x + y + z




f = open('input.txt', 'r')

N = int(f.readline().rstrip());

array = [int(x.rstrip('\n\r')) for x in f]

newarray = [0 for x in range(0, N)]

inversions = 0
print mergesort(array, 0, N-1)

#print newarray

