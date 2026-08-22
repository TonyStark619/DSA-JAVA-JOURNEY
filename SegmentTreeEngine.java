public class SegmentTreeEngine {
    private int[] tree;
    private int[] originalArray;
    private int n;

    public SegmentTreeEngine(int[] arr) {
        this.originalArray = arr;
        this.n = arr.length;
        // The maximum size of a segment tree array is strictly 4 * N
        this.tree = new int[4 * n]; 
        buildTree(0, 0, n - 1);
    }

    // O(N) Time - Initial Tree Construction
    private void buildTree(int nodeIndex, int left, int right) {
        if (left == right) {
            tree[nodeIndex] = originalArray[left]; // Leaf node
            return;
        }
        
        int mid = left + (right - left) / 2;
        int leftChild = 2 * nodeIndex + 1;
        int rightChild = 2 * nodeIndex + 2;
        
        buildTree(leftChild, left, mid);
        buildTree(rightChild, mid + 1, right);
        
        // The core logic: A parent node is the sum of its left and right children
        tree[nodeIndex] = tree[leftChild] + tree[rightChild];
    }

    // O(log N) Time - Dynamic Point Update
    public void update(int nodeIndex, int left, int right, int targetIndex, int newValue) {
        if (left == right) {
            tree[nodeIndex] = newValue;
            originalArray[targetIndex] = newValue;
            return;
        }
        
        int mid = left + (right - left) / 2;
        int leftChild = 2 * nodeIndex + 1;
        int rightChild = 2 * nodeIndex + 2;
        
        // Route the update left or right depending on where the target index lies
        if (targetIndex <= mid) {
            update(leftChild, left, mid, targetIndex, newValue);
        } else {
            update(rightChild, mid + 1, right, targetIndex, newValue);
        }
        
        // Recalculate the parent node on the way back up the recursive stack
        tree[nodeIndex] = tree[leftChild] + tree[rightChild];
    }

    // O(log N) Time - High-Speed Range Query
    public int queryRange(int nodeIndex, int left, int right, int queryStart, int queryEnd) {
        // Case 1: Complete Overlap - This node's exact range is completely inside our query
        if (queryStart <= left && queryEnd >= right) {
            return tree[nodeIndex];
        }
        
        // Case 2: No Overlap - This node is entirely outside our query parameters
        if (queryEnd < left || queryStart > right) {
            return 0; // Return 0 because adding 0 doesn't affect a sum
        }
        
        // Case 3: Partial Overlap - We must split the query and look deeper
        int mid = left + (right - left) / 2;
        int leftChild = 2 * nodeIndex + 1;
        int rightChild = 2 * nodeIndex + 2;
        
        int leftSum = queryRange(leftChild, left, mid, queryStart, queryEnd);
        int rightSum = queryRange(rightChild, mid + 1, right, queryStart, queryEnd);
        
        return leftSum + rightSum;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting O(log N) Segment Tree Architecture ---");
        
        int[] database = {1, 3, 5, 7, 9, 11};
        SegmentTreeEngine segTree = new SegmentTreeEngine(database);
        
        System.out.println("Executing rapid range queries on dataset: [1, 3, 5, 7, 9, 11]");
        
        // Query sum from index 1 to 3: (3 + 5 + 7 = 15)
        int sum = segTree.queryRange(0, 0, database.length - 1, 1, 3);
        System.out.println("Sum of range [1, 3]: " + sum);
        
        System.out.println("\nExecuting O(log N) database mutation at Index 1 (Value 3 -> 10)...");
        // Update index 1 to value 10. Array becomes [1, 10, 5, 7, 9, 11]
        segTree.update(0, 0, database.length - 1, 1, 10);
        
        // Query sum from index 1 to 3 again: (10 + 5 + 7 = 22)
        int newSum = segTree.queryRange(0, 0, database.length - 1, 1, 3);
        System.out.println("New Sum of range [1, 3]: " + newSum);
        
        System.out.println("\nStatus: Dynamic range queried and updated flawlessly without O(N) loop bottlenecks.");
    }
}