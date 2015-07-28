#include <stdlib.h>
#include <fcntl.h>
#include <stdint.h>
#include <stdio.h>

int inline min(a, b)
{
        if (a < b)
                    return a;
            return b;
}


int main(int argc, char *argv[])
{
        FILE *fp = fopen("test.txt", "r");
        if (!fp) {
            printf("error opening file\n");
            return 0;
        }
        int d;
        fscanf(fp, "%d", &d);
        printf("%d\n", d);
}
        
