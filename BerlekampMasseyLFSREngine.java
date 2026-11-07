import java.util.*;

public class BerlekampMasseyLFSREngine {

    private static final long MOD = 1_000_000_007L;

    // O(log MOD) - Fermat's Little Theorem Modular Inverse
    private static long modPow(long base, long exp) {
        long res = 1;
        base %= MOD;
        while (exp > 0) {
            if ((exp & 1) == 1) res = (res * base) % MOD;
            base = (base * base) % MOD;
            exp >>= 1;
        }
        return res;
    }

    private static long modInverse(long n) {
        return modPow(n, MOD - 2);
    }

    // O(N^2) - Synthesizes the shortest Linear Feedback Shift Register (LFSR) recurrence
    public static List<Long> synthesizeMinimalRecurrence(long[] sequence) {
        System.out.println("  [System] Executing Berlekamp-Massey Discrepancy Sweep over GF(" + MOD + ")...");

        // C holds the current connection polynomial; B holds the last polynomial prior to a length jump
        List<Long> C = new ArrayList<>(Collections.singletonList(1L));
        List<Long> B = new ArrayList<>(Collections.singletonList(1L));

        int L = 0;      // Current minimal LFSR length
        int m = 1;      // Number of iterations since the last length jump
        long b = 1;     // Discrepancy value at the last length jump

        for (int i = 0; i < sequence.length; i++) {
            // 1. Calculate the discrepancy d at position i
            long d = sequence[i] % MOD;
            for (int j = 1; j <= L; j++) {
                d = (d + C.get(j) * sequence[i - j]) % MOD;
            }

            // 2. If discrepancy is 0, current recurrence accurately predicts sequence[i]
            if (d == 0) {
                m++;
                continue;
            }

            // 3. Discrepancy detected: repair the recurrence polynomial C(x)
            List<Long> T = new ArrayList<>(C);
            long factor = (d * modInverse(b)) % MOD;

            while (C.size() < B.size() + m) {
                C.add(0L);
            }

            for (int j = 0; j < B.size(); j++) {
                int targetIdx = j + m;
                long updatedVal = (C.get(targetIdx) - (factor * B.get(j)) % MOD + MOD) % MOD;
                C.set(targetIdx, updatedVal);
            }

            // 4. Massey's Theorem: If 2*L <= i, the LFSR register length must expand
            if (2 * L <= i) {
                L = i + 1 - L;
                B = T;
                b = d;
                m = 1;
            } else {
                m++;
            }
        }

        // Convert internal connection polynomial C[1..L] into positive recurrence weights
        List<Long> recurrenceWeights = new ArrayList<>(L);
        for (int j = 1; j <= L; j++) {
            recurrenceWeights.add((MOD - C.get(j)) % MOD);
        }
        return recurrenceWeights;
    }

    // Extrapolates the next state using the discovered LFSR recurrence weights
    public static long extrapolateNextTerm(long[] sequence, List<Long> weights) {
        long nextVal = 0;
        int n = sequence.length;
        for (int j = 0; j < weights.size(); j++) {
            nextVal = (nextVal + weights.get(j) * sequence[n - 1 - j]) % MOD;
        }
        return nextVal;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Berlekamp-Massey LFSR Cryptanalysis Engine ---");

        // Hidden recurrence: S[n] = 2*S[n-1] + 3*S[n-2]
        // Terms: 1, 2, 7, 20, 61, 182
        long[] observedStream = {1, 2, 7, 20, 61, 182};
        System.out.println("  Observed Telemetry Stream : " + Arrays.toString(observedStream));

        List<Long> discoveredWeights = synthesizeMinimalRecurrence(observedStream);

        System.out.println("\n--- Discovered Minimal LFSR Recurrence ---");
        System.out.println("  Minimal Register Length L : " + discoveredWeights.size());
        System.out.println("  Recurrence Coefficients   : " + discoveredWeights + " (Represents S[n] = 2*S[n-1] + 3*S[n-2])");

        long predictedTerm6 = extrapolateNextTerm(observedStream, discoveredWeights);
        // Expected: 2*(182) + 3*(61) = 364 + 183 = 547
        System.out.println("  Extrapolated Next State   : " + predictedTerm6);

        System.out.println("\nStatus: Unknown stream recurrence synthesized from raw observations in O(N^2) time.");
    }
}