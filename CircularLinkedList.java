public class CircularLinkedList {
    private Node head;
    private Node tail;

    private class Node {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }

    // O(1) Insertion for a Circular Architecture
    public void insert(int val) {
        Node node = new Node(val);
        // If the list is empty, the node points to itself
        if (head == null) {
            head = node;
            tail = node;
            return;
        }
        
        // The new node gets added after the tail
        tail.next = node;
        // The new node points back to the head, completing the circle
        node.next = head;
        // The tail pointer moves to the new node
        tail = node;
    }

    // Traversal requires a do-while loop so we don't accidentally stop at the head immediately
    public void display() {
        if (head == null) {
            System.out.println("List is empty.");
            return;
        }
        
        Node node = head;
        do {
            System.out.print(node.value + " -> ");
            node = node.next;
        } while (node != head); // Stop when we loop back around to the start
        
        System.out.println("HEAD (Circle Complete)");
    }

    public static void main(String[] args) {
        CircularLinkedList list = new CircularLinkedList();
        System.out.println("--- Booting Circular Memory Architecture ---");
        
        list.insert(10);
        list.insert(20);
        list.insert(30);
        list.insert(40);
        
        list.display();
    }
}