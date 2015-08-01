public class Board {
    private int N;
    private char[] tiles;
    private int manhattanDst;
    private int hammingDst;

    private Board(char[] blocks, int dst, int N)
    {
        
        tiles = blocks.clone();
        this.N = N;
        manhattanDst = dst;
    }

    public Board(int[][] blocks)           // construct a board from an N-by-N array of blocks
    {
        N  = blocks[0].length;
        tiles = new char[N*N+1];

        int pos = 1;
        for (int i = 0; i < N; i++) 
            for (int j = 0; j < N; j++) {
                tiles[pos++] = (char) blocks[i][j];
            }
        
        //printTiles();
        manhattanDst = getManhattan();
        hammingDst = hamming();
    }

                                           // (where blocks[i][j] = block in row i, column j)
    public int dimension()                 // board dimension N
    {
        return N;
    }

    public int hamming()                   // number of blocks out of place
    {
        int dst = 0;
        for (int pos = 1; pos <= (N*N); pos++)
            if (tiles[pos] > 0 && (tiles[pos] != pos)) 
                dst++;
          
        return dst;
    }

    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        return manhattanDst;
    }

    private int getManhattan()                 // sum of Manhattan distances between blocks and goal
    {
        int pos;
        int pi, pj;
        int dst;
        
        dst = 0;
        pos = 1;
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                //StdOut.println((int)tiles[pos] + " " + pos);
                if ((int) tiles[pos] > 0 && ((int) tiles[pos] != pos)) {
                    int v = tiles[pos];
                    pi = (v-1) / N + 1;
                    pj = (v-1) % N + 1;
                    dst += Math.abs(pi-i) + Math.abs(pj-j);
                }
                //StdOut.println(i + " "+ j + " " + dst);
                pos++;
            }
        }
 
        return dst;
   
    }
    public boolean isGoal()                // is this board the goal board?
    {
        return manhattanDst == 0;
    }

    private int dist(int a, int b)
    {
        int pi, pj, di, dj;
        pi = (a-1) / N + 1;
        pj = (a-1) % N + 1;
        di = (b-1) / N + 1;
        dj = (b-1) % N + 1;
        return Math.abs(pi-di) + Math.abs(pj-dj);
    }

    private int getMandist(char[] A, int mdst, int i, int j)
    {
        int dst = mdst; 
        if (A[i] != 0) {
            dst -= dist(A[i], j);
            dst += dist(A[i], i);
        }
        
        if (A[j] != 0) {
            dst -= dist(A[j], i);
            dst += dist(A[j], j);
        }
        
        return dst;
    }

    public Board twin()                    // a board that is obtained by exchanging two adjacent blocks in the same r
    {
        
        if (this.N == 1)
            return null;

        Board tw = new Board(tiles, manhattanDst, N);

        int i;
        for (i = 1; i <= N; i++) 
            if (tiles[i] == 0)
                break;
        
        int p1, p2;
        if (i == N+1) { // no blank in first row
            p1 = 1;
            p2 = 2;
        }
        else {          //blank in first row, so choose next row
            p1 = 1 + N;
            p2 = 2 + N;
        }
        

        exch(tw.tiles, p1, p2);
//        StdOut.println(tw.toString());
        tw.manhattanDst = getMandist(tw.tiles, manhattanDst, p1, p2);
        return tw;

    }

    //@Override
    public boolean equals(Object y)        // does this board equal y?
    {
        if (y == this) return true;
    
        if (y == null) return false;
        
        if (y.getClass() != this.getClass()) 
            return false;

        Board b = (Board) y;
        if (this.dimension() != b.dimension())
            return false;

        for (int i = 1; i <= N*N; i++)
            if (this.tiles[i] != b.tiles[i])
                return false;

        return true;
    }

    private void exch(char[] T, int i, int j)
    {
        char ch = T[i];
        T[i]    = T[j];
        T[j]    = ch;
    }

    private void insertTile(Stack<Board> nbrs, char[] A, int n, int dst, int i, int j)
    {
        exch(A, i, j);  
        nbrs.push(new Board(A, getMandist(A, dst, i, j), n));
        exch(A, i, j);  
    }
    
    public Iterable<Board> neighbors()     // all neighboring boards
    {
        int blank;
        Stack<Board> nbrs = new Stack<>();

        for (blank = 1; blank <= (N*N); blank++)
            if (tiles[blank] == 0)
                break;
        
        int down, up, left, right;
        down  = blank + N;  
        up    = blank - N;  
        left  = blank - 1;
        right = blank + 1;

        //StdOut.println("down = " + down );
        if (down <= (N*N)) 
            insertTile(nbrs, tiles, N, manhattanDst, down, blank);

        if (up >= 1) 
            insertTile(nbrs, tiles, N, manhattanDst, up, blank);
        
        if (left % N != 0)    //(l = i-1, (1, 4, 7 - 1 = 0, 3, 6) multiples of 3, for which left edge is absent
            insertTile(nbrs, tiles, N, manhattanDst, left, blank);
        
        if ((right-1) % N != 0) //(r-1 = (i+1-1)%N == 0 i.e multiples of 3, for which right block does not exist
            insertTile(nbrs, tiles, N, manhattanDst, right, blank);
        
        return nbrs;
    }

    public String toString()               // string representation of this board (in the output format specified below)
    {
        StringBuilder s = new StringBuilder();
        s.append(N+"\n");
        int count = 1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", (int) tiles[count++]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) // unit tests (not graded)
    {
        int[][] arr =  {
                        {8, 1, 3}, 
                        {7, 0, 5},
                        {4, 6, 2}
                        };
        Board t = new Board(arr);
        StdOut.println(t.toString() + " " + t.manhattan() + " " + t.hamming());
        for (Board p:t.neighbors()) {
            StdOut.println(p.toString() + "\n" + p.manhattanDst+"\n");
            assert p.manhattan() == p.manhattanDst;
        }
        Board tw = t.twin();
        StdOut.println(tw.toString());
        assert tw.manhattan() == tw.manhattanDst;
    }
}
