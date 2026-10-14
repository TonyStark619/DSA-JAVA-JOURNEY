import java.util.BitSet;

public class EnterpriseBloomFilter {

    // A memory-efficient array of raw bits (0s and 1s)
    private BitSet bitset;
    private int bitSize;
    private int numberOfHashes;

    // The optimal size and hash count are calculated using the target capacity and acceptable false-positive rate
    public EnterpriseBloomFilter(int capacity, double falsePositiveRate) {
        System.out.println("  [System] Forging Probabilistic Geometry...");
        
        // Mathematical formula to determine optimal bit array size
        this.bitSize = (int) Math.ceil((capacity * Math.log(falsePositiveRate)) / Math.log(1 / Math.pow(2, Math.log(2))));
        
        // Mathematical formula to determine optimal number of hash functions
        this.numberOfHashes = (int) Math.round((bitSize / (double) capacity) * Math.log(2));
        
        this.bitset = new BitSet(bitSize);
        
        System.out.println("  -> Optimal Bit Array Size: " + bitSize + " bits");
        System.out.println("  -> Optimal Hash Functions Required: " + numberOfHashes);
    }

    // A simulated hyper-fast non-cryptographic hash (like MurmurHash in production)
    // We use a seed to simulate multiple different hash functions
    private int getHash(String item, int seed) {
        int hash = 0;
        for (char c : item.toCharArray()) {
            hash = (hash * seed) + c;
        }
        // Ensure the hash maps cleanly into our bit array bounds
        return Math.abs(hash % bitSize);
    }

    // O(K) - Hashes the item K times and flips the corresponding bits to 1
    public void add(String item) {
        for (int i = 0; i < numberOfHashes; i++) {
            // We use prime numbers (31, 37, 41...) as seeds to simulate different hash algorithms
            int primeSeed = 31 + (i * 6); 
            int hashIndex = getHash(item, primeSeed);
            bitset.set(hashIndex, true);
        }
    }

    // O(K) - The Shield Engine. 
    // Returns FALSE if it is DEFINITELY NOT in the database.
    // Returns TRUE if it MIGHT be in the database.
    public boolean mightContain(String item) {
        for (int i = 0; i < numberOfHashes; i++) {
            int primeSeed = 31 + (i * 6);
            int hashIndex = getHash(item, primeSeed);
            
            // If even a single bit is 0, this item has mathematically never been added.
            if (!bitset.get(hashIndex)) {
                return false; 
            }
        }
        return true; // All bits are 1. It is PROBABLY in the database.
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Enterprise Bloom Filter Architecture ---");
        
        // We expect to store 10,000 items with a 1% acceptable false-positive rate
        EnterpriseBloomFilter databaseShield = new EnterpriseBloomFilter(10000, 0.01);
        
        System.out.println("\n[Database] Registering System Users...");
        databaseShield.add("TIT_Student_99");
        databaseShield.add("AlphaCoder_01");
        databaseShield.add("Admin_Root");
        
        System.out.println("\n--- Executing O(K) Memory-Resident Queries ---");
        
        // 1. A True Negative (Will return false, saving a database disk read)
        boolean check1 = databaseShield.mightContain("Unknown_User_404");
        System.out.println("Is 'Unknown_User_404' registered? -> " + (check1 ? "PROBABLY" : "ABSOLUTELY NOT"));
        
        // 2. A True Positive (Will return true, requires a database read to confirm)
        boolean check2 = databaseShield.mightContain("Admin_Root");
        System.out.println("Is 'Admin_Root' registered?       -> " + (check2 ? "PROBABLY" : "ABSOLUTELY NOT"));
        
        System.out.println("\nStatus: Database disk I/O mathematically shielded via probabilistic bit-arrays.");
    }
}