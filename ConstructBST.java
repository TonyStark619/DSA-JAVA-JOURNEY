public class ConstructBST {
    private static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }

    private int index = 0; // Global pointer to track our position in the array

    // O(N) Time, O(H) Space - Single Pass Construction via Boundary Limits
    public Node bstFromPreorder(int[] preorder) {
        index = 0; // Reset for safety
        // We start with an upper bound of Infinity
        return constructRecursive(preorder, Integer.MAX_VALUE);
    }

    private Node constructRecursive(int[] preorder, int upperBound) {
        // Base Case 1: We ran out of numbers
        if (index == preorder.length) return null;
        
        // Base Case 2: The current number is larger than our allowed boundary, 
        // meaning it belongs in a different branch.
        if (preorder[index] > upperBound) return null;

        // Valid node found. Construct it and move the pointer forward.
        Node root = new Node(preorder[index]);
        index++;

        // Build the Left Branch: The new upper bound becomes the current node's value
        root.left = constructRecursive(preorder, root.value);
        
        // Build the Right Branch: The upper bound remains the parent's upper bound
        root.right = constructRecursive(preorder, upperBound);

        return root;
    }

    // Diagnostic tool to verify the architecture
    public void displayInOrder(Node node) {
        if (node == null) return;
        displayInOrder(node.left);
        System.out.print(node.value + " -> ");
        displayInOrder(node.right);
    }

    public static void main(String[] args) {
        ConstructBST engine = new ConstructBST();
        System.out.println("--- Booting Tree Reconstruction Protocol ---");

        // Simulating a Pre-Order array: [8, 5, 1, 7, 10, 12]
        // This should rebuild into a tree rooted at 8.
        int[] preorderData = {8, 5, 1, 7, 10, 12};
        
        System.out.println("Ingesting Pre-Order Data Stream...");
        Node reconstructedRoot = engine.bstFromPreorder(preorderData);

        System.out.print("Verifying structural integrity (In-Order output should be sorted): ");
        engine.displayInOrder(reconstructedRoot);
        System.out.println("END\nStatus: Architecture successfully reconstructed in O(N).");
    }
}