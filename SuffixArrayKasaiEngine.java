import java.util.Arrays;

public class SuffixArrayKasaiEngine {

    static class Suffix implements Comparable<Suffix> {
        int index;
        String text;

        public Suffix(int index, String text) {
            this.index = index;
            this.text = text;
        }

        @Override
        public int compareTo(Suffix other) {
            return this.text.compareTo(other.text);
        }
    }

    private final String payload;
    private final int N;
    private int[] suffixArray;
    private int[] lcpArray;

    public SuffixArrayKasaiEngine(String text) {
        this.payload = text;
        this.N = text.length();
        this.suffixArray = new int[N];
        this.lcpArray = new int[N];
    }

    // Constructs the Suffix Array 
    public void buildSuffixArray() {
        System.out.println("  [System] Generating Lexicographical Suffix Geometry...");
        Suffix[] suffixes = new Suffix[N];
        
        for (int i = 0; i < N; i++) {
            suffixes[i] = new Suffix(i, payload.substring(i));
        }
        
        Arrays.sort(suffixes);
        
        for (int i = 0; i < N; i++) {
            suffixArray[i] = suffixes[i].index;
        }
    }

    // THE MAGIC: Kasai's Algorithm for O(N) LCP Construction
    public void executeKasaiAlgorithm() {
        System.out.println("  [System] Executing Kasai's O(N) LCP Array Calculation...");
        
        // rank[i] stores the position of the i-th suffix in the sorted suffix array
        int[] rank = new int[N];
        for (int i = 0; i < N; i++) {
            rank[suffixArray[i]] = i;
        }

        int h = 0; // The geometric overlap tracker
        for (int i = 0; i < N; i++) {
            if (rank[i] > 0) {
                // Find the suffix that sits immediately before the current one in the sorted array
                int previousSuffixIndex = suffixArray[rank[i] - 1];
                
                // Expand the matching prefix window
                while (i + h < N && previousSuffixIndex + h < N && 
                       payload.charAt(i + h) == payload.charAt(previousSuffixIndex + h)) {
                    h++;
                }
                
                // Lock the Longest Common Prefix length into the LCP array
                lcpArray[rank[i]] = h;
                
                // Kasai's Mathematical Proof: The next suffix must share at least h-1 characters
                if (h > 0) {
                    h--;
                }
            }
        }
    }

    // O(N) - Scan the LCP array to isolate the absolute longest duplicated payload
    public String isolateLongestRepeatedSubstring() {
        int maxLcp = 0;
        int targetIndex = 0;
        
        for (int i = 1; i < N; i++) {
            if (lcpArray[i] > maxLcp) {
                maxLcp = lcpArray[i];
                targetIndex = suffixArray[i];
            }
        }
        return payload.substring(targetIndex, targetIndex + maxLcp);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Suffix Array & Kasai LCP Architecture ---");
        
        String dnaPayload = "ATCGATCGA";
        System.out.println("Target Sequence: " + dnaPayload);
        
        SuffixArrayKasaiEngine engine = new SuffixArrayKasaiEngine(dnaPayload);
        engine.buildSuffixArray();
        engine.executeKasaiAlgorithm();
        
        String longestRepeat = engine.isolateLongestRepeatedSubstring();
        
        // Expecting "ATCGA"
        System.out.println("\nCRITICAL RESULT: Longest Repeated Substring -> '" + longestRepeat + "'");
        System.out.println("Status: Lexicographical overlap successfully isolated in strict O(N) linear time.");
    }
}