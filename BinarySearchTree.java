public class BinarySearchTree {
    private Node root;

    private static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }

    // Public method to insert data into the tree
    public void insert(int value) {
        root = insertRecursive(root, value);
    }

    // Recursive helper to navigate the hierarchy and find the correct memory slot
    private Node insertRecursive(Node node, int value) {
        // Base case: If we reach an empty spot, create and return the new node
        if (node == null) {
            return new Node(value);
        }

        // If value is less than current node, branch left
        if (value < node.value) {
            node.left = insertRecursive(node.left, value);
        } 
        // If value is greater than current node, branch right
        else if (value > node.value) {
            node.right = insertRecursive(node.right, value);
        }

        // Return the unchanged node pointer
        return node;
    }

    // In-Order traversal (Left, Root, Right) to prove the BST sorting property
    public void displaySorted() {
        displayInOrder(root);
        System.out.println("END");
    }

    private void displayInOrder(Node node) {
        if (node == null) return;
        displayInOrder(node.left);
        System.out.print(node.value + " -> ");
        displayInOrder(node.right);
    }

    public static void main(String[] args) {
        BinarySearchTree bst = new BinarySearchTree();
        System.out.println("--- Booting Binary Search Tree Architecture ---");

        // Inserting unsorted data
        bst.insert(50);
        bst.insert(30);
        bst.insert(70);
        bst.insert(20);
        bst.insert(40);
        bst.insert(60);
        bst.insert(80);

        System.out.print("Verifying BST Property via In-Order Traversal: ");
        bst.displaySorted();
    }
}