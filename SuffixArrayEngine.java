import java.util.Arrays;

public class SuffixArrayEngine {
    
    // 1. The Core Architecture for a Suffix Element
    static class Suffix implements Comparable<Suffix> {
        int originalIndex;
        String text;

        public Suffix(int originalIndex, String text) {
            this.originalIndex = originalIndex;
            this.text = text;
        }

        // We sort them alphabetically to enable binary search later
        public int compareTo(Suffix other) {
            return this.text.compareTo(other.text);
        }
    }

    // 2. Build the Index (O(N^2 log N) for this simple string approach, O(N log N) in advanced implementations)
    public static int[] buildSuffixArray(String text) {
        int n = text.length();
        Suffix[] suffixes = new Suffix[n];

        System.out.println("Generating and indexing all suffixes...");
        // Generate all suffixes (e.g., for "banana": "banana", "anana", "nana", "ana", "na", "a")
        for (int i = 0; i < n; i++) {
            suffixes[i] = new Suffix(i, text.substring(i));
        }

        Arrays.sort(suffixes);

        // Extract the original starting indices of the sorted suffixes
        int[] suffixArray = new int[n];
        for (int i = 0; i < n; i++) {
            suffixArray[i] = suffixes[i].originalIndex;
        }
        return suffixArray;
    }

    // 3. O(M log N) Time - Binary Search across the Suffix Index
    public static void searchPattern(String text, String pattern, int[] suffixArray) {
        int n = text.length();
        int m = pattern.length();
        int left = 0, right = n - 1;

        System.out.println("Executing O(M log N) Binary Search on Suffix Index...");

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int suffixIndex = suffixArray[mid];

            // Extract the substring to compare
            String currentSuffix = text.substring(suffixIndex);
            
            // Compare only up to the length of our target pattern
            int cmp;
            if (currentSuffix.length() >= m) {
                cmp = currentSuffix.substring(0, m).compareTo(pattern);
            } else {
                cmp = currentSuffix.compareTo(pattern);
            }

            if (cmp == 0) {
                System.out.println("CRITICAL MATCH: Pattern found at memory index " + suffixIndex);
                return; // In production, we would expand left/right to find ALL occurrences
            }
            // Standard binary search adjustment
            if (cmp < 0) left = mid + 1;
            else right = mid - 1;
        }
        System.out.println("Pattern does not exist within the indexed database.");
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Suffix Array Architecture ---");
        
        String databaseText = "GATTACA"; // Simulated DNA Sequence
        String targetPattern = "TAC";
        
        int[] suffixArray = buildSuffixArray(databaseText);
        
        System.out.println("Suffix Array Constructed: " + Arrays.toString(suffixArray));
        
        searchPattern(databaseText, targetPattern, suffixArray);
        System.out.println("Status: Genome-scale binary search complete.");
    }
}