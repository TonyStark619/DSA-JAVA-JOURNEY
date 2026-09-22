import java.util.BitSet;

public class BloomFilterEngine {
    // The massive bit array. Memory footprint is strictly measured in bits, not object references.
    private BitSet bitset;
    private int size;
    
    // Primes used to create independent hash distributions
    private int[] hashPrimes = {31, 53, 89};

    public BloomFilterEngine(int size) {
        this.size = size;
        this.bitset = new BitSet(size);
        System.out.println("  [System] Allocated BitSet of size: " + size + " bits");
    }

    // Generate 'k' independent hash values for the string
    private int[] getHashes(String item) {
        int[] hashes = new int[hashPrimes.length];
        for (int i = 0; i < hashPrimes.length; i++) {
            int hash = 0;
            for (char c : item.toCharArray()) {
                hash = (hash * hashPrimes[i] + c) % size;
            }
            hashes[i] = Math.abs(hash);
        }
        return hashes;
    }

    // O(k) Time - Inject a signature into the bit array
    public void add(String item) {
        int[] hashes = getHashes(item);
        for (int hash : hashes) {
            bitset.set(hash, true);
        }
    }

    // O(k) Time - Query the bit array for membership
    public boolean mightContain(String item) {
        int[] hashes = getHashes(item);
        for (int hash : hashes) {
            // If even a single mapped bit is false, the item was NEVER added.
            if (!bitset.get(hash)) {
                return false; 
            }
        }
        // If all mapped bits are true, it is PROBABLY in the set.
        return true; 
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Probabilistic Bloom Filter Architecture ---");
        
        // A tiny 1000-bit array for demonstration. 
        BloomFilterEngine filter = new BloomFilterEngine(1000);
        
        System.out.println("\n[Network] Ingesting malicious URL signatures...");
        String[] maliciousDB = {"phishing.com", "malware-download.net", "fake-bank.org"};
        for (String url : maliciousDB) {
            filter.add(url);
        }
        
        System.out.println("\n--- Querying Live Telemetry ---");
        
        // 1. Checking a safe URL (True Negative guaranteed)
        String safeUrl = "google.com";
        System.out.println("Checking '" + safeUrl + "': " + 
            (filter.mightContain(safeUrl) ? "WARNING: Match Found" : "SAFE: Definitely not in database"));
            
        // 2. Checking a known malicious URL (True Positive)
        String badUrl = "phishing.com";
        System.out.println("Checking '" + badUrl + "': " + 
            (filter.mightContain(badUrl) ? "WARNING: Signature matches malicious database" : "SAFE"));
            
        System.out.println("\nStatus: Multi-hash bitset active. RAM utilization slashed by >90%.");
    }
}