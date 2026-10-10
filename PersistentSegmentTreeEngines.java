import java.util.*;

public class PersistentSegmentTreeEngines {

    static class Node {
        int sum;
        Node left, right;

        public Node(int sum, Node left, Node right) {
            this.sum = sum;
            this.left = left;
            this.right = right;
        }
    }

    // Stores the root node of the Segment Tree for every single historical version
    private List<Node> versionRoots = new ArrayList<>();
    private int n;

    public PersistentSegmentTreeEngine(int[] arr) {
        this.n = arr.length;
        System.out.println("  [System] Forging Base Geometry (Version 0)...");
        versionRoots.add(build(arr, 0, n - 1));
    }

    // O(N) - Builds the initial geometry
    private Node build(int[] arr, int l, int r) {
        if (l == r) {
            return new Node(arr[l], null, null);
        }
        int mid = l + (r - l) / 2;
        Node leftChild = build(arr, l, mid);
        Node rightChild = build(arr, mid + 1, r);
        return new Node(leftChild.sum + rightChild.sum, leftChild, rightChild);
    }

    // THE MAGIC: O(log N) - Creates a new historical snapshot sharing memory with the old version
    private Node upgrade(Node prev, int l, int r, int targetIndex, int newValue) {
        if (l == r) {
            // Reached the target leaf. Create a brand new node with the new value.
            return new Node(newValue, null, null);
        }

        int mid = l + (r - l) / 2;
        
        // If the update falls in the left half, we create a NEW left child, 
        // but we literally recycle the exact memory pointer of the OLD right child.
        if (targetIndex <= mid) {
            Node newLeft = upgrade(prev.left, l, mid, targetIndex, newValue);
            return new Node(newLeft.sum + prev.right.sum, newLeft, prev.right);
        } 
        // Vice versa for the right half.
        else {
            Node newRight = upgrade(prev.right, mid + 1, r, targetIndex, newValue);
            return new Node(prev.left.sum + newRight.sum, prev.left, newRight);
        }
    }

    // Core Execution: Triggers an upgrade and locks the new version into history
    public void executeUpdate(int targetIndex, int newValue) {
        Node previousRoot = versionRoots.get(versionRoots.size() - 1);
        Node newRoot = upgrade(previousRoot, 0, n - 1, targetIndex, newValue);
        versionRoots.add(newRoot);
        System.out.println("  -> [System] Update committed. Version " + (versionRoots.size() - 1) + " locked in memory.");
    }

    // O(log N) - Queries ANY historical version of the array
    public int queryHistory(Node node, int l, int r, int queryL, int queryR) {
        if (queryL > r || queryR < l) return 0;
        if (queryL <= l && r <= queryR) return node.sum;

        int mid = l + (r - l) / 2;
        return queryHistory(node.left, l, mid, queryL, queryR) + 
               queryHistory(node.right, mid + 1, r, queryL, queryR);
    }

    public void runDiagnostics(int version, int L, int R) {
        Node targetRoot = versionRoots.get(version);
        int result = queryHistory(targetRoot, 0, n - 1, L, R);
        System.out.println("Querying Version " + version + " [Range " + L + " to " + R + "]: " + result);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Persistent Segment Tree Architecture ---");
        
        int[] initialLedger = {1, 2, 3, 4, 5};
        System.out.println("Initial State (Version 0): {1, 2, 3, 4, 5}");
        PersistentSegmentTreeEngine engine = new PersistentSegmentTreeEngine(initialLedger);
        
        // Update 1: Change index 2 (value 3) to value 10
        System.out.println("\n[Transaction] Modifying Index 2 to value 10...");
        engine.executeUpdate(2, 10); // State is now {1, 2, 10, 4, 5} -> Version 1
        
        // Update 2: Change index 4 (value 5) to value 20
        System.out.println("[Transaction] Modifying Index 4 to value 20...");
        engine.executeUpdate(4, 20); // State is now {1, 2, 10, 4, 20} -> Version 2
        
        System.out.println("\n--- Executing Time-Travel Queries ---");
        // Sum of indices 1 through 4
        engine.runDiagnostics(0, 1, 4); // Oldest history: 2+3+4+5 = 14
        engine.runDiagnostics(1, 1, 4); // Version 1: 2+10+4+5 = 21
        engine.runDiagnostics(2, 1, 4); // Latest version: 2+10+4+20 = 36
        
        System.out.println("\nStatus: Historical array states successfully isolated and queried in strict O(log N) via path-copying.");
    }
}