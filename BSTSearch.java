public class BSTSearch {
    private Node root;

    private static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }

    // Helper to quickly build the tree for testing
    public void insert(int value) {
        root = insertRecursive(root, value);
    }

    private Node insertRecursive(Node node, int value) {
        if (node == null) return new Node(value);
        if (value < node.value) node.left = insertRecursive(node.left, value);
        else if (value > node.value) node.right = insertRecursive(node.right, value);
        return node;
    }

    // O(log N) Time - The Binary Search Engine
    public boolean search(int target) {
        return searchRecursive(root, target);
    }

    private boolean searchRecursive(Node node, int target) {
        // Base Case 1: We hit a dead end (target does not exist)
        if (node == null) {
            return false;
        }
        
        // Base Case 2: Target acquired
        if (node.value == target) {
            return true;
        }
        
        // If target is smaller, strictly search the left branch
        if (target < node.value) {
            return searchRecursive(node.left, target);
        } 
        // If target is larger, strictly search the right branch
        else {
            return searchRecursive(node.right, target);
        }
    }

    public static void main(String[] args) {
        BSTSearch bst = new BSTSearch();
        System.out.println("--- Booting O(log N) Search Architecture ---");

        // Constructing the BST: 50 is root.
        int[] data = {50, 30, 70, 20, 40, 60, 80};
        for (int num : data) {
            bst.insert(num);
        }

        System.out.println("Executing High-Speed Lookups...");
        
        int target1 = 60;
        System.out.println("Searching for " + target1 + ": " + (bst.search(target1) ? "FOUND" : "NOT FOUND"));
        
        int target2 = 99;
        System.out.println("Searching for " + target2 + ": " + (bst.search(target2) ? "FOUND" : "NOT FOUND"));
    }
}