public class DoublyLinkedList {
    private Node head;

    private class Node {
        int value;
        Node next;
        Node prev; // The architectural upgrade: pointing backwards

        Node(int value) {
            this.value = value;
        }
    }

    // O(1) Insertion at the head, now updating two pointers
    public void insertFirst(int val) {
        Node node = new Node(val);
        node.next = head;
        node.prev = null;

        if (head != null) {
            head.prev = node;
        }
        head = node;
    }

    // Traversal demonstrating two-way movement
    public void display() {
        Node node = head;
        Node last = null;
        
        System.out.println("Forward Traversal:");
        while (node != null) {
            System.out.print(node.value + " -> ");
            last = node; // Capture the last node before we fall off the end
            node = node.next;
        }
        System.out.println("END");

        System.out.println("\nBackward Traversal:");
        while (last != null) {
            System.out.print(last.value + " -> ");
            last = last.prev;
        }
        System.out.println("START");
    }

    public static void main(String[] args) {
        DoublyLinkedList list = new DoublyLinkedList();
        System.out.println("--- Initializing Doubly Linked List ---");
        
        list.insertFirst(30);
        list.insertFirst(20);
        list.insertFirst(10);
        
        list.display();
    }
}