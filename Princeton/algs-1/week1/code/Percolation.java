public class Percolation {
    private boolean OPEN = true;
    private boolean BLOCKED = false;
    private int N;
    private int top;
    private int bottom;
    private boolean[][] state;
    private WeightedQuickUnionUF wtquf;

/* The logic is: we will have a one-to-one mapping of grid to N*N objects in a union-find tree
 * we shall have two more objects, 0 and N*N+1, who would not be used in the grid, but would be
 * used in the union-find tree. 
 * 1. Set the parents of objects row = 1 to 0.
 * 2. Set the parents of obejcts row = N to N*N+1
 *
 */
    public Percolation(int n)               // create N-by-N grid, with all sites blocked
    {
        int TOTAL;
        if (n <= 0)
            throw new java.lang.IllegalArgumentException("N is less than or equal to zero" + n);

        N = n;
        TOTAL = N * N + 2;
        wtquf = new WeightedQuickUnionUF(TOTAL);
        state = new boolean[N+1][N+1];

        top = 0;
        bottom = N * N + 1;
        int i, j;
        for (i = 0; i <= N; i++)
            for (j = 0; j <= N; j++)
                state[i][j] = BLOCKED;
    }
    private void check(int index)
    {
        if (index <= 0 || index > N) {
            throw new java.lang.IndexOutOfBoundsException("index is out of bounds " + index);
        }
    }
    private int getParent(int i, int j)
    {
        return wtquf.find(getIndex(i, j));
    }
    private int getIndex(int i, int j)
    {
        return (i-1) * N + j; 
    }
    public void open(int i, int j)          // open site (row i, column j) if it is not open already
    {
        int index, nindex;
        check(i);
        check(j);
        if (state[i][j] != OPEN) {
            state[i][j] = OPEN;
            index = getIndex(i, j);
            
            if (i == 1)  // top row, special treatment, add the current to parent 0
                wtquf.union(top, index);

         //   if (i == N) // bottom row, special treatment, add the current to parent N+1
         //       wtquf.union(bottom, index);

            // Now add the index neighbors
           
            // top neighbor
            if (i > 1 && state[i-1][j] != BLOCKED) {
                nindex = getIndex(i-1, j);
                wtquf.union(index, nindex); 
            }

            // bottom neighbor
            if (i < N && state[i+1][j] != BLOCKED) {
                nindex = getIndex(i+1, j);
                wtquf.union(index, nindex); 
            }

            // left neighbor
            if (j > 1 && state[i][j-1] != BLOCKED) {
                nindex = getIndex(i, j-1);
                wtquf.union(index, nindex); 
            }

            // right neighbor
            if (j < N && state[i][j+1] != BLOCKED) {
                nindex = getIndex(i, j+1);
                wtquf.union(index, nindex); 
            }
        }
    }

    public boolean isOpen(int i, int j)     // is site (row i, column j) open?
    {
        check(i);
        check(j);
        return state[i][j] == OPEN;
    }
    public boolean isFull(int i, int j)     // is site (row i, column j) full?
    {
        check(i);
        check(j);
        return wtquf.connected(top, getIndex(i, j));
    }
    public boolean percolates()             // does the system percolate?
    {
        int i;
        int p = N * N - N;
        for (i = 1; i <= N; i++) 
            if (state[N][i] == OPEN)
                if (wtquf.connected(top, p + i))
                    return true;
        return false;
    }
    //public static void main(String[] args)   // test client (optional)
    //{
    //}
}
