import java.util.Iterator;
public class RandomizedQueue<Item> implements Iterable<Item> {
    private int N;
    private int first;
    private int last;
    private Item[] data; 
    public RandomizedQueue()                           // construct an empty deque
    {
        N = 0;
        first = 0;
        last  = 0;
        data = (Item[]) new Object[10];
    }

    public boolean isEmpty()                 // is the deque empty?
    {
        return N == 0; 
    }
    
    public int size()  // return the number of items on the deque
    {
        return N;
    }
   
    private void resize(int newlen)
    {
        //StdOut.println("Resizing array from " + N + " to " + newlen);
        Item[] newdata = (Item[]) new Object[newlen];
        int i;
        for (i = 0; i < N; i++)
            newdata[i] = data[(first + i) % data.length];

        data = newdata;
        first = 0;
        last  = N;
    }

    public void enqueue(Item item) 
    {
        if (item == null)
            throw new NullPointerException();

        if (N == data.length)  //resize array
            resize(2 * data.length);
        
        data[last++] = item;
        if (last == data.length)
            last = 0;
        N++;
    }
    
    public Item dequeue()           
    {
        if (isEmpty()) 
            throw new java.util.NoSuchElementException("Queue underflow");
       
        int n = StdRandom.uniform(N);
        int off = (n + first) % data.length;
        Item i = data[off];
        last   = (last - 1 + data.length) % data.length;
        data[off] = data[last];
        data[last] = null;
        N--; 
        if (data.length >= 40 && N == data.length/4) 
            resize(data.length/2);

        return i;
    }
    
    public Item sample()           
    {
        if (isEmpty()) 
            throw new java.util.NoSuchElementException("Queue underflow");

        return data[StdRandom.uniform(N)];
    }
// return an iterator over items in order from front to end
    public Iterator<Item> iterator()
    {
        return new Diterator<Item>();
    }
   
    private class Diterator<Item> implements Iterator<Item> {
        private Item[] arr;
        private int len;
        public Diterator()
        {
            //StdOut.println(N);
            arr = (Item[]) new Object[N];
            int i;
            for (i = 0; i < N; i++)
                arr[i] = (Item) data[(first + i) % data.length];
             
            StdRandom.shuffle(arr);
            len = N;
        }

        public boolean hasNext()
        {
            return len > 0;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        public Item next()
        {
            if (!hasNext())
                throw new java.util.NoSuchElementException();
            Item i = arr[len-1];
            arr[--len] = null;
            return i;
        }
    }

    public static void main(String[] args)   // unit testing
    {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) q.enqueue(item);
            else if (!q.isEmpty()) StdOut.println(q.dequeue() + " ");
            Iterator itr = q.iterator();
            while (itr.hasNext()) {
                StdOut.print(itr.next() + " ");
            }
            StdOut.println();
        }
        StdOut.println("(" + q.size() + " left on queue)");
    }
}
