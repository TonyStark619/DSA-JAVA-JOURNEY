public class ZAlgorithmMatcher {

    // O(N + M) Time - Construct the Z-Array memory map
    private void computeZArray(String concatenatedStr, int[] Z) {
        int n = concatenatedStr.length();
        // [L, R] define the window (the box) which matches the prefix
        int L = 0, R = 0;

        for (int i = 1; i < n; i++) {
            // Case 1: i is outside our current matching window
            if (i > R) {
                L = R = i;
                // Expand the right boundary as long as characters match the prefix
                while (R < n && concatenatedStr.charAt(R - L) == concatenatedStr.charAt(R)) {
                    R++;
                }
                Z[i] = R - L;
                R--; // Step back to the last matching character
            } 
            // Case 2: i is inside the current matching window
            else {
                // k is the corresponding index in the prefix
                int k = i - L;
                
                // If the previously computed Z value doesn't stretch beyond our window, 
                // we can just blindly copy it (O(1) optimization).
                if (Z[k] < R - i + 1) {
                    Z[i] = Z[k];
                } 
                // If it DOES stretch beyond, we must recalculate starting from R
                else {
                    L = i;
                    while (R < n && concatenatedStr.charAt(R - L) == concatenatedStr.charAt(R)) {
                        R++;
                    }
                    Z[i] = R - L;
                    R--;
                }
            }
        }
    }

    // O(N + M) Time - Core Search Engine
    public void search(String text, String pattern) {
        System.out.println("Executing O(N+M) Z-Algorithm Substring Analysis...");
        
        // We inject a special character '$' that never appears in the text
        // This ensures the Z-value can never exceed the pattern length
        String concatenated = pattern + "$" + text;
        int concatLength = concatenated.length();
        
        int[] Z = new int[concatLength];
        computeZArray(concatenated, Z);

        boolean found = false;
        // Scan the Z-array for values exactly equal to the pattern's length
        for (int i = 0; i < concatLength; i++) {
            if (Z[i] == pattern.length()) {
                // We subtract pattern.length() + 1 to account for the "Pattern$" prefix we added
                int matchIndex = i - pattern.length() - 1;
                System.out.println("CRITICAL MATCH: Pattern isolated at memory index " + matchIndex);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("Pattern does not exist within the given string.");
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Z-Algorithm Search Architecture ---");
        ZAlgorithmMatcher engine = new ZAlgorithmMatcher();
        
        String databaseText = "AABAACAADAABAABA";
        String targetPattern = "AABA";
        
        engine.search(databaseText, targetPattern);
        System.out.println("Status: Dynamic boundary sliding successful. Zero redundant comparisons.");
    }
}