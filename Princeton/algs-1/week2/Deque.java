import java.util.Iterator;
public class Deque<Item> implements Iterable<Item> {
    private int N;
    private Node first;
    private Node last;
    public Deque()                           // construct an empty deque
    {
        N = 0;
        first = null;
        last  = null;
    }

    public boolean isEmpty()                 // is the deque empty?
    {
        return N == 0; 
    }
    
    public int size() // return the number of items on the deque
    {
        return N;
    }
    
    public void addFirst(Item item)          // add the item to the front
    {
        if (item == null)
            throw new NullPointerException();

        Node n = new Node(item);   
        n.next = first;
        if (!isEmpty()) 
            first.prev = n;
        else
            last = n;

        first = n;
        N++;
    }
    
    public void addLast(Item item)           // add the item to the end
    {
        if (item == null)
            throw new NullPointerException();

        Node n = new Node(item);
        n.prev = last;
        if (!isEmpty())
            last.next = n;
        else
            first = n;

        last = n;
        N++;
    }
    
    public Item removeFirst()     // remove and return the item from the front
    {
        if (isEmpty()) 
            throw new java.util.NoSuchElementException("Queue underflow");

        Node<Item> temp  = first;
        Item i = (Item) first.item;
        first  = first.next;
        N--;

        temp.prev = null;
        temp.next = null;

        if (isEmpty()) { 
            first = null;
            last = null;
        }
        else 
            first.prev = null;


        return i;
    }

    public Item removeLast()   // remove and return the item from the end
    {
    
        if (isEmpty()) 
            throw new java.util.NoSuchElementException("Queue underflow");

        Node<Item> temp = last;
        Item i = (Item) last.item;
        last   = last.prev;
        N--;

        temp.prev = null;
        temp.next = null;
        if (isEmpty()) {
            first = null;
            last = null;
        }
        else
            last.next = null;

        return i;
    }
         // return an iterator over items in order from front to end
    public Iterator<Item> iterator()    
    {
        return new Diterator<Item>(first);
    }
   
    private class Diterator<Item> implements Iterator<Item> {
        private Node<Item> curr;
        public Diterator(Node<Item> i)
        {
            curr = i;
        }

        public boolean hasNext()
        {
            return curr != null;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }
        public Item next()
        {
            Node<Item> n;

            if (!hasNext())
                throw new java.util.NoSuchElementException();
            n = curr;
            curr = curr.next;
            return n.item;
        }
    }

    private class Node<Item> {
        private Item item;
        private Node next;
        private Node prev;
        public Node(Item i)
        {
            item = i;
            next = null;
            prev = null;
        }
    }
    public static void main(String[] args)   // unit testing
    {
        Deque<String> q = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) q.addLast(item);
            else if (!q.isEmpty()) StdOut.println(q.removeFirst() + " ");
            Iterator itr = q.iterator();
            while (itr.hasNext()) {
                StdOut.print(itr.next() + " ");
            }
            StdOut.println();
        }
        StdOut.println("(" + q.size() + " left on queue)");
    }
}
