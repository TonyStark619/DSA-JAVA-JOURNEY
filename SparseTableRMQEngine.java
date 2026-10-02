public class SparseTableRMQEngine {
    private int[][] sparseTable;
    private int[] log2;
    private int n;

    // O(N log N) - Heavy preprocessing to build the 2^i geometry
    public SparseTableRMQEngine(int[] arr) {
        this.n = arr.length;
        int maxLog = (int) (Math.log(n) / Math.log(2)) + 1;
        
        sparseTable = new int[n][maxLog];
        log2 = new int[n + 1];

        // Precompute logarithm values for O(1) query resolution
        log2[1] = 0;
        for (int i = 2; i <= n; i++) {
            log2[i] = log2[i / 2] + 1;
        }

        // Base Case: 2^0 (Length 1) is just the element itself
        for (int i = 0; i < n; i++) {
            sparseTable[i][0] = arr[i];
        }

        System.out.println("  [System] Executing O(N log N) Dynamic Programming Sweep...");
        // DP: The minimum of a block of size 2^j is the minimum of two blocks of size 2^(j-1)
        for (int j = 1; j < maxLog; j++) {
            for (int i = 0; i + (1 << j) <= n; i++) {
                sparseTable[i][j] = Math.min(
                    sparseTable[i][j - 1], 
                    sparseTable[i + (1 << (j - 1))][j - 1]
                );
            }
        }
    }

    // O(1) - Absolute Constant Time Range Resolution
    public int queryMin(int L, int R) {
        int length = R - L + 1;
        // Find the largest power of 2 that fits entirely inside this length
        int j = log2[length];
        
        // THE MAGIC: We overlap two blocks of size 2^j. 
        // Block 1 starts at L. Block 2 ends exactly at R.
        // Because MIN(a, a) = a, the overlapping center doesn't corrupt the data.
        return Math.min(sparseTable[L][j], sparseTable[R - (1 << j) + 1][j]);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Sparse Table (O(1) RMQ) Architecture ---");
        
        // Database of stock prices or telemetry values
        int[] database = {7, 2, 3, 0, 5, 10, 3, 12, 18};
        System.out.println("Target Array: {7, 2, 3, 0, 5, 10, 3, 12, 18}");
        
        SparseTableRMQEngine st = new SparseTableRMQEngine(database);
        
        System.out.println("\n--- Executing Strict O(1) Queries ---");
        System.out.println("Minimum between index [0] and [2]: " + st.queryMin(0, 2)); // Expected: 2
        System.out.println("Minimum between index [4] and [7]: " + st.queryMin(4, 7)); // Expected: 3
        System.out.println("Minimum between index [0] and [8]: " + st.queryMin(0, 8)); // Expected: 0
        
        System.out.println("\nStatus: Segment Tree O(log N) bottleneck mathematically eliminated via idempotent block overlapping.");
    }
}