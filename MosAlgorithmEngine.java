import java.util.*;

public class MosAlgorithmEngine {
    
    // 1. The Offline Query Structure
    static class Query implements Comparable<Query> {
        int L, R, originalIndex;
        int blockSize;

        public Query(int L, int R, int originalIndex, int blockSize) {
            this.L = L;
            this.R = R;
            this.originalIndex = originalIndex;
            this.blockSize = blockSize;
        }

        // THE MAGIC: Sort by Block Number first. If in the same block, sort by R.
        @Override
        public int compareTo(Query other) {
            int thisBlock = this.L / this.blockSize;
            int otherBlock = other.L / other.blockSize;
            
            if (thisBlock != otherBlock) {
                return Integer.compare(thisBlock, otherBlock);
            }
            // Optimization: Alternate the sorting of R to minimize the right pointer's movement
            return (thisBlock % 2 == 0) ? Integer.compare(this.R, other.R) : Integer.compare(other.R, this.R);
        }
    }

    // O((N+Q) * sqrt(N)) Time - The Core Sliding Window Engine
    public static void executeOfflineQueries(int[] array, int[][] queries) {
        System.out.println("Executing O((N+Q)*sqrt(N)) Mo's Algorithm Block Sort...");
        
        int n = array.length;
        int q = queries.length;
        int blockSize = (int) Math.sqrt(n);
        
        // Wrap raw queries into objects for sorting
        Query[] offlineQueries = new Query[q];
        for (int i = 0; i < q; i++) {
            offlineQueries[i] = new Query(queries[i][0], queries[i][1], i, blockSize);
        }
        
        Arrays.sort(offlineQueries);
        
        int[] answers = new int[q];
        
        // Initialize the two sliding pointers and our tracking variable
        int currL = 0;
        int currR = -1;
        int currentSum = 0; 
        
        // Process the perfectly sorted queries
        for (int i = 0; i < q; i++) {
            int L = offlineQueries[i].L;
            int R = offlineQueries[i].R;
            
            // 1. Expand the window on the right
            while (currR < R) {
                currR++;
                currentSum += array[currR];
            }
            // 2. Shrink the window on the left
            while (currL < L) {
                currentSum -= array[currL];
                currL++;
            }
            // 3. Expand the window on the left
            while (currL > L) {
                currL--;
                currentSum += array[currL];
            }
            // 4. Shrink the window on the right
            while (currR > R) {
                currentSum -= array[currR];
                currR--;
            }
            
            // Store the answer at its original chronological index
            answers[offlineQueries[i].originalIndex] = currentSum;
        }
        
        System.out.println("\n--- Query Resolution Complete ---");
        for (int i = 0; i < q; i++) {
            System.out.println("Query " + i + " [L=" + queries[i][0] + ", R=" + queries[i][1] + "] -> Sum: " + answers[i]);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Square Root Decomposition Engine ---");
        
        int[] database = {1, 1, 2, 1, 3, 4, 5, 2, 8};
        // Raw queries: {L, R}
        int[][] rawQueries = {
            {0, 4}, // Sum of indices 0 to 4
            {1, 3}, // Sum of indices 1 to 3
            {2, 7}  // Sum of indices 2 to 7
        };
        
        executeOfflineQueries(database, rawQueries);
        
        System.out.println("\nStatus: Pointer manipulation optimized. Redundant loops mathematically eliminated.");
    }
}