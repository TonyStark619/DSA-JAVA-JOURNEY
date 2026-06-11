public class LLDeleteFirst {
    private Node head;
    private Node tail;
    private int size;

    private class Node {
        int value;
        Node next;
        Node(int value) {
            this.value = value;
        }
    }

    // Standard O(1) insertion to build the test list
    public void insertFirst(int val) {
        Node node = new Node(val);
        node.next = head;
        head = node;
        if (tail == null) {
            tail = head;
        }
        size++;
    }

    // O(1) Deletion from the front of the Linked List
    public int deleteFirst() {
        if (head == null) {
            System.out.println("List is empty.");
            return -1; 
        }
        
        int val = head.value; // Capture the value before severing it
        head = head.next;     // Move the head pointer forward
        
        // If the list is now empty, the tail must also be null
        if (head == null) {
            tail = null;
        }
        
        size--;
        return val;
    }

    public void display() {
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.value + " -> ");
            temp = temp.next;
        }
        System.out.println("END");
    }

    public static void main(String[] args) {
        LLDeleteFirst list = new LLDeleteFirst();
        list.insertFirst(30);
        list.insertFirst(20);
        list.insertFirst(10);

        System.out.println("--- Original Architecture ---");
        list.display();

        System.out.println("\nExecuting O(1) Head Deletion...");
        int removedValue = list.deleteFirst();
        
        System.out.println("Node Destroyed: " + removedValue);
        list.display();
    }
}