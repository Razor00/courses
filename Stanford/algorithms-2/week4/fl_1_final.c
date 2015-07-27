#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <stdint.h>

long inline min(long a, long b)
{
    if (a < b)
        return a;
    return b;
}


#define INFINITY (INT32_MAX)
#define MAX_VERTICES 20002
long sdist[MAX_VERTICES][MAX_VERTICES]; 
long cdist[MAX_VERTICES][MAX_VERTICES]; 
int main(int argc, char **argv)
{
    FILE *fp = fopen(argv[1], "r");
    if (!fp) {
        printf("Error opening file\n");
        exit(EXIT_FAILURE);
    }
    int nvertices = 0;
    int nedges = 0;
    fscanf(fp, "%d %d", &nvertices, &nedges);

    printf("vertices = %d, edges = %d \n", nvertices, nedges);
    int u, v, cost;
    int i, j, k;

    for (i = 1; i <= nvertices; i++) 
        for (j = 1; j <= nvertices; j++)         
            cdist[i][j] = INFINITY;


    for (i = 0; i < nedges; i++) {
        fscanf(fp, "%d %d %d", &u, &v, &cost);
        cdist[u][v] =  cost;
    }
    
    for (i = 1; i <= nvertices; i++)
        for (j = 1; j <= nvertices; j++)
            if (i == j)
                cdist[i][j] = 0;

    int p = 0, s;
    long (*s1)[MAX_VERTICES] = sdist, (*s2)[MAX_VERTICES] = cdist;
    for (k = 1; k <= nvertices; k++) {
        for (i = 1; i <= nvertices; i++) {
            for (j = 1; j <= nvertices; j++) {
                s1[i][j] = min(s2[i][j], s2[i][k] + s2[k][j]);
            }
        }
        long (*t)[MAX_VERTICES] = s1;
        s1 = s2;
        s2 = t;
    }

    int min_v = INT32_MAX; 
    for (i = 1; i <= nvertices; i++) {
        if (s1[i][i] != 0) {
            printf("Graph has a cycle\n");
            return 0;
        }
        for (j = 1; j <= nvertices; j++) {
           if (s1[i][j] < min_v)
               min_v = sdist[i][j];
        }
    }
    printf("%d\n", min_v);
}
