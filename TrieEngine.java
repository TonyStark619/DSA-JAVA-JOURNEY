public class TrieEngine {

    // 1. The Core Architecture of a Node
    class TrieNode {
        TrieNode[] children;
        boolean isEndOfWord;

        public TrieNode() {
            // 26 slots for the English lowercase alphabet
            children = new TrieNode[26]; 
            isEndOfWord = false;
        }
    }

    private TrieNode root;

    public TrieEngine() {
        root = new TrieNode();
    }

    // O(L) Time - Ingesting data into the architecture
    public void insert(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            // If the letter doesn't exist in the current path, build a new node
            if (current.children[index] == null) {
                current.children[index] = new TrieNode();
            }
            // Move down the branch
            current = current.children[index];
        }
        // Mark the final node as a valid stopping point for a complete word
        current.isEndOfWord = true;
    }

    // O(L) Time - Exact Word Search
    public boolean search(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (current.children[index] == null) {
                return false; // The path broke. Word doesn't exist.
            }
            current = current.children[index];
        }
        return current.isEndOfWord; // Returns true ONLY if it's a complete word
    }

    // O(L) Time - The Autocomplete Foundation
    public boolean startsWith(String prefix) {
        TrieNode current = root;
        for (char c : prefix.toCharArray()) {
            int index = c - 'a';
            if (current.children[index] == null) {
                return false;
            }
            current = current.children[index];
        }
        return true; // We successfully traversed the entire prefix
    }

    public static void main(String[] args) {
        System.out.println("--- Booting O(L) Prefix Tree (Trie) Architecture ---");
        TrieEngine database = new TrieEngine();

        System.out.println("Ingesting enterprise dictionary...");
        database.insert("apple");
        database.insert("application");
        database.insert("appetite");
        database.insert("api");
        database.insert("bat");

        System.out.println("\nExecuting Diagnostics:");
        System.out.println("Search 'apple': " + database.search("apple"));       // true
        System.out.println("Search 'app':   " + database.search("app"));         // false (it's a prefix, not a full word)
        
        System.out.println("\nExecuting Autocomplete Prefix Scan:");
        System.out.println("Starts with 'app': " + database.startsWith("app")); // true
        System.out.println("Starts with 'apt': " + database.startsWith("apt")); // false

        System.out.println("\nStatus: Hierarchical prefix architecture successfully deployed.");
    }
}