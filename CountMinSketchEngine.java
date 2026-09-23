import java.util.Arrays;

public class CountMinSketchEngine {
    // 2D Array: Rows = Number of Hash Functions, Cols = Size of Hash Space
    private int[][] sketchTable;
    private int numHashes;
    private int hashSpace;
    
    // Primes used to simulate independent hash distributions
    private int[] hashPrimes = {31, 53, 89, 107};

    public CountMinSketchEngine(int hashes, int width) {
        this.numHashes = hashes;
        this.hashSpace = width;
        this.sketchTable = new int[hashes][width];
        System.out.println("  [System] Allocated Count-Min Sketch: " + hashes + "x" + width + " Matrix");
    }

    // Generate independent hash indices for a given string
    private int[] getHashes(String item) {
        int[] hashes = new int[numHashes];
        for (int i = 0; i < numHashes; i++) {
            int hash = 0;
            for (char c : item.toCharArray()) {
                hash = (hash * hashPrimes[i] + c) % hashSpace;
            }
            hashes[i] = Math.abs(hash);
        }
        return hashes;
    }

    // O(1) Time - Increment frequencies across the probabilistic matrix
    public void recordEvent(String item) {
        int[] hashes = getHashes(item);
        for (int i = 0; i < numHashes; i++) {
            sketchTable[i][hashes[i]]++;
        }
    }

    // O(1) Time - Extract the estimated frequency by taking the mathematical minimum
    public int estimateFrequency(String item) {
        int[] hashes = getHashes(item);
        int minFrequency = Integer.MAX_VALUE;
        
        for (int i = 0; i < numHashes; i++) {
            minFrequency = Math.min(minFrequency, sketchTable[i][hashes[i]]);
        }
        
        return minFrequency;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Count-Min Sketch Telemetry ---");
        
        // A highly constrained memory matrix (4 hashes, 100 width)
        CountMinSketchEngine tracker = new CountMinSketchEngine(4, 100);
        
        System.out.println("\n[Network] Ingesting massive data stream...");
        
        // Simulating a live traffic stream
        tracker.recordEvent("Exception_NullPointer");
        tracker.recordEvent("Exception_NullPointer");
        tracker.recordEvent("Exception_NullPointer");
        
        tracker.recordEvent("Login_Success");
        tracker.recordEvent("Login_Success");
        
        tracker.recordEvent("Exception_Timeout");
        
        System.out.println("\n--- Querying O(1) Stream Frequencies ---");
        System.out.println("NullPointer Frequency: " + tracker.estimateFrequency("Exception_NullPointer") + " (Expected: 3)");
        System.out.println("Login_Success Frequency: " + tracker.estimateFrequency("Login_Success") + " (Expected: 2)");
        System.out.println("Timeout Frequency: " + tracker.estimateFrequency("Exception_Timeout") + " (Expected: 1)");
        System.out.println("Unseen Event Frequency: " + tracker.estimateFrequency("Database_Crash") + " (Expected: 0)");
            
        System.out.println("\nStatus: Real-time frequency tracked. HashMap memory overhead mathematically bypassed.");
    }
}