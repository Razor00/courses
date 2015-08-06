import java.util.*;
public class LSI {
    final static char RIGHT = 1;
    final static char LEFT  = 2;
    final static char NONE  = 3;
    public static void main(String[] args)
    {
        int x1, y1, x2, y2;
        int i;
        Point p;
        MinPQ<Point> pq = new MinPQ<Point>();

        int N = StdIn.readInt();
        for (i = 0; i < N; i++) {
            x1 = StdIn.readInt();
            y1 = StdIn.readInt();
            x2 = StdIn.readInt();
            y2 = StdIn.readInt();
            if (x1 != x2) {
                pq.insert(new Point(x1, y1, LEFT));
                pq.insert(new Point(x2, y2, RIGHT));
            }
            else {
                pq.insert(new Point(x1, y1, NONE));
                pq.insert(new Point(x2, y2, NONE));
            }
        }
        Iterator <Point> itr = pq.iterator();
        while(itr.hasNext()) 
            itr.next().String();

        TreeSet<Integer> st = new TreeSet<>();
        while (!pq.isEmpty()) {
            p = pq.delMin();
            char pos = p.pos();
            if (pos == LEFT)
                st.add(p.y());
            else
            if (pos == RIGHT)
                st.remove(p.y());
            else
            if (pos == NONE) {
                Point p1, p2;
                p1 = p;
                p2 = pq.delMin();
                p1.String();
                p2.String();
                assert(p2.pos() == NONE);
                StdOut.println("Intersection Points = x = " + p1.x() + " " + st.subSet(p1.y(), true, p2.y(), true));
            }
            
        }
    }
}
