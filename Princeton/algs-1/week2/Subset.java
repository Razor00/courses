import java.util.Iterator;
public class Subset {
    public static void main(String[] args)
    {
        int j, k;
        int count;
        RandomizedQueue<String> rdq = new RandomizedQueue<String>();
        k = Integer.parseInt(args[0]);

        if (k > 0) {
            for (count = 1; count <= k; count++) {
                String item = StdIn.readString();
                rdq.enqueue(item);
            }
            while (!StdIn.isEmpty()) {
                String item = StdIn.readString();
                j = 1 + StdRandom.uniform(count);
                if (j <= k) {
                    rdq.dequeue();
                    rdq.enqueue(item);
                }
                count++;
            }
            Iterator itr = rdq.iterator();
            while (itr.hasNext()) {
                StdOut.println(itr.next());
            }
        }
    }
}
