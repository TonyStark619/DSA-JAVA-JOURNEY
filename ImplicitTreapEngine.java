import java.util.Random;

public class ImplicitTreapEngine {

    static class Node {
        int value;
        int priority;
        int subtreeSize;
        boolean reverseFlag;
        Node left, right;

        public Node(int value) {
            this.value = value;
            this.priority = new Random().nextInt();
            this.subtreeSize = 1;
            this.reverseFlag = false;
        }
    }

    private Node root;

    private int getSize(Node node) {
        return node == null ? 0 : node.subtreeSize;
    }

    private void updateGeometry(Node node) {
        if (node != null) {
            node.subtreeSize = 1 + getSize(node.left) + getSize(node.right);
        }
    }

    // O(1) - Lazy Propagation: Pushes the reverse flag down to the children only when needed
    private void push(Node node) {
        if (node != null && node.reverseFlag) {
            Node temp = node.left;
            node.left = node.right;
            node.right = temp;
            
            if (node.left != null) node.left.reverseFlag = !node.left.reverseFlag;
            if (node.right != null) node.right.reverseFlag = !node.right.reverseFlag;
            
            node.reverseFlag = false;
        }
    }

    // THE MAGIC: O(log N) - Splits the array into two separate trees at exact index K
    private Node[] split(Node node, int k) {
        if (node == null) return new Node[]{null, null};
        push(node);

        int leftSize = getSize(node.left);
        if (leftSize >= k) {
            Node[] splitResult = split(node.left, k);
            node.left = splitResult[1];
            updateGeometry(node);
            return new Node[]{splitResult[0], node};
        } else {
            Node[] splitResult = split(node.right, k - leftSize - 1);
            node.right = splitResult[0];
            updateGeometry(node);
            return new Node[]{node, splitResult[1]};
        }
    }

    // THE MAGIC: O(log N) - Merges two arrays back together maintaining Heap priorities
    private Node merge(Node leftTree, Node rightTree) {
        push(leftTree);
        push(rightTree);
        if (leftTree == null) return rightTree;
        if (rightTree == null) return leftTree;

        if (leftTree.priority > rightTree.priority) {
            leftTree.right = merge(leftTree.right, rightTree);
            updateGeometry(leftTree);
            return leftTree;
        } else {
            rightTree.left = merge(leftTree, rightTree.left);
            updateGeometry(rightTree);
            return rightTree;
        }
    }

    public void insertAtEnd(int value) {
        root = merge(root, new Node(value));
    }

    // O(log N) - Reverse a massive subarray instantly using geometric splitting
    public void reverseRange(int L, int R) {
        System.out.println("  [System] Executing O(log N) geometric split and lazy reversal on range [" + L + ", " + R + "]...");
        
        // 1. Split array into [0 to L-1] and [L to End]
        Node[] split1 = split(root, L);
        
        // 2. Split [L to End] into [L to R] and [R+1 to End]
        Node[] split2 = split(split1[1], R - L + 1);
        
        // 3. The target range is now isolated. Flip the O(1) lazy flag.
        Node targetRange = split2[0];
        if (targetRange != null) targetRange.reverseFlag = !targetRange.reverseFlag;
        
        // 4. Merge them all back together seamlessly
        root = merge(split1[0], merge(targetRange, split2[1]));
    }

    public void printArray(Node node) {
        if (node == null) return;
        push(node);
        printArray(node.left);
        System.out.print(node.value + " ");
        printArray(node.right);
    }

    public void display() {
        printArray(root);
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Implicit Treap (Cartesian Array) Architecture ---");
        
        ImplicitTreapEngine treapArray = new ImplicitTreapEngine();
        System.out.println("  [System] Building array from index 0 to 9...");
        for (int i = 0; i < 10; i++) treapArray.insertAtEnd(i);
        
        System.out.print("Original Memory State: ");
        treapArray.display();
        
        // Target: Reverse indices 2 through 7 (which contains 2,3,4,5,6,7)
        treapArray.reverseRange(2, 7);
        
        System.out.print("Mutated Memory State:  ");
        treapArray.display(); 
        // Expected: 0 1 7 6 5 4 3 2 8 9
        
        System.out.println("\nStatus: Massive array block operations successfully reduced to O(log N) pointer rewiring.");
    }
}