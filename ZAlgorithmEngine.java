import java.util.*;

public class ZAlgorithmEngine {

    // O(N + M) Time - Constructing the Mathematical Z-Array
    private static int[] buildZArray(String str) {
        int n = str.length();
        int[] Z = new int[n];
        
        // The Z-Box boundaries
        int left = 0, right = 0;

        for (int i = 1; i < n; i++) {
            // Case 1: We are outside the current Z-Box. We must do manual character comparison.
            if (i > right) {
                left = right = i;
                while (right < n && str.charAt(right - left) == str.charAt(right)) {
                    right++;
                }
                Z[i] = right - left;
                right--;
            } 
            // Case 2: We are inside the Z-Box. We can use previously computed values to skip checks.
            else {
                int k = i - left;
                
                // If the copied value doesn't stretch past the right boundary, just copy it.
                if (Z[k] < right - i + 1) {
                    Z[i] = Z[k];
                } 
                // If it touches or crosses the boundary, we must manually expand the box further right.
                else {
                    left = i;
                    while (right < n && str.charAt(right - left) == str.charAt(right)) {
                        right++;
                    }
                    Z[i] = right - left;
                    right--;
                }
            }
        }
        return Z;
    }

    // O(N + M) Time - The Core Execution
    public static void searchPattern(String text, String pattern) {
        System.out.println("Executing O(N+M) Z-Algorithm Pattern Scan...");
        
        // Create the combined string with a delimiter that never appears in the text
        String combined = pattern + "$" + text;
        
        int[] Z = buildZArray(combined);
        List<Integer> matchIndices = new ArrayList<>();

        // Scan the Z-Array. If any value equals the length of the pattern, it's a perfect match.
        for (int i = 0; i < Z.length; i++) {
            if (Z[i] == pattern.length()) {
                // Mathematically calculate the original index in the raw text
                matchIndices.add(i - pattern.length() - 1);
            }
        }

        if (matchIndices.isEmpty()) {
            System.out.println("Result: Pattern not found in the database.");
        } else {
            System.out.println("CRITICAL MATCHES FOUND AT INDICES: " + matchIndices);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Linear Time Z-Algorithm Architecture ---");
        
        String databaseText = "ababaaababaab";
        String targetPattern = "ababa";
        
        System.out.println("Database: '" + databaseText + "'");
        System.out.println("Target:   '" + targetPattern + "'\n");
        
        searchPattern(databaseText, targetPattern);
        
        System.out.println("\nStatus: Substring extraction complete. O(N*M) bottlenecks completely bypassed.");
    }
}