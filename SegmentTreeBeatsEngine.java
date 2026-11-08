import java.util.Arrays;

public class SegmentTreeBeatsEngine {

    private static final long NEG_INF = Long.MIN_VALUE / 2;

    private final int n;
    private final long[] sum;
    private final long[] max1;     // Strict maximum in the segment
    private final long[] max2;     // Strictly second-largest value in the segment
    private final int[] countMax;  // Frequency of max1 in the segment

    public SegmentTreeBeatsEngine(long[] initialArray) {
        this.n = initialArray.length;
        int size = 4 * n;
        this.sum = new long[size];
        this.max1 = new long[size];
        this.max2 = new long[size];
        this.countMax = new int[size];

        System.out.println("  [System] Forging Ji Driver Segment Tree Beats Hierarchy...");
        buildTree(1, 0, n - 1, initialArray);
    }

    // O(1) - Merges child invariants into the parent node
    private void pullUp(int node) {
        int left = node << 1;
        int right = (node << 1) | 1;

        sum[node] = sum[left] + sum[right];

        if (max1[left] == max1[right]) {
            max1[node] = max1[left];
            max2[node] = Math.max(max2[left], max2[right]);
            countMax[node] = countMax[left] + countMax[right];
        } else if (max1[left] > max1[right]) {
            max1[node] = max1[left];
            max2[node] = Math.max(max2[left], max1[right]);
            countMax[node] = countMax[left];
        } else {
            max1[node] = max1[right];
            max2[node] = Math.max(max1[left], max2[right]);
            countMax[node] = countMax[right];
        }
    }

    // O(1) - Applies a ceiling cap to a node where (max2 < capValue < max1)
    private void applyCapTag(int node, long capValue) {
        if (capValue >= max1[node]) return;
        sum[node] -= (long) countMax[node] * (max1[node] - capValue);
        max1[node] = capValue;
    }

    // O(1) - Pushes the parent's ceiling cap down to its children
    private void pushDown(int node) {
        int left = node << 1;
        int right = (node << 1) | 1;
        // A child's maximum can never legally exceed its parent's capped max1
        applyCapTag(left, max1[node]);
        applyCapTag(right, max1[node]);
    }

    private void buildTree(int node, int l, int r, long[] arr) {
        if (l == r) {
            sum[node] = arr[l];
            max1[node] = arr[l];
            max2[node] = NEG_INF;
            countMax[node] = 1;
            return;
        }
        int mid = l + (r - l) / 2;
        buildTree(node << 1, l, mid, arr);
        buildTree((node << 1) | 1, mid + 1, r, arr);
        pullUp(node);
    }

    // Amortized O(log N) - Range Chmin: Sets A[i] = min(A[i], capValue) for all i in [ql, qr]
    public void updateRangeChmin(int ql, int qr, long capValue) {
        updateChminRecursive(1, 0, n - 1, ql, qr, capValue);
    }

    private void updateChminRecursive(int node, int l, int r, int ql, int qr, long capValue) {
        // 1. Ji Driver Break Condition: Subtree is out of range OR all elements are already <= capValue
        if (ql > r || qr < l || capValue >= max1[node]) {
            return;
        }

        // 2. Ji Driver Tag Condition: Full range match AND capValue only affects the strict maximum (max1)
        if (ql <= l && r <= qr && capValue > max2[node]) {
            applyCapTag(node, capValue);
            return;
        }

        // 3. Recurse Condition: capValue <= max2[node], so we must collapse distinct values deeper
        pushDown(node);
        int mid = l + (r - l) / 2;
        updateChminRecursive(node << 1, l, mid, ql, qr, capValue);
        updateChminRecursive((node << 1) | 1, mid + 1, r, ql, qr, capValue);
        pullUp(node);
    }

    // O(log N) - Standard Range Sum Query over [ql, qr]
    public long queryRangeSum(int ql, int qr) {
        return querySumRecursive(1, 0, n - 1, ql, qr);
    }

    private long querySumRecursive(int node, int l, int r, int ql, int qr) {
        if (ql > r || qr < l) return 0;
        if (ql <= l && r <= qr) return sum[node];

        pushDown(node);
        int mid = l + (r - l) / 2;
        return querySumRecursive(node << 1, l, mid, ql, qr) +
               querySumRecursive((node << 1) | 1, mid + 1, r, ql, qr);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Ji Driver's Segment Tree Beats Engine ---");

        long[] serverBurstLoads = {10, 50, 30, 80, 50, 20};
        System.out.println("  Initial Burst Loads : " + Arrays.toString(serverBurstLoads));

        SegmentTreeBeatsEngine beats = new SegmentTreeBeatsEngine(serverBurstLoads);
        System.out.println("  Baseline Sum [0..5] : " + beats.queryRangeSum(0, 5)); // 240

        System.out.println("\n--- Executing Amortized O(log N) Range Ceiling Caps (chmin) ---");
        // Cap subarray [1..4] at 45 -> {10, 45, 30, 45, 45, 20}
        beats.updateRangeChmin(1, 4, 45);
        System.out.println("  After Cap([1..4], 45) -> Range Sum [0..5] : " + beats.queryRangeSum(0, 5)); // Expected: 195

        // Cap subarray [0..5] at 25 -> {10, 25, 25, 25, 25, 20}
        beats.updateRangeChmin(0, 5, 25);
        System.out.println("  After Cap([0..5], 25) -> Range Sum [0..5] : " + beats.queryRangeSum(0, 5)); // Expected: 130
        System.out.println("  Subarray Sum [1..3]                       : " + beats.queryRangeSum(1, 3)); // Expected: 75

        System.out.println("\nStatus: Non-linear range-min updates resolved in amortized O(log N) via second-maximum potential pruning.");
    }
}