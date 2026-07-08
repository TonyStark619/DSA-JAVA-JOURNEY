import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class TreeSerialization {
    private static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }

    // O(N) Time - Flattens the 2D architecture into a 1D String
    public String serialize(Node root) {
        if (root == null) {
            return "X,"; // 'X' represents a null pointer
        }
        // Pre-Order: Root, Left, Right
        String leftSerialized = serialize(root.left);
        String rightSerialized = serialize(root.right);
        
        return root.value + "," + leftSerialized + rightSerialized;
    }

    // O(N) Time - Reads the String and reconstructs the memory pointers
    public Node deserialize(String data) {
        // Convert the comma-separated string into a FIFO Queue for continuous reading
        Queue<String> nodes = new LinkedList<>(Arrays.asList(data.split(",")));
        return buildTree(nodes);
    }

    private Node buildTree(Queue<String> nodes) {
        String currentVal = nodes.poll();
        
        if (currentVal.equals("X")) {
            return null; // Dead end, branch terminates here
        }
        
        // Reconstruct the node
        Node node = new Node(Integer.parseInt(currentVal));
        
        // Because it was serialized in Pre-Order, the next items belong to the left branch, then the right
        node.left = buildTree(nodes);
        node.right = buildTree(nodes);
        
        return node;
    }

    // Diagnostic tool
    public void displayPreOrder(Node node) {
        if (node == null) return;
        System.out.print(node.value + " ");
        displayPreOrder(node.left);
        displayPreOrder(node.right);
    }

    public static void main(String[] args) {
        TreeSerialization engine = new TreeSerialization();
        System.out.println("--- Booting Architecture Compression Engine ---");

        // Building the Original Architecture:
        //       10
        //      /  \
        //     20   30
        //         /  \
        //        40   50
        Node root = new Node(10);
        root.left = new Node(20);
        root.right = new Node(30);
        root.right.left = new Node(40);
        root.right.right = new Node(50);

        // Step 1: Serialize to a raw data stream
        String compressedData = engine.serialize(root);
        System.out.println("\nSerialized Data Stream: " + compressedData);

        // Step 2: Deserialize back into a structural hierarchy
        Node reconstructedRoot = engine.deserialize(compressedData);
        System.out.print("\nReconstructed Pre-Order Signature: ");
        engine.displayPreOrder(reconstructedRoot);
        System.out.println("\n\nStatus: 2D-to-1D translation and architectural reconstruction successful.");
    }
}