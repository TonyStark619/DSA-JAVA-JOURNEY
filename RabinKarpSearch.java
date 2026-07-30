public class RabinKarpSearch {
    // d is the number of characters in the input alphabet (256 for standard ASCII)
    public final static int d = 256;

    // O(N + M) Average Time - Rolling Hash Substring Analysis
    public void search(String pattern, String text, int primeMultiplier) {
        int patternLen = pattern.length();
        int textLen = text.length();
        int patternHash = 0; // Hash value for pattern
        int textHash = 0;    // Hash value for text
        int h = 1;

        System.out.println("Executing Cryptographic Rolling Hash Analysis...");

        // The value of h would be "pow(d, patternLen-1) % primeMultiplier"
        for (int i = 0; i < patternLen - 1; i++) {
            h = (h * d) % primeMultiplier;
        }

        // Calculate the initial hash value of pattern and the first window of text
        for (int i = 0; i < patternLen; i++) {
            patternHash = (d * patternHash + pattern.charAt(i)) % primeMultiplier;
            textHash = (d * textHash + text.charAt(i)) % primeMultiplier;
        }

        // Slide the pattern over text one character at a time
        for (int i = 0; i <= textLen - patternLen; i++) {
            
            // If the cryptographic hashes match, we execute a deep character verification
            if (patternHash == textHash) {
                boolean matchFound = true;
                for (int j = 0; j < patternLen; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        matchFound = false;
                        break;
                    }
                }
                if (matchFound) {
                    System.out.println("CRITICAL MATCH: Pattern isolated at memory index " + i);
                }
            }

            // The Magic: Slide the window and mathematically calculate the new hash in O(1)
            if (i < textLen - patternLen) {
                textHash = (d * (textHash - text.charAt(i) * h) + text.charAt(i + patternLen)) % primeMultiplier;

                // We might get a negative value depending on the prime, so we wrap it to positive
                if (textHash < 0) {
                    textHash = (textHash + primeMultiplier);
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Rabin-Karp Substring Engine ---");
        RabinKarpSearch engine = new RabinKarpSearch();
        
        String databaseText = "GEEKS FOR GEEKS";
        String targetPattern = "GEEK";
        
        // A prime number used to prevent integer overflow during hashing
        int prime = 101; 
        
        engine.search(targetPattern, databaseText, prime);
        System.out.println("Status: O(1) Rolling Hash traversal complete.");
    }
}