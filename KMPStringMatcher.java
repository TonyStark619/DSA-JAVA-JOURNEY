public class KMPStringMatcher {

    // O(M) Time - The Mathematical Memory Map
    private int[] computeLPSArray(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int length = 0; // Length of the previous longest prefix suffix
        int i = 1;

        lps[0] = 0; // LPS of a single character is always 0

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    // Backtrack the length using our previously computed LPS values
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    // O(N + M) Time - High-Speed Forward-Only Search
    public void search(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        
        int[] lps = computeLPSArray(pattern);
        int i = 0; // Index for text
        int j = 0; // Index for pattern

        System.out.println("Executing O(N+M) KMP Substring Analysis...");
        boolean found = false;

        while (i < n) {
            if (pattern.charAt(j) == text.charAt(i)) {
                j++;
                i++;
            }
            if (j == m) {
                System.out.println("CRITICAL MATCH: Pattern found starting at memory index " + (i - j));
                found = true;
                // Use the LPS array to avoid resetting j to 0, preventing backtracking
                j = lps[j - 1];
            } 
            else if (i < n && pattern.charAt(j) != text.charAt(i)) {
                // Mismatch after j matches
                if (j != 0) {
                    // Skip ahead safely using the LPS map
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        
        if (!found) {
            System.out.println("Pattern does not exist within the given string.");
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Knuth-Morris-Pratt Search Engine ---");
        KMPStringMatcher engine = new KMPStringMatcher();
        
        String databaseText = "ABABDABACDABABCABAB";
        String targetPattern = "ABABCABAB";
        
        engine.search(databaseText, targetPattern);
        System.out.println("Status: String traversal complete. Zero backward regressions executed.");
    }
}