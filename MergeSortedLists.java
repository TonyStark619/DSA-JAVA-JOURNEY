public class MergeSortedLists {
    private Node head;

    private static class Node {
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

    public void display() {
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.value + " -> ");
            temp = temp.next;
        }
        System.out.println("END");
    }

    // O(N + M) Time, O(1) Space - Pointer Rewiring
    public static MergeSortedLists merge(MergeSortedLists list1, MergeSortedLists list2) {
        Node head1 = list1.head;
        Node head2 = list2.head;

        // A dummy node acts as the anchor for our new merged list
        Node dummy = new Node(-1);
        Node tail = dummy;

        // Traverse both lists, picking the smaller value and rewiring the tail pointer
        while (head1 != null && head2 != null) {
            if (head1.value < head2.value) {
                tail.next = head1;
                head1 = head1.next;
            } else {
                tail.next = head2;
                head2 = head2.next;
            }
            tail = tail.next; // Advance the tail
        }

        // If one list finishes before the other, simply attach the remainder
        tail.next = (head1 != null) ? head1 : head2;

        // Wrap the merged chain back into a new object
        MergeSortedLists mergedList = new MergeSortedLists();
        mergedList.head = dummy.next; // Skip the dummy node
        return mergedList;
    }

    public static void main(String[] args) {
        MergeSortedLists listA = new MergeSortedLists();
        listA.insertLast(1); listA.insertLast(3); listA.insertLast(5);

        MergeSortedLists listB = new MergeSortedLists();
        listB.insertLast(2); listB.insertLast(4); listB.insertLast(6);

        System.out.println("--- Memory Architecture A ---");
        listA.display();
        System.out.println("--- Memory Architecture B ---");
        listB.display();

        System.out.println("\nExecuting O(1) Space Pointer Fusion...");
        MergeSortedLists result = merge(listA, listB);
        result.display();
    }
}