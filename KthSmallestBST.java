public class KthSmallestBST {
    private static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }

    // Global variables to track state during recursion
    private int count = 0;
    private int result = -1;

    // O(N) Time, O(1) Space (ignoring call stack)
    public int kthSmallest(Node root, int k) {
        count = 0; // Reset for safety
        inOrderTraversal(root, k);
        return result;
    }

    private void inOrderTraversal(Node node, int k) {
        // Base case or if we already found the answer, stop searching
        if (node == null || count >= k) return;

        // 1. Traverse Left (The smallest values)
        inOrderTraversal(node.left, k);

        // 2. Process Root
        count++;
        if (count == k) {
            result = node.value;
            return; // Target locked. Stop further processing.
        }

        // 3. Traverse Right (The larger values)
        inOrderTraversal(node.right, k);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting In-Order Extraction Protocol ---");

        // Constructing BST:
        //       50
        //      /  \
        //     30   70
        //    /  \
        //   20  40
        Node root = new Node(50);
        root.left = new Node(30);
        root.right = new Node(70);
        root.left.left = new Node(20);
        root.left.right = new Node(40);

        KthSmallestBST engine = new KthSmallestBST();
        
        int k1 = 1;
        System.out.println("The " + k1 + "st smallest element is: " + engine.kthSmallest(root, k1)); // Should be 20

        int k2 = 3;
        System.out.println("The " + k2 + "rd smallest element is: " + engine.kthSmallest(root, k2)); // Should be 40
        
        int k3 = 5;
        System.out.println("The " + k3 + "th smallest element is: " + engine.kthSmallest(root, k3)); // Should be 70
    }
}