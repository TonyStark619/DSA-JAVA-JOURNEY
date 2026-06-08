public class SinglyLinkedList {
    private Node head;

    // The blueprint for a single element in the list
    private class Node {
        int value;
        Node next; // The pointer to the next node in memory

        Node(int value) {
            this.value = value;
        }
    }

    // Pushing a new node to the front of the list in O(1) time
    public void insertFirst(int val) {
        Node node = new Node(val);
        node.next = head;
        head = node;
    }

    // Traversal without using a standard index (i)
    public void display() {
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.value + " -> ");
            temp = temp.next;
        }
        System.out.println("END");
    }

    public static void main(String[] args) {
        SinglyLinkedList list = new SinglyLinkedList();
        
        System.out.println("Initializing Custom Linked List...");
        list.insertFirst(3);
        list.insertFirst(2);
        list.insertFirst(8);
        
        list.display();
    }
}