public class Sorts { 
    private <T> void printElements(T[] A, int lo, int hi, boolean show)
    {
        if (!show)
            return;

        StdOut.println("====================");
        for (int i = lo; i <= hi; i++)
            StdOut.print(A[i] + " ");
        StdOut.println();
    }


    private <T> void printElements(T[] A, boolean show)
    {
        printElements(A, 0, A.length-1, show);
    }
    
    private <T> void printElements(T[] A)
    {
        printElements(A, 0, A.length-1, true);
    }



    private <T> void swap(T[] A, int a, int b)
    {
        T p = A[a];
        A[a] = A[b];
        A[b] = p;
    }
    
    private <T>void swap(T[] A, T[] B)
    {
        T[] t = A;
        A = B;
        B = t;
    }

 
    private static <T extends Comparable<T>> boolean less(T[] A, int i, int j)
    {
        return A[i].compareTo(A[j]) < 0;
    }
    private static <T extends Comparable<T>> boolean less(T i, T j)
    {
        return i.compareTo(j) < 0;
    }

    private static <T extends Comparable<T>> boolean lessOrequal(T i, T j)
    {
        return i.compareTo(j) <= 0;
    }

    private static <T extends Comparable<T>> boolean equal(T i, T j)
    {
        return i.compareTo(j) == 0;
    }


    private static <T extends Comparable<T>>boolean isSorted(T[] A)
    {
        return isSorted(A, 0, A.length-1);
    }
    private static <T extends Comparable<T>>boolean isSorted(T[] A, int s, int h)
    {
        for (int i = s; i < h; i++)
            if(!lessOrequal(A[i], A[i+1]))
                return false;
        return true;
    }

    public <T extends Comparable<T>>void insertionsort(T[] A, int s, int h)
    {
        int i, j, k;
        for (i = s; i < A.length; i += h) {
            k = i;
            for (j = i-h; j >= 0; j -= h) {
                if (A[k].compareTo(A[j]) >= 0) 
                    break;
                swap(A, k, j);
                k = j;
            }
            printElements(A, false);
            StdIn.readLine();
        }
    }
    public <T extends Comparable<T>>void insertionsort(T[] A)
    {
        insertionsort(A, 0, 1);
    }

    public <T extends Comparable<T>>void selectionsort(T[] A)
    {
        int i, j, k;
        T min;

        if (A.length == 1)
            return;

        for (i = 0; i < (A.length-1); i++) { //last element would be in the correct position
            min = A[i];
            k   = i;
            for (j = i+1; j < A.length; j++) {
                if (min.compareTo(A[j]) > 0) {
                    min = A[j];
                    k   = j;
                }
            }
            swap(A, i, k);
            printElements(A, false);
        }

    }

