import java.util.Arrays;

public class NumberTheoreticTransformEngine {

    // Proth Prime: 998244353 = 119 * 2^23 + 1 (Supports exact NTT up to length 2^23 = 8,388,608)
    private static final long MOD = 998_244_353L;
    private static final long PRIMITIVE_ROOT = 3L;

    // O(log exp) - Fast Modular Exponentiation
    private static long modPow(long base, long exp) {
        long result = 1L;
        base %= MOD;
        while (exp > 0) {
            if ((exp & 1L) == 1L) {
                result = (result * base) % MOD;
            }
            base = (base * base) % MOD;
            exp >>= 1;
        }
        return result;
    }

    // O(log MOD) - Fermat's Little Theorem Modular Multiplicative Inverse
    private static long modInverse(long value) {
        return modPow(value, MOD - 2);
    }

    // O(N log N) - Iterative Cooley-Tukey Bit-Reversal NTT Butterfly
    // invert = false -> Forward Transform into Modular Frequency Domain
    // invert = true  -> Inverse Transform back into Exact Coefficient Domain
    public static void executeNTT(long[] poly, boolean invert) {
        int n = poly.length;

        // Stage 1: In-place Bit-Reversal Permutation
        for (int i = 1, j = 0; i < n; i++) {
            int bit = n >> 1;
            for (; (j & bit) != 0; bit >>= 1) {
                j ^= bit;
            }
            j ^= bit;
            if (i < j) {
                long temp = poly[i];
                poly[i] = poly[j];
                poly[j] = temp;
            }
        }

        // Stage 2: Iterative Butterfly Merging across Powers of 2
        for (int len = 2; len <= n; len <<= 1) {
            // Compute exact principal len-th root of unity in GF(998244353)
            long wlen = modPow(PRIMITIVE_ROOT, (MOD - 1) / len);
            if (invert) {
                wlen = modInverse(wlen);
            }

            int half = len >> 1;
            for (int i = 0; i < n; i += len) {
                long w = 1L;
                for (int j = 0; j < half; j++) {
                    long u = poly[i + j];
                    long v = (poly[i + j + half] * w) % MOD;

                    long sum = u + v;
                    if (sum >= MOD) sum -= MOD;

                    long diff = u - v;
                    if (diff < 0) diff += MOD;

                    poly[i + j] = sum;
                    poly[i + j + half] = diff;

                    w = (w * wlen) % MOD;
                }
            }
        }

        // Stage 3: Normalize by N^(-1) mod P during the Inverse Transform
        if (invert) {
            long invN = modInverse(n);
            for (int i = 0; i < n; i++) {
                poly[i] = (poly[i] * invN) % MOD;
            }
        }
    }

    // O(N log N) - Exact Modular Polynomial Multiplication: C(x) = A(x) * B(x) mod 998244353
    public static long[] multiplyPolynomials(long[] polyA, long[] polyB) {
        int targetLen = polyA.length + polyB.length - 1;
        int n = 1;
        while (n < targetLen) {
            n <<= 1;
        }

        long[] fa = new long[n];
        long[] fb = new long[n];
        for (int i = 0; i < polyA.length; i++) fa[i] = ((polyA[i] % MOD) + MOD) % MOD;
        for (int i = 0; i < polyB.length; i++) fb[i] = ((polyB[i] % MOD) + MOD) % MOD;

        System.out.println("  [System] Executing Forward NTT over GF(" + MOD + ") with N = " + n + "...");
        executeNTT(fa, false);
        executeNTT(fb, false);

        System.out.println("  [System] Performing O(N) pointwise spectral multiplication...");
        for (int i = 0; i < n; i++) {
            fa[i] = (fa[i] * fb[i]) % MOD;
        }

        System.out.println("  [System] Executing Inverse NTT Butterfly & N^-1 normalization...");
        executeNTT(fa, true);

        return Arrays.copyOf(fa, targetLen);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Number Theoretic Transform (NTT) Exact Convolution Engine ---");

        // A(x) = 3 + 2x + 5x^2 + 1x^3
        // B(x) = 4 + 1x + 2x^2 + 6x^3
        long[] polyA = {3, 2, 5, 1};
        long[] polyB = {4, 1, 2, 6};

        System.out.println("  Polynomial A(x) : " + Arrays.toString(polyA));
        System.out.println("  Polynomial B(x) : " + Arrays.toString(polyB));

        long[] nttProduct = multiplyPolynomials(polyA, polyB);

        // Cross-verify against direct O(N^2) schoolbook convolution
        long[] exactCheck = new long[polyA.length + polyB.length - 1];
        for (int i = 0; i < polyA.length; i++) {
            for (int j = 0; j < polyB.length; j++) {
                exactCheck[i + j] = (exactCheck[i + j] + polyA[i] * polyB[j]) % MOD;
            }
        }

        System.out.println("\n--- Exact Modular Verification ---");
        System.out.println("  NTT Product C(x)      : " + Arrays.toString(nttProduct));
        System.out.println("  Direct O(N^2) Check   : " + Arrays.toString(exactCheck));
        System.out.println("  Bit-Exact Match?      : " + Arrays.equals(nttProduct, exactCheck));

        System.out.println("\nStatus: O(N^2) polynomial convolution reduced to O(N log N) with zero floating-point drift.");
    }
}