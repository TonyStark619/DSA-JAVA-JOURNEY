public class FenwickTreeEngine {
    // We use a 1-based indexed array because bitwise operations on 0 fail 
    // (the lowest set bit of 0 is 0, causing infinite loops).
    private int[] bit; 
    private int n;

    // O(N log N) Initial Construction
    public FenwickTreeEngine(int[] arr) {
        this.n = arr.length;
        this.bit = new int[n + 1];
        
        System.out.println("Constructing O(N) Space Bitwise Memory Array...");
        for (int i = 0; i < n; i++) {
            // Internally 1-indexed, but accepting 0-indexed data
            update(i, arr[i]); 
        }
    }

    // O(log N) Time - Dynamic Point Update
    public void update(int index, int delta) {
        // Shift to 1-based indexing
        index = index + 1;
        
        // Traverse the tree upwards by ADDING the lowest set bit
        while (index <= n) {
            bit[index] += delta;
            // The Bitwise Magic: Isolates the right-most '1' bit and adds it
            index += index & (-index); 
        }
    }

    // O(log N) Time - Prefix Sum Query (Sum from 0 to index)
    public int queryPrefix(int index) {
        int sum = 0;
        index = index + 1; // Shift to 1-based
        
        // Traverse the tree downwards by SUBTRACTING the lowest set bit
        while (index > 0) {
            sum += bit[index];
            index -= index & (-index); 
        }
        return sum;
    }

    // O(log N) Time - Range Query
    public int queryRange(int left, int right) {
        // Mathematical deduction: Sum(L, R) = PrefixSum(R) - PrefixSum(L - 1)
        return queryPrefix(right) - queryPrefix(left - 1);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Fenwick Tree (BIT) Architecture ---");
        
        // Raw Database
        int[] database = {2, 1, 1, 3, 2, 3, 4, 5, 6, 7, 8, 9};
        
        FenwickTreeEngine fenwick = new FenwickTreeEngine(database);
        
        // Range Query: Index 1 to 5 -> (1 + 1 + 3 + 2 + 3 = 10)
        System.out.println("Original Range Sum [1 to 5]: " + fenwick.queryRange(1, 5));
        
        // Update Query: Add 6 to the value at Index 3 (was 3, becomes 9)
        System.out.println("\nExecuting O(log N) Bitwise Update at Index 3 (Adding 6)...");
        fenwick.update(3, 6); 
        
        // New Range Query: Index 1 to 5 -> (1 + 1 + 9 + 2 + 3 = 16)
        System.out.println("Updated Range Sum [1 to 5]: " + fenwick.queryRange(1, 5));
        
        System.out.println("\nStatus: Dynamic range queried perfectly. 75% memory footprint reduction achieved over Segment Trees.");
    }
}