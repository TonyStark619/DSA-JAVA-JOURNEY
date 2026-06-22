public class IntersectionLinkedList {
    private static class Node {
        int value;
        Node next;
        Node(int value) { this.value = value; }
    }

    // O(N+M) Time, strictly O(1) Space
    public static Node getIntersectionNode(Node headA, Node headB) {
        if (headA == null || headB == null) return null;

        Node pointerA = headA;
        Node pointerB = headB;

        // The loop breaks when they collide (either at the intersection, or at null)
        while (pointerA != pointerB) {
            // If pointerA hits the end, redirect it to headB
            pointerA = (pointerA == null) ? headB : pointerA.next;
            
            // If pointerB hits the end, redirect it to headA
            pointerB = (pointerB == null) ? headA : pointerB.next;
        }

        return pointerA; 
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Memory Intersection Diagnostics ---");
        
        // Constructing the shared intersection part
        Node intersection = new Node(50);
        intersection.next = new Node(60);

        // Constructing List A: 10 -> 20 -> 50 -> 60
        Node headA = new Node(10);
        headA.next = new Node(20);
        headA.next.next = intersection;

        // Constructing List B: 30 -> 40 -> 45 -> 50 -> 60
        Node headB = new Node(30);
        headB.next = new Node(40);
        headB.next.next = new Node(45);
        headB.next.next.next = intersection;

        System.out.println("Executing Two-Pointer Teleportation Algorithm...");
        Node collisionNode = getIntersectionNode(headA, headB);

        if (collisionNode != null) {
            System.out.println("Intersection confirmed at Node Value: " + collisionNode.value);
        } else {
            System.out.println("Parallel architectures. No intersection found.");
        }
    }
}