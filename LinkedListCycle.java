public class LinkedListCycle {
    private class Node {
        int value;
        Node next;
        Node(int value) { this.value = value; }
    }

    // O(N) Time, O(1) Space - Floyd's Cycle-Finding Algorithm
    public boolean hasCycle(Node head) {
        if (head == null || head.next == null) {
            return false;
        }

        Node slow = head; // The Tortoise (moves 1 step)
        Node fast = head; // The Hare (moves 2 steps)

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;

            // If the fast pointer laps the slow pointer, a cycle exists
            if (slow == fast) {
                return true; 
            }
        }
        
        // If the fast pointer reaches null, there is an end to the list (no cycle)
        return false;
    }

    public static void main(String[] args) {
        LinkedListCycle list = new LinkedListCycle();
        
        // Manually building a list: 10 -> 20 -> 30 -> 40 -> (points back to 20)
        Node head = list.new Node(10);
        Node node2 = list.new Node(20);
        Node node3 = list.new Node(30);
        Node node4 = list.new Node(40);
        
        head.next = node2;
        node2.next = node3;
        node3.next = node4;
        
        System.out.println("--- Diagnostics: Architecture 1 (Linear) ---");
        System.out.println("Infinite Loop Detected? " + list.hasCycle(head));

        System.out.println("\n--- Diagnostics: Architecture 2 (Corrupted/Cycle) ---");
        // Corrupting the architecture by pointing the tail back to node2
        node4.next = node2; 
        System.out.println("Infinite Loop Detected? " + list.hasCycle(head));
    }
}