import java.util.Arrays;

public class FastWalshHadamardEngine {

    // O(N log N) In-Place Butterfly Transform
    // invert = false -> Forward Transform into the Spectral Hadamard Domain
    // invert = true  -> Inverse Transform back into the Spatial XOR Domain
    public static void executeButterflyTransform(long[] poly, boolean invert) {
        int n = poly.length;

        // Iterate across bit dimensions: block lengths of 1, 2, 4, 8 ... N/2
        for (int len = 1; 2 * len <= n; len <<= 1) {
            for (int i = 0; i < n; i += 2 * len) {
                for (int j = 0; j < len; j++) {
                    long u = poly[i + j];
                    long v = poly[i + len + j];

                    // Hadamard Matrix Transformation: [u + v, u - v]
                    poly[i + j] = u + v;
                    poly[i + len + j] = u - v;

                    // During the Inverse Transform, divide by 2 at each of the log2(N) stages
                    if (invert) {
                        poly[i + j] /= 2;
                        poly[i + len + j] /= 2;
                    }
                }
            }
        }
    }

    // O(N log N) - Computes C[k] = Sum of (A[i] * B[j]) for all (i ^ j == k)
    public static long[] computeXORConvolution(long[] arrayA, long[] arrayB) {
        int n = arrayA.length;
        if ((n & (n - 1)) != 0) {
            throw new IllegalArgumentException("Vector length must be an exact power of 2.");
        }

        long[] fa = Arrays.copyOf(arrayA, n);
        long[] fb = Arrays.copyOf(arrayB, n);

        System.out.println("  [System] Projecting vectors into the Spectral Walsh-Hadamard Domain...");
        executeButterflyTransform(fa, false);
        executeButterflyTransform(fb, false);

        System.out.println("  [System] Executing O(N) pointwise spectral multiplication...");
        for (int i = 0; i < n; i++) {
            fa[i] = fa[i] * fb[i];
        }

        System.out.println("  [System] Applying Inverse FWHT Butterfly to recover XOR Convolution...");
        executeButterflyTransform(fa, true);

        return fa;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Fast Walsh-Hadamard Transform (FWHT) Engine ---");

        // Frequency counts for 4-bit states (indices 0 to 7)
        long[] stateVectorA = {1, 2, 0, 3, 4, 0, 1, 2};
        long[] stateVectorB = {2, 0, 1, 1, 0, 3, 2, 1};

        System.out.println("  Vector A : " + Arrays.toString(stateVectorA));
        System.out.println("  Vector B : " + Arrays.toString(stateVectorB));

        long[] xorConvolved = computeXORConvolution(stateVectorA, stateVectorB);

        System.out.println("\n--- Executing O(N log N) Spectral Verification ---");
        System.out.println("  Result C : " + Arrays.toString(xorConvolved));

        // Verify mathematically against a direct O(N^2) check for index k = 3
        long manualCheckK3 = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i ^ j) == 3) {
                    manualCheckK3 += stateVectorA[i] * stateVectorB[j];
                }
            }
        }
        System.out.println("  Index [k = 3] Spectral Result : " + xorConvolved[3]);
        System.out.println("  Index [k = 3] Manual Check    : " + manualCheckK3);

        System.out.println("\nStatus: O(N^2) bitwise convolution bottleneck collapsed to O(N log N) via Hadamard butterfly stages.");
    }
}