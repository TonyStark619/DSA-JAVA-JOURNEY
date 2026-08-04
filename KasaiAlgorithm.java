import java.util.Arrays;

public class KasaiAlgorithm {
    
    // O(N) Time - The Core Pattern Engine
    public static int[] buildLCPArray(String text, int[] suffixArray) {
        int n = text.length();
        int[] lcp = new int[n];
        
        // The Inverse Suffix Array: tells us the alphabetical rank of the suffix starting at index i
        int[] invSuff = new int[n];
        for (int i = 0; i < n; i++) {
            invSuff[suffixArray[i]] = i;
        }

        int k = 0; // Represents the length of the matching prefix
        
        System.out.println("Executing O(N) Kasai Traversal...");
        
        for (int i = 0; i < n; i++) {
            // The alphabetically last suffix has no next suffix to compare against
            if (invSuff[i] == n - 1) {
                k = 0;
                continue;
            }

            // Get the starting index of the NEXT suffix in the sorted alphabetical array
            int j = suffixArray[invSuff[i] + 1];

            // Compare characters and expand the prefix window (k)
            while (i + k < n && j + k < n && text.charAt(i + k) == text.charAt(j + k)) {
                k++;
            }

            // Store the length of the longest common prefix for this rank
            lcp[invSuff[i]] = k;

            // THE MAGIC: If a prefix matched for length k, the next suffix in the text 
            // MUST match for at least length (k - 1). We do not reset k to 0. 
            // We just subtract 1 and continue, strictly binding the time complexity to O(N).
            if (k > 0) {
                k--;
            }
        }
        return lcp;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting LCP (Longest Common Prefix) Engine ---");
        
        String databaseText = "banana";
        // Pre-computed Suffix Array for "banana": [5, 3, 1, 0, 4, 2]
        // Suffixes: a, ana, anana, banana, na, nana
        int[] suffixArray = {5, 3, 1, 0, 4, 2}; 
        
        int[] lcpArray = buildLCPArray(databaseText, suffixArray);
        
        System.out.println("\nText: " + databaseText);
        System.out.println("Suffix Array: " + Arrays.toString(suffixArray));
        System.out.println("LCP Array:    " + Arrays.toString(lcpArray));
        
        // The maximum value in the LCP array represents the longest repeated substring
        int maxLCP = 0;
        int maxIndex = 0;
        for (int i = 0; i < lcpArray.length; i++) {
            if (lcpArray[i] > maxLCP) {
                maxLCP = lcpArray[i];
                maxIndex = i;
            }
        }
        
        String longestRepeating = databaseText.substring(suffixArray[maxIndex], suffixArray[maxIndex] + maxLCP);
        System.out.println("\nCRITICAL INSIGHT: Longest Repeating Substring is '" + longestRepeating + "' (Length: " + maxLCP + ")");
        System.out.println("Status: Deep pattern extraction complete in linear time.");
    }
}