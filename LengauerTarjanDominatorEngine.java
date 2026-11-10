import java.util.*;

public class LengauerTarjanDominatorEngine {

    private final int n;
    private final List<List<Integer>> forwardGraph;
    private final List<List<Integer>> reverseGraph;
    private final List<List<Integer>> semiDomBuckets;

    // DFS Timestamp Mapping
    private int timer = 0;
    private final int[] dfn;        // Node -> DFS discovery timestamp (1-indexed)
    private final int[] vertexAt;   // DFS timestamp -> Original Node ID
    private final int[] parent;     // Parent in the DFS spanning tree

    // Lengauer-Tarjan Core Arrays
    private final int[] sdom;       // Semi-dominator of node u
    private final int[] idom;       // Immediate dominator of node u

    // Path-Compressed Link-Eval Disjoint Forest
    private final int[] dsuParent;
    private final int[] minSdomNode; // Tracks node with minimum sdom[v] on ancestor chain

    public LengauerTarjanDominatorEngine(int vertices) {
        this.n = vertices;
        this.forwardGraph = new ArrayList<>(n);
        this.reverseGraph = new ArrayList<>(n);
        this.semiDomBuckets = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            forwardGraph.add(new ArrayList<>());
            reverseGraph.add(new ArrayList<>());
            semiDomBuckets.add(new ArrayList<>());
        }

        this.dfn = new int[n];
        this.vertexAt = new int[n + 1];
        this.parent = new int[n];
        this.sdom = new int[n];
        this.idom = new int[n];
        this.dsuParent = new int[n];
        this.minSdomNode = new int[n];

        Arrays.fill(idom, -1);
        for (int i = 0; i < n; i++) {
            sdom[i] = i;
            dsuParent[i] = i;
            minSdomNode[i] = i;
        }
    }

    public void addDirectedEdge(int u, int v) {
        forwardGraph.get(u).add(v);
        reverseGraph.get(v).add(u);
    }

    // Step 1: Establish DFS Spanning Tree and Discovery Order
    private void executeDfsNumbering(int u) {
        dfn[u] = ++timer;
        vertexAt[timer] = u;

        for (int v : forwardGraph.get(u)) {
            if (dfn[v] == 0) {
                parent[v] = u;
                executeDfsNumbering(v);
            }
        }
    }

    // Path-Compressed Eval: Returns the ancestor of 'u' with the minimum sdom timestamp
    private int eval(int u) {
        if (dsuParent[u] == u) {
            return u;
        }
        compressPath(u);
        return minSdomNode[u];
    }

    private void compressPath(int u) {
        int p = dsuParent[u];
        if (dsuParent[p] != p) {
            compressPath(p);
            if (dfn[sdom[minSdomNode[p]]] < dfn[sdom[minSdomNode[u]]]) {
                minSdomNode[u] = minSdomNode[p];
            }
            dsuParent[u] = dsuParent[p];
        }
    }

    // O((V + E) log V) - Synthesizes the Immediate Dominator (idom) for all reachable nodes
    public int[] buildDominatorTree(int entryRoot) {
        System.out.println("  [System] Executing Lengauer-Tarjan Semi-Dominator & Link-Eval Sweep...");
        executeDfsNumbering(entryRoot);

        // Process vertices in reverse DFS discovery order (from N down to 2)
        for (int i = timer; i >= 2; i--) {
            int u = vertexAt[i];

            // 1. Compute Semi-Dominator sdom[u] across all incoming edges (v -> u)
            for (int v : reverseGraph.get(u)) {
                if (dfn[v] == 0) continue; // Ignore unreachable nodes
                int candidate;
                if (dfn[v] < dfn[u]) {
                    candidate = v;
                } else {
                    candidate = sdom[eval(v)];
                }
                if (dfn[candidate] < dfn[sdom[u]]) {
                    sdom[u] = candidate;
                }
            }

            // Place 'u' into the bucket of its semi-dominator and link 'u' to its DFS parent
            semiDomBuckets.get(sdom[u]).add(u);
            dsuParent[u] = parent[u];

            // 2. Implicitly resolve idom for all nodes in parent[u]'s bucket
            int p = parent[u];
            for (int w : semiDomBuckets.get(p)) {
                int bestNode = eval(w);
                if (sdom[bestNode] == sdom[w]) {
                    idom[w] = p;
                } else {
                    idom[w] = bestNode; // Defer exact resolution to Pass 3
                }
            }
            semiDomBuckets.get(p).clear();
        }

        // Step 3: Forward pass in increasing DFS order to finalize deferred immediate dominators
        for (int i = 2; i <= timer; i++) {
            int u = vertexAt[i];
            if (idom[u] != sdom[u]) {
                idom[u] = idom[idom[u]];
            }
        }
        idom[entryRoot] = entryRoot;
        return idom;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Lengauer-Tarjan Dominator Tree (CFG / Heap Retention) Engine ---");

        // Directed Control-Flow / Gateway Graph (Root = 0)
        // Node 0 -> 1
        // Node 1 branches to 2 and 3; both 2 and 3 converge into 4; 1 also has a direct bypass to 5 via 4
        LengauerTarjanDominatorEngine engine = new LengauerTarjanDominatorEngine(6);
        engine.addDirectedEdge(0, 1);
        engine.addDirectedEdge(1, 2);
        engine.addDirectedEdge(1, 3);
        engine.addDirectedEdge(2, 4);
        engine.addDirectedEdge(3, 4);
        engine.addDirectedEdge(4, 5);

        int[] immediateDominators = engine.buildDominatorTree(0);

        System.out.println("\n--- Immediate Dominator (idom) Hierarchy ---");
        for (int node = 0; node < 6; node++) {
            System.out.println("  Node " + node + " is immediately dominated by -> Gate Node " + immediateDominators[node]);
        }
        // Notice: Even though Node 4 is only reachable via 2 and 3, neither 2 nor 3 dominates 4 alone!
        // Node 1 is the Immediate Dominator of 4 because paths can bypass 2 (via 3) or bypass 3 (via 2).

        System.out.println("\nStatus: O(V*(V+E)) reachability bottleneck collapsed to O((V+E) log V) via Semi-Dominator Link-Eval compression.");
    }
}