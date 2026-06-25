public class CustomQueue {
    protected int[] data;
    private static final int DEFAULT_SIZE = 10;
    int end = 0; // Points to the next empty space

    public CustomQueue() {
        this(DEFAULT_SIZE);
    }

    public CustomQueue(int size) {
        this.data = new int[size];
    }

    // O(1) Time - Inserting an item at the rear of the line
    public boolean insert(int item) {
        if (isFull()) {
            System.out.println("Exception: Queue is full.");
            return false;
        }
        data[end] = item; // Insert at the current end pointer
        end++;            // Move the pointer to the next empty slot
        return true;
    }

    // O(N) Time - Removing the front item (Requires shifting everything forward)
    public int remove() throws Exception {
        if (isEmpty()) {
            throw new Exception("Exception: Queue is empty.");
        }
        
        int removed = data[0]; // The first person in line gets removed
        
        // Shift every remaining person one step forward in line
        for (int i = 1; i < end; i++) {
            data[i - 1] = data[i];
        }
        end--; // The line is now one person shorter
        
        return removed;
    }

    // O(1) Time - Looking at who is first in line
    public int front() throws Exception {
        if (isEmpty()) {
            throw new Exception("Exception: Queue is empty.");
        }
        return data[0];
    }

    public boolean isFull() {
        return end == data.length;
    }

    public boolean isEmpty() {
        return end == 0;
    }

    public void display() {
        for (int i = 0; i < end; i++) {
            System.out.print(data[i] + " <- ");
        }
        System.out.println("END");
    }

    public static void main(String[] args) throws Exception {
        CustomQueue queue = new CustomQueue(5);
        System.out.println("--- Booting FIFO Memory Architecture ---");

        queue.insert(10);
        queue.insert(20);
        queue.insert(30);
        queue.insert(40);
        
        queue.display();
        System.out.println("First in line is currently: " + queue.front());

        System.out.println("\nExecuting O(N) front removal...");
        System.out.println("Removed: " + queue.remove());
        queue.display();
        System.out.println("New first in line is: " + queue.front());
    }
}