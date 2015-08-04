public class Solver {

   
    private class SearchNode implements Comparable<SearchNode> {
        private int moves;
        private SearchNode prev;
        private final Board board;
        
        public SearchNode(SearchNode parent, Board brd, int moves)
        {
            this.prev   = parent;
            this.board  = brd;
            this.moves  = moves;
        }

        public int compareTo(SearchNode node)
        {
            int  dstDiff = this.board.manhattan() - node.board.manhattan();
            int  movesDiff = this.moves  - node.moves;
            if (dstDiff + movesDiff == 0) {   //if priority is equal
                if (dstDiff != 0)            // break with manhattan distance
                    return dstDiff;
                return 0;
            }
            return dstDiff + movesDiff;
        }
    }


    
    private SearchNode last;
    private MinPQ pq, tpq; 

    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        if (initial == null)
            throw new java.lang.NullPointerException();

        pq = new MinPQ();
        tpq = new MinPQ();
        
        SearchNode node, tnode;
       
        node  = new SearchNode(null, initial, 0);
        tnode = new SearchNode(null, initial.twin(), 0);
        pq.insert(node);
        tpq.insert(tnode);
        last = null;
        
        while (true) {
            node  = (SearchNode) pq.delMin();
            tnode = (SearchNode) tpq.delMin();   
            
            if (node.board.isGoal()) {
                last = node;
                break;
            }
            
            if (tnode.board.isGoal())
                break;
            
                    
            boolean added = false;
            for (Board p:node.board.neighbors()) {
                if (!((node.prev != null) && node.prev.board.equals(p))) {
                    pq.insert(new SearchNode(node, p, node.moves + 1));
                    added = true;
                }
            }
            for (Board p:tnode.board.neighbors()) {
                if (!((tnode.prev != null) && tnode.prev.board.equals(p))) 
                    tpq.insert(new SearchNode(tnode, p, tnode.moves + 1));
            }   
            
        }
    }

    public boolean isSolvable()            // is the initial board solvable?
    {
        return (last != null);
    }

    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
        if (last == null)
            return -1;

        return last.moves;
    }

    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        if (last != null) {
            SearchNode node = last;
            Stack<Board> stack = new Stack<>();
            while (node.prev != null) {
                stack.push(node.board);
                node = node.prev;
            }
            stack.push(node.board);
            return stack;
        }
        return null;
    }

    public final static void clearConsole()
    {
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                Runtime.getRuntime().exec("cls");
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception e)
        {
            //  Handle any exceptions.
            //      }
         }
    }
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
    
        int[][] blocks = new int[N][N];
        
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                clearConsole();
                StdOut.print("\u001b[2J");
                StdOut.println(board);

                try {
                    Thread.sleep(1000);
                }
                catch (Exception e) {
                    StdOut.println(e);
                }
            }
        }
    }
}
