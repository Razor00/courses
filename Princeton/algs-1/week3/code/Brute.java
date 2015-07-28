import java.util.Arrays;
public class Brute {
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
        double s1, s2, s3;
        for (int i = 0; i < N-3; i++) {
            for (int j = i+1; j < N-2; j++) {
                s1 = p[i].slopeTo(p[j]);
                for (int k = j+1; k < N-1; k++) {
                    s2 = p[i].slopeTo(p[k]);
                    if (s1 == s2) {
                        for (int l = k+1; l < N; l++) {
                            s3 = p[i].slopeTo(p[l]);
                            if (s1 == s3) {
                                Point[] tp = {p[i], p[j], p[k], p[l]};
                                Arrays.sort(tp);
                                tp[0].drawTo(tp[3]);
                                StdOut.println(tp[0].toString() + " -> " + tp[1].toString() 
                                        + " -> " + tp[2].toString() + " -> " + tp[3].toString());

                            }
                        }
                    }
                }
            }
        }
        StdDraw.show(0);
    }
}
