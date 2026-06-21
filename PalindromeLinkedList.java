public class PalindromeLinkedList {
    private class Node {
        int value;
        Node next;
        Node(int value) { this.value = value; }
    }

    // Helper 1: Find the middle (Tortoise and Hare)
    private Node getMiddle(Node head) {
        Node slow = head;
        Node fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    // Helper 2: Reverse a linked list in-place
    private Node reverse(Node head) {
        Node prev = null;
        Node current = head;
        while (current != null) {
            Node next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        return prev;
    }

    // Main Algorithm: O(N) Time, strictly O(1) Space
    public boolean isPalindrome(Node head) {
        if (head == null || head.next == null) return true;

        // Step 1: Find the middle node
        Node middle = getMiddle(head);

        // Step 2: Reverse the second half of the list
        Node secondHead = reverse(middle);
        Node reverseHeadCopy = secondHead; // Save for optional restoration later

        // Step 3: Compare both halves
        Node firstHead = head;
        while (secondHead != null) {
            if (firstHead.value != secondHead.value) {
                return false; // Mismatch found
            }
            firstHead = firstHead.next;
            secondHead = secondHead.next;
        }
        
        return true; 
    }

    public static void main(String[] args) {
        PalindromeLinkedList list = new PalindromeLinkedList();
        
        // Simulating a palindrome: 1 -> 2 -> 3 -> 2 -> 1
        Node head = list.new Node(1);
        head.next = list.new Node(2);
        head.next.next = list.new Node(3);
        head.next.next.next = list.new Node(2);
        head.next.next.next.next = list.new Node(1);

        System.out.println("--- Executing O(1) Space Palindrome Diagnostics ---");
        boolean result = list.isPalindrome(head);
        System.out.println("Is the Memory Architecture a Palindrome? " + result);
    }
}