import java.util.*;

public class KMPEngine {

    // O(M) Time - Pre-computing the LPS (Longest Prefix Suffix) Array
    private static int[] computeLPSArray(String pattern) {
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
                // Mismatch after at least one match
                if (length != 0) {
                    // THE MAGIC: We do NOT reset 'length' to 0. We fall back to the previous LPS value.
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    // O(N) Time - The Core Search Engine
    public static void executeKMPSearch(String text, String pattern) {
        System.out.println("Executing O(N+M) KMP Pointer Analysis...");
        
        int n = text.length();
        int m = pattern.length();
        
        int[] lps = computeLPSArray(pattern);
        List<Integer> matchIndices = new ArrayList<>();
        
        int i = 0; // Pointer for the main text
        int j = 0; // Pointer for the pattern
        
        while (i < n) {
            // Match found, move both pointers forward
            if (pattern.charAt(j) == text.charAt(i)) {
                j++;
                i++;
            }
            
            // Full pattern matched!
            if (j == m) {
                matchIndices.add(i - j);
                // Reset 'j' using the LPS array to continue searching for more matches
                j = lps[j - 1];
            } 
            // Mismatch detected after a partial match
            else if (i < n && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    // Shift the pattern using LPS (Never move 'i' backwards!)
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        if (matchIndices.isEmpty()) {
            System.out.println("Result: Pattern not found in the database.");
        } else {
            System.out.println("CRITICAL MATCHES FOUND AT INDICES: " + matchIndices);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Knuth-Morris-Pratt (KMP) Architecture ---");
        
        String databaseText = "ONIONIONSPL";
        String targetPattern = "ONIONS";
        
        System.out.println("Database: '" + databaseText + "'");
        System.out.println("Target:   '" + targetPattern + "'\n");
        
        executeKMPSearch(databaseText, targetPattern);
        
        System.out.println("\nStatus: LPS array generated. Zero-backtracking search completed.");
    }
}