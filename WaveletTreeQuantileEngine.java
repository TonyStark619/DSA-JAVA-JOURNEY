import java.util.*;

public class WaveletTreeQuantileEngine {

    static class WaveletNode {
        int lowVal;
        int highVal;
        // mapLeft[i] stores how many elements from prefix [0..i-1] went to the left child
        int[] mapLeft;
        WaveletNode left;
        WaveletNode right;

        public WaveletNode(int[] array, int n, int low, int high) {
            this.lowVal = low;
            this.highVal = high;
            this.mapLeft = new int[n + 1];

            // Base Case: Value domain has collapsed to a single unique integer
            if (low == high || n == 0) {
                return;
            }

            int mid = low + (high - low) / 2;

            // First pass: Build the O(1) prefix-rank lookup table
            for (int i = 0; i < n; i++) {
                mapLeft[i + 1] = mapLeft[i] + (array[i] <= mid ? 1 : 0);
            }

            int leftCount = mapLeft[n];
            int rightCount = n - leftCount;

            int[] leftSubArray = new int[leftCount];
            int[] rightSubArray = new int[rightCount];

            int lIdx = 0, rIdx = 0;
            for (int i = 0; i < n; i++) {
                if (array[i] <= mid) {
                    leftSubArray[lIdx++] = array[i];
                } else {
                    rightSubArray[rIdx++] = array[i];
                }
            }

            // Recursively partition the value alphabet
            if (leftCount > 0) {
                this.left = new WaveletNode(leftSubArray, leftCount, low, mid);
            }
            if (rightCount > 0) {
                this.right = new WaveletNode(rightSubArray, rightCount, mid + 1, high);
            }
        }
    }

    private final WaveletNode root;

    public WaveletTreeQuantileEngine(int[] dataset, int minAlphabet, int maxAlphabet) {
        System.out.println("  [System] Constructing Wavelet Tree over Value Domain [" + minAlphabet + ".." + maxAlphabet + "]...");
        this.root = new WaveletNode(dataset, dataset.length, minAlphabet, maxAlphabet);
    }

    // O(log Sigma) - Isolates the k-th smallest element (1-indexed) in subarray [l..r] (0-indexed)
    public int queryKthSmallest(int l, int r, int k) {
        return kthSmallestRecursive(root, l, r, k);
    }

    private int kthSmallestRecursive(WaveletNode node, int l, int r, int k) {
        // Base Case: Leaf node reached; the value domain is our exact answer
        if (node.lowVal == node.highVal) {
            return node.lowVal;
        }

        // O(1) Rank Mapping: Count how many elements in current range [l..r] went to the Left Child
        int leftBeforeL = node.mapLeft[l];
        int leftUpToR = node.mapLeft[r + 1];
        int countInLeftChild = leftUpToR - leftBeforeL;

        // If at least 'k' elements in [l..r] are <= mid, the k-th smallest MUST lie in the Left Child
        if (k <= countInLeftChild) {
            int newL = leftBeforeL;
            int newR = leftUpToR - 1;
            return kthSmallestRecursive(node.left, newL, newR, k);
        } 
        // Otherwise, we subtract the left elements and search for the (k - countInLeftChild)-th in the Right Child
        else {
            int newL = l - leftBeforeL;
            int newR = r - leftUpToR;
            return kthSmallestRecursive(node.right, newL, newR, k - countInLeftChild);
        }
    }

    // O(log Sigma) - Counts how many elements in subarray [l..r] are <= targetThreshold
    public int countLessThanOrEqual(int l, int r, int targetThreshold) {
        return countRecursive(root, l, r, targetThreshold);
    }

    private int countRecursive(WaveletNode node, int l, int r, int target) {
        if (node == null || l > r || target < node.lowVal) return 0;
        if (target >= node.highVal) return (r - l + 1);

        int leftBeforeL = node.mapLeft[l];
        int leftUpToR = node.mapLeft[r + 1];

        return countRecursive(node.left, leftBeforeL, leftUpToR - 1, target) +
               countRecursive(node.right, l - leftBeforeL, r - leftUpToR, target);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Wavelet Tree Succinct Range Quantile Engine ---");

        // Simulated server latency stream in milliseconds
        int[] latencyStream = {12, 5, 29, 8, 12, 45, 3, 19, 8, 33};
        System.out.println("  Telemetry Array : " + Arrays.toString(latencyStream));

        WaveletTreeQuantileEngine engine = new WaveletTreeQuantileEngine(latencyStream, 1, 50);

        System.out.println("\n--- Executing O(log Sigma) Subarray Order-Statistic Queries ---");
        // Subarray [2..7] is {29, 8, 12, 45, 3, 19} -> Sorted: {3, 8, 12, 19, 29, 45}
        System.out.println("Subarray [2..7] 1st Smallest (Min)    -> " + engine.queryKthSmallest(2, 7, 1) + " ms"); // 3
        System.out.println("Subarray [2..7] 3rd Smallest (Median) -> " + engine.queryKthSmallest(2, 7, 3) + " ms"); // 12
        System.out.println("Subarray [2..7] 6th Smallest (Max)    -> " + engine.queryKthSmallest(2, 7, 6) + " ms"); // 45

        int slaCount = engine.countLessThanOrEqual(2, 7, 15);
        System.out.println("Subarray [2..7] Requests <= 15ms SLA  -> " + slaCount); // {3, 8, 12} -> 3

        System.out.println("\nStatus: O(N log N) subarray sorting bottleneck eliminated via O(log Sigma) alphabet bit-partitioning.");
    }
}