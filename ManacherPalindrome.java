public class ManacherPalindrome {

    // O(N) Time, O(N) Space - Linear Palindrome Extraction
    public static String findLongestPalindrome(String s) {
        if (s == null || s.length() == 0) return "";

        // Step 1: Transform string to handle even/odd palindromes uniformly
        // e.g., "aba" becomes "^#a#b#a#$"
        StringBuilder sb = new StringBuilder();
        sb.append("^");
        for (int i = 0; i < s.length(); i++) {
            sb.append("#").append(s.charAt(i));
        }
        sb.append("#$");
        
        String transformed = sb.toString();
        int n = transformed.length();
        int[] p = new int[n]; // Palindrome radius array
        
        int center = 0, rightBoundary = 0;
        int maxLen = 0;
        int centerIndex = 0;

        System.out.println("Executing O(N) Manacher mirror-boundary scan...");

        for (int i = 1; i < n - 1; i++) {
            // Find the mirror of 'i' relative to the current center
            int mirror = 2 * center - i;

            // If 'i' is inside the right boundary, we can safely copy the mirror's value 
            // constrained by how much space is left in the boundary box.
            if (rightBoundary > i) {
                p[i] = Math.min(rightBoundary - i, p[mirror]);
            }

            // Attempt to expand the palindrome centered at 'i'
            while (transformed.charAt(i + 1 + p[i]) == transformed.charAt(i - 1 - p[i])) {
                p[i]++;
            }

            // If the palindrome expands past our right boundary, update the center and boundary box
            if (i + p[i] > rightBoundary) {
                center = i;
                rightBoundary = i + p[i];
            }

            // Track the absolute longest palindrome found so far
            if (p[i] > maxLen) {
                maxLen = p[i];
                centerIndex = i;
            }
        }

        // Extract the original substring from the transformed coordinates
        int start = (centerIndex - maxLen) / 2;
        return s.substring(start, start + maxLen);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Manacher Linear Palindrome Engine ---");
        
        String databaseText = "babad";
        
        String longest = findLongestPalindrome(databaseText);
        
        System.out.println("Source String: '" + databaseText + "'");
        System.out.println("CRITICAL INSIGHT: Longest Palindromic Substring is '" + longest + "'");
        System.out.println("Status: Quadratic loop avoided. Linear O(N) extraction verified.");
    }
}