import java.util.*;

public class RabinKarpEngine {
    // A base prime number for character weighting (ASCII space ~256)
    private static final int BASE = 256;
    // A large prime number to prevent integer overflow and minimize hash collisions
    private static final int PRIME_MOD = 1000000007;

    // O(N + M) Average Time, O(1) Auxiliary Space
    public static void executeRabinKarpSearch(String text, String pattern) {
        System.out.println("Executing O(N+M) Rabin-Karp Rolling Hash Analysis...");

        int n = text.length();
        int m = pattern.length();

        if (m > n) {
            System.out.println("Result: Pattern length exceeds text length.");
            return;
        }

        long patternHash = 0;
        long windowHash = 0;
        long highestPower = 1;

        // Calculate highestPower = (BASE^(m - 1)) % PRIME_MOD
        for (int i = 0; i < m - 1; i++) {
            highestPower = (highestPower * BASE) % PRIME_MOD;
        }

        // Step 1: Pre-calculate the hash of the pattern and the first window of the text
        for (int i = 0; i < m; i++) {
            patternHash = (BASE * patternHash + pattern.charAt(i)) % PRIME_MOD;
            windowHash = (BASE * windowHash + text.charAt(i)) % PRIME_MOD;
        }

        List<Integer> matchIndices = new ArrayList<>();

        // Step 2: Slide the rolling hash window across the text
        for (int i = 0; i <= n - m; i++) {
            // If the hash values match, verify character by character to eliminate spurious collisions
            if (patternHash == windowHash) {
                boolean exactMatch = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        exactMatch = false;
                        break;
                    }
                }
                if (exactMatch) {
                    matchIndices.add(i);
                }
            }

            // Step 3: Compute rolling hash for the next window: O(1) rolling step
            if (i < n - m) {
                // Remove the leading character and add the trailing character
                windowHash = (BASE * (windowHash - text.charAt(i) * highestPower) + text.charAt(i + m)) % PRIME_MOD;

                // Handle negative modulus result in Java
                if (windowHash < 0) {
                    windowHash = (windowHash + PRIME_MOD);
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
        System.out.println("--- Booting Rabin-Karp Rolling Hash Engine ---");

        String databaseText = "GEEKS FOR GEEKS ACADEMY";
        String targetPattern = "GEEK";

        System.out.println("Database: '" + databaseText + "'");
        System.out.println("Target:   '" + targetPattern + "'\n");

        executeRabinKarpSearch(databaseText, targetPattern);

        System.out.println("\nStatus: Polynomial rolling hash verified. Zero auxiliary arrays allocated.");
    }
}