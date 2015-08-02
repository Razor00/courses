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

    public Value min()
    {
        if (root == null)
            return null;

        Node node = root;
        while (node.left != null)
            node = node.left;
        return node.value;
    }

    public Value max()
    {
        if (root == null)
            return null;

        Node node = root;
        while (node.right != null)
            node = node.right;
        return node.value;
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
        if (node == null) return null;
        if (node.left == null) return node.right;

        node.left   = deleteMin(node.left);
        node.count  = 1 + size(node.left) + size(node.right);
        return node;
    }

    private Node delete(Node node, Key key)
    {
        int cmp = key.compareTo(node.key);

        if (cmp < 0) delete(node.left, key);
        if (cmp > 0) delete(node.right, key); 

    }

    public void delete(Key key)
    {
        delete(root, key);
    }
    private void inorder(Node node, Queue<Key> q)
    {
        if (node == null) return;
        inorder(node.left, q);
        q.enqueue(node.key);
        inorder(node.right, q);
    }

    public Iterable<Key> keys()
    {
        Queue<Key> queue = new Queue<>();
        inorder(root, queue);
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

    public static void main(String[] args)
    {
        ST<String, Integer> st = new ST<>();
        String k;
        Integer v;
        while (true) {
            StdOut.printf("\n\n 1: Insert\n 2: Get \n 3: Print keys\n 4: Rank \n 5: Ceiling\n 6: Floor\n");
            int n = StdIn.readInt();
            switch (n) {
                case 1:
                    StdOut.printf("Enter key (space) value:");
                    k = StdIn.readString();
                    v = StdIn.readInt();
                    st.put(k, v);
                    break;
                case 2:
                    StdOut.printf("Enter key :");
                    k = StdIn.readString();
                    v = st.get(k);
                    if (v == null) 
                        StdOut.println("Value of Key " + k + ": null");
                    else
                        StdOut.println("Value of Key " + k + ":" + v);
                    break;
    
                case 3:
                    for (String s:st.keys())
                        StdOut.print(s + " ");
                    StdOut.println();
                    break;

                case 4:
                    StdOut.printf("Enter key:");
                    k = StdIn.readString();
                    StdOut.println("Rank " + k + " = " + st.rank(k));
                    break;
                
                case 5:
                    StdOut.printf("Enter key:");
                    k = StdIn.readString();
                    String  ceil = st.ceiling(k);
                    if (ceil == null) ceil = "null";
                    StdOut.println("Ceiling " + k + " = " + ceil);
                    break;

                case 6:
                    StdOut.printf("Enter key:");
                    k = StdIn.readString();
                    String  floor = st.floor(k);
                    if (floor == null) floor = "null";
                    StdOut.println("Floor " + k + " = " + floor);
                    break;

                default:
                    StdOut.println("Invalid option chosen = " + n);
                    break;
            }
        }

    }



}
