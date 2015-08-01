public class MinPQ<T extends Comparable<T>> {

    private T[] A;
    int N;
    int max = 10;

    void MinPQ()
    {
        N = 0;
        A = (T[]) new Comparable[max];
    }


    private boolean less(T[] A, int i, int j)
    {
        return A[i].compareTo(A[j]) < 0;
    }

    private void resize(int n)
    {
        max = n;
        T[] E = (T[]) new Comparable[max];
        for (int i = 1; i <= N; i++)
            E[i] = A[i];

        A = E;
    }

    private void exch(T[] A, int i, int j)
    {
        T t = A[i];
        A[i] = A[j];
        A[j] = t;
    }


    private void swim(int n)
    {
        while (n > 1 && less(A, n, n/2)) {
            exch(A, n, n/2);
            n = n/2;
        }
    }

    private void sink(int n)
    {
        while (2*n <= N) {
            int j = 2*n;
            if (((j+1) <= N)  && less(A, j+1, j))
                j++;
            
            if (!less(A, j, n))
                break;
            
            exch(A, j, n);
            n = j;
        }
    }

    public void insert(T e)
    {
        if (N+1 >= max)
            resize(2*max);

        A[++N] = e;
        swim(N);
    }

    public T delete()
    {
        exch(A, 1, N);
        N--;
        sink(1);
        return A[N+1];
    }

    private void printElements()
    {
        for (int i = 1; i <= N; i++)
            StdOut.print(A[i] + " ");
        StdOut.println();
    }

    public static void main(String[] args)
    {
        MinPQ<String> minpq = new MinPQ<String>();
    }
}
    
