public class Sorts { 
    private <Item> void printElements(Item[] A, int lo, int hi, boolean show)
    {
        if (!show)
            return;

        StdOut.println("====================");
        for (int i = lo; i <= hi; i++)
            StdOut.print(A[i] + " ");
        StdOut.println();
    }


    private <Item> void printElements(Item[] A, boolean show)
    {
        printElements(A, 0, A.length-1, show);
    }
    
    private <Item> void printElements(Item[] A)
    {
        printElements(A, 0, A.length-1, true);
    }



    private <Item> void swap(Item[] A, int a, int b)
    {
        Item p = A[a];
        A[a] = A[b];
        A[b] = p;
    }
    
    private <Item>void swap(Item[] A, Item[] B)
    {
        Item[] t = A;
        A = B;
        B = t;
    }

 
    private static <Item extends Comparable<Item>> boolean less(Item i, Item j)
    {
        return i.compareTo(j) < 0;
    }

    private static <Item extends Comparable<Item>> boolean lessOrequal(Item i, Item j)
    {
        return i.compareTo(j) <= 0;
    }

    private static <Item extends Comparable<Item>> boolean equal(Item i, Item j)
    {
        return i.compareTo(j) == 0;
    }


    private static <Item extends Comparable<Item>>boolean isSorted(Item[] A)
    {
        return isSorted(A, 0, A.length-1);
    }
    private static <Item extends Comparable<Item>>boolean isSorted(Item[] A, int s, int h)
    {
        for (int i = s; i < h; i++)
            if(!lessOrequal(A[i], A[i+1]))
                return false;
        return true;
    }

    public <Item extends Comparable<Item>>void insertionsort(Item[] A, int s, int h)
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
    public <Item extends Comparable<Item>>void insertionsort(Item[] A)
    {
        insertionsort(A, 0, 1);
    }

    public <Item extends Comparable<Item>>void selectionsort(Item[] A)
    {
        int i, j, k;
        Item min;

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
    public <Item extends Comparable<Item>>void shellsort(Item[] A)
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

    public <Item extends Comparable<Item>> void merge(Item[] A, Item[] aux, int lo, int mid, int hi, boolean copy)
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


    public <Item extends Comparable<Item>> void top_mergesort(Item[] A, Item[] aux, int lo, int hi)
    {
        int m;
        
        if (lo < hi) {
            m = (lo + hi)/2;
            top_mergesort(A, aux, lo, m);
            top_mergesort(A, aux, m+1, hi);
            merge(A, aux, lo, m, hi, true);
        }
    }

   public <Item extends Comparable<Item>> void bottom_mergesort(Item[] A, Item[] aux)
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


    private <Item extends Comparable<Item>> int median3(Item[] A, int i, int j, int k)
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

    private <Item extends Comparable<Item>> int partition(Item[] A, int lo, int hi)
    {
        int i;
        int j;
        int pivot;

        Item v = A[lo];
        StdOut.println("v = " + v);
        i = lo;
        j = hi+1;
        while (true) {
            while (i < hi && less(A[++i], v));   //checking for equalto will lead to worst time
            while (less(v, A[--j]));
            if (i >= j) break;
            swap(A, i, j);
        }
        swap(A, lo, j);
        printElements(A, true);
//        StdOut.println("index = " + j);
        return j;
    }

    public <Item extends Comparable<Item>> Item select(Item[] A, int pos)
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
    public <Item extends Comparable<Item>> void qselectionsort(Item[] A)
    {
        for (int i = 0; i < A.length; i++)
            select(A, i);
    }

    public <Item extends Comparable<Item>> void quicksort(Item[] A, int lo, int hi)
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

    public <Item extends Comparable<Item>> void quicksort_3way(Item[] A, int lo, int hi)
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
        printElements(A);
        quicksort_3way(A, lo, lt-1);
        quicksort_3way(A, gt+1, hi);
    }


    public static <Item extends Comparable<Item>> double time(Item[] A, Item[] B, String str, String arg)
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
               // StdRandom.shuffle(A);
                S.quicksort(A, 0, A.length-1);
                break;
    
            case "qselectionsort":
    //            StdRandom.shuffle(A);
                S.qselectionsort(A);
                break;
            
            case "quicksort_3way":
//                StdRandom.shuffle(A);
                S.quicksort_3way(A, 0, A.length-1);
                break;



            case "select":
                if (arg == null) {
                    StdOut.println("select pos");
                    break;
                }
                StdOut.println(S.select(A, Integer.parseInt(arg)));
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
            StdOut.println("Usage:javac programname sortname[shellsort|insertionsort|selectionsort|quicksort|top_mergesort|bottom_mergesort|select]");
            return;
        }
        String[] str   = StdIn.readAllStrings();
        String[] aux   = str.clone();//new String[str.length];
        if (args.length >= 2)
            StdOut.println(args[0] + " "+ "Time = " + time(str, aux, args[0], args[1]));
        else
            StdOut.println(args[0] + " "+ "Time = " + time(str, aux, args[0], ""));

    }
}
