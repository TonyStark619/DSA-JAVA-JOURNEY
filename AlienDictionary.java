import java.util.*;

public class AlienDictionary {

    // O(C) Time where C is total characters in dictionary - Elite Graph Resolution
    public static String findAlienOrder(String[] dict, int k) {
        System.out.println("Executing Lexicographical Graph Extraction...");
        
        // Step 1: Initialize the Graph and In-Degree array
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < k; i++) graph.add(new ArrayList<>());
        int[] inDegree = new int[k];

        // Step 2: Compare adjacent words to build the dependency graph
        for (int i = 0; i < dict.length - 1; i++) {
            String word1 = dict[i];
            String word2 = dict[i + 1];
            
            // Edge Case: If "abcd" comes before "abc", the dictionary is invalid.
            if (word1.length() > word2.length() && word1.startsWith(word2)) {
                System.out.println("CRITICAL FAILURE: Invalid dictionary sequence detected.");
                return "";
            }

            // Find the first character that differs between the two words
            int minLen = Math.min(word1.length(), word2.length());
            for (int j = 0; j < minLen; j++) {
                if (word1.charAt(j) != word2.charAt(j)) {
                    int u = word1.charAt(j) - 'a'; // The letter that comes first
                    int v = word2.charAt(j) - 'a'; // The letter that comes second
                    
                    graph.get(u).add(v); // Create directed edge: u -> v
                    inDegree[v]++;       // Increase prerequisite count for v
                    break;               // Only the first differing character matters
                }
            }
        }

        // Step 3: Execute Kahn's Algorithm (Topological Sort via BFS)
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < k; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        StringBuilder alienAlphabet = new StringBuilder();

        while (!queue.isEmpty()) {
            int current = queue.poll();
            alienAlphabet.append((char) (current + 'a'));

            for (int neighbor : graph.get(current)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // Step 4: Cycle Check
        if (alienAlphabet.length() != k) {
            System.out.println("CRITICAL FAILURE: Circular dependency in alphabet rules.");
            return "";
        }

        return alienAlphabet.toString();
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Alien Dictionary Decoder ---");
        
        // Simulating an alien dictionary with K = 4 standard characters (a, b, c, d)
        String[] dictionary = {"baa", "abcd", "abca", "cab", "cad"};
        int uniqueCharacters = 4;
        
        String result = findAlienOrder(dictionary, uniqueCharacters);
        
        System.out.println("\n--- Extracted Alphabet Order ---");
        System.out.println("CRITICAL INSIGHT: " + result);
        System.out.println("Status: Lexicographical constraints successfully resolved via Topological DAG.");
    }
}