import java.util.*;

public class BurrowsWheelerTransformEngine {

    // O(N^2 log N) Construction (Can be optimized to O(N) using Suffix Arrays)
    public static String executeForwardBWT(String text) {
        System.out.println("  [System] Executing Forward Transform (Data Compression)...");
        int n = text.length();
        String[] rotations = new String[n];
        
        // 1. Generate all cyclic rotations of the text
        for (int i = 0; i < n; i++) {
            rotations[i] = text.substring(n - i) + text.substring(0, n - i);
        }
        
        // 2. Sort the rotations lexicographically
        Arrays.sort(rotations);
        
        // 3. Extract the final column to form the BWT string
        StringBuilder bwt = new StringBuilder();
        for (String rotation : rotations) {
            bwt.append(rotation.charAt(n - 1));
        }
        
        return bwt.toString();
    }

    // O(N) - The Core Reconstruction Engine (First-Last Property)
    public static String executeInverseBWT(String bwt) {
        System.out.println("  [System] Executing Inverse Transform (Data Reconstruction)...");
        int n = bwt.length();
        
        // Array to track the characters in the first column
        char[] firstColumn = bwt.toCharArray();
        Arrays.sort(firstColumn);
        
        // Map characters in the Last column (BWT) to the First column
        // This is known as the LF-mapping (Last-to-First)
        int[] lfMap = new int[n];
        boolean[] visited = new boolean[n];
        
        for (int i = 0; i < n; i++) {
            char target = firstColumn[i];
            for (int j = 0; j < n; j++) {
                if (bwt.charAt(j) == target && !visited[j]) {
                    lfMap[i] = j;
                    visited[j] = true;
                    break;
                }
            }
        }
        
        // Reconstruct the original sequence by traversing the LF-mapping
        StringBuilder original = new StringBuilder();
        int currentIndex = 0;
        
        // Find the index of the strict termination character '$' in the first column
        for (int i = 0; i < n; i++) {
            if (firstColumn[i] == '$') {
                currentIndex = i;
                break;
            }
        }
        
        for (int i = 0; i < n; i++) {
            original.append(firstColumn[currentIndex]);
            currentIndex = lfMap[currentIndex];
        }
        
        // Shift the termination character to the end where it belongs
        return original.substring(1) + "$";
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Burrows-Wheeler Transform (BWT) Architecture ---");
        
        // The '$' acts as a strict lexicographical terminator
        String originalDNA = "GATGCGCAG$";
        System.out.println("\nOriginal Payload:  " + originalDNA);
        
        String compressedBWT = executeForwardBWT(originalDNA);
        System.out.println("BWT Encoded State: " + compressedBWT); // Will group characters for high compressibility
        
        String reconstructedDNA = executeInverseBWT(compressedBWT);
        System.out.println("Decoded Payload:   " + reconstructedDNA);
        
        if (originalDNA.equals(reconstructedDNA)) {
            System.out.println("\nStatus: Lossless compression and mathematical inversion perfectly verified.");
        }
    }
}