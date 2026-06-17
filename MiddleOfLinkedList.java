public class MiddleOfLinkedList {
    private class Node {
        int value;
        Node next;
        Node(int value) { this.value = value; }
    }

    // O(N) Time, O(1) Space - Single Pass Middle Finding
    public Node getMiddle(Node head) {
        if (head == null) return null;

        Node slow = head; // Moves 1 step
        Node fast = head; // Moves 2 steps

        // Traverse until fast pointer reaches the end
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // When fast is at the end, slow is exactly in the middle
        return slow;
    }

    public static void main(String[] args) {
        MiddleOfLinkedList list = new MiddleOfLinkedList();
        
        // Simulating a list: 10 -> 20 -> 30 -> 40 -> 50
        Node head = list.new Node(10);
        head.next = list.new Node(20);
        head.next.next = list.new Node(30);
        head.next.next.next = list.new Node(40);
        head.next.next.next.next = list.new Node(50);
        
        System.out.println("--- Executing Tortoise and Hare Protocol ---");
        Node middleNode = list.getMiddle(head);
        
        if (middleNode != null) {
            System.out.println("The middle node value is: " + middleNode.value);
        } else {
            System.out.println("The list is empty.");
        }
    }
}