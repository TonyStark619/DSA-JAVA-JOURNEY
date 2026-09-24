public class LazySegmentTreeEngine {
    private int[] tree;
    private int[] lazy;
    private int n;

    public LazySegmentTreeEngine(int[] arr) {
        this.n = arr.length;
        this.tree = new int[4 * n];
        this.lazy = new int[4 * n];
        buildTree(arr, 0, 0, n - 1);
    }

    // O(N) - Initial Tree Construction
    private void buildTree(int[] arr, int node, int start, int end) {
        if (start == end) {
            tree[node] = arr[start];
            return;
        }
        int mid = start + (end - start) / 2;
        int leftChild = 2 * node + 1;
        int rightChild = 2 * node + 2;
        
        buildTree(arr, leftChild, start, mid);
        buildTree(arr, rightChild, mid + 1, end);
        tree[node] = tree[leftChild] + tree[rightChild];
    }

    // O(1) - The Core Lazy Logic
    private void propagate(int node, int start, int end) {
        if (lazy[node] != 0) {
            // Apply the pending lazy update to the current node
            tree[node] += (end - start + 1) * lazy[node];
            
            // If it's not a leaf node, push the lazy flag down to its children
            if (start != end) {
                lazy[2 * node + 1] += lazy[node];
                lazy[2 * node + 2] += lazy[node];
            }
            // Clear the current node's lazy flag
            lazy[node] = 0;
        }
    }

    // O(log N) - Range Update utilizing Lazy Flags
    public void updateRange(int node, int start, int end, int l, int r, int value) {
        propagate(node, start, end); // Always clear pending updates first

        // Out of bounds
        if (start > end || start > r || end < l) return;

        // Current segment is FULLY inside the update range
        if (start >= l && end <= r) {
            // THE MAGIC: We update this parent node, flag it as lazy, and STOP. 
            // We do not traverse down to the leaves.
            tree[node] += (end - start + 1) * value;
            if (start != end) {
                lazy[2 * node + 1] += value;
                lazy[2 * node + 2] += value;
            }
            return;
        }

        // Partial overlap - traverse deeper
        int mid = start + (end - start) / 2;
        updateRange(2 * node + 1, start, mid, l, r, value);
        updateRange(2 * node + 2, mid + 1, end, l, r, value);
        
        tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
    }

    // O(log N) - Range Query with Lazy Resolution
    public int queryRange(int node, int start, int end, int l, int r) {
        propagate(node, start, end); // Ensure the node has accurate data before returning it

        if (start > end || start > r || end < l) return 0; // Out of bounds

        if (start >= l && end <= r) return tree[node]; // Fully inside

        int mid = start + (end - start) / 2;
        int leftSum = queryRange(2 * node + 1, start, mid, l, r);
        int rightSum = queryRange(2 * node + 2, mid + 1, end, l, r);
        
        return leftSum + rightSum;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Lazy Segment Tree Architecture ---");
        
        int[] database = {1, 3, 5, 7, 9, 11};
        LazySegmentTreeEngine engine = new LazySegmentTreeEngine(database);
        
        System.out.println("\n[System] Initial sum of range [1, 3]: " + engine.queryRange(0, 0, 5, 1, 3)); // 3+5+7 = 15
        
        System.out.println("  -> Executing massive range update: Add +10 to indices [1, 5]...");
        engine.updateRange(0, 0, 5, 1, 5, 10);
        
        System.out.println("[System] New sum of range [1, 3]: " + engine.queryRange(0, 0, 5, 1, 3)); // (3+10) + (5+10) + (7+10) = 45
        
        System.out.println("\nStatus: O(N) traversal mathematically bypassed. Pending updates successfully stored in memory.");
    }
}