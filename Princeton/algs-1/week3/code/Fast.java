import java.util.Arrays;
public class Fast {
    public static void  main(String[] args)
    {
        In in = new In(args[0]);
        int N = in.readInt();

        Point[] p = new Point[N];
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);
        StdDraw.setPenRadius(1.0/N);  // make the points a bit larger



        for (int i = 0; i < N; i++) {
            p[i] = new Point(in.readInt(), in.readInt());
            p[i].draw();
        }
        StdDraw.show(0);
        StdDraw.setPenRadius(0);  // make the points a bit larger
        Point[] tp = p.clone();
        Arrays.sort(tp);

        for (int i = 0; i < N; i++) {
            Arrays.sort(p, tp[i].SLOPE_ORDER);      //sort based on the backedup points data

            assert tp[i].slopeTo(p[0]) == Double.NEGATIVE_INFINITY; //first elements should be tp[i]
           
            int j, start;
            double slope;
            boolean skip;
            Point max;
            int count;

            j = 1;
            while (j < N) {

                skip = false;
                start = j;
                
                slope = p[0].slopeTo(p[j]);
                if (p[0].compareTo(p[j]) > 0) 
                    skip = true;

//                max = p[j++];
                j++;
                count = 2;  //p[0] and p[j] on line now
                while (j < N && (slope == p[0].slopeTo(p[j]))) {
                    if (p[0].compareTo(p[j]) > 0)       //mark this item that p[j] is less than p[0]
                        skip = true;                    //but according to or sorting at the top, we cover
                                                        //all the lowest orders first, so if we see order
                                                        //of hi , lo i.e it a repetetion, so skip the whole line
  //                  if (max.compareTo(p[j]) < 0)        //store the max 
  //                      max = p[j];
                    
                    count++;
                    j++;
                }
                if (!skip && count >= 4) { 
                    int end = j-1;
                    Arrays.sort(p, start, end+1);
                    p[0].drawTo(p[end]);
                    
                    StdOut.print(p[0].toString() + " -> ");
                    for (int k = start; k <= end-1; k++)
                        StdOut.print(p[k].toString() + " -> ");
                    StdOut.print(p[end].toString());
                    StdOut.println();
                }
            }
        }
        StdDraw.show(0);
    }
}
