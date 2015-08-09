
public class KdTree {
    private static final boolean LEFT = true;
    private static final boolean RIGHT = false;
    private int N;
    private Node root;
    private static class Node {
        private Point2D p;
        private Node left, right;

        public Node(Point2D p)
        {
            this.p = p;
            this.left = null;
            this.right = null;
        }
    }

    public KdTree()
    {
        root = null;
        N = 0;
    }

    private Node createNode(Point2D p)
    {
        N++;
        return new Node(p);
    }

    private int compare(Point2D p1, Point2D p2, boolean odd)
    {
        if ((p1.x() == p2.x()) && p1.y() == p2.y()) 
            return 0;

        if (odd) {
            if   (p1.x() < p2.x())  return -1;
            else                    return +1;
        }
        else {
            if   (p1.y() < p2.y())  return -1;
            else                    return +1;
        }
    }

    private boolean contains(Node r, Point2D p, boolean level)
    {
        Node h = r;
        boolean odd = level;
        while (h != null) {
            int cmp = compare(p, h.p, odd);
            odd = !odd;
            if   (cmp < 0) h = h.left;
            else
            if   (cmp > 0) h = h.right;
            else           return true;
        }
        return false;
    }

    private RectHV getRect(boolean side, RectHV rect, Point2D p, boolean odd)
    {
        if (odd) {
            if (side == LEFT) return new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax());
            else              return new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax());
        }
        else {
            if (side == LEFT) return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
            else              return new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
         }   
    }


    public boolean contains(Point2D p)
    {
        return contains(root, p, true);
    }
    
    private Node put(Node h, Point2D p, boolean odd)
    {
        if (h == null) return createNode(p);

        int cmp = compare(p, h.p, odd);
        if      (cmp < 0)  h.left  = put(h.left,  p, !odd);
        else if (cmp > 0)  h.right = put(h.right, p, !odd); 
        return h;
    }

    public void insert(Point2D p)
    {
        root = put(root, p, true);
    }
    
    public boolean isEmpty()
    {
        return N == 0;
    }

    public int size()
    {
        return N;
    }
 
    private void inorder(Node h, RectHV rect, boolean odd)
    {
        if (h == null) return;
        Point2D p;

        if (odd) {
            RectHV r = new RectHV(rect.xmin(), rect.ymin(), h.p.x(), rect.ymax());
            inorder(h.left,  r, !odd);

            StdDraw.setPenColor(StdDraw.RED);
            p = new Point2D(h.p.x(), rect.ymin()); 
            StdDraw.setPenRadius(.03);
            h.p.draw();
            StdDraw.setPenRadius(.005);
            p.drawTo(new Point2D(h.p.x(), rect.ymax()));
            
            r = new RectHV(h.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
            StdOut.println("h.p = " + h.p.toString());
            inorder(h.right, r, !odd);
        }
        else {
            RectHV r = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), h.p.y());
            inorder(h.left, r, !odd);
            StdDraw.setPenColor(StdDraw.BLUE);
            p = new Point2D(rect.xmin(), h.p.y()); 
            StdDraw.setPenRadius(.03);
            h.p.draw();
            StdDraw.setPenRadius(.005);
            p.drawTo(new Point2D(rect.xmax(), h.p.y()));

            StdOut.println("h.p = " + h.p.toString());
            inorder(h.right, new RectHV(rect.xmin(), h.p.y(), rect.xmax(), rect.ymax()), !odd);
        }

    }
    
