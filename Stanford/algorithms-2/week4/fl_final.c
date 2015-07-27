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
#define MAX_VERTICES 1002
int sdist[MAX_VERTICES][MAX_VERTICES][MAX_VERTICES]; 
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
            sdist[i][j][0] = INFINITY;


    for (i = 0; i < nedges; i++) {
        fscanf(fp, "%d %d %d", &u, &v, &cost);
        sdist[u][v][0] =  cost;
//        printf("%d, %d, %d, \n", u, v, cost);
    }
    
    for (i = 1; i <= nvertices; i++)
        for (j = 1; j <= nvertices; j++)
            if (i == j)
                sdist[i][j][0] = 0;

    int p = 0, s;
    for (k = 1; k <= nvertices; k++)
        for (i = 1; i <= nvertices; i++)
            for (j = 1; j <= nvertices; j++) {
                s = sdist[i][k][k-1] + sdist[k][j][k-1];
                if (((sdist[i][k][k-1] == INFINITY)&&(sdist[k][j][k-1] > 0)) || ((sdist[k][j][k-1] == INFINITY) && (sdist[i][k][k-1] > 0)))
                    s = INFINITY;
                sdist[i][j][k] = min(sdist[i][j][k-1], s);
                p++;
            }

    sdist[i][j] = cdist[i][k][k-1] + cdist[k][j][k-1]
    int min_v = INT32_MAX; 
    for (i = 1; i <= nvertices; i++) {
        if (sdist[i][i][nvertices] != 0) {
            printf("Graph has a cycle\n");
            return 0;
        }
//        printf("%d:", i);
        for (j = 1; j <= nvertices; j++) {
           if (sdist[i][j][nvertices] < min_v){
               min_v = sdist[i][j][nvertices];
            }
//            printf("%d ", sdist[i][j][nvertices]);
        }
//        printf("\n");
    }
    printf("%d\n", min_v);
}
