public class PrefixTree {
    
    // The core memory node. Instead of left/right, it branches out to 26 possible letters.
    private class Node {
        Node[] children;
        boolean isEndOfWord;

        public Node() {
            children = new Node[26]; // a-z
            isEndOfWord = false;
        }
    }

    private Node root;

    public PrefixTree() {
        root = new Node();
    }

    // O(L) Time - Inserts a word character by character
    public void insert(String word) {
        Node current = root;
        for (int i = 0; i < word.length(); i++) {
            int index = word.charAt(i) - 'a'; // Convert 'a' to 0, 'b' to 1, etc.
            
            if (current.children[index] == null) {
                current.children[index] = new Node();
            }
            current = current.children[index];
        }
        current.isEndOfWord = true; // Mark the termination of the string
    }

    // O(L) Time - Exact word lookup
    public boolean search(String word) {
        Node current = root;
        for (int i = 0; i < word.length(); i++) {
            int index = word.charAt(i) - 'a';
            if (current.children[index] == null) {
                return false; // Architecture breach: Path does not exist
            }
            current = current.children[index];
        }
        return current.isEndOfWord; // Did we actually finish a word, or just a prefix?
    }

    // O(L) Time - The Autocomplete Engine (Prefix matching)
    public boolean startsWith(String prefix) {
        Node current = root;
        for (int i = 0; i < prefix.length(); i++) {
            int index = prefix.charAt(i) - 'a';
            if (current.children[index] == null) {
                return false;
            }
            current = current.children[index];
        }
        return true; // We successfully navigated the prefix path
    }

    public static void main(String[] args) {
        PrefixTree autocompleteEngine = new PrefixTree();
        System.out.println("--- Booting Autocomplete Prefix Architecture ---");

        // Ingesting dictionary data
        autocompleteEngine.insert("apple");
        autocompleteEngine.insert("app");
        autocompleteEngine.insert("application");
        autocompleteEngine.insert("aptitude");

        System.out.println("\nExecuting High-Speed Lookups...");
        System.out.println("Search 'app': " + autocompleteEngine.search("app")); // True
        System.out.println("Search 'appl': " + autocompleteEngine.search("appl")); // False (It's a prefix, not a full word)
        
        System.out.println("\nExecuting Prefix Diagnostics...");
        System.out.println("Starts with 'appl': " + autocompleteEngine.startsWith("appl")); // True
        System.out.println("Starts with 'apt': " + autocompleteEngine.startsWith("apt"));   // True
        System.out.println("Starts with 'api': " + autocompleteEngine.startsWith("api"));   // False
        
        System.out.println("\nStatus: Prefix Tree mapping and retrieval fully operational.");
    }
}