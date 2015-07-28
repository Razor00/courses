#!/usr/bin/python

import sys
print sys.argv

txt = sys.argv[1]
count = int(sys.argv[2])

for i in range(count):
    print txt.replace("#", str(i))
