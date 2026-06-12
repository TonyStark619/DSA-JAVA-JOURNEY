public class LLDeleteLast {
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

    public void insertFirst(int val) {
        Node node = new Node(val);
        node.next = head;
        head = node;
        if (tail == null) {
            tail = head;
        }
        size++;
    }

    // Helper method to fetch a node at a precise index
    public Node getNode(int index) {
        Node node = head;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    }

    // O(N) Deletion from the tail of a Singly Linked List
    public int deleteLast() {
        if (size <= 1) {
            if (head == null) return -1;
            int val = head.value;
            head = null;
            tail = null;
            size = 0;
            return val;
        }

        // Find the second-to-last node (index size - 2)
        Node secondLast = getNode(size - 2);
        int val = tail.value; // Save old tail value
        
        tail = secondLast;    // Update tail pointer backward
        tail.next = null;     // Sever connection to the old tail
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
        LLDeleteLast list = new LLDeleteLast();
        list.insertFirst(30);
        list.insertFirst(20);
        list.insertFirst(10);

        System.out.println("--- Original Architecture ---");
        list.display();

        System.out.println("\nExecuting O(N) Tail Deletion...");
        int removedValue = list.deleteLast();
        
        System.out.println("Node Destroyed: " + removedValue);
        list.display();
    }
}