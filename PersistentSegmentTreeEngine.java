import java.util.ArrayList;
import java.util.List;

public class PersistentSegmentTreeEngine {

    static class Node {
        int sum;
        Node left, right;
        
        // Constructor for leaf nodes
        public Node(int sum) {
            this.sum = sum;
        }
        
        // Constructor for internal nodes connecting existing children
        public Node(Node left, Node right) {
            this.left = left;
            this.right = right;
            this.sum = (left != null ? left.sum : 0) + (right != null ? right.sum : 0);
        }
    }

    // Stores the root node of the tree for EVERY distinct version in history
    private List<Node> versionRoots = new ArrayList<>();
    private int n;

    public PersistentSegmentTreeEngine(int[] arr) {
        this.n = arr.length;
        // Version 0: The initial state of the database
        versionRoots.add(build(arr, 0, n - 1));
    }

    // O(N) - Initial Tree Construction
    private Node build(int[] arr, int start, int end) {
        if (start == end) {
            return new Node(arr[start]);
        }
        int mid = start + (end - start) / 2;
        return new Node(build(arr, start, mid), build(arr, mid + 1, end));
    }

    // O(log N) - The Core Time-Bending Update
    // Instead of modifying, we return a NEW node path that points to the unchanged historical branches
    private Node upgrade(Node prevNode, int start, int end, int targetIndex, int newValue) {
        if (start == end) {
            return new Node(newValue); // New leaf node
        }
        int mid = start + (end - start) / 2;
        
        if (targetIndex <= mid) {
            // Update goes left. We create a new left child, but reuse the old right child!
            return new Node(upgrade(prevNode.left, start, mid, targetIndex, newValue), prevNode.right);
        } else {
            // Update goes right. We reuse the old left child!
            return new Node(prevNode.left, upgrade(prevNode.right, mid + 1, end, targetIndex, newValue));
        }
    }

    // Public method to trigger an update and lock in a new timeline version
    public void executeVersionedUpdate(int index, int newValue) {
        Node previousRoot = versionRoots.get(versionRoots.size() - 1);
        Node newRoot = upgrade(previousRoot, 0, n - 1, index, newValue);
        versionRoots.add(newRoot);
        System.out.println("  [System] Update committed. New Timeline Locked: Version " + (versionRoots.size() - 1));
    }

    // O(log N) - Querying a specific historical timeline
    private int query(Node node, int start, int end, int l, int r) {
        if (l > end || r < start) return 0;
        if (l <= start && end <= r) return node.sum;
        
        int mid = start + (end - start) / 2;
        return query(node.left, start, mid, l, r) + query(node.right, mid + 1, end, l, r);
    }

    public int queryHistory(int version, int l, int r) {
        if (version >= versionRoots.size()) throw new IllegalArgumentException("Version does not exist");
        return query(versionRoots.get(version), 0, n - 1, l, r);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Persistent Segment Tree (Time-Travel DB) ---");
        
        int[] initialDatabase = {1, 2, 3, 4, 5};
        PersistentSegmentTreeEngine db = new PersistentSegmentTreeEngine(initialDatabase);
        
        System.out.println("\n[Timeline] Version 0 Initialized: {1, 2, 3, 4, 5}");
        
        // Version 1: Change index 2 (val 3) to 10. Array becomes {1, 2, 10, 4, 5}
        db.executeVersionedUpdate(2, 10);
        
        // Version 2: Change index 4 (val 5) to 20. Array becomes {1, 2, 10, 4, 20}
        db.executeVersionedUpdate(4, 20);
        
        System.out.println("\n--- Executing Historical O(log N) Queries ---");
        System.out.println("Sum of range [1, 4] in Version 0: " + db.queryHistory(0, 1, 4)); // 2+3+4+5 = 14
        System.out.println("Sum of range [1, 4] in Version 1: " + db.queryHistory(1, 1, 4)); // 2+10+4+5 = 21
        System.out.println("Sum of range [1, 4] in Version 2: " + db.queryHistory(2, 1, 4)); // 2+10+4+20 = 36
        
        System.out.println("\nStatus: Database versioning complete. Historical queries resolved without memory duplication.");
    }
}