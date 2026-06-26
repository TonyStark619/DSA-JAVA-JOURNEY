public class CircularQueue {
    protected int[] data;
    private static final int DEFAULT_SIZE = 10;
    
    protected int front = 0;
    protected int end = 0;
    private int size = 0;

    public CircularQueue() {
        this(DEFAULT_SIZE);
    }

    public CircularQueue(int size) {
        this.data = new int[size];
    }

    public boolean isFull() {
        return size == data.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // O(1) Time - Inserting at the rear, wrapping around if necessary
    public boolean insert(int item) {
        if (isFull()) {
            System.out.println("Exception: Circular Architecture is full.");
            return false;
        }
        data[end] = item;
        end = (end + 1) % data.length; // The Modulo Wrap-Around
        size++;
        return true;
    }

    // O(1) Time - Removing from the front without shifting data
    public int remove() throws Exception {
        if (isEmpty()) {
            throw new Exception("Exception: Queue is empty.");
        }
        int removed = data[front];
        front = (front + 1) % data.length; // Move the front pointer forward
        size--;
        return removed;
    }

    public void display() {
        if (isEmpty()) {
            System.out.println("Empty");
            return;
        }
        int i = front;
        do {
            System.out.print(data[i] + " <- ");
            i = (i + 1) % data.length;
        } while (i != end);
        System.out.println("END");
    }

    public static void main(String[] args) throws Exception {
        CircularQueue queue = new CircularQueue(5);
        System.out.println("--- Booting Optimized Circular FIFO Architecture ---");

        queue.insert(10);
        queue.insert(20);
        queue.insert(30);
        queue.insert(40);
        queue.insert(50);
        queue.display();

        System.out.println("\nExecuting O(1) removals...");
        System.out.println("Removed: " + queue.remove());
        System.out.println("Removed: " + queue.remove());
        queue.display();

        System.out.println("\nMemory wrapped. Inserting new data at the physical start of the array...");
        queue.insert(60);
        queue.insert(70);
        queue.display();
    }
}