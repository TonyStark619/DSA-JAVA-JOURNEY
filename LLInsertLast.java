public class LLInsertLast {
    private Node head;
    private Node tail;
    private int size;

    public LLInsertLast() {
        this.size = 0;
    }

    private class Node {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }

    // O(1) Constant Time Insertion at the end using the tail pointer
    public void insertLast(int val) {
        Node node = new Node(val);
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
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
        LLInsertLast list = new LLInsertLast();
        
        System.out.println("Building optimized tail-pointer list...");
        list.insertLast(10);
        list.insertLast(20);
        list.insertLast(30);
        
        list.display();
        System.out.println("List Size: " + list.size);
    }
}