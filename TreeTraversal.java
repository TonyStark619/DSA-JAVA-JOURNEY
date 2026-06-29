public class TreeTraversal {
    private static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }

    // 1. Pre-Order (Root, Left, Right) - Evaluates the node before its children
    public static void preOrder(Node node) {
        if (node == null) return;
        System.out.print(node.value + " ");
        preOrder(node.left);
        preOrder(node.right);
    }

    // 2. In-Order (Left, Root, Right) - Evaluates bottom-left first, then moves right
    public static void inOrder(Node node) {
        if (node == null) return;
        inOrder(node.left);
        System.out.print(node.value + " ");
        inOrder(node.right);
    }

    // 3. Post-Order (Left, Right, Root) - Evaluates children completely before the parent
    public static void postOrder(Node node) {
        if (node == null) return;
        postOrder(node.left);
        postOrder(node.right);
        System.out.print(node.value + " ");
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Hierarchical Navigation Diagnostics ---");
        
        // Manually constructing a small test architecture:
        //        10
        //       /  \
        //      20   30
        //     / \
        //    40 50
        Node root = new Node(10);
        root.left = new Node(20);
        root.right = new Node(30);
        root.left.left = new Node(40);
        root.left.right = new Node(50);

        System.out.print("Pre-Order Traversal:  ");
        preOrder(root);
        
        System.out.print("\nIn-Order Traversal:   ");
        inOrder(root);
        
        System.out.print("\nPost-Order Traversal: ");
        postOrder(root);
        System.out.println("\n\nStatus: DFS memory navigation fully operational.");
    }
}