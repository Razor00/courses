import java.util.Arrays;

public class hull {
    public static void main(String args[]) 
    {
        Stack<Point2D> hull = new Stack<Point2D>();
        String[] str = StdIn.readAllStrings(); 

        Point2D[] points = new Point2D[str.length];

        for (int i = 0; i < str.length; i += 2) {
            points[i] = new Point2D(Integer.parseInt(str[i]), Integer.parseInt(str[i+1]));
        }
        Arrays.sort(points, Point2D.Y_ORDER);
        Arrays.sort(points, points[0].BY_POLAR_ORDER);
        hull.push(points[0]);
        hull.push(points[1]);

        for (int i = 2; i < points.length; i++) {
            Point2D top = hull.pop();
            while (Point2D.ccw(hull.peek(), top, points[i]) <= 0)
                top = hull.pop();
            hull.push(top);
            hull.push(points[i]);
        }
       
        while(!hull.isEmpty()) 
            StdOut.print(hull.pop() + " ");
        StdOut.println();
    }
}
