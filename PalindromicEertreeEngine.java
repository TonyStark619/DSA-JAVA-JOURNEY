public class PalindromicEertreeEngine {

    static class Node {
        int length; // Length of the palindrome represented by this node
        int suffixLink; // Pointer to the longest palindromic proper suffix
        int[] next = new int[26]; // Edges representing characters added to both sides
        int count; // How many times this exact palindrome has appeared

        public Node(int length, int suffixLink) {
            this.length = length;
            this.suffixLink = suffixLink;
            this.count = 0;
            for (int i = 0; i < 26; i++) next[i] = 0;
        }
    }

    private final int MAX_NODES;
    private Node[] tree;
    private int currentNodeCount;
    private int longestSuffixNode; // Tracks the maximal palindrome suffix of the current string
    private String stream;

    public PalindromicEertreeEngine(int maxLen) {
        this.MAX_NODES = maxLen + 2;
        this.tree = new Node[MAX_NODES];
        this.stream = "";

        // Root 1: Represents palindromes of ODD length (Geometrically initialized to length -1)
        tree[1] = new Node(-1, 1);
        
        // Root 2: Represents palindromes of EVEN length (Length 0)
        tree[2] = new Node(0, 1);
        
        this.currentNodeCount = 2; // We used indices 1 and 2 for the roots
        this.longestSuffixNode = 2;
    }

    // O(1) Amortized - Finds the correct suffix link by checking if the character mathematically mirrors
    private int findMaxPalindromeSuffix(int startNode, int currentIndex, char newChar) {
        int curr = startNode;
        int streamLength = stream.length();
        
        // Traverse suffix links until we find a mathematical mirror:
        // Text[currentIndex - Node.length - 1] == Text[currentIndex]
        while (true) {
            int mirrorIndex = currentIndex - 1 - tree[curr].length;
            if (mirrorIndex >= 0 && stream.charAt(mirrorIndex) == newChar) {
                break;
            }
            curr = tree[curr].suffixLink;
        }
        return curr;
    }

    // O(1) Amortized - Ingests a new character and dynamically shifts the Eertree geometry
    public void addCharacter(char c) {
        stream += c;
        int currentIndex = stream.length() - 1;
        int charIndex = c - 'a';

        // Find the node where we can geometrically append 'c' to both sides
        int curr = findMaxPalindromeSuffix(longestSuffixNode, currentIndex, c);

        // If this exact palindrome doesn't exist yet, we forge it
        if (tree[curr].next[charIndex] == 0) {
            currentNodeCount++;
            tree[currentNodeCount] = new Node(tree[curr].length + 2, 0);

            // Calculate the suffix link for the newly forged node
            if (tree[currentNodeCount].length == 1) {
                // Single characters always link to the Even Root (Node 2)
                tree[currentNodeCount].suffixLink = 2;
            } else {
                // Find the next maximal suffix for the new node
                int suffixNode = findMaxPalindromeSuffix(tree[curr].suffixLink, currentIndex, c);
                tree[currentNodeCount].suffixLink = tree[suffixNode].next[charIndex];
            }

            tree[curr].next[charIndex] = currentNodeCount;
        }

        // Advance the pointer to the newly added (or existing) maximal suffix node
        longestSuffixNode = tree[curr].next[charIndex];
        tree[longestSuffixNode].count++;
    }

    // O(N) - Geometrically propagates the occurrence counts down the suffix links
    public void finalizeCounts() {
        System.out.println("  [System] Propagating counts through suffix links...");
        // Traverse backwards so parent palindromes inherit counts from their longer extensions
        for (int i = currentNodeCount; i > 2; i--) {
            tree[tree[i].suffixLink].count += tree[i].count;
        }
    }

    public void runDiagnostics() {
        // The roots (1 and 2) are geometric anchors. Actual palindromes start at index 3.
        int distinctPalindromes = currentNodeCount - 2;
        System.out.println("  -> Total Distinct Palindromes Isolated: " + distinctPalindromes);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Eertree (Palindromic Tree) Architecture ---");
        
        String payload = "abacaba";
        System.out.println("Target Stream: '" + payload + "'");
        
        PalindromicEertreeEngine eertree = new PalindromicEertreeEngine(payload.length());
        
        for (char c : payload.toCharArray()) {
            eertree.addCharacter(c);
        }
        
        eertree.finalizeCounts();
        
        System.out.println("\n--- Executing O(N) Palindromic Diagnostics ---");
        eertree.runDiagnostics(); 
        // Expected for "abacaba": a, b, c, aba, bacab, abacaba, aca (7 distinct)
        
        System.out.println("\nStatus: N^2 Palindrome bottleneck mathematically eliminated via dual-root suffix traversal.");
    }
}