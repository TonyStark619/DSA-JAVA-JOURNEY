public class SparseTableRMQ {
    private int[][] st;
    private int[] logTable;
    private int n;

    // O(N log N) - Precomputing the Sparse Table
    public SparseTableRMQ(int[] arr) {
        System.out.println("Executing O(N log N) Sparse Table DP Pre-computation...");
        this.n = arr.length;
        
        // logTable[i] stores the floor of log2(i) for O(1) lookups
        logTable = new int[n + 1];
        for (int i = 2; i <= n; i++) {
            logTable[i] = logTable[i / 2] + 1;
        }

        int maxLog = logTable[n] + 1;
        st = new int[n][maxLog];

        // Base case: The minimum of a range of length 2^0 (length 1) is the element itself
        for (int i = 0; i < n; i++) {
            st[i][0] = arr[i];
        }

        // DP transition: st[i][j] covers range [i, i + 2^j - 1]
        // We split it into two overlapping intervals of length 2^(j-1)
        for (int j = 1; j < maxLog; j++) {
            for (int i = 0; i + (1 << j) <= n; i++) {
                st[i][j] = Math.min(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
            }
        }
    }

    // O(1) - The Core Range Minimum Query
    public int queryMinimum(int L, int R) {
        // Find the largest power of 2 that fits inside the length of the query range
        int length = R - L + 1;
        int j = logTable[length];

        // Overlap two blocks of length 2^j to cover the entire [L, R] range
        // Block 1: Starts at L. Block 2: Ends at R.
        return Math.min(st[L][j], st[R - (1 << j) + 1][j]);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Sparse Table RMQ Architecture ---");
        
        int[] serverLoads = {7, 2, 3, 0, 5, 10, 3, 12, 18};
        SparseTableRMQ engine = new SparseTableRMQ(serverLoads);
        
        System.out.println("\n--- Querying Live Telemetry (O(1) Resolution) ---");
        
        // Querying Range [0, 4] -> {7, 2, 3, 0, 5}. Expected: 0
        System.out.println("Minimum load between index 0 and 4: " + engine.queryMinimum(0, 4));
        
        // Querying Range [4, 7] -> {5, 10, 3, 12}. Expected: 3
        System.out.println("Minimum load between index 4 and 7: " + engine.queryMinimum(4, 7));
        
        // Querying Range [7, 8] -> {12, 18}. Expected: 12
        System.out.println("Minimum load between index 7 and 8: " + engine.queryMinimum(7, 8));
        
        System.out.println("\nStatus: O(1) query vectors locked. Segment Tree overhead bypassed for idempotent telemetry.");
    }
}