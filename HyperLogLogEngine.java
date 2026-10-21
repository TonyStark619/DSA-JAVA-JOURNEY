public class HyperLogLogEngine {

    // Number of bits to determine the register bucket (p). 14 bits = 16,384 registers.
    private final int p = 14; 
    private final int m = 1 << p; // Total number of registers
    private final byte[] registers;
    private final double alphaMM;

    public HyperLogLogEngine() {
        this.registers = new byte[m];
        
        // Mathematical constant for harmonic mean bias correction based on register size
        double alpha;
        if (m == 16) alpha = 0.673;
        else if (m == 32) alpha = 0.697;
        else if (m == 64) alpha = 0.709;
        else alpha = 0.7213 / (1.0 + 1.079 / m);
        
        this.alphaMM = alpha * m * m;
    }

    // A simulated 64-bit Avalanche Hash (e.g., MurmurHash3 in production)
    private long hash(String data) {
        long hash = 5381;
        for (char c : data.toCharArray()) {
            hash = ((hash << 5) + hash) + c; // hash * 33 + c
        }
        // Force avalanche effect to randomize bit spread
        hash ^= hash >>> 33;
        hash *= 0xff51afd7ed558ccdL;
        hash ^= hash >>> 33;
        hash *= 0xc4ceb9fe1a85ec53L;
        hash ^= hash >>> 33;
        return hash;
    }

    // Mathematically counts the number of leading zeros in the binary payload
    private byte countLeadingZeros(long val) {
        if (val == 0) return 1;
        return (byte) (Long.numberOfLeadingZeros(val) + 1);
    }

    // O(1) - Ingests a raw data string from the infinite stream
    public void add(String element) {
        long hashValue = hash(element);
        
        // Extract the first 'p' bits to determine the bucket index
        int bucketIndex = (int) (hashValue >>> (64 - p));
        
        // The remaining bits are used to count the leading zeros
        long remainingBits = hashValue << p;
        byte rank = countLeadingZeros(remainingBits);
        
        // Store the maximum leading zero sequence observed in this specific bucket
        if (rank > registers[bucketIndex]) {
            registers[bucketIndex] = rank;
        }
    }

    // O(M) - Computes the estimated cardinality using Harmonic Mean
    public long estimateCardinality() {
        double harmonicMeanSum = 0;
        
        for (int i = 0; i < m; i++) {
            harmonicMeanSum += Math.pow(2.0, -registers[i]);
        }
        
        double estimate = alphaMM / harmonicMeanSum;
        
        return Math.round(estimate);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting HyperLogLog Cardinality Architecture ---");
        
        HyperLogLogEngine hll = new HyperLogLogEngine();
        int trueUniqueCount = 500_000;
        
        System.out.println("  [Network] Ingesting " + trueUniqueCount + " highly redundant user streams...");
        
        for (int i = 0; i < trueUniqueCount; i++) {
            // We simulate hitting the server multiple times, but the unique count stays the same
            String userId = "User-ID-" + i;
            hll.add(userId);
            hll.add(userId); // Redundant hit
            hll.add(userId); // Redundant hit
        }
        
        long estimatedCount = hll.estimateCardinality();
        double errorMargin = Math.abs(trueUniqueCount - estimatedCount) / (double) trueUniqueCount * 100;
        
        System.out.println("\n--- O(1) Probabilistic Diagnostics ---");
        System.out.println("Actual Unique Users:    " + trueUniqueCount);
        System.out.println("HyperLogLog Estimation: " + estimatedCount);
        System.out.println("Margin of Error:        " + String.format("%.3f", errorMargin) + "%");
        System.out.println("RAM Consumed:           ~" + (hll.m / 1024.0) + " KB");
        
        System.out.println("\nStatus: Gigabyte-level Set tracking mathematically compressed into KBs via bit-pattern probability.");
    }
}