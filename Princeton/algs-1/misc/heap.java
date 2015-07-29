public class heap<T extends Comparable<T>> {
    private void swap(T[] A, int i, int j)
    {
        T t;
        t    = A[i];
        A[i] = A[j];
        A[j] = t;
    }

    private boolean less(T[] A, int i, int j)
    {
        return A[i].compareTo(A[j]) < 0; 
    }

    private void swim(T[] A, int i)
    {
        while (i > 1 && less(A, i, i/2)) {
            swap(A, i, i/2);
            i = i/2;
        }
    }
    void printElements(T[] A, int l)
    {
        for (int i = 1; i <= l; i++)
            StdOut.print(A[i]);
        StdOut.println();
    }

    private void sink(T[] A, int p, int N)
    {

        int c; 
        while (2*p <= N) {
            c = 2*p;
            if (((c+1) <= N) && less(A, c+1, c)) 
                c++;

            if (!less(A, c, p))
                break;

            swap(A, c, p);
            p = c; 
        }


    }

    public void sort(T[] E)
    {
        T[] A = (T[])new Comparable[E.length+1];

        for (int i = 0; i < E.length; i++) {
            A[i+1] = E[i];
        }
        
        int N = E.length;
        for (int i = N/2; i >= 1; i--)
            sink(A, i, N);
       // StdOut.println("done"); 
        for (int i = 1, K = N; i <= N; i++, K--) {
            StdOut.print(A[1] + " ");
            swap(A, 1, K); 
            sink(A, 1, K-1);
        }
        StdOut.println();
    }

    public static void main(String[] args)
    {
        String[] str = StdIn.readAllStrings();
        heap<String> h = new heap<String>();
        h.sort(str);
    }
}
