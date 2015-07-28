public class PercolationStats {
    private int N, T;
    private double mu, sigma;
    private Percolation pc;
    private double[] stats;
    public PercolationStats(int n, int t)     // perform T independent experiments on an N-by-N grid
    {

        if (n <= 0 || t <= 0)
            throw new java.lang.IllegalArgumentException("N or T are less than zero:" + "N : " + n + " " + "T: " + t);
        N = n;
        T = t;
        stats = new double[T];
        int i;
        for (i = 0; i < T; i++) {
            pc = new Percolation(N);
            stats[i] = test();
        }
        mu = mean();
        sigma = stddev();
    }

    private double test()
    {
        double uprb;
        int index;
        int i, j;
        int count = 0;
        while (true) {
            uprb = StdRandom.uniform();
            index  = (int) (uprb * N * N);      //convert uniform probability into an index
            i = index / N + 1;      // eg: 56 means row = 0,1,2,3,4,5 i.e 6th row, column = 
            j = index % N + 1;
            if (!pc.isOpen(i, j)) {
                pc.open(i, j);
                count++;
                if (pc.percolates()) 
                    return (1.0*count)/(N*N);
            }
        }
    }

    public double mean()                      // sample mean of percolation threshold
    {
        return StdStats.mean(stats, 0, T-1);
    }

    public double stddev()                    // sample standard deviation of percolation threshold
    {
        return StdStats.stddev(stats);
    }

    public double confidenceLo()              // low  endpoint of 95% confidence interval
    {
        return (mu - (1.96 * sigma * Math.sqrt(1.0/T)));
    }

    public double confidenceHi()              // high endpoint of 95% confidence interval
    {
        return (mu + (1.96 * sigma * Math.sqrt(1.0/T)));
    }

    public static void main(String[] args)    // test client (described below)
    {
        int n, t;
        n = Integer.parseInt(args[0]);
        t = Integer.parseInt(args[1]);
        PercolationStats pcs = new PercolationStats(n, t);
        StdOut.println("mean                        = " + pcs.mu);
        StdOut.println("stddev                      = " + pcs.sigma);
        StdOut.println("95% confidence interval     = " + pcs.confidenceLo() + ", " + pcs.confidenceHi());
    }
}