/*
    private void inorder(Node h, boolean odd, String dir)
    {
        Point2D p, q;

        if (h == null) return;

        inorder(h.left, !odd, "left");


        //StdDraw.setPenRadius(.03);
        //StdDraw.setPenColor(StdDraw.BLACK);
        //h.p.draw();
        if (odd) { 
            StdDraw.setPenColor(StdDraw.RED);
            p = new Point2D(h.p.x(), h.rect.ymin()); 
            q = new Point2D(h.p.x(), h.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            p = new Point2D(h.rect.xmin(), h.p.y());
            q = new Point2D(h.rect.xmax(), h.p.y());
        }

        StdDraw.setPenRadius(.005);
        p.drawTo(q);
        StdOut.println("h.p = " + h.p.toString() + " " +dir);
    
        inorder(h.right, !odd, "right");

    }

    private void preorder(Node h, String dir)
    {
        if (h == null) return;
        StdOut.println("h.p = " + h.p.toString() + " " +dir);
        preorder(h.left, "left");
        preorder(h.right, "right");
    }
    public void preorder()
    {
    preorder(root, "none");
    }

*/
    public void draw()
    {
        //inorder(root, true, "none");
        inorder(root, new RectHV(0, 0, 1, 1), true);
    }

    private void range(Node h, RectHV rect, double xmin, double ymin, double xmax, double ymax, boolean odd, Queue<Point2D> q)
    {
        if (h == null) return;
        
        boolean go = false;
        // a rectange cannot intersect with another, if one is completely above/below or to right/left
        // i.e like x1.max < x2.min or x2.max < x1.min ==> left/right
        // i.e      y1.max < y2.min or y2.max < y1.min -> below/above
        // four "or" conditions leads to false or apply negatition
        // results in four and conditions
        if (xmax >= rect.xmin() && ymax >= rect.ymin()
                && rect.xmax() >= xmin && rect.ymax() >= ymin)
            go = true;

        if (go) {
            if (rect.contains(h.p))
                q.enqueue(h.p);
           
            if (odd) range(h.left, rect, xmin, ymin, h.p.x(), ymax, !odd, q);     //xmax = h.p.x()
            else     range(h.left, rect, xmin, ymin, xmax, h.p.y(), !odd, q);     //ymax = h.p.y();

            if (odd) range(h.right, rect, h.p.x(), ymin, xmax, ymax, !odd, q);     //xmin = h.p.x();
            else     range(h.right, rect, xmin, h.p.y(), xmax, ymax, !odd, q);          //ymin = h.p.y();

        }
    }

    public Iterable<Point2D> range(RectHV rect)
    {
        Queue<Point2D> q = new Queue<>();
        range(root, rect, 0, 0, 1, 1, true, q);
        return q;
    }
    
    private Point2D nearest(Node h, Point2D p, Point2D q, boolean odd)
    {
        Point2D bestP = q;
        
        if (h != null) { 
            double hd, delta;
            double bestDist;
            int cmp;
            
            bestDist = bestP.distanceSquaredTo(p);
            hd = h.p.distanceSquaredTo(p);
            if (bestDist > hd) {
                bestDist = hd;
                bestP = h.p;
            }

            cmp = compare(p, h.p, odd);
            if (cmp < 0) {
                bestP = nearest(h.left, p, bestP, !odd);
                bestDist = bestP.distanceSquaredTo(p);
                if (odd) delta = h.p.x() - p.x();
                else     delta = h.p.y() - p.y();
                assert (delta >= 0);
                
                if (delta * delta < bestDist) { //if a point in left subtree is greater than the line passing thru the h then go thru right subtree else prune
                    bestP = nearest(h.right, p, bestP, !odd);
                }
            }
            else {
                bestP = nearest(h.right, p, bestP, !odd);
                bestDist = bestP.distanceSquaredTo(p);
                if (odd) delta = p.x() - h.p.x(); 
                else     delta = p.y() - h.p.y();

                assert (delta >= 0);
                if (delta * delta < bestDist) { //if a point in right subtree is greater than the line passing thru the h then go thru left btree else prune
                    bestP = nearest(h.left, p, bestP, !odd);
                }
            }
        }
        return bestP;
    }

    public Point2D nearest(Point2D p)
    {
        if (root != null)
           return nearest(root, p, root.p, true);
       
        return null;
    }
    
    public static void main(String[] args)
    {
        KdTree kd = new KdTree();
        In in  = new In(args[0]);
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kd.insert(p);
            StdOut.println("size = " + kd.size());
        }
        //kd.preorder();
    }
}
