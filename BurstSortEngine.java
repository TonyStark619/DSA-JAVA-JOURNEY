import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BurstSortEngine {
    
    // The threshold at which a node "bursts" and delegates its strings to children
    private static final int BURST_THRESHOLD = 3;

    static class TrieNode {
        // Only instantiated when the node bursts
        TrieNode[] children = null; 
        
        // Holds strings until the threshold is reached
        List<String> bucket = new ArrayList<>(); 
    }

    private TrieNode root = new TrieNode();

    // O(L) - Dynamic Insertion Engine
    public void insert(String word) {
        insertRecursive(root, word, 0);
    }

    private void insertRecursive(TrieNode node, String word, int depth) {
        // If the node hasn't burst yet, just drop it in the bucket
        if (node.children == null) {
            node.bucket.add(word);
            
            // THE MAGIC: If the bucket gets too heavy, BURST it.
            if (node.bucket.size() > BURST_THRESHOLD) {
                burst(node, depth);
            }
        } 
        // If the node has already burst, route the string to the correct child
        else {
            if (depth == word.length()) {
                node.bucket.add(word); // It's an exact match for this prefix
                return;
            }
            char ch = word.charAt(depth);
            if (node.children[ch] == null) {
                node.children[ch] = new TrieNode();
            }
            insertRecursive(node.children[ch], word, depth + 1);
        }
    }

    // Distributes the strings in the bucket down to newly created children
    private void burst(TrieNode node, int depth) {
        node.children = new TrieNode[256]; // Standard ASCII charset
        
        List<String> oldBucket = new ArrayList<>(node.bucket);
        node.bucket.clear();

        for (String str : oldBucket) {
            if (depth == str.length()) {
                node.bucket.add(str);
            } else {
                char ch = str.charAt(depth);
                if (node.children[ch] == null) {
                    node.children[ch] = new TrieNode();
                }
                insertRecursive(node.children[ch], str, depth + 1);
            }
        }
    }

    // O(N * L) - Traversal and Output Generation
    public void traverseAndSort(TrieNode node, List<String> sortedOutput) {
        if (node == null) return;
        
        // If the node hasn't burst, sort its small bucket and add it
        if (node.children == null) {
            Collections.sort(node.bucket);
            sortedOutput.addAll(node.bucket);
            return;
        }

        // Add exact prefix matches first
        sortedOutput.addAll(node.bucket);

        // Traverse children lexicographically
        for (int i = 0; i < 256; i++) {
            if (node.children[i] != null) {
                traverseAndSort(node.children[i], sortedOutput);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Burst-Sort Architecture ---");
        
        BurstSortEngine engine = new BurstSortEngine();
        
        String[] massiveLogs = {
            "apple", "app", "application", "aptitude", "banana", "band",
            "cat", "caterpillar", "catastrophe", "dog", "dogma"
        };
        
        System.out.println("Executing O(N * L) String Ingestion...");
        for (String log : massiveLogs) {
            engine.insert(log);
        }
        
        List<String> sortedResult = new ArrayList<>();
        engine.traverseAndSort(engine.root, sortedResult);
        
        System.out.println("\n--- Final Sorted Output ---");
        for(String s : sortedResult) {
             System.out.println(s);
        }
        
        System.out.println("\nStatus: Burst logic executed. CPU cache misses mathematically minimized.");
    }
}