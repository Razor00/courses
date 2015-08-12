import java.util.*;

public class RB<Key extends Comparable<Key>, Value> {
    private final boolean RED=true;
    private final boolean BLACK=false;

    private int N;
    Node root;
    private class Node {
        Key key;
        Value value;
        int   count;        //number of nodes on left and right subtree
        boolean color; 
        Node left, right;
        public Node(Key k, Value v)
        {
            key     = k;
            value   = v;
            count   = 1;
            left    = null;
            right   = null;
            color   = RED;
        }
    }

    public RB()
    {
        N = 0;
    }
   
    private boolean isRed(Node node)
    {
        if (node == null) return false;

        return node.color == RED;
    }

    private Node rotateLeft(Node h)
    {
        assert(isRed(h.right));
        Node x = h.right;
        h.right = x.left;
        x.left  = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private Node rotateRight(Node h)
    {
        assert(isRed(h.left));
        Node x = h.left;
        h.left  = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(Node h)
    {
        assert(!isRed(h));
        assert(isRed(h.left));
        assert(isRed(h.right));
        
        h.left.color  = BLACK;
        h.right.color = BLACK;
        h.color = RED;
    }


    private Node insert(Node node, Key key, Value value)
    {
        if (node == null) return  new Node(key, value);
        
        int cmp = key.compareTo(node.key);

        if (cmp < 0)        
            node.left = insert(node.left, key, value);
        else 
        if (cmp > 0)   
            node.right = insert(node.right, key, value);
        else
            node.value = value;

        node.count = size(node.left) + size(node.right) + 1;

         
        // every parent  node is traversed, so we can see the left and right children from here.
        // this order is important
        // 1. First check for Right red node (rotateLeft)
        // 2. Check for two red left node (rotate right)
        // 3. flipcolors if have red nodes on left and right
        
        if (isRed(node.right) && !isRed(node.left))     node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left))  node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right))      flipColors(node);
       
        return node;

    }

    private Node search(Node node, Key key)
    {
        int cmp;
        while (node != null) {

            cmp = key.compareTo(node.key);

            if (cmp < 0) 
                node = node.left;
            else
            if (cmp > 0) 
                node = node.right;
            else    
                return node;

        }
        return null;
    }

    // key immediately smaller than the given key
    public Key floor(Key key)
    {
        if (key == null)
            return null;

        Node n = floor(root, key);
        if (n == null)
            return null;

        return n.key;
    }

    private Node floor(Node node, Key key)
    {
        if (node == null)
            return null;

        int cmp = key.compareTo(node.key);

        if (cmp == 0)   return node;
        if (cmp < 0)    return floor(node.left, key);

    /*
     * if a key is greater than the current node then the 
     * smallest key in the right subtree is the floor 
     */ 
        Node rnode = floor(node.right, key);
        if (rnode == null)
            return node;
        else
            return rnode;
    }
 
    //key immediately larger than the provided key
    public Key ceiling(Key key)
    {
        if (key == null)
            return null;

        Node n = ceiling(root, key);
        if (n == null)
            return null;

        return n.key;
    }

    private Node ceiling(Node node, Key key)
    {
        if (node == null)
            return null;

        int cmp = key.compareTo(node.key);

        if (cmp == 0)   return node;
        if (cmp > 0)    return ceiling(node.right, key);

    /*
     * if a key is less than the current node then the 
     * largest key in the left subtree is the ceiling 
     */ 
        Node lnode = ceiling(node.left, key);
        if (lnode == null)
            return node;
        else
            return lnode;
    }


    public int rank(Key key) 
    {
        return rank(root, key);
    }

    //Position of the key, if its order among the keys
    private int rank(Node node, Key key)
    {
        if (node == null)   return 0;

        int cmp = key.compareTo(node.key); 
        
        if (cmp == 0)   return size(node.left);
        if (cmp <  0)   return rank(node.left, key);

        // if we go to the right, then the rank includes
        // 1. current node 
        // 2. left subtree nodes
        // 3. rank of right subtree
        return 1 + size(node.left) + rank(node.right, key); 
    }

    private Node min(Node node)
    {
        if (node == null) return null;

        while (node.left != null)
            node = node.left;

        return node;
    }

    private Node max(Node node)
    {
        if (node == null) return null;

        while (node.right != null)
            node = node.right;

        return node;
    }


    public void put(Key key, Value value)
    {
        root = insert(root, key, value);
        root.color = BLACK;
    }
    
    public Value get(Key key)
    {
        Node node = search(root, key);
        if (node == null)
            return null;

        return node.value;
    }

    public boolean contains(Key key)
    {
        return get(key) != null;
    }


    private Node deleteMin(Node node)
    {
        if (node.left == null) return node.right;

        node.left   = deleteMin(node.left);
        node.count  = 1 + size(node.left) + size(node.right);
        return node;
    }

    private Node delete(Node node, Key key)
    {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);

        if (cmp < 0) 
            node.left  = delete(node.left, key);
        else
        if (cmp > 0) 
            node.right = delete(node.right, key); 
        else {
            if (node.right == null) return node.left;
            if (node.left  == null) return node.right;

            // if a node has both it's children, then 
            // make the smallest child of the right 
            // subtree the current node
            Node T = node;
            node       = min(T.right);
            node.right = deleteMin(T.right);
            node.left  = T.left;
            node.count = 1 + size(node.left) + size(node.right);
        }
        return node;
    }

    public void delete(Key key)
    {
        if (root == null)
            return;

        root = delete(root, key);
    }

    private Key getItem(Node node, int pos)
    {
        int lsz, rsz;
        while (node != null) {
            lsz = size(node.left);
            rsz = size(node.right);
            if (pos <= lsz) {
                node = node.left;
            }
            else {
                pos = pos - lsz - 1;
                if (pos == 0)
                    return node.key;
                node = node.right;
            }
        }
        return null;
    }

    public Key getItem(int pos)
    {
        return getItem(root, pos);
    }

    public void inorder()
    {
        inorder(root);
    }

    private void inorder(Node node)
    {
        if (node == null) return;
        inorder(node.left);
        StdOut.println(node.key + " " + node.color + ",");
        inorder(node.right);
    }

    private void inorder(Node node, Queue<Key> q)
    {
        if (node == null) return;
        inorder(node.left, q);
        q.enqueue(node.key);
        inorder(node.right, q);
    }

    private void levelorder(Node node, Queue<Key> q)
    {
        Queue<Node> t  = new Queue<>();
        if (node != null)
            t.enqueue(node);
        Key k;
        Node np;
        while (!t.isEmpty()) {
            np = t.dequeue();
            q.enqueue(np.key);
            
            if (np.left != null)
                t.enqueue(np.left);

            if (np.right != null)
                t.enqueue(np.right);
        }
    }
    private int range(Node h, Queue<Key> q, int pos, int lo, int hi)
    {
        if (h == null)  return 0;
        if (lo > hi)    return 0;

        if (lo < pos && h.left != null) { 
            int off = 1 + size(h.left.right);
            lo = range(h.left, q, pos - off, lo, hi);
        }

        if (lo <= hi && pos == lo) { 
            q.enqueue(h.key);
            lo++;
        }
       
        if (lo > pos & h.right != null) { 
            int off = 1 + size(h.right.left);
            lo = range(h.right, q, pos + off, lo, hi);
        }         
        return lo;
    }

    public Iterable<Key> range(int lo, int hi)
    {
        assert(lo <= hi);
        assert(lo > 0);
        assert(root != null);
        Queue<Key> q = new Queue<>();
        range(root, q, (1 + size(root.left)), lo, hi);

        return q;
    }
    public Iterable<Key> keys()
    {
        Queue<Key> queue = new Queue<>();
        inorder(root, queue);
        //levelorder(root, queue);
        return queue;
    }


    public boolean isEmpty()
    {
        return root == null;
    }

    public int size(Node node)
    {
        if (node == null) return 0;
        return node.count;
    }

    private int height(Node node)
    {
        if (node == null) return 0;
        return Math.max(height(node.left), height(node.right)) + 1;
    }

    public int height()
    {
        return height(root)-1;
    }

    public boolean isBalanced()
    {
        Node node = root;
        int black = 0;
        while (node != null) {
            if (!isRed(node)) black++;
            node = node.left;
        }
        return isBalanced(root, black);
    }

    public boolean isBalanced(Node node, int black)
    {
        if (node == null) return black == 0;
        if (!isRed(node)) black--;
        return (isBalanced(node.left, black) && isBalanced(node.right, black));
    }

    public static void main(String[] args)
    {
        RB<String, Integer> rb = new RB<>();
        String k;
        Integer v;
        while (true) {
            StdOut.printf("\n\n i: Insert\n g: Get \n p: Print keys\n r: Rank \n c: Ceiling\n f: Floor\n d: Delete\n");
            StdOut.printf(" l: Order \n h: height \n R: Range \n x: Exit \n\n");
            String ch = StdIn.readString();
            switch (ch) {
                case "i":
                    StdOut.printf("Enter key (space) value:");
                    k = StdIn.readString();
                    v = StdIn.readInt();
                    rb.put(k, v);
                    break;
                case "g":
                    StdOut.printf("Enter key :");
                    k = StdIn.readString();
                    v = rb.get(k);
                    if (v == null) 
                        StdOut.println("Value of Key " + k + ": null");
                    else
                        StdOut.println("Value of Key " + k + ":" + v);
                    break;
    
                case "p":
                    for (String s:rb.keys())
                        StdOut.print(s + ":");
                    StdOut.println();
                    break;

                case "r":
                    StdOut.printf("Enter key:");
                    k = StdIn.readString();
                    StdOut.println("Rank " + k + " = " + rb.rank(k));
                    break;
                
                case "c":
                    StdOut.printf("Enter key:");
                    k = StdIn.readString();
                    String  ceil = rb.ceiling(k);
                    if (ceil == null) ceil = "null";
                    StdOut.println("Ceiling " + k + " = " + ceil);
                    break;

                case "f":
                    StdOut.printf("Enter key:");
                    k = StdIn.readString();
                    String  floor = rb.floor(k);
                    if (floor == null) floor = "null";
                    StdOut.println("Floor " + k + " = " + floor);
                    break;
                
                case "l":
                    StdOut.printf("Enter position:");
                    v = StdIn.readInt();
                    StdOut.println("Item at Position: "+ v + " " + rb.getItem(v));
                    break;

                case "d":
                    StdOut.printf("Enter key:");
                    k = StdIn.readString();
                    rb.delete(k);
                    break;

                case "h":
                    StdOut.println("Height = " + rb.height());
                    break;

                case "R":
                    int r1, r2;
                    StdOut.printf("Enter key1 and key2: ");
                    r1 = StdIn.readInt();
                    r2 = StdIn.readInt();
                    StdOut.println("Range " + "(" + r1 + "," + r2 + ")" + " = " + rb.range(r1, r2));
                    break;

                case "x":
                    return;

                default:
                    StdOut.println("Invalid option chosen = " + ch);
                    break;
            }
//            rb.inorder();

            assert(rb.isBalanced());
        }
    }
}
