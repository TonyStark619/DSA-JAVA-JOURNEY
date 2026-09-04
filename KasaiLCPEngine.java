import java.util.*;

public class KasaiLCPEngine {

    // O(N) Time, O(N) Space - Constructing the LCP Array from a Suffix Array
    public static int[] buildLCPArray(String text, int[] suffixArray) {
        System.out.println("Executing O(N) Kasai's Algorithm for LCP Construction...");
        
        int n = suffixArray.length;
        int[] lcp = new int[n];
        
        // Step 1: Build the Inverse Suffix Array
        // invSuff[i] stores the index in the suffixArray where the suffix starting at text[i] is located.
        int[] invSuff = new int[n];
        for (int i = 0; i < n; i++) {
            invSuff[suffixArray[i]] = i;
        }

        // Step 2: Kasai's Core Logic
        int prefixLength = 0;
        for (int i = 0; i < n; i++) {
            // If the current suffix is the very last one in the alphabetical order, 
            // it has no "next" suffix to compare against.
            if (invSuff[i] == n - 1) {
                prefixLength = 0;
                continue;
            }

            // Find the starting index of the NEXT suffix in the sorted alphabetical array
            int nextSuffixIndex = suffixArray[invSuff[i] + 1];

            // Manually match characters, starting from the known overlapping prefixLength
            while (i + prefixLength < n && nextSuffixIndex + prefixLength < n 
                   && text.charAt(i + prefixLength) == text.charAt(nextSuffixIndex + prefixLength)) {
                prefixLength++;
            }

            // Log the longest common prefix length for this adjacent pair
            lcp[invSuff[i]] = prefixLength;

            // THE MAGIC: When we move to the next suffix in the original text (i+1), 
            // we know at least (prefixLength - 1) characters will still match. We never start from 0!
            if (prefixLength > 0) {
                prefixLength--;
            }
        }
        return lcp;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting LCP (Longest Common Prefix) Architecture ---");
        
        String databaseText = "banana";
        
        // Hardcoding the Suffix Array for "banana" generated from yesterday's code
        // Alphabetical order: a, ana, anana, banana, na, nana
        // Original indices:   5, 3,   1,     0,      4,  2
        int[] suffixArray = {5, 3, 1, 0, 4, 2};
        
        int[] lcpArray = buildLCPArray(databaseText, suffixArray);
        
        System.out.println("\n--- Final LCP Array ---");
        System.out.println("Suffix Array: " + Arrays.toString(suffixArray));
        System.out.println("LCP Array:    " + Arrays.toString(lcpArray));
        
        // Finding the Longest Repeated Substring
        int maxLCP = 0;
        int maxIndex = 0;
        for (int i = 0; i < lcpArray.length; i++) {
            if (lcpArray[i] > maxLCP) {
                maxLCP = lcpArray[i];
                maxIndex = i;
            }
        }
        
        System.out.println("\nCRITICAL INSIGHT: Longest Repeated Substring is '" 
            + databaseText.substring(suffixArray[maxIndex], suffixArray[maxIndex] + maxLCP) 
            + "' with length " + maxLCP);
            
        System.out.println("Status: Linear time prefix resolution successful.");
    }
}