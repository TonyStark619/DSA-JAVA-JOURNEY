public class DeleteNodeBST {
    private static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }

    // O(log N) Time - The Deletion Engine
    public Node deleteNode(Node root, int key) {
        if (root == null) return null;

        // Step 1: Search for the target node
        if (key < root.value) {
            root.left = deleteNode(root.left, key);
        } else if (key > root.value) {
            root.right = deleteNode(root.right, key);
        } 
        // Step 2: Target Acquired. Execute deletion protocol.
        else {
            // Case A: Node has only a right child (or no children)
            if (root.left == null) return root.right;
            
            // Case B: Node has only a left child
            if (root.right == null) return root.left;

            // Case C: Node has TWO children. 
            // We must find the In-Order Successor (smallest node in the right branch)
            Node successor = findMin(root.right);
            
            // Promote the successor's value to the current node
            root.value = successor.value;
            
            // Recursively delete the original successor from the right branch
            root.right = deleteNode(root.right, successor.value);
        }
        return root;
    }

    // Helper: Finds the minimum value in a given branch
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public void displayInOrder(Node node) {
        if (node == null) return;
        displayInOrder(node.left);
        System.out.print(node.value + " -> ");
        displayInOrder(node.right);
    }

    public static void main(String[] args) {
        DeleteNodeBST engine = new DeleteNodeBST();
        System.out.println("--- Booting Architecture Deletion Protocol ---");

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

        System.out.print("Pre-Deletion Architecture:  ");
        engine.displayInOrder(root);

        // Deleting 30 (A node with TWO children)
        int target = 30;
        System.out.println("\n\nExecuting complex deletion on node: " + target);
        root = engine.deleteNode(root, target);

        System.out.print("Post-Deletion Architecture: ");
        engine.displayInOrder(root);
        System.out.println("END\nStatus: Tree integrity maintained.");
    }
}