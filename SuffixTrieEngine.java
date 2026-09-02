import java.util.*;

public class SuffixTrieEngine {

    // 1. The Trie Node Architecture
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        // Optional: Store starting indices to know exactly where the substring appears in the original text
        List<Integer> startingIndices = new ArrayList<>();
    }

    private TrieNode root = new TrieNode();

    // O(N^2) Time - Pre-processing the database (Run once)
    public SuffixTrieEngine(String text) {
        System.out.println("Constructing Suffix Trie for text length: " + text.length());
        for (int i = 0; i < text.length(); i++) {
            insertSuffix(text.substring(i), i);
        }
    }

    // Insert a single suffix into the Trie
    private void insertSuffix(String suffix, int originalIndex) {
        TrieNode current = root;
        for (int i = 0; i < suffix.length(); i++) {
            char c = suffix.charAt(i);
            
            // If the path doesn't exist, create it
            current.children.putIfAbsent(c, new TrieNode());
            current = current.children.get(c);
            
            // Log the original starting index of this suffix path
            current.startingIndices.add(originalIndex);
        }
    }

    // O(M) Time - The Core Search Engine (M = length of pattern)
    public List<Integer> searchSubstring(String pattern) {
        TrieNode current = root;
        
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            
            // If the character path breaks, the substring does not exist in the text
            if (!current.children.containsKey(c)) {
                return new ArrayList<>(); 
            }
            current = current.children.get(c);
        }
        
        // Return all indices where this path was walked during construction
        return current.startingIndices;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting O(M) Suffix Trie Substring Engine ---");
        
        String databaseText = "ababaaababaab";
        System.out.println("Database: '" + databaseText + "'");
        
        // Build the structure once
        SuffixTrieEngine engine = new SuffixTrieEngine(databaseText);
        
        // Execute lightning-fast O(M) queries
        String query1 = "ababa";
        String query2 = "baa";
        String query3 = "xyz";
        
        System.out.println("\nExecuting Substring Queries...");
        System.out.println("Query '" + query1 + "' found at indices: " + engine.searchSubstring(query1));
        System.out.println("Query '" + query2 + "' found at indices: " + engine.searchSubstring(query2));
        System.out.println("Query '" + query3 + "' found at indices: " + engine.searchSubstring(query3));
        
        System.out.println("\nStatus: Database pre-processed. O(M) substring resolution achieved.");
    }
}