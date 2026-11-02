public class ManachersPalindromeEngine {

    // O(N) - Geometrically transforms the string to handle even/odd lengths uniformly
    // E.g., "aba" -> "^#a#b#a#$"
    private String preProcessPayload(String s) {
        if (s == null || s.length() == 0) return "^$";
        StringBuilder transformed = new StringBuilder("^");
        for (int i = 0; i < s.length(); i++) {
            transformed.append("#").append(s.charAt(i));
        }
        transformed.append("#$");
        return transformed.toString();
    }

    // O(N) - The Core Manacher's Engine
    public String executeO1MirrorSweep(String originalString) {
        System.out.println("  [System] Executing Manacher's Linear Palindrome Sweep...");
        
        String T = preProcessPayload(originalString);
        int n = T.length();
        
        // Array to store the exact mathematical radius of the palindrome centered at index i
        int[] palindromeRadii = new int[n];
        
        int center = 0;
        int rightBoundary = 0;
        
        int maxRadius = 0;
        int centerIndexForMax = 0;

        for (int i = 1; i < n - 1; i++) {
            // THE MAGIC: Find the exact mathematical mirror of 'i' relative to our current 'center'
            int mirrorIndex = 2 * center - i;

            // If we are operating inside the established right boundary, 
            // we instantly inherit the calculated radius from the mirror index in O(1) time.
            if (i < rightBoundary) {
                palindromeRadii[i] = Math.min(rightBoundary - i, palindromeRadii[mirrorIndex]);
            }

            // Attempt to expand beyond the inherited radius
            while (T.charAt(i + 1 + palindromeRadii[i]) == T.charAt(i - 1 - palindromeRadii[i])) {
                palindromeRadii[i]++;
            }

            // If our new palindrome stretches beyond the old right boundary, 
            // we shift our geometric center and boundary to the new extreme right.
            if (i + palindromeRadii[i] > rightBoundary) {
                center = i;
                rightBoundary = i + palindromeRadii[i];
            }

            // Track the absolute maximum found across the entire sweep
            if (palindromeRadii[i] > maxRadius) {
                maxRadius = palindromeRadii[i];
                centerIndexForMax = i;
            }
        }

        // Mathematically reconstruct the original substring from the transformed coordinates
        int startIndex = (centerIndexForMax - 1 - maxRadius) / 2;
        return originalString.substring(startIndex, startIndex + maxRadius);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Manacher's Algorithm Architecture ---");
        
        ManachersPalindromeEngine engine = new ManachersPalindromeEngine();
        String massivePayload = "abacdfgdcaba";
        
        System.out.println("  [Target Payload] " + massivePayload);
        
        String optimalPalindrome = engine.executeO1MirrorSweep(massivePayload);
        
        System.out.println("\nCRITICAL RESULT: Longest Palindrome Isolated -> '" + optimalPalindrome + "'");
        // Expected: "aba"
        System.out.println("Status: O(N^2) bottleneck mathematically eliminated via O(1) mirror radius inheritance.");
    }
}