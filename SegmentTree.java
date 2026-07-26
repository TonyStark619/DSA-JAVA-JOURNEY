public class SegmentTree {
    private int[] tree;
    private int n;

    // Initialize the architecture (A segment tree requires up to 4N memory space)
    public SegmentTree(int[] arr) {
        n = arr.length;
        tree = new int[4 * n];
        buildTree(arr, 0, 0, n - 1);
    }

    // O(N) Time - Construct the hierarchical range memory
    private void buildTree(int[] arr, int treeIndex, int left, int right) {
        if (left == right) {
            tree[treeIndex] = arr[left]; // Leaf node: holds the actual array value
            return;
        }
        int mid = left + (right - left) / 2;
        int leftChild = 2 * treeIndex + 1;
        int rightChild = 2 * treeIndex + 2;

        buildTree(arr, leftChild, left, mid);
        buildTree(arr, rightChild, mid + 1, right);

        // The current node stores the sum of its left and right sub-ranges
        tree[treeIndex] = tree[leftChild] + tree[rightChild];
    }

    // O(log N) Time - High-Speed Range Analytics
    public int querySum(int queryLeft, int queryRight) {
        return queryRange(0, 0, n - 1, queryLeft, queryRight);
    }

    private int queryRange(int treeIndex, int left, int right, int queryLeft, int queryRight) {
        // Case 1: Total Overlap (This node's range is completely inside the query)
        if (queryLeft <= left && queryRight >= right) {
            return tree[treeIndex];
        }
        // Case 2: No Overlap (This node is completely outside the query boundary)
        if (queryRight < left || queryLeft > right) {
            return 0;
        }
        // Case 3: Partial Overlap (We must split and search both branches)
        int mid = left + (right - left) / 2;
        int leftSum = queryRange(2 * treeIndex + 1, left, mid, queryLeft, queryRight);
        int rightSum = queryRange(2 * treeIndex + 2, mid + 1, right, queryLeft, queryRight);
        
        return leftSum + rightSum;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting O(log N) Segment Tree Architecture ---");
        
        // Simulating 6 days of financial revenue data
        int[] dailyRevenue = {10, 20, 30, 40, 50, 60};
        SegmentTree analyticsEngine = new SegmentTree(dailyRevenue);
        
        System.out.println("Financial data ingested and hierarchical tree constructed.");
        
        // Querying the sum from Day 1 to Day 3 (Indices 1 to 3) -> 20 + 30 + 40 = 90
        int qLeft = 1, qRight = 3;
        int result = analyticsEngine.querySum(qLeft, qRight);
        
        System.out.println("\nExecuting Range Query [Day " + qLeft + " to Day " + qRight + "]...");
        System.out.println("Total Revenue: $" + result);
        System.out.println("Status: Range successfully calculated in logarithmic time.");
    }
}