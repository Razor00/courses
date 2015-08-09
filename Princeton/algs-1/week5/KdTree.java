
public class KdTree {
    private static final boolean LEFT = true;
    private static final boolean RIGHT = false;
    private int N;
    private Node root;
    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node left, right;

        public Node(Point2D p, RectHV rect)
        {
            this.p = p;
            this.rect = rect;
            this.left = null;
            this.right = null;
        }
    }

    public KdTree()
    {
        root = null;
        N = 0;
    }

    private Node createNode(Point2D p, RectHV rect)
    {
        N++;
        return new Node(p, rect);
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
    
    private Node put(Node h, RectHV rect, Point2D p, boolean odd)
    {
        if (h == null) return createNode(p, rect);

        int cmp = compare(p, h.p, odd);
        if      (cmp < 0)  h.left  = put(h.left,  getRect(LEFT, rect, h.p, odd), p, !odd);
        else if (cmp > 0)  h.right = put(h.right, getRect(RIGHT, rect, h.p, odd), p, !odd); 
        return h;
    }

    public void insert(Point2D p)
    {
        root = put(root, new RectHV(0, 0, 1, 1), p, true);
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

        StdOut.println(h.p.toString() + " "+  h.rect.toString() + " " +rect.toString());
        if (odd) {
            RectHV r = new RectHV(rect.xmin(), rect.ymin(), h.p.x(), rect.ymax());
        //    assert(h.rect.equals(r) == true);
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
//            assert(h.rect.equals(r) == true);
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
//    public void preorder()
//    {
//        preorder(root, "none");
//    }
    public void draw()
    {
        inorder(root, true, "none");
//        inorder(root, new RectHV(0, 0, 1, 1), true);
    }

    private void range(Node h, RectHV rect, Queue<Point2D> q)
    {
        if (h == null) return;

        if (rect.intersects(h.rect)) {
            if (rect.contains(h.p))
                q.enqueue(h.p);
            range(h.left, rect, q);
            range(h.right, rect, q);
        }
    }

    public Iterable<Point2D> range(RectHV rect)
    {
        Queue<Point2D> q = new Queue<>();
        range(root, rect, q);
        return q;
    }
    
    private Point2D nearest(Node h, Point2D p, Point2D q, boolean odd)
    {
        Point2D bestP = q;
//        StdOut.println("bestpoint so far = " + q.toString());
//        if (h == null)
//            StdOut.println("node is null returning");
        if (h != null) { 
            double hd, delta;
            double bestDist;
            int cmp;
            
            bestDist = bestP.distanceSquaredTo(p);
            hd = h.p.distanceSquaredTo(p);
//            StdOut.println("h = " + " " +h.p.toString()  + " hd = " + hd + " q = " + q.toString()+ " bestdistance = " + bestDist);
            if (bestDist > hd) {
                bestDist = hd;
                bestP = h.p;
            }

            cmp = compare(p, h.p, odd);
            if (cmp < 0) {
//                StdOut.println("Trying to Going left");
                bestP = nearest(h.left, p, bestP, !odd);
//                StdOut.println("Returned: Trying to Going left");
                bestDist = bestP.distanceSquaredTo(p);
                if (odd) delta = h.p.x() - p.x();
                else     delta = h.p.y() - p.y();
                assert (delta >= 0);
                if (delta * delta < bestDist) { //if a point in left subtree is greater than the line passing thru the h then go thru right subtree else prune
//                    StdOut.println("Trying to Going right");
                    bestP = nearest(h.right, p, bestP, !odd);
                }
            }
            else {
//                StdOut.println("Going Right");
                bestP = nearest(h.right, p, bestP, !odd);
                bestDist = bestP.distanceSquaredTo(p);
                if (odd) delta = p.x() - h.p.x(); 
                else     delta = p.y() - h.p.y();

                assert (delta >= 0);
                if (delta * delta < bestDist) { //if a point in left subtree is greater than the line passing thru the h then go thru right subtree else prune
                    bestP = nearest(h.left, p, bestP, !odd);
//                    StdOut.println("Going left");
                }
            }
        }
        return bestP;
    }

    public Point2D nearest(Point2D p)
    {
//        StdOut.println("===========================");
        if (root != null)
           return nearest(root, p, root.p, true);
        else
            return null;
//        StdOut.println("given point = " + p.toString());
//        StdOut.println("kdtree point = " + p1.toString() + " dist = " + p1.distanceSquaredTo(p));
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


        /*
           boolean flag = false;
           while (flag) {
           StdOut.printf("\n\n i: Insert\n g: Get \n p: Print keys\n r: Rank \n c: Ceiling\n f: Floor\n d: Delete\n");
           StdOut.printf(" l: Order \n R: Range :\n x: Exit \n\n");
           String ch = StdIn.readString();
           switch (ch) {
           case "i":
           StdOut.printf("Enter key (space) value:");
           k = StdIn.readString();
           v = StdIn.readInt();
           st.put(k, v);
           break;
           case "g":
           StdOut.printf("Enter key :");
           k = StdIn.readString();
           v = st.get(k);
           if (v == null) 
           StdOut.println("Value of Key " + k + ": null");
           else
           StdOut.println("Value of Key " + k + ":" + v);
           break;

           case "p":
           for (String s:st.keys())
           StdOut.print(s + " ");
           StdOut.println();
           break;

           case "r":
           StdOut.printf("Enter key:");
           k = StdIn.readString();
           StdOut.println("Rank " + k + " = " + st.rank(k));
           break;

           case "c":
           StdOut.printf("Enter key:");
           k = StdIn.readString();
           String  ceil = st.ceiling(k);
           if (ceil == null) ceil = "null";
           StdOut.println("Ceiling " + k + " = " + ceil);
           break;

           case "f":
           StdOut.printf("Enter key:");
           k = StdIn.readString();
           String  floor = st.floor(k);
           if (floor == null) floor = "null";
           StdOut.println("Floor " + k + " = " + floor);
           break;

           case "l":
           StdOut.printf("Enter position:");
           v = StdIn.readInt();
           StdOut.println("Item at Position: "+ v + " " + st.getItem(v));
           break;

           case "d":
           StdOut.printf("Enter key:");
           k = StdIn.readString();
           st.delete(k);
           break;

           case "h":
           StdOut.println("Height = " + st.height());
           break;

           case "R":
           int r1, r2;
           StdOut.printf("Enter key1 and key2: ");
           r1 = StdIn.readInt();
        r2 = StdIn.readInt();
        StdOut.println("Range " + "(" + r1 + "," + r2 + ")" + " = " + st.range(r1, r2));
        break;

        case "x":
        return;

        default:
        StdOut.println("Invalid option chosen = " + ch);
        break;
           }
           }
    */

    }
}
