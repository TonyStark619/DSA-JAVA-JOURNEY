public class LLInsertIndex {
    private Node head;

    private class Node {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }

    // Standard O(1) insertion to build the initial list
    public void insertFirst(int val) {
        Node node = new Node(val);
        node.next = head;
        head = node;
    }

    // O(N) insertion at a specific index
    public void insert(int val, int index) {
        if (index == 0) {
            insertFirst(val);
            return;
        }

        Node temp = head;
        // Traverse to the node immediately BEFORE the target index
        for (int i = 1; i < index; i++) {
            if (temp != null) {
                temp = temp.next;
            }
        }

        // If index is valid, insert the new node and reconnect pointers
        if (temp != null) {
            Node newNode = new Node(val);
            newNode.next = temp.next; // New node points to the rest of the list
            temp.next = newNode;      // Previous node points to the new node
        }
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
        LLInsertIndex list = new LLInsertIndex();
        
        list.insertFirst(30);
        list.insertFirst(20);
        list.insertFirst(10);
        
        System.out.println("Original List:");
        list.display();
        
        System.out.println("\nInserting '25' at index 2...");
        list.insert(25, 2);
        list.display();
    }
}