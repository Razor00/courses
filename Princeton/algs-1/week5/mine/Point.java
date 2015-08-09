import java.util.*;
public class Point implements Comparable <Point>{
        final private int x;
        final private int y;
        private char pos;
        public Point(int X, int Y, char POS)
        {
            x = X;
            y = Y;
            pos = POS;
        }

        public int compareTo(Point that)
        {
            if (this.x < that.x) return -1;
            if (this.x > that.x) return +1;
            if (this.y < that.y) return -1;
            if (this.y > that.y) return +1;
            return 0;
        }
        public char pos()
        {
            return pos;
        }
        public int x()
        {
            return x;
        }
        public int y()
        {
            return y;
        }
        public void String()
        {
            StdOut.println(x + " " + y);
        }
    }
