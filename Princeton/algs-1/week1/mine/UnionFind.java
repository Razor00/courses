public class UnionFind {
    private int N;
    private int count;
    private int[] parent;
    private int[] rank;

    public UnionFind(int N)
    {
        this.N  = N;
        parent  = new int[N];
        rank    = new int[N];
        for (int i = 0; i < N; i++) {
            parent[i] = i;
            rank[i]   = 1;
        }
        count = N;
    }


    private void printParents()
    {
        for (int i = 0; i < N; i++) 
            StdOut.printf("element : %d, h = %d, parent: %d, h = %d \n", i, rank[i], parent[i], rank[parent[i]]); 
    }

    public void union(int a, int b)
    {
        int pa, pb;
        pa = find(a);
        pb = find(b);
        if (pa == pb)   return;

        if (rank[pa] > rank[pb]) { 
            parent[pb] = pa;
        }
        else if (rank[pb] > rank[pa]) {
            parent[pa] = pb;
        }
        else {
            parent[pa] = pb;
            rank[pb] += 1;
        }
        count--;
    }

    public void checkUF()
    {
        int p = find(1);
        for (int i = 0; i < N; i++)
            StdOut.println(i + " " + parent[i]);
    }
    public int find(int i)
    {
        if (parent[i] != i)
            parent[i] = find(parent[i]);

        return parent[i];
    }
/*
    public int find(int i)
    {
        int j = i;
        if (parent[i] != i) {
            do {
                i = parent[i];
            } while (parent[i] != i);

            while (j != i) {
                j = parent[j];
                parent[j] = i;
            }
        }
        return i;
    }
*/
    public int nodes()
    {
        return N;
    }

    public int count()
    {
        return count;
    }

    public static void main(String[] args)
    {
        int a = 0;
        int b = 0;
        int n = StdIn.readInt();
        UnionFind uf = new UnionFind(n);
        while (!StdIn.isEmpty()) {
            a = StdIn.readInt();
            b = StdIn.readInt();
            if (uf.find(a) != uf.find(b))
                uf.union(a, b);
            //StdOut.println("a = " + a + " : b " + b + " " +"count = " + uf.count());
        }
        //uf.printParents();
        //StdOut.printf("Top Parent = %d\n", uf.find(a));
        StdOut.println("count = " + uf.count());
        //uf.checkUF();
    }
}
