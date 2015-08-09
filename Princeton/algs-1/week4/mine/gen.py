import string
import random

a=string.ascii_lowercase
print a
b = [[j,i+1] for i,j in enumerate(a)]
random.shuffle(b)
for i,j in b:
    print 1, i, j+1,
