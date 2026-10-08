public class VanEmdeBoasTreeEngine {
    private int universeSize; // U
    private Integer min;      // O(1) minimum tracking
    private Integer max;      // O(1) maximum tracking
    
    private VanEmdeBoasTreeEngine summary;
    private VanEmdeBoasTreeEngine[] clusters;

    public VanEmdeBoasTreeEngine(int universeSize) {
        this.universeSize = universeSize;
        this.min = null;
        this.max = null;

        // Base case: Universe of size 2 (Bits 0 and 1)
        if (universeSize > 2) {
            int upperSqrt = (int) Math.ceil(Math.sqrt(universeSize));
            int lowerSqrt = (int) Math.floor(Math.sqrt(universeSize));
            
            this.summary = new VanEmdeBoasTreeEngine(upperSqrt);
            this.clusters = new VanEmdeBoasTreeEngine[upperSqrt];
            
            for (int i = 0; i < upperSqrt; i++) {
                this.clusters[i] = new VanEmdeBoasTreeEngine(lowerSqrt);
            }
        }
    }

    // High = Which cluster does this number belong to?
    private int high(int x) {
        return (int) Math.floor(x / Math.sqrt(universeSize));
    }

    // Low = What is its position inside that specific cluster?
    private int low(int x) {
        return (int) (x % Math.ceil(Math.sqrt(universeSize)));
    }

    // Index = Reconstructs the absolute value from High and Low
    private int index(int x, int y) {
        return (int) (x * Math.floor(Math.sqrt(universeSize)) + y);
    }

    // O(log log U) - Ultra-fast mathematical insertion
    public void insert(int x) {
        if (min == null) {
            min = x;
            max = x;
            return;
        }

        // Swap to maintain the O(1) minimum at the top level
        if (x < min) {
            int temp = min;
            min = x;
            x = temp;
        }

        if (universeSize > 2) {
            int h = high(x);
            int l = low(x);
            
            // If the target cluster is completely empty, we insert the cluster ID into the summary
            if (clusters[h].min == null) {
                summary.insert(h);
                clusters[h].min = l;
                clusters[h].max = l;
            } else {
                // Otherwise, we dive directly into the specific cluster
                clusters[h].insert(l);
            }
        }

        if (x > max) {
            max = x;
        }
    }

    // O(log log U) - Finds the absolute next highest number in the database
    public Integer successor(int x) {
        if (universeSize == 2) {
            if (x == 0 && max != null && max == 1) return 1;
            return null;
        }
        
        if (min != null && x < min) return min; // O(1) bypass
        
        int h = high(x);
        int l = low(x);
        
        // Check if the successor is hiding inside the exact same cluster
        Integer maxLow = clusters[h].max;
        if (maxLow != null && l < maxLow) {
            Integer offset = clusters[h].successor(l);
            return index(h, offset);
        }
        
        // Otherwise, ask the summary for the NEXT populated cluster
        Integer successorCluster = summary.successor(h);
        if (successorCluster == null) return null;
        
        // Return the absolute minimum value of that next cluster
        Integer offset = clusters[successorCluster].min;
        return index(successorCluster, offset);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Van Emde Boas (vEB) Tree Architecture ---");
        
        // Initializing a universe of size 16 (Values 0 to 15)
        // A standard BST takes O(log N). vEB takes O(log log 16) = O(log 4) = O(2) operations!
        VanEmdeBoasTreeEngine veb = new VanEmdeBoasTreeEngine(16);
        
        System.out.println("  [System] Fracturing Universe into Sqrt(U) clusters...");
        int[] dataset = {2, 3, 4, 13, 14};
        for (int val : dataset) veb.insert(val);
        
        System.out.println("\n--- Executing O(log log U) Successor Queries ---");
        System.out.println("Successor of 4: " + veb.successor(4));   // Expected: 13
        System.out.println("Successor of 2: " + veb.successor(2));   // Expected: 3
        System.out.println("Successor of 13: " + veb.successor(13)); // Expected: 14
        
        System.out.println("\nStatus: Logarithmic barriers mathematically shattered via integer universe fracturing.");
    }
}