    public static void exch(Comparable[] a, int i, int j)
    {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
/**
    public static void sort(Comparable[] a)
    { // Sort a[] into increasing order.
        int N = a.length;
        int h = 1;
        while (h < N/3) h = 3*h + 1; // 1, 4, 13, 40, 121, 364, 1093, ...
        while (h >= 1)
        { // h-sort the array.
            for (int i = h; i < N; i++)
            { // Insert a[i] among a[i-h], a[i-2*h], a[i-3*h]... .
                for (int j = i; j >= h && a[j].compareTo(a[j-h]) < 0; j -= h)
                    exch(a, j, j-h);
            }
            h = h/3;
        }
    }
**/
    public <T extends Comparable<T>>void shellsort(T[] A)
    {
        int i;
        int h = 1;
        while (h < A.length/3)
            h = h*3+1;

        while (h >= 1) { 
            for (i = 0; i < h; i++)  
                insertionsort(A, i, h);
            h = h/3;
            printElements(A, false);
            StdIn.readLine();
        }
    }

    public <T extends Comparable<T>> void merge(T[] A, T[] aux, int lo, int mid, int hi, boolean copy)
    {
        int i, j, k;
        i = lo;
        j = mid+1;

        if (copy) {
            for (int p = lo; p <= hi; p++)
                aux[p] = A[p];
        }

        //       StdOut.println("lo = " + lo + " mid = " + mid + " high = "+ hi);
        for (k = lo; k <= hi; k++) {
            if      (i > mid)               A[k] = aux[j++];
            else if (j > hi)                A[k] = aux[i++];
            else if (less(aux[i], aux[j]))  A[k] = aux[i++];
            else                            A[k] = aux[j++];
        }
        //        printElements(A, true);
    
    }


    public <T extends Comparable<T>> void top_mergesort(T[] A, T[] aux, int lo, int hi)
    {
        int m;
        
        if (lo < hi) {
            m = (lo + hi)/2;
            top_mergesort(A, aux, lo, m);
            top_mergesort(A, aux, m+1, hi);
            merge(A, aux, lo, m, hi, true);
        }
    }

   public <T extends Comparable<T>> void bottom_mergesort(T[] A, T[] aux)
    {
        int m, h;
        int lo, hi;
        lo = 0;
        hi = A.length-1;
        /* really really important, the sub-arrays should be power of 2 except for the last 1*/
        for (int sz = 1; sz <= hi; sz = 2*sz) {
            for (int l = lo; l <= hi; l += 2*sz) {
                h = Math.min(l+2*sz-1, A.length-1); //here we are making sure that the left subarray is power of 2 for all arrays
                merge(A, aux, l, l+sz-1, h, true);
            }
        }
    }


    private <T extends Comparable<T>> int median3(T[] A, int i, int j, int k)
    {
        int p = i;

        if (true)
            return p;

        if (less(A[i], A[j])) {
            if (less(A[j], A[k]))           p = j;
            else if (less(A[i], A[k]))      p = k;
            else                            p = i;
        }
        else {
            if (less(A[k], A[j]))           p = j;
            else if (less(A[k], A[i]))      p = k;
            else                            p = i;

        }
       return p; 
    }

    private <T extends Comparable<T>> int partition(T[] A, int lo, int hi)
    {
        int i;
        int j;
        int pivot;

        T v = A[lo];
        i = lo;
        j = hi+1;
        while (true) {
            while (i < hi && less(A[++i], v));   //checking for equalto will lead to worst time
            while (less(v, A[--j]));
            if (i >= j) break;
            swap(A, i, j);
        }
        swap(A, lo, j);
        return j;
    }

    public <T extends Comparable<T>> T select(T[] A, int pos)
    {
        int j;
        int hi, lo;
        lo = 0;
        hi = A.length-1;
        assert (pos <= hi):"given pos is greater than size of array";
            assert (pos >= lo):"given pos is lesser than 0";
                while (true) {
                    j = partition(A, lo, hi);
                    if (pos == j) {
                        return A[j];
                    }
                    if (pos < j)
                        hi = j-1;
                    else
                        lo = j+1;
                }
    }
    public <T extends Comparable<T>> void qselectionsort(T[] A)
    {
        for (int i = 0; i < A.length; i++)
            select(A, i);
    }

    public <T extends Comparable<T>> void quicksort(T[] A, int lo, int hi)
    {

        int p, l;

        if (lo < hi) {
        //    p = median3(A, lo, (lo+hi)/2, hi);    
        //    swap(A, lo, p);
            
            l = partition(A, lo, hi);
            quicksort(A, lo, l-1);
            quicksort(A, l+1, hi);
        }
    }

    public <T extends Comparable<T>> void quicksort_3way(T[] A, int lo, int hi)
    {
        
        if (lo >= hi)
            return;

        int lt, gt, i;
        int cmp;

        lt = lo;
        gt = hi;
        i = lo;

        while (i <= gt) {
            cmp = A[i].compareTo(A[lt]);
            if (cmp < 0) 
                swap(A, lt++, i++);
            else
            if (cmp > 0)
                swap(A, i, gt--);
            else
                i++;
        }
        //printElements(A);
        quicksort_3way(A, lo, lt-1);
        quicksort_3way(A, gt+1, hi);
    }


    private <T extends Comparable<T>> void swim(T[] A, int i)
    {
        while (i > 1 && less(A, i, i/2)) {
            swap(A, i, i/2);
            i = i/2;
        }
    }

    private <T extends Comparable<T>> void sink(T[] A, int p, int N)
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

    public <T extends Comparable<T>> void heapsort(T[] A, boolean print)
    {
        int N = A.length;
        T[] E = (T[])new Comparable[N+1];

        for (int i = 0; i < N; i++) {
            E[i+1] = A[i];
        }
        A = E; 
        for (int i = N/2; i >= 1; i--)
            sink(A, i, N);

        for (int i = 1, K = N; i <= N; i++, K--) {
            if (print)
                StdOut.print(A[1] + " ");
            swap(A, 1, K); 
            sink(A, 1, K-1);
        }
        if (print)
            StdOut.println();
    }



    public static <T extends Comparable<T>> double time(T[] A, T[] B, String str, String arg)
    {
        Stopwatch timer = new Stopwatch();

        Sorts S = new Sorts(); 
        switch (str) {

            case "shellsort":
                S.shellsort(A);
                break;

            case "insertionsort":
                S.insertionsort(A);
                break;

            case "selectionsort":
                S.selectionsort(A);
                break;

            case "top_mergesort":
                S.top_mergesort(A, B, 0, A.length-1);
                break;

            case "bottom_mergesort":
                S.bottom_mergesort(A, B);
                break;

            case "quicksort":
                StdRandom.shuffle(A);
                S.quicksort(A, 0, A.length-1);
                break;
    
            case "qselectionsort":
                StdRandom.shuffle(A);
                S.qselectionsort(A);
                break;
            
            case "quicksort_3way":
                StdRandom.shuffle(A);
                S.quicksort_3way(A, 0, A.length-1);
                break;

            case "select":
                if (arg == null) {
                    StdOut.println("select pos");
                    break;
                }
                StdOut.println(S.select(A, Integer.parseInt(arg)));
                break;

            case "heapsort":
                S.heapsort(A, arg.equals("show"));
                break;


            default:
                StdOut.println("Invalid sort "+ str);
                break;
        }
        double t = timer.elapsedTime();

        assert isSorted(A);
        if (arg != null && arg.equals("show"))
            S.printElements(A, true);
        return t;
    }

    public static void main(String[] args)
    {
        if (args.length == 0 || args[0] == null) {
            StdOut.println("Usage:javac programname sortname[shellsort|insertionsort|selectionsort");
            StdOut.println("|quicksort|top_mergesort|bottom_mergesort|select|heapsort]");
            return;
        }
        String[] str   = StdIn.readAllStrings();
        String[] aux   = str.clone();//new String[str.length];
        if (args.length >= 2) 
            StdOut.printf("Algo = %-18s, Time = %s\n", args[0], time(str, aux, args[0], args[1]));
        else
            StdOut.printf("Algo = %-18s, Time = %s\n", args[0], time(str, aux, args[0], ""));

    }
}
