public class MaximumXORTrieEngine {

    // A Trie Node holding exactly two mathematical paths: bit 0 (left) and bit 1 (right)
    static class TrieNode {
        TrieNode[] children = new TrieNode[2];
    }

    private final TrieNode root;

    public MaximumXORTrieEngine() {
        this.root = new TrieNode();
    }

    // O(32) - Injects a 32-bit integer into the geometric bit-tree
    public void insert(int number) {
        TrieNode current = root;
        // Traverse from the most significant bit (31) down to the least significant (0)
        for (int i = 31; i >= 0; i--) {
            int bit = (number >> i) & 1;
            
            if (current.children[bit] == null) {
                current.children[bit] = new TrieNode();
            }
            current = current.children[bit];
        }
    }

    // O(32) - Greedily hunts for the opposite bits to mathematically maximize the XOR output
    public int queryMaxXOR(int number) {
        TrieNode current = root;
        int maxXor = 0;
        
        for (int i = 31; i >= 0; i--) {
            int bit = (number >> i) & 1;
            int oppositeBit = 1 - bit; // The mathematical ideal we WANT to find
            
            // If the opposite bit path exists, we take it. XOR is maximized.
            if (current.children[oppositeBit] != null) {
                maxXor = maxXor | (1 << i); // Lock this bit into our maximum result
                current = current.children[oppositeBit];
            } 
            // If the ideal path doesn't exist, we are forced to take the matching bit path
            else if (current.children[bit] != null) {
                current = current.children[bit];
            } 
            else {
                break; // Tree is empty
            }
        }
        return maxXor;
    }

    // O(N) - The core engine computing the max XOR subarray
    public int executeSubarraySweep(int[] nums) {
        System.out.println("  [System] Executing Bitwise Trie Subarray Sweep...");
        // Insert 0 to handle the case where a prefix XOR itself is the maximum
        insert(0); 
        
        int globalMax = 0;
        int currentPrefixXor = 0;
        
        for (int num : nums) {
            currentPrefixXor ^= num;
            insert(currentPrefixXor);
            
            // Query the Trie for the best mathematical partner for this specific prefix
            int localMax = queryMaxXOR(currentPrefixXor);
            globalMax = Math.max(globalMax, localMax);
        }
        
        return globalMax;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Bitwise XOR Trie Architecture ---");
        
        MaximumXORTrieEngine engine = new MaximumXORTrieEngine();
        int[] dataset = {8, 1, 2, 12, 7, 6};
        
        System.out.println("Target Array: {8, 1, 2, 12, 7, 6}");
        
        int absoluteMaxXor = engine.executeSubarraySweep(dataset);
        
        // The subarray {1, 2, 12} yields 1 ^ 2 ^ 12 = 15
        System.out.println("\nCRITICAL RESULT: Maximum XOR Subarray Value -> " + absoluteMaxXor);
        System.out.println("Status: O(N^2) double-loop mathematical bottleneck shattered via 32-bit geometric traversal.");
    }
}