import java.util.Arrays;

public class HeldKarpTSPBitmask {

    private static final int INF = 100000000;

    // O(N^2 * 2^N) Time, O(N * 2^N) Space - Bitmask DP Engine
    public static int solveTSP(int[][] distanceMatrix, int n) {
        System.out.println("Executing O(N^2 * 2^N) Held-Karp Bitmask DP Optimization...");

        // dp[mask][u]: Shortest path visiting cities marked in 'mask', ending at city 'u'
        int totalStates = 1 << n; // 2^N states
        int[][] dp = new int[totalStates][n];

        for (int[] row : dp) {
            Arrays.fill(row, -1);
        }

        // Start from City 0. Initial mask = 1 (binary 00...0001, meaning City 0 is visited)
        int minCost = tspRecurse(1, 0, distanceMatrix, dp, n);
        return minCost;
    }

    private static int tspRecurse(int mask, int currentCity, int[][] dist, int[][] dp, int n) {
        // Base Case: All cities visited (mask is all 1s: (1 << n) - 1)
        if (mask == (1 << n) - 1) {
            // Return cost to cycle back from currentCity to starting City 0
            return dist[currentCity][0];
        }

        // Return memoized result if state was previously solved
        if (dp[mask][currentCity] != -1) {
            return dp[mask][currentCity];
        }

        int optimalCost = INF;

        // Try transitioning to every unvisited city 'nextCity'
        for (int nextCity = 0; nextCity < n; nextCity++) {
            // Check if the nextCity bit is NOT set in mask
            if ((mask & (1 << nextCity)) == 0) {
                // Set the bit for nextCity: mask | (1 << nextCity)
                int newMask = mask | (1 << nextCity);
                int cost = dist[currentCity][nextCity] + tspRecurse(newMask, nextCity, dist, dp, n);
                optimalCost = Math.min(optimalCost, cost);
            }
        }

        // Memoize and return
        dp[mask][currentCity] = optimalCost;
        return optimalCost;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Held-Karp Bitmask DP Architecture ---");

        // Distance matrix between 4 cities (symmetric routing graph)
        int n = 4;
        int[][] routingGrid = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };

        int optimalTour = solveTSP(routingGrid, n);

        System.out.println("\n--- Global Routing Resolution ---");
        System.out.println("CRITICAL RESULT: Optimal TSP Cycle Distance = " + optimalTour + " units.");
        System.out.println("Status: Combinatorial explosion avoided. Permutation search bounded by Bitmask DP.");
    }
}