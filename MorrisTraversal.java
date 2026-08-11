public class MorrisTraversal {
    
    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    // O(N) Time, Strict O(1) Space - Threaded Tree Traversal
    public static void inorderMorrisTraversal(TreeNode root) {
        TreeNode current = root;
        
        System.out.println("Executing O(1) Memory Morris Traversal...");
        System.out.print("Extraction Sequence: ");

        while (current != null) {
            // Case 1: No left child. We can safely process current and move right.
            if (current.left == null) {
                System.out.print(current.val + " -> ");
                current = current.right;
            } 
            else {
                // Case 2: We have a left child. We must find the In-Order Predecessor.
                // The predecessor is the right-most node in the left subtree.
                TreeNode predecessor = current.left;
                while (predecessor.right != null && predecessor.right != current) {
                    predecessor = predecessor.right;
                }

                // Sub-case A: The predecessor's right pointer is null.
                // Establish the temporary thread back to the current node and dive left.
                if (predecessor.right == null) {
                    predecessor.right = current;
                    current = current.left;
                } 
                // Sub-case B: The thread already exists! This means we've returned from the left.
                // Cut the thread to restore the tree, process current, and move right.
                else {
                    predecessor.right = null;
                    System.out.print(current.val + " -> ");
                    current = current.right;
                }
            }
        }
        System.out.println("END");
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Zero-Memory Tree Architecture ---");
        
        // Constructing a Binary Tree
        //       4
        //     /   \
        //    2     6
        //   / \   / \
        //  1   3 5   7
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(6);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
        root.right.left = new TreeNode(5);
        root.right.right = new TreeNode(7);

        // Expected In-Order: 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7
        inorderMorrisTraversal(root);
        
        System.out.println("\nStatus: Tree successfully traversed and original pointer architecture restored.");
    }
}