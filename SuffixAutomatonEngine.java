import java.util.*;

public class SuffixAutomatonEngine {

    static class State {
        int length;
        int link;
        Map<Character, Integer> next = new HashMap<>();

        public State(int length, int link) {
            this.length = length;
            this.link = link;
        }
    }

    private List<State> states = new ArrayList<>();
    private int lastState;

    public SuffixAutomatonEngine() {
        // Initialize the root state
        states.add(new State(0, -1));
        lastState = 0;
    }

    // O(1) Amortized - Dynamically extend the automaton with a new character
    public void extend(char c) {
        int currentState = states.size();
        states.add(new State(states.get(lastState).length + 1, 0));
        
        int p = lastState;
        // Follow the suffix links and add transitions to the new state
        while (p != -1 && !states.get(p).next.containsKey(c)) {
            states.get(p).next.put(c, currentState);
            p = states.get(p).link;
        }

        if (p == -1) {
            states.get(currentState).link = 0;
        } else {
            int q = states.get(p).next.get(c);
            // If the length is contiguous, simply link to it
            if (states.get(p).length + 1 == states.get(q).length) {
                states.get(currentState).link = q;
            } else {
                // Otherwise, clone the state to split the transition paths
                int cloneState = states.size();
                State qState = states.get(q);
                State clone = new State(states.get(p).length + 1, qState.link);
                clone.next.putAll(qState.next); // Copy transitions
                states.add(clone);

                // Re-wire links
                while (p != -1 && states.get(p).next.get(c) == q) {
                    states.get(p).next.put(c, cloneState);
                    p = states.get(p).link;
                }
                states.get(q).link = cloneState;
                states.get(currentState).link = cloneState;
            }
        }
        lastState = currentState;
    }

    // O(N) - Calculate the exact number of distinct substrings mathematically
    public long countDistinctSubstrings() {
        long totalDistinct = 0;
        // The number of substrings ending at a state is exactly its length minus the length of its link
        for (int i = 1; i < states.size(); i++) {
            totalDistinct += states.get(i).length - states.get(states.get(i).link).length;
        }
        return totalDistinct;
    }

    // O(M) - Verify if a pattern exists (M is pattern length, completely independent of text length)
    public boolean containsPattern(String pattern) {
        int current = 0;
        for (char c : pattern.toCharArray()) {
            if (!states.get(current).next.containsKey(c)) {
                return false;
            }
            current = states.get(current).next.get(c);
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Suffix Automaton (DAWG) Architecture ---");
        
        String genome = "ATATATA";
        SuffixAutomatonEngine dawg = new SuffixAutomatonEngine();
        
        System.out.println("\n[System] Ingesting telemetry character-by-character (Online Mode)...");
        for (char c : genome.toCharArray()) {
            dawg.extend(c);
        }
        
        System.out.println("CRITICAL RESULT: Total Distinct Substrings mathematically verified -> " + dawg.countDistinctSubstrings());
        
        System.out.println("\n--- O(M) Sub-Linear Pattern Queries ---");
        System.out.println("Pattern 'TATA' isolated: " + dawg.containsPattern("TATA"));
        System.out.println("Pattern 'GATA' isolated: " + dawg.containsPattern("GATA"));
        
        System.out.println("\nStatus: Directed Acyclic Word Graph active. String complexity mathematically compressed.");
    }
}