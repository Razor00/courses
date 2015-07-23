public class Percolation {

    private char BLOCKED = 1;
    private char EMPTY   = 2;
    private char FULL    = 4;

//    private boolean OPEN = true;
//    private boolean BLOCKED = false;
    private boolean percolates = false;
    private int N;
    private int virtualTop;
//    private int virtual_bottom;
    private char[][] state;
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
        TOTAL = N * N + 1;
        wtquf = new WeightedQuickUnionUF(TOTAL);
        state = new char[N+1][N+1];

        virtualTop = 0;
    //    virtual_bottom = N * N + 1;
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

    private int parent(int i, int j)
    {
        return wtquf.find(getIndex(i, j));
    }

    private int getIndex(int i, int j)
    {
        return (i-1) * N + j; 
    }

    private boolean empty(int val)
    {
        return (val == EMPTY);
    }
    private boolean blocked(int val)
    {
        return (val == BLOCKED);
    }

    private boolean opened(int val)
    {
        return (val & (EMPTY | FULL)) > 0;
    }

    public void open(int i, int j)          // open site (row i, column j) if it is not open already
    {
        int index, nindex;
        check(i);
        check(j);
        if (!opened(state[i][j])) {
            state[i][j] = EMPTY;
            index = getIndex(i, j);
            
            if (i == 1) {  // top row, special treatment, add the current to parent 0
                wtquf.union(virtualTop, index);
            }
/*
            if (i == N) // bottom row, special treatment, add the current to parent N+1
                wtquf.union(bottom, index);
*/
            // Now add the index neighbors
           
            // top neighbor
            if (i > 1 && opened(state[i-1][j])) {
                nindex = getIndex(i-1, j);
                wtquf.union(index, nindex); 
            }

            // bottom neighbor
            if (i < N && opened(state[i+1][j])) {
                nindex = getIndex(i+1, j);
                wtquf.union(index, nindex); 
            }

            // left neighbor
            if (j > 1 && opened(state[i][j-1])) {
                nindex = getIndex(i, j-1);
                wtquf.union(index, nindex); 
            }

            // right neighbor
            if (j < N && opened(state[i][j+1])) {
                nindex = getIndex(i, j+1);
                wtquf.union(index, nindex); 
            }
            if (wtquf.connected(virtualTop, index)) {
                checkFull(i, j);
            }
        }
    }

    private void checkFull(int i, int j)
    {
        state[i][j] = FULL;
        /* if at the bottom and can reach top, percolates */
        if (i == N)
            percolates = true;
       
        // check for neighbors and if they are empty (i.e open and not full)
        //
        // top neighbor
        if (i > 1 && empty(state[i-1][j])) 
            checkFull(i-1, j);

        // bottom neighbor
        if (i < N && empty(state[i+1][j])) 
            checkFull(i+1, j);

        // left neighbor
        if (j > 1 && empty(state[i][j-1])) 
            checkFull(i, j-1);

        // right neighbor
        if (j < N && empty(state[i][j+1])) 
            checkFull(i, j+1);
    }


    public boolean isOpen(int i, int j)     // is site (row i, column j) open?
    {
        check(i);
        check(j);
        return (opened(state[i][j]));
    }

    public boolean isFull(int i, int j)     // is site (row i, column j) full?
    {
        check(i);
        check(j);
        return (state[i][j] & FULL) == FULL;
    }

    public boolean percolates()             // does the system percolate?
    {
        return percolates;
/*
        int i;
        int p = N * N - N;
        for (i = 1; i <= N; i++) 
            if (state[N][i] == EMPTY)
                if (wtquf.connected(virtualTop, p + i))
                    return true;
        return false;
*/
    }

    //public static void main(String[] args)   // test client (optional)
    //{
    //}
}
