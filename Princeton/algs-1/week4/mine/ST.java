import java.util.*;

public class ST<Key extends Comparable<Key>, Value> {
    private int N;
    Node root;
    private class Node {
        Key key;
        Value value;
        int   count;        //number of nodes on left and right subtree
        Node left, right;
        public Node(Key k, Value v)
        {
            key     = k;
            value   = v;
            count   = 1;
            left    = null;
            right   = null;
        }
    }

    public ST()
    {
        N = 0;
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
        return rank(root, key) + 1;
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

    public Iterable<Key> keys()
    {
        Queue<Key> queue = new Queue<>();
        inorder(root, queue);
        //levelorder(root, queue);
        return queue;
    }


    private int range(Node h, Queue<Key> q, int pos, int lo, int hi)
    {
        if (h == null)  return 0;
        if (lo > hi)    return 0;

        if (lo < pos && h.left != null) { 
            int off = 1 + size(h.left.right);
            lo = range(h.left, q, pos - off, lo, hi);
        }
        //   A (1)
        //    \
        //     D(3)
        //   /   \
        //  C(2)   Z(4)
        //  After finish C, it would go back to D right
        //  and D is above code.
        //  lo = 2+1, pos = 3
        //  as lo > hi, we should not print anymore, but below code without that check would print it.

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
        Queue<Key> queue = new Queue<>();
        assert(lo <= hi);
        
        if (lo <= size(root))
            range(root, queue, size(root.left) + 1, lo, hi);
        return queue;
    }

    private int height(Node h)
    {
        if (h == null) return 0;

        return Math.max(height(h.left), height(h.right)) + 1;
    }

    public int height()
    {
        return height(root)-1;
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

    public static void main(String[] args)
    {
        ST<String, Integer> st = new ST<>();
        String k;
        Integer v;
/*
        String[] str = StdIn.readAllStrings();
        for (int i = 0; i < str.length; i++)
            st.put(str[i], 1);

        StdOut.println("Range "  + st.range(3, 20));
*/

        boolean flag = true;
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

    }
}
