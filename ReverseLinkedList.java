public class ReverseLinkedList {
    private Node head;

    private class Node {
        int value;
        Node next;
        Node(int value) { this.value = value; }
    }

    public void insertFirst(int val) {
        Node node = new Node(val);
        node.next = head;
        head = node;
    }

    // O(N) Time, O(1) Space In-Place Reversal
    public void reverse() {
        if (head == null || head.next == null) {
            return; // Nothing to reverse
        }

        Node prev = null;
        Node current = head;
        Node next = null;

        while (current != null) {
            next = current.next;  // Save the next node so we don't lose the chain
            current.next = prev;  // Reverse the pointer backward
            
            // Shift both pointers forward for the next iteration
            prev = current;
            current = next;
        }
        
        // After the loop, 'prev' will be resting on the final node, which is our new head
        head = prev; 
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
        ReverseLinkedList list = new ReverseLinkedList();
        list.insertFirst(40);
        list.insertFirst(30);
        list.insertFirst(20);
        list.insertFirst(10);

        System.out.println("--- Original Memory Architecture ---");
        list.display();

        System.out.println("\nExecuting O(1) Space Pointer Reversal...");
        list.reverse();
        
        System.out.println("--- Reversed Architecture ---");
        list.display();
    }
}