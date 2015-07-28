import java.util.Comparator;
public class Point2D implements Comparable<Point2D>{
    public static final Comparator<Point2D>Y_ORDER = new byYorder();
    public final Comparator<Point2D>BY_POLAR_ORDER = new byPolarOrder();
    private final double x;
    private final double y;

    public Point2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    private static class byYorder implements Comparator<Point2D> {
   
        public int compare(Point2D A, Point2D B)
        {
            if (A.y < B.y) return -1;
            if (A.y > B.y) return +1;
            return  0;
        }
    }
    public double x()
    {
        return this.x;
    }
    public double y()
    { 
        return this.y;
    }

    private class byPolarOrder implements Comparator<Point2D> {
        
        public int compare(Point2D q1, Point2D q2)
        {
            double dx1 = q1.x - x;
            double dy1 = q1.y - y;
            double dx2 = q2.x - x;
            double dy2 = q2.y - y;

            if      (dy1 >= 0 && dy2 < 0) return -1;    // q1 above; q2 below
            else if (dy2 >= 0 && dy1 < 0) return +1;    // q1 below; q2 above
            else if (dy1 == 0 && dy2 == 0) {            // 3-collinear and horizontal
                if      (dx1 >= 0 && dx2 < 0) return -1;
                else if (dx2 >= 0 && dx1 < 0) return +1;
                else                          return  0;
            }
            else return -ccw(Point2D.this, q1, q2);     // both above or below

        }
    }
    public int compareTo(Point2D A)
    {
        if (this.y > A.y) return +1;
        if (this.y < A.y) return -1;
        if (this.x > A.x) return +1;
        if (this.x < A.x) return -1;
        return 0;
    }

    public static int ccw(Point2D A, Point2D B, Point2D C)
    {
        double area;
        area = (B.x - A.x) * (C.y - A.y) - (B.y - A.y) * (C.x - A.x);

        if      (area < 0) return -1; //clockwise
        else if (area > 0) return +1; //counter clock wise 
        else                return 0; //collinear
    }

}
