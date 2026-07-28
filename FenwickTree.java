public class FenwickTree {
    private int[] bit; 
    private int n;

    // O(N) Space - Strictly 1N memory footprint
    public FenwickTree(int size) {
        this.n = size;
        // Fenwick trees operate on a 1-based index internally for the bitwise math to function
        this.bit = new int[n + 1];
    }

    // O(log N) Time - Point Update
    public void update(int index, int delta) {
        // Shift to 1-based indexing
        index = index + 1; 
        
        while (index <= n) {
            bit[index] += delta;
            // THE MAGIC: Add the Lowest Significant Bit (LSB) to traverse UP the tree
            index += (index & -index);
        }
    }

    // O(log N) Time - Prefix Sum Query (Sum from index 0 to target index)
    public int queryPrefix(int index) {
        // Shift to 1-based indexing
        index = index + 1; 
        int sum = 0;
        
        while (index > 0) {
            sum += bit[index];
            // THE MAGIC: Subtract the LSB to traverse DOWN to the parent node
            index -= (index & -index);
        }
        return sum;
    }

    // O(log N) Time - Arbitrary Range Query (Sum between left and right)
    public int queryRange(int left, int right) {
        if (left == 0) return queryPrefix(right);
        // Calculate the full prefix, then subtract the unwanted prefix portion
        return queryPrefix(right) - queryPrefix(left - 1);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting O(log N) Fenwick Architecture ---");
        
        // Simulating a dataset of 6 items (e.g., daily revenue)
        int size = 6;
        FenwickTree memoryEngine = new FenwickTree(size);
        
        System.out.println("Ingesting Data Stream...");
        memoryEngine.update(0, 10);
        memoryEngine.update(1, 20);
        memoryEngine.update(2, 30);
        memoryEngine.update(3, 40);
        memoryEngine.update(4, 50);
        memoryEngine.update(5, 60);
        
        // Querying the sum from Index 1 to Index 3 (20 + 30 + 40 = 90)
        int qLeft = 1, qRight = 3;
        int result = memoryEngine.queryRange(qLeft, qRight);
        
        System.out.println("\nExecuting Range Query [" + qLeft + " to " + qRight + "]...");
        System.out.println("Total Sum: " + result);
        System.out.println("Status: 1N memory footprint maintained. Bitwise traversal successful.");
    }
}