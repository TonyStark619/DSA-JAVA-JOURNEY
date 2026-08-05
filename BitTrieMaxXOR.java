public class BitTrieMaxXOR {
    
    // 1. Architecture: A Trie node that only holds 0 or 1
    static class Node {
        Node[] links = new Node[2];
    }
    
    private Node root;

    public BitTrieMaxXOR() {
        root = new Node();
    }

    // O(32) Time - Insert the 32-bit binary representation into the Trie
    public void insert(int num) {
        Node current = root;
        // Start from the Most Significant Bit (MSB) on the far left
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            if (current.links[bit] == null) {
                current.links[bit] = new Node();
            }
            current = current.links[bit];
        }
    }

    // O(32) Time - The Greedy Optimization Engine
    public int getMaxXor(int num) {
        Node current = root;
        int maxXor = 0;
        
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            // To maximize XOR, we want the EXACT OPPOSITE bit
            int oppositeBit = 1 - bit;
            
            // If the opposite bit exists in our database, take that path
            if (current.links[oppositeBit] != null) {
                // Flip that specific bit to 1 in our running total
                maxXor = maxXor | (1 << i);
                current = current.links[oppositeBit];
            } 
            // If it doesn't exist, we are forced to take the identical bit
            else {
                current = current.links[bit];
            }
        }
        return maxXor;
    }

    // O(N) Time - The Core Execution
    public int findMaximumXOR(int[] nums) {
        System.out.println("Executing O(N) Bitwise Trie Traversal...");
        int max_ans = 0;
        
        // 1. Build the database
        for (int num : nums) {
            insert(num);
        }
        
        // 2. Query every number against the database greedily
        for (int num : nums) {
            max_ans = Math.max(max_ans, getMaxXor(num));
        }
        return max_ans;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Maximum XOR Bitwise Engine ---");
        BitTrieMaxXOR engine = new BitTrieMaxXOR();
        
        int[] database = {3, 10, 5, 25, 2, 8};
        
        int result = engine.findMaximumXOR(database);
        
        // The optimal pair is 5 (00101) and 25 (11001) -> XOR is 28 (11100)
        System.out.println("CRITICAL INSIGHT: Maximum XOR produced by any two numbers is " + result);
        System.out.println("Status: Array parsed and optimal pair resolved in linear time.");
    }
}