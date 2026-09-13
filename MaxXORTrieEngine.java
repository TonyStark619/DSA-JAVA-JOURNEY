public class MaxXORTrieEngine {

    // 1. The Binary Node Architecture
    static class TrieNode {
        TrieNode[] children = new TrieNode[2]; // Index 0 for bit '0', Index 1 for bit '1'
    }

    private TrieNode root;

    public MaxXORTrieEngine() {
        root = new TrieNode();
    }

    // O(32) - Insert a 32-bit integer into the Trie
    public void insert(int num) {
        TrieNode current = root;
        // Start from the most significant bit (MSB) down to the least significant bit (LSB)
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            if (current.children[bit] == null) {
                current.children[bit] = new TrieNode();
            }
            current = current.children[bit];
        }
    }

    // O(32) - Find the maximum XOR for a given number against all numbers in the Trie
    public int findMaxXORForNum(int num) {
        TrieNode current = root;
        int maxXOR = 0;
        
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            int oppositeBit = 1 - bit; // The bit we WANT to find to maximize XOR
            
            // If the opposite bit exists in the Trie, take that path
            if (current.children[oppositeBit] != null) {
                maxXOR |= (1 << i); // Set the i-th bit to 1 in our running total
                current = current.children[oppositeBit];
            } 
            // Otherwise, we are forced to take the same bit path (XOR becomes 0 for this bit)
            else {
                current = current.children[bit];
            }
        }
        return maxXOR;
    }

    // O(N) - The Core Execution Engine
    public int findMaximumXOR(int[] nums) {
        System.out.println("Executing O(N) Bitwise Trie XOR Resolution...");
        int max = 0;
        
        // Step 1: Pre-process the array into the Trie
        for (int num : nums) {
            insert(num);
        }
        
        // Step 2: Query the Trie to find the maximum possible XOR
        for (int num : nums) {
            max = Math.max(max, findMaxXORForNum(num));
        }
        
        return max;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Bitwise Trie Architecture ---");
        
        int[] database = {3, 10, 5, 25, 2, 8};
        
        MaxXORTrieEngine engine = new MaxXORTrieEngine();
        int result = engine.findMaximumXOR(database);
        
        System.out.println("\nDatabase array: [3, 10, 5, 25, 2, 8]");
        System.out.println("CRITICAL RESULT: Maximum XOR Pair Output = " + result); 
        // 5 (00101) XOR 25 (11001) = 28 (11100)
        
        System.out.println("Status: O(N^2) bottlenecks bypassed. Linear time resolution achieved.");
    }
}