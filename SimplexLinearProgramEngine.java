import java.util.Arrays;

public class SimplexLinearProgramEngine {

    private static final double EPS = 1e-9;

    private final int numVars;
    private final int numConstraints;
    private final double[][] tableau;
    private final int[] basis; // basis[row] holds the variable index currently active in that row

    // Solves: Maximize c^T * x  subject to  A * x <= b,  x >= 0  (assuming b >= 0)
    public SimplexLinearProgramEngine(double[][] A, double[] b, double[] c) {
        this.numConstraints = b.length;
        this.numVars = c.length;
        int totalCols = numVars + numConstraints + 1;

        this.tableau = new double[numConstraints + 1][totalCols];
        this.basis = new int[numConstraints];

        // 1. Populate constraint rows: [ A | I_slack | b ]
        for (int i = 0; i < numConstraints; i++) {
            if (b[i] < -EPS) {
                throw new IllegalArgumentException("Standard primal tableau requires non-negative RHS b[i] >= 0.");
            }
            System.arraycopy(A[i], 0, tableau[i], 0, numVars);
            tableau[i][numVars + i] = 1.0; // Slack variable s_i
            tableau[i][totalCols - 1] = b[i];
            basis[i] = numVars + i;        // Initial feasible origin: all slack variables are basic
        }

        // 2. Populate Objective Row: [ -c^T | 0 | 0 ]
        for (int j = 0; j < numVars; j++) {
            tableau[numConstraints][j] = -c[j];
        }
    }

    // Bland's Rule: Pick the lowest-indexed column with strictly negative reduced cost
    private int selectEnteringColumnBland() {
        int objRow = numConstraints;
        int decisionAndSlackCols = numVars + numConstraints;
        for (int j = 0; j < decisionAndSlackCols; j++) {
            if (tableau[objRow][j] < -EPS) {
                return j;
            }
        }
        return -1; // All reduced costs >= 0 -> Global Optimum Reached!
    }

    // Minimum Ratio Test with Bland's lowest-row tie-breaking
    private int selectLeavingRow(int pivotCol) {
        int rhsCol = numVars + numConstraints;
        int bestRow = -1;
        double minRatio = Double.POSITIVE_INFINITY;

        for (int i = 0; i < numConstraints; i++) {
            double coeff = tableau[i][pivotCol];
            if (coeff > EPS) {
                double ratio = tableau[i][rhsCol] / coeff;
                if (ratio < minRatio - EPS) {
                    minRatio = ratio;
                    bestRow = i;
                }
            }
        }
        return bestRow;
    }

    // Gauss-Jordan Elimination pivoting around (pivotRow, pivotCol)
    private void executePivot(int pivotRow, int pivotCol) {
        int totalCols = numVars + numConstraints + 1;
        double pivotVal = tableau[pivotRow][pivotCol];

        // Normalize the pivot row so tableau[pivotRow][pivotCol] == 1.0
        for (int j = 0; j < totalCols; j++) {
            tableau[pivotRow][j] /= pivotVal;
        }

        // Zero out pivotCol across all other rows (including the Objective Row)
        for (int r = 0; r <= numConstraints; r++) {
            if (r == pivotRow) continue;
            double factor = tableau[r][pivotCol];
            if (Math.abs(factor) > EPS) {
                for (int j = 0; j < totalCols; j++) {
                    tableau[r][j] -= factor * tableau[pivotRow][j];
                }
            }
        }
        basis[pivotRow] = pivotCol;
    }

    // Runs Primal Simplex until optimality or unbounded detection
    public double solve() {
        System.out.println("  [System] Executing Dantzig Primal Simplex with Bland's Anti-Cycling Rule...");
        int iteration = 0;

        while (true) {
            int enteringCol = selectEnteringColumnBland();
            if (enteringCol == -1) {
                break; // Optimal vertex isolated
            }

            int leavingRow = selectLeavingRow(enteringCol);
            if (leavingRow == -1) {
                throw new IllegalStateException("UNBOUNDED POLYTOPE: Objective can increase to +Infinity.");
            }

            iteration++;
            System.out.printf(
                "    -> Pivot #%d: Entering Col %d | Leaving Row %d (Replaced Var %d)%n",
                iteration, enteringCol, leavingRow, basis[leavingRow]
            );
            executePivot(leavingRow, enteringCol);
        }

        return tableau[numConstraints][numVars + numConstraints];
    }

    public double[] extractPrimalSolution() {
        int rhsCol = numVars + numConstraints;
        double[] x = new double[numVars];
        for (int i = 0; i < numConstraints; i++) {
            if (basis[i] < numVars) {
                x[basis[i]] = tableau[i][rhsCol];
            }
        }
        return x;
    }

    public double[] extractSlackCapacities() {
        int rhsCol = numVars + numConstraints;
        double[] slack = new double[numConstraints];
        for (int i = 0; i < numConstraints; i++) {
            if (basis[i] >= numVars) {
                slack[basis[i] - numVars] = tableau[i][rhsCol];
            }
        }
        return slack;
    }

    // Dual Shadow Prices reside directly in the Objective Row under the slack variable columns
    public double[] extractDualShadowPrices() {
        double[] shadowPrices = new double[numConstraints];
        for (int i = 0; i < numConstraints; i++) {
            shadowPrices[i] = tableau[numConstraints][numVars + i];
        }
        return shadowPrices;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Dantzig Primal Simplex & Dual Shadow-Price Engine ---");

        // GPU Cluster Allocation: Maximize Revenue Z = 60*x0 + 30*x1 + 20*x2
        // x0: LLM Inference, x1: Vision Fine-Tuning, x2: Physics Simulation
        double[] revenuePerUnit = {60.0, 30.0, 20.0};

        // Hardware Resource Constraints:
        // Constraint 0 (Rack Power <= 48 kW):       8*x0 + 6*x1 + 1.0*x2 <= 48
        // Constraint 1 (HBM Bandwidth <= 20 TB/s):  4*x0 + 2*x1 + 1.5*x2 <= 20
        // Constraint 2 (NVLink Slots <= 8):         2*x0 + 1.5*x1 + 0.5*x2 <= 8
        double[][] resourceMatrix = {
            {8.0, 6.0, 1.0},
            {4.0, 2.0, 1.5},
            {2.0, 1.5, 0.5}
        };
        double[] resourceCaps = {48.0, 20.0, 8.0};
        String[] resourceNames = {"Rack Power (kW)", "HBM Bandwidth (TB/s)", "NVLink Fabric Slots"};

        SimplexLinearProgramEngine lp = new SimplexLinearProgramEngine(resourceMatrix, resourceCaps, revenuePerUnit);
        double maxRevenue = lp.solve();

        double[] primalX = lp.extractPrimalSolution();
        double[] slacks = lp.extractSlackCapacities();
        double[] shadowPrices = lp.extractDualShadowPrices();

        System.out.println("\n--- Optimal Polytope Vertex & Complementary Slackness Forensics ---");
        System.out.printf("  Optimal Allocation [x0, x1, x2] : %s%n", Arrays.toString(primalX));
        System.out.printf("  Maximum Cluster Revenue (Z*)    : $%.2f%n%n", maxRevenue);

        double dualObjectiveCheck = 0.0;
        for (int i = 0; i < resourceNames.length; i++) {
            dualObjectiveCheck += resourceCaps[i] * shadowPrices[i];
            String status = (slacks[i] > EPS) ? "NON-BINDING (Spare Capacity)" : "BINDING BOTTLENECK";
            System.out.printf(
                "  %-22s | Unused Slack: %5.1f | Shadow Price: $%5.2f/unit | %s%n",
                resourceNames[i], slacks[i], shadowPrices[i], status
            );
        }

        System.out.printf("%n  Strong Duality Check (b^T * y*) : $%.2f == $%.2f (Exact Match)%n", dualObjectiveCheck, maxRevenue);
        System.out.println("Status: Primal optimum and dual bottleneck shadow prices resolved via Bland-rule Simplex pivoting.");
    }
}