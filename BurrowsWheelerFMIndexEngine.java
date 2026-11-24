import java.util.*;

public class BurrowsWheelerFMIndexEngine {

    private static final int ASCII_SIZE = 128;
    private static final char SENTINEL = '$';

    private final int n;
    private final int[] suffixArray;     // Sorted cyclic rotation start offsets
    private final char[] bwt;            // Last column (L) of the sorted BWT rotation matrix
    private final int[] charStartOffset; // C[c]: count of text characters strictly smaller than c
    private final int[] charFreq;        // Total occurrences of each ASCII character
    private final int[][] occRank;       // occRank[c][i]: count of 'c' in bwt[0..i-1]

    public BurrowsWheelerFMIndexEngine(String rawText) {
        if (rawText.indexOf(SENTINEL) != -1) {
            throw new IllegalArgumentException("Input text must not contain sentinel character '$'.");
        }

        String terminated = rawText + SENTINEL;
        this.n = terminated.length();

        System.out.println("  [System] Computing O(N log N) Cyclic-Shift Suffix Array & BWT Transform...");
        this.suffixArray = buildCyclicSuffixArray(terminated);
        this.bwt = new char[n];
        this.charFreq = new int[ASCII_SIZE];
        this.charStartOffset = new int[ASCII_SIZE];
        this.occRank = new int[ASCII_SIZE][n + 1];

        // Extract BWT (Last Column) and build prefix rank table Occ(c, i)
        for (int i = 0; i < n; i++) {
            char lastChar = terminated.charAt((suffixArray[i] + n - 1) % n);
            bwt[i] = lastChar;
            charFreq[lastChar]++;

            for (int c = 0; c < ASCII_SIZE; c++) {
                occRank[c][i + 1] = occRank[c][i];
            }
            occRank[lastChar][i + 1]++;
        }

        // Build C[c]: starting row index of character 'c' in the sorted First Column (F)
        int cumulative = 0;
        for (int c = 0; c < ASCII_SIZE; c++) {
            charStartOffset[c] = cumulative;
            cumulative += charFreq[c];
        }
    }

    // O(N log N) - Prefix-Doubling Counting Sort for Cyclic Rotations (Zero substring allocations)
    private int[] buildCyclicSuffixArray(String s) {
        int len = s.length();
        int[] sa = new int[len];
        int[] cls = new int[len];
        int[] cnt = new int[Math.max(ASCII_SIZE, len)];

        for (int i = 0; i < len; i++) cnt[s.charAt(i)]++;
        for (int i = 1; i < ASCII_SIZE; i++) cnt[i] += cnt[i - 1];
        for (int i = len - 1; i >= 0; i--) sa[--cnt[s.charAt(i)]] = i;

        cls[sa[0]] = 0;
        int classes = 1;
        for (int i = 1; i < len; i++) {
            if (s.charAt(sa[i]) != s.charAt(sa[i - 1])) classes++;
            cls[sa[i]] = classes - 1;
        }

        int[] pn = new int[len];
        int[] cn = new int[len];
        for (int h = 1; h < len && classes < len; h <<= 1) {
            for (int i = 0; i < len; i++) {
                pn[i] = sa[i] - h;
                if (pn[i] < 0) pn[i] += len;
            }
            Arrays.fill(cnt, 0, classes, 0);
            for (int i = 0; i < len; i++) cnt[cls[pn[i]]]++;
            for (int i = 1; i < classes; i++) cnt[i] += cnt[i - 1];
            for (int i = len - 1; i >= 0; i--) sa[--cnt[cls[pn[i]]]] = pn[i];

            cn[sa[0]] = 0;
            classes = 1;
            for (int i = 1; i < len; i++) {
                int cur1 = cls[sa[i]], cur2 = cls[(sa[i] + h) % len];
                int prev1 = cls[sa[i - 1]], prev2 = cls[(sa[i - 1] + h) % len];
                if (cur1 != prev1 || cur2 != prev2) classes++;
                cn[sa[i]] = classes - 1;
            }
            System.arraycopy(cn, 0, cls, 0, len);
        }
        return sa;
    }

    public String getBwtString() {
        return new String(bwt);
    }

    // O(N) - Lossless Inversion: Reconstructs the exact original text purely from BWT via LF-Mapping
    public String invertBWT() {
        char[] recovered = new char[n - 1];
        int row = 0; // Row 0 in sorted First Column always starts with '$'

        for (int writeIdx = n - 2; writeIdx >= 0; writeIdx--) {
            char ch = bwt[row];
            recovered[writeIdx] = ch;
            // Last-to-First (LF) Property jump in O(1)
            row = charStartOffset[ch] + occRank[ch][row];
        }
        return new String(recovered);
    }

    // O(|P|) - FM-Index Backward Search returning the half-open SA range [top, bottom)
    public int[] backwardSearchRange(String pattern) {
        int top = 0;
        int bottom = n;

        for (int i = pattern.length() - 1; i >= 0; i--) {
            char ch = pattern.charAt(i);
            if (ch >= ASCII_SIZE || charFreq[ch] == 0) {
                return new int[]{0, 0};
            }
            top = charStartOffset[ch] + occRank[ch][top];
            bottom = charStartOffset[ch] + occRank[ch][bottom];
            if (top >= bottom) {
                return new int[]{0, 0};
            }
        }
        return new int[]{top, bottom};
    }

    // O(|P| + k) - Locates all exact starting positions of 'pattern' in the original text
    public List<Integer> locatePatternOffsets(String pattern) {
        int[] range = backwardSearchRange(pattern);
        List<Integer> offsets = new ArrayList<>(range[1] - range[0]);
        for (int r = range[0]; r < range[1]; r++) {
            offsets.add(suffixArray[r]);
        }
        Collections.sort(offsets);
        return offsets;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Burrows-Wheeler Transform & FM-Index Backward Search Engine ---");

        String originalCorpus = "ABRACADABRA";
        BurrowsWheelerFMIndexEngine fmIndex = new BurrowsWheelerFMIndexEngine(originalCorpus);

        String bwtPayload = fmIndex.getBwtString();
        String invertedCorpus = fmIndex.invertBWT();

        System.out.println("\n--- Diagnostic 1: BWT Run-Clustering & Lossless LF-Mapping Inversion ---");
        System.out.println("  Original Text             : " + originalCorpus);
        System.out.println("  Compressed BWT (Column L) : " + bwtPayload + "  (Notice clustered 'AAAA' & 'BB' runs)");
        System.out.println("  LF-Inverted Reconstruction: " + invertedCorpus);
        System.out.println("  Bit-Exact Roundtrip?      : " + originalCorpus.equals(invertedCorpus));

        System.out.println("\n--- Diagnostic 2: O(|P|) FM-Index Backward Pattern Queries ---");
        String[] queries = {"ABRA", "CAD", "RA", "BARA"};
        for (String q : queries) {
            List<Integer> hits = fmIndex.locatePatternOffsets(q);
            System.out.printf("  Pattern %-6s -> Count: %d | Exact Text Offsets: %s%n", "'" + q + "'", hits.size(), hits);
        }

        System.out.println("\nStatus: O(|P|) compressed full-text search and lossless BWT inversion verified via LF-Mapping.");
    }
}