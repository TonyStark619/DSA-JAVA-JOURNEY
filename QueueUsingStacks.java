import java.util.Stack;

public class QueueUsingStacks {
    private Stack<Integer> first;
    private Stack<Integer> second;

    public QueueUsingStacks() {
        first = new Stack<>();
        second = new Stack<>();
    }

    // O(1) Time - Adding an item to the queue is as simple as pushing to the first stack
    public void push(int x) {
        first.push(x);
    }

    // Amortized O(1) Time - Removing the front item
    public int pop() {
        // If the second stack is empty, shift everything from the first stack to invert the order
        if (second.isEmpty()) {
            while (!first.isEmpty()) {
                second.push(first.pop());
            }
        }
        return second.pop();
    }

    // Amortized O(1) Time - Peeking at the front element without removing it
    public int peek() {
        if (second.isEmpty()) {
            while (!first.isEmpty()) {
                second.push(first.pop());
            }
        }
        return second.peek();
    }

    public boolean empty() {
        return first.isEmpty() && second.isEmpty();
    }

    public static void main(String[] args) {
        QueueUsingStacks customQueue = new QueueUsingStacks();
        System.out.println("--- Booting Synthesized FIFO Queue Architecture ---");

        customQueue.push(10);
        customQueue.push(20);
        customQueue.push(30);

        System.out.println("Front element (peek): " + customQueue.peek()); 
        System.out.println("Popped from queue: " + customQueue.pop());  
        System.out.println("New front element (peek): " + customQueue.peek()); 
        
        customQueue.push(40);
        System.out.println("Is queue empty? " + customQueue.empty());
    }
}