public class ValidateBST {
    private static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }

    // O(N) Time, O(H) Space - Mathematical Boundary Validation
    public boolean isValidBST(Node root) {
        // We use Integer objects to allow for 'null' as infinity bounds initially
        return validateRecursive(root, null, null);
    }

    private boolean validateRecursive(Node node, Integer min, Integer max) {
        // Base Case: An empty node is technically a valid BST
        if (node == null) return true;
        
        // Architecture Breach: Node violates the minimum boundary
        if (min != null && node.value <= min) return false;
        
        // Architecture Breach: Node violates the maximum boundary
        if (max != null && node.value >= max) return false;
        
        // Recursively validate both branches. 
        // Branching left? The current node becomes the new maximum limit.
        // Branching right? The current node becomes the new minimum limit.
        return validateRecursive(node.left, min, node.value) && 
               validateRecursive(node.right, node.value, max);
    }

    public static void main(String[] args) {
        ValidateBST validator = new ValidateBST();
        System.out.println("--- Booting Architecture Validation Protocol ---");

        // Constructing a VALID Architecture
        //       50
        //      /  \
        //     30   70
        Node validRoot = new Node(50);
        validRoot.left = new Node(30);
        validRoot.right = new Node(70);
        
        System.out.println("Validating Tree A (Clean Architecture): " + validator.isValidBST(validRoot));

        // Constructing a CORRUPTED Architecture
        //       50
        //      /  \
        //     30   70
        //         /  \
        //        40   80  <-- 40 is a breach. It is on the right of 50, so it MUST be > 50.
        Node corruptedRoot = new Node(50);
        corruptedRoot.left = new Node(30);
        corruptedRoot.right = new Node(70);
        corruptedRoot.right.left = new Node(40); 
        corruptedRoot.right.right = new Node(80);

        System.out.println("Validating Tree B (Corrupted Architecture): " + validator.isValidBST(corruptedRoot));
    }
}