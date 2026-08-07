public class DistinctSubstringsTrie {
    
    // 1. Minimalist Node Architecture
    static class Node {
        Node[] links = new Node[26];
        
        boolean containsKey(char ch) {
            return links[ch - 'a'] != null;
        }
        
        void put(char ch, Node node) {
            links[ch - 'a'] = node;
        }
        
        Node get(char ch) {
            return links[ch - 'a'];
        }
    }

    // O(N^2) Time, O(Unique Substrings) Space - The Elite Counting Engine
    public static int countDistinctSubstrings(String word) {
        Node root = new Node();
        int n = word.length();
        
        // We start with 1 to account for the empty string "" which is technically a substring
        int distinctCount = 1; 
        
        System.out.println("Executing prefix expansion and node mapping...");

        // Iterate through every possible starting character
        for (int i = 0; i < n; i++) {
            Node current = root;
            
            // Expand the string character by character from the starting point
            for (int j = i; j < n; j++) {
                char ch = word.charAt(j);
                
                // THE MAGIC: If this path doesn't exist, we've found a new distinct substring
                if (!current.containsKey(ch)) {
                    current.put(ch, new Node());
                    distinctCount++; 
                }
                
                // Move down the Trie to continue the prefix path
                current = current.get(ch);
            }
        }
        return distinctCount;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Substring Extraction Trie ---");
        
        String databaseText = "abab";
        
        // Substrings of "abab": 
        // "a", "ab", "aba", "abab", "b", "ba", "bab", ""
        // Total should be exactly 8.
        
        int totalUnique = countDistinctSubstrings(databaseText);
        
        System.out.println("String Analyzed: '" + databaseText + "'");
        System.out.println("CRITICAL INSIGHT: Total distinct substrings identified: " + totalUnique);
        System.out.println("Status: Trie node mapping complete. HashSet memory overhead successfully bypassed.");
    }
}