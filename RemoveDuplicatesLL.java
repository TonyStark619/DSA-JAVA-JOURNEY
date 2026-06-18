public class RemoveDuplicatesLL {
    private Node head;

    private class Node {
        int value;
        Node next;
        Node(int value) { this.value = value; }
    }

    public void insertLast(int val) {
        Node newNode = new Node(val);
        if (head == null) {
            head = newNode;
            return;
        }
        Node temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = newNode;
    }

    // O(N) Time, O(1) Space - In-place duplicate elimination
    public void removeDuplicates() {
        if (head == null) return;
        
        Node temp = head;
        while (temp.next != null) {
            // If current value matches the next value, slice out the next node
            if (temp.value == temp.next.value) {
                temp.next = temp.next.next; 
            } else {
                // Only advance the pointer if no duplicate was found
                temp = temp.next;
            }
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
        RemoveDuplicatesLL list = new RemoveDuplicatesLL();
        // Building a sorted list with duplicates: 1 -> 1 -> 2 -> 3 -> 3
        list.insertLast(1);
        list.insertLast(1);
        list.insertLast(2);
        list.insertLast(3);
        list.insertLast(3);

        System.out.println("--- Pre-Processing Architecture ---");
        list.display();

        System.out.println("\nExecuting Pointer Bypass Algorithm...");
        list.removeDuplicates();

        System.out.println("--- Post-Processing Architecture ---");
        list.display();
    }
}