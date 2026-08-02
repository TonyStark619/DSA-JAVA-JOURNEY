import java.util.*;

public class AhoCorasickEngine {
    static class Node {
        Node[] children = new Node[26];
        Node fail; // The KMP-style failure link
        List<Integer> output = new ArrayList<>(); // Stores lengths of matched words
    }

    private Node root = new Node();

    // 1. Build the Standard Trie
    public void insert(String word) {
        Node current = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (current.children[index] == null) {
                current.children[index] = new Node();
            }
            current = current.children[index];
        }
        current.output.add(word.length());
    }

    // 2. Build the Failure Links (The Automaton Brain)
    public void buildFailureLinks() {
        Queue<Node> queue = new LinkedList<>();
        
        // Initialize depth 1 nodes: their failure link must point back to root
        for (int i = 0; i < 26; i++) {
            if (root.children[i] != null) {
                root.children[i].fail = root;
                queue.add(root.children[i]);
            } else {
                // Optimization: Virtual links back to root for missing children
                root.children[i] = root; 
            }
        }

        System.out.println("Executing BFS to weave KMP failure links across the Trie...");
        
        while (!queue.isEmpty()) {
            Node current = queue.poll();

            for (int i = 0; i < 26; i++) {
                if (current.children[i] != null) {
                    Node child = current.children[i];
                    Node failureNode = current.fail;
                    
                    // Follow failure links back until we find a valid continuation
                    while (failureNode != null && failureNode.children[i] == null) {
                        failureNode = failureNode.fail;
                    }
                    
                    child.fail = (failureNode != null) ? failureNode.children[i] : root;
                    
                    // Merge outputs (if failure node completes a word, so does this child)
                    child.output.addAll(child.fail.output);
                    queue.add(child);
                }
            }
        }
    }

    // 3. O(N) Multi-Pattern Scan
    public void search(String text) {
        Node current = root;
        System.out.println("Scanning text payload for all signatures simultaneously...");
        
        for (int i = 0; i < text.length(); i++) {
            int index = text.charAt(i) - 'a';
            
            while (current != root && current.children[index] == null) {
                current = current.fail; // Backtrack safely using failure link
            }
            
            current = current.children[index];
            if (current == null) current = root;

            for (int length : current.output) {
                System.out.println("CRITICAL MATCH: Found signature of length " + length + 
                                   " ending at index " + i + 
                                   " (" + text.substring(i - length + 1, i + 1) + ")");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Aho-Corasick Multi-Pattern Automaton ---");
        AhoCorasickEngine scanner = new AhoCorasickEngine();
        
        scanner.insert("he");
        scanner.insert("she");
        scanner.insert("hers");
        scanner.insert("his");
        
        scanner.buildFailureLinks();
        
        String databaseText = "ahishers";
        scanner.search(databaseText);
        
        System.out.println("Status: O(N) simultaneous multi-pattern extraction complete.");
    }
}