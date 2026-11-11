import java.util.Arrays;

public class HungarianMinCostAssignmentEngine {

    private static final long INF = Long.MAX_VALUE / 4;

    // O(N^3) - Resolves the exact Minimum-Cost 1-to-1 Bipartite Assignment
    // costMatrix is 0-indexed [N][N]; internally shifted to 1-indexed for dummy-node augmentation
    public static long solveMinCostAssignment(long[][] costMatrix, int[] assignedWorkerForTask) {
        int n = costMatrix.length;

        // Dual Linear Programming Potentials: u[i] + v[j] <= costMatrix[i-1][j-1]
        long[] u = new long[n + 1];
        long[] v = new long[n + 1];

        // matchCol[j] = Task (1..N) currently matched to Worker j (1..N); 0 means unmatched
        int[] matchCol = new int[n + 1];

        // way[j] = Previous worker column in the alternating augmenting path tree
        int[] way = new int[n + 1];

        System.out.println("  [System] Executing O(N^3) Kuhn-Munkres Primal-Dual Potential Augmentation...");

        for (int task = 1; task <= n; task++) {
            // Place the current task at dummy column 0 to root the alternating tree
            matchCol[0] = task;
            int currentCol = 0;

            long[] minSlack = new long[n + 1];
            boolean[] visitedCol = new boolean[n + 1];
            Arrays.fill(minSlack, INF);

            // Grow the Hungarian alternating tree until an unmatched worker column is reached
            do {
                visitedCol[currentCol] = true;
                int currentTask = matchCol[currentCol];
                long delta = INF;
                int nextCol = 0;

                for (int worker = 1; worker <= n; worker++) {
                    if (!visitedCol[worker]) {
                        // Reduced cost (slack) on edge (currentTask -> worker)
                        long reducedCost = costMatrix[currentTask - 1][worker - 1] - u[currentTask] - v[worker];

                        if (reducedCost < minSlack[worker]) {
                            minSlack[worker] = reducedCost;
                            way[worker] = currentCol;
                        }
                        if (minSlack[worker] < delta) {
                            delta = minSlack[worker];
                            nextCol = worker;
                        }
                    }
                }

                // Dual Potential Update: Shift potentials by 'delta' to force at least one new tight edge
                for (int j = 0; j <= n; j++) {
                    if (visitedCol[j]) {
                        u[matchCol[j]] += delta;
                        v[j] -= delta;
                    } else {
                        minSlack[j] -= delta;
                    }
                }

                currentCol = nextCol;
            } while (matchCol[currentCol] != 0);

            // Augment the matching along the discovered tight alternating path via 'way' pointers
            do {
                int prevCol = way[currentCol];
                matchCol[currentCol] = matchCol[prevCol];
                currentCol = prevCol;
            } while (currentCol != 0);
        }

        // Populate the 0-indexed assignment map: Task -> Worker
        for (int worker = 1; worker <= n; worker++) {
            assignedWorkerForTask[matchCol[worker] - 1] = worker - 1;
        }

        // By LP strong duality, -v[0] equals the exact sum of optimal assignment costs
        return -v[0];
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Kuhn-Munkres Hungarian Min-Cost Assignment Engine ---");

        // 4 AI Agent Tasks assigned across 4 Heterogeneous Compute Nodes (Latency in ms)
        // Notice: A greedy choice for Task 0 picks Worker 0 (cost 10), forcing Task 1 into cost 80!
        long[][] clusterCostMatrix = {
            {10, 19,  8, 15},
            {10, 18,  7, 17},
            {13, 16,  9, 14},
            {12, 19,  8, 18}
        };

        int n = clusterCostMatrix.length;
        int[] optimalAssignment = new int[n];

        long minTotalLatency = solveMinCostAssignment(clusterCostMatrix, optimalAssignment);

        System.out.println("\n--- Optimal Primal-Dual Dispatch Schedule ---");
        long verifiedSum = 0;
        for (int task = 0; task < n; task++) {
            int worker = optimalAssignment[task];
            long cost = clusterCostMatrix[task][worker];
            verifiedSum += cost;
            System.out.println("  Task " + task + " --> Assigned to Worker Node " + worker + " (Cost: " + cost + " ms)");
        }

        System.out.println("\nCRITICAL RESULT: Dual Potential Optimum = " + minTotalLatency + " ms | Verified Sum = " + verifiedSum + " ms");
        System.out.println("Status: O(N!) combinatorial assignment bottleneck collapsed to strict O(N^3) via dual slack potentials.");
    }
}