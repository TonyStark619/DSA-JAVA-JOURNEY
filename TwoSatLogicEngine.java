import java.util.*;

public class TwoSatLogicEngine {
    private final int N;
    private final List<List<Integer>> implicationGraph;
    
    // Tarjan's SCC State Variables
    private int timer = 0;
    private int sccCount = 0;
    private final int[] discoveryTime;
    private final int[] lowestReachable;
    private final int[] sccId;
    private final boolean[] onStack;
    private final Stack<Integer> stack;

    public TwoSatLogicEngine(int variables) {
        this.N = variables;
        // Size is 2 * N (to hold both the variable and its negation)
        implicationGraph = new ArrayList<>(2 * N);
        for (int i = 0; i < 2 * N; i++) implicationGraph.add(new ArrayList<>());
        
        discoveryTime = new int[2 * N];
        lowestReachable = new int[2 * N];
        sccId = new int[2 * N];
        onStack = new boolean[2 * N];
        stack = new Stack<>();
        Arrays.fill(discoveryTime, -1);
    }

    // Mathematical Mapping: X is mapped to 2X. NOT X is mapped to 2X + 1.
    private int getLiteralNode(int variable, boolean isNegated) {
        return 2 * variable + (isNegated ? 1 : 0);
    }
    
    private int getNegatedLiteralNode(int literalNode) {
        return literalNode ^ 1; // XOR with 1 elegantly flips between 2X and 2X+1
    }

    // O(1) - Adds a constraint: (varA OR varB)
    public void addClause(int varA, boolean negA, int varB, boolean negB) {
        int u = getLiteralNode(varA, negA);
        int v = getLiteralNode(varB, negB);
        
        // (A OR B) translates to (!A -> B) AND (!B -> A)
        implicationGraph.get(getNegatedLiteralNode(u)).add(v);
        implicationGraph.get(getNegatedLiteralNode(v)).add(u);
    }

    // O(V + E) - Tarjan's Algorithm to isolate Strongly Connected Components
    private void executeTarjanDFS(int u) {
        discoveryTime[u] = lowestReachable[u] = ++timer;
        stack.push(u);
        onStack[u] = true;

        for (int v : implicationGraph.get(u)) {
            if (discoveryTime[v] == -1) {
                executeTarjanDFS(v);
                lowestReachable[u] = Math.min(lowestReachable[u], lowestReachable[v]);
            } else if (onStack[v]) {
                lowestReachable[u] = Math.min(lowestReachable[u], discoveryTime[v]);
            }
        }

        // If 'u' is the absolute root of an SCC
        if (lowestReachable[u] == discoveryTime[u]) {
            while (true) {
                int v = stack.pop();
                onStack[v] = false;
                sccId[v] = sccCount;
                if (u == v) break;
            }
            sccCount++;
        }
    }

    // O(N) - Core Resolution Engine
    public boolean solveSystem() {
        System.out.println("  [System] Executing Tarjan's SCC Sweep on Implication Graph...");
        
        for (int i = 0; i < 2 * N; i++) {
            if (discoveryTime[i] == -1) {
                executeTarjanDFS(i);
            }
        }

        System.out.println("  [System] Verifying Boolean Satisfiability...");
        // If variable X and NOT X are in the exact same SCC, a logical paradox exists
        for (int i = 0; i < N; i++) {
            if (sccId[2 * i] == sccId[2 * i + 1]) {
                return false; // Mathematical contradiction isolated
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting 2-SAT Boolean Logic Engine ---");
        
        // 3 Variables: V0, V1, V2
        TwoSatLogicEngine logicEngine = new TwoSatLogicEngine(3);
        
        // Clause 1: (V0 OR V1)
        logicEngine.addClause(0, false, 1, false);
        // Clause 2: (NOT V0 OR V2)
        logicEngine.addClause(0, true, 2, false);
        // Clause 3: (NOT V1 OR NOT V2)
        logicEngine.addClause(1, true, 2, true);
        
        System.out.println("\n[Network] Boolean Constraints Loaded.");
        boolean isSolvable = logicEngine.solveSystem();
        
        System.out.println("\nCRITICAL RESULT: System is Satisfiable? -> " + isSolvable);
        System.out.println("Status: O(2^N) backtracking bottleneck mathematically eliminated via linear graph implications.");
    }
}