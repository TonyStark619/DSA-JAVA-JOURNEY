import java.util.Scanner;

public class BinaryTreeFoundation {
    private Node root;

    private static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }

    // Initialize the root of the tree
    public void populate(Scanner scanner) {
        System.out.print("Enter the root node value: ");
        int value = scanner.nextInt();
        root = new Node(value);
        populate(scanner, root);
    }

    // Recursive function to build the left and right branches
    private void populate(Scanner scanner, Node node) {
        System.out.print("Do you want to enter left of " + node.value + "? (true/false): ");
        boolean left = scanner.nextBoolean();
        if (left) {
            System.out.print("Enter the value of the left node: ");
            int value = scanner.nextInt();
            node.left = new Node(value);
            populate(scanner, node.left);
        }

        System.out.print("Do you want to enter right of " + node.value + "? (true/false): ");
        boolean right = scanner.nextBoolean();
        if (right) {
            System.out.print("Enter the value of the right node: ");
            int value = scanner.nextInt();
            node.right = new Node(value);
            populate(scanner, node.right);
        }
    }

    // Basic display to verify the architecture
    public void display() {
        System.out.println("\n--- Booting Tree Architecture ---");
        display(this.root, "");
    }

    private void display(Node node, String indent) {
        if (node == null) return;
        System.out.println(indent + node.value);
        display(node.left, indent + "\t");
        display(node.right, indent + "\t");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BinaryTreeFoundation tree = new BinaryTreeFoundation();
        
        tree.populate(scanner);
        tree.display();
        
        scanner.close();
    }
}