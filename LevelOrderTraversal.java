import java.util.LinkedList;
import java.util.Queue;

public class LevelOrderTraversal {
    private static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }

    // O(N) Time, O(N) Space - Level-by-Level Scan using a FIFO Queue
    public static void breadthFirstSearch(Node root) {
        if (root == null) return;

        // Initialize the FIFO Queue and inject the root node
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            // 1. Remove the front node
            Node currentNode = queue.poll();
            System.out.print(currentNode.value + " ");

            // 2. If it has a left child, queue it up for the next level
            if (currentNode.left != null) {
                queue.add(currentNode.left);
            }

            // 3. If it has a right child, queue it up for the next level
            if (currentNode.right != null) {
                queue.add(currentNode.right);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting BFS Level Scanner ---");
        
        // Constructing the Architecture:
        //        10          (Level 1)
        //       /  \
        //      20   30       (Level 2)
        //     / \     \
        //    40 50    60     (Level 3)
        Node root = new Node(10);
        root.left = new Node(20);
        root.right = new Node(30);
        root.left.left = new Node(40);
        root.left.right = new Node(50);
        root.right.right = new Node(60);

        System.out.print("Level Order Output: ");
        breadthFirstSearch(root);
        
        System.out.println("\nStatus: Hierarchical breadth mapping successful.");
    }
}