import java.util.*;

public class WaveletTreeEngine {

    static class WaveletNode {
        int lowVal, highVal;
        WaveletNode left, right;
        // Prefix sums of elements that went to the left child
        List<Integer> leftPrefixSums;

        public WaveletNode(int lowVal, int highVal) {
            this.lowVal = lowVal;
            this.highVal = highVal;
            this.leftPrefixSums = new ArrayList<>();
        }
    }

    private WaveletNode root;

    // O(N log Σ) - Build the Wavelet Tree geometry
    public WaveletTreeEngine(int[] arr, int minVal, int maxVal) {
        System.out.println("  [System] Forging Wavelet Bit-Vector Geometry...");
        this.root = buildNode(arr, minVal, maxVal);
    }

    private WaveletNode buildNode(int[] arr, int low, int high) {
        if (arr.length == 0 || low > high) return null;

        WaveletNode node = new WaveletNode(low, high);
        node.leftPrefixSums.add(0); // Base prefix sum

        if (low == high) return node;

        int mid = low + (high - low) / 2;
        int leftCount = 0;

        // Count how many elements belong to the left bucket and build prefix sums
        for (int val : arr) {
            if (val <= mid) {
                leftCount++;
            }
            node.leftPrefixSums.add(leftCount);
        }

        // Mathematically partition the array into left and right branches
        int[] leftArr = new int[leftCount];
        int[] rightArr = new int[arr.length - leftCount];
        int lIdx = 0, rIdx = 0;

        for (int val : arr) {
            if (val <= mid) leftArr[lIdx++] = val;
            else rightArr[rIdx++] = val;
        }

        // Recursively build children
        node.left = buildNode(leftArr, low, mid);
        node.right = buildNode(rightArr, mid + 1, high);

        return node;
    }

    // O(log Σ) - Core Quantile Engine: Finds the k-th smallest element in range [L, R]
    public int kthSmallest(int L, int R, int k) {
        return queryKth(this.root, L, R, k);
    }

    private int queryKth(WaveletNode node, int L, int R, int k) {
        if (node.lowVal == node.highVal) {
            return node.lowVal; // We have isolated the exact mathematical value
        }

        // Calculate how many elements in our specific [L, R] range went to the left child
        int countLeftInL = node.leftPrefixSums.get(L - 1);
        int countLeftInR = node.leftPrefixSums.get(R);
        int elementsGoingLeft = countLeftInR - countLeftInL;

        if (k <= elementsGoingLeft) {
            // The k-th smallest element MUST be in the left child.
            // We map our L and R pointers to the new geometric bounds in the left branch.
            int newL = countLeftInL + 1;
            int newR = countLeftInR;
            return queryKth(node.left, newL, newR, k);
        } else {
            // The k-th smallest element is in the right child.
            // We map our pointers to the right branch and adjust k.
            int newL = (L - 1 - countLeftInL) + 1;
            int newR = (R - countLeftInR);
            return queryKth(node.right, newL, newR, k - elementsGoingLeft);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Wavelet Tree Quantile Architecture ---");
        
        // Target Array (1-based indexing logic handled internally)
        int[] database = {3, 1, 7, 4, 9, 2, 8, 5, 6};
        System.out.println("Target Database: {3, 1, 7, 4, 9, 2, 8, 5, 6}");
        
        WaveletTreeEngine wavelet = new WaveletTreeEngine(database, 1, 9);
        
        System.out.println("\n--- Executing Strict O(log Σ) Quantile Queries ---");
        
        // Range [1, 5] is {3, 1, 7, 4, 9}. Sorted it is {1, 3, 4, 7, 9}.
        int k = 3;
        int result = wavelet.kthSmallest(1, 5, k);
        System.out.println("The " + k + "-rd smallest element in range [1, 5]: " + result); // Expected: 4
        
        // Range [6, 9] is {2, 8, 5, 6}. Sorted it is {2, 5, 6, 8}.
        k = 2;
        result = wavelet.kthSmallest(6, 9, k);
        System.out.println("The " + k + "-nd smallest element in range [6, 9]: " + result); // Expected: 5
        
        System.out.println("\nStatus: Range quantile bottlenecks completely shattered via bit-vector prefix geometry.");
    }
}