public class CustomStack {
    protected int[] data;
    private static final int DEFAULT_SIZE = 10;
    int pointer = -1; // Points to the top of the stack

    public CustomStack() {
        this(DEFAULT_SIZE);
    }

    public CustomStack(int size) {
        this.data = new int[size];
    }

    // O(1) Time - Pushing an item to the top
    public boolean push(int item) {
        if (isFull()) {
            System.out.println("Exception: Stack Overflow. Cannot insert " + item);
            return false;
        }
        pointer++;
        data[pointer] = item;
        return true;
    }

    // O(1) Time - Popping the top item off
    public int pop() throws Exception {
        if (isEmpty()) {
            throw new Exception("Exception: Stack is empty. Nothing to pop.");
        }
        int removed = data[pointer];
        pointer--;
        return removed;
    }

    // O(1) Time - Peeking at the top without removing it
    public int peek() throws Exception {
        if (isEmpty()) {
            throw new Exception("Exception: Stack is empty.");
        }
        return data[pointer];
    }

    public boolean isFull() {
        return pointer == data.length - 1;
    }

    public boolean isEmpty() {
        return pointer == -1;
    }

    public static void main(String[] args) throws Exception {
        CustomStack stack = new CustomStack(5);
        System.out.println("--- Booting LIFO Memory Architecture ---");

        stack.push(34);
        stack.push(45);
        stack.push(2);
        stack.push(9);
        stack.push(18);

        System.out.println("Top of the stack is currently: " + stack.peek());

        System.out.println("\nExecuting sequential pops:");
        System.out.println("Popped: " + stack.pop());
        System.out.println("Popped: " + stack.pop());
        System.out.println("Current top is now: " + stack.peek());
    }
}