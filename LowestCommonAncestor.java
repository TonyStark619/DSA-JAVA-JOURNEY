public class LowestCommonAncestor {
    private static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }

    // O(log N) Time, O(1) Space - Iterative Split-Point Detection
    public static Node findLCA(Node root, Node p, Node q) {
        Node current = root;

        while (current != null) {
            // Both targets are strictly smaller? The ancestor must be to the left.
            if (p.value < current.value && q.value < current.value) {
                current = current.left;
            }
            // Both targets are strictly larger? The ancestor must be to the right.
            else if (p.value > current.value && q.value > current.value) {
                current = current.right;
            }
            // A split occurs (one is smaller, one is larger, or we hit a target directly).
            // This mathematically guarantees we are standing on the Lowest Common Ancestor.
            else {
                return current;
            }
        }
        return null; // Fallback (should not hit if targets exist)
    }

    public static void main(String[] args) {
        System.out.println("--- Booting LCA Memory Architecture ---");

        // Constructing a standard BST hierarchy:
        //          50
        //        /    \
        //      30      70
        //     /  \    /  \
        //    20  40  60  80
        Node root = new Node(50);
        root.left = new Node(30);
        root.right = new Node(70);
        root.left.left = new Node(20);
        root.left.right = new Node(40);
        root.right.left = new Node(60);
        root.right.right = new Node(80);

        // Test Case 1: LCA of 20 and 40 (Should be 30)
        Node p1 = root.left.left;   // 20
        Node q1 = root.left.right;  // 40
        Node lca1 = findLCA(root, p1, q1);
        System.out.println("LCA of 20 and 40: " + lca1.value);

        // Test Case 2: LCA of 20 and 80 (Should be the root, 50)
        Node p2 = root.left.left;   // 20
        Node q2 = root.right.right; // 80
        Node lca2 = findLCA(root, p2, q2);
        System.out.println("LCA of 20 and 80: " + lca2.value);
    }
}