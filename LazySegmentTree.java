public class LazySegmentTree {
    private int[] tree;
    private int[] lazy;
    private int n;

    public LazySegmentTree(int[] arr) {
        n = arr.length;
        tree = new int[4 * n];
        lazy = new int[4 * n];
        buildTree(arr, 0, 0, n - 1);
    }

    private void buildTree(int[] arr, int node, int start, int end) {
        if (start == end) {
            tree[node] = arr[start];
            return;
        }
        int mid = start + (end - start) / 2;
        buildTree(arr, 2 * node + 1, start, mid);
        buildTree(arr, 2 * node + 2, mid + 1, end);
        tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
    }

    // The Core Optimization: Resolve pending updates before processing the node
    private void propagate(int node, int start, int end) {
        if (lazy[node] != 0) {
            // Apply the pending update to the current node
            tree[node] += (end - start + 1) * lazy[node];
            
            // If it has children, pass the "lazy" debt down to them
            if (start != end) {
                lazy[2 * node + 1] += lazy[node];
                lazy[2 * node + 2] += lazy[node];
            }
            // Clear the current node's debt
            lazy[node] = 0; 
        }
    }

    // O(log N) Time - Update a massive range instantly
    public void updateRange(int node, int start, int end, int l, int r, int val) {
        propagate(node, start, end); // Clear pending debts first

        // Case 1: No overlap
        if (start > end || start > r || end < l) return;

        // Case 2: Total Overlap
        if (start >= l && end <= r) {
            // Update this covering node, leave a lazy tag for the children, and return instantly
            tree[node] += (end - start + 1) * val;
            if (start != end) {
                lazy[2 * node + 1] += val;
                lazy[2 * node + 2] += val;
            }
            return;
        }

        // Case 3: Partial Overlap
        int mid = start + (end - start) / 2;
        updateRange(2 * node + 1, start, mid, l, r, val);
        updateRange(2 * node + 2, mid + 1, end, l, r, val);
        tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
    }

    // O(log N) Time - Query with Lazy Resolution
    public int queryRange(int node, int start, int end, int l, int r) {
        propagate(node, start, end); // Ensure the node has accurate data before reading

        if (start > end || start > r || end < l) return 0;
        if (start >= l && end <= r) return tree[node];

        int mid = start + (end - start) / 2;
        int leftSum = queryRange(2 * node + 1, start, mid, l, r);
        int rightSum = queryRange(2 * node + 2, mid + 1, end, l, r);
        return leftSum + rightSum;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Lazy Propagation Architecture ---");
        int[] data = {1, 2, 3, 4, 5, 6};
        LazySegmentTree engine = new LazySegmentTree(data);

        System.out.println("Initial Sum (Indices 0 to 3): " + engine.queryRange(0, 0, 5, 0, 3)); 
        
        System.out.println("\nInjecting +10 to massive range (Indices 0 to 5) in O(log N)...");
        // Update the entire array with +10. A standard loop takes O(N). We do it instantly.
        engine.updateRange(0, 0, 5, 0, 5, 10);
        
        System.out.println("\nNew Sum (Indices 0 to 3): " + engine.queryRange(0, 0, 5, 0, 3)); 
        System.out.println("Status: Lazy updates successfully propagated and calculated.");
    }
}