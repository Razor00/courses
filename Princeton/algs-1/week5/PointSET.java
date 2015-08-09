/**
 * Created by madan on 8/7/15.
 */
import java.util.TreeSet;
import java.util.Iterator;
public class PointSET {
    private TreeSet<Point2D> set;

    public PointSET()                               // consetruct an empty set of points
    {
        set = new TreeSet<>();

    }
    public boolean isEmpty()                      // is the set empty?
    {
        return set.isEmpty();
    }


    public int size()                         // number of points in the set
    {
        return set.size();
    }

    private void checkData(Object p)
    {
        if (p == null)
            throw new java.lang.NullPointerException();
    }
    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        checkData(p);
        set.add(p);
    }
    public boolean contains(Point2D p)            // does the set contain point p?
    {
        checkData(p);
        return set.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        Iterator<Point2D> itr = set.iterator();
        while (itr.hasNext()) {
            Point2D p = itr.next();
            p.draw();
//            StdDraw.text(p.x(), p.y(), p.toString());

        }
    }
    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle
    {
        checkData(rect);
        Queue<Point2D> q = new Queue<>();
        Iterator<Point2D> itr = set.iterator();
        while (itr.hasNext()) {
            Point2D p = itr.next();
            if (rect.contains(p)) {
                q.enqueue(p);
            }
        }
        return q;
    }


    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        checkData(p);
        double minDist = 0;
        double dist;
        Point2D minPoint = null;
        Iterator<Point2D> itr = set.iterator();
        if (itr.hasNext()) {
            minPoint = itr.next();
            minDist = p.distanceSquaredTo(minPoint);
        }

        while (itr.hasNext()) {
            Point2D d = itr.next();
            dist = d.distanceSquaredTo(p);
            if (dist < minDist) {
                minDist = dist;
                minPoint = d;
            }
        }
        //StdOut.println("Brute = " + min_point + " dist = " + min_dist);
        return minPoint;
    }

    /*
       public static void main(String[] args)                  // unit testing of the methods (optional)
}
*/
}
