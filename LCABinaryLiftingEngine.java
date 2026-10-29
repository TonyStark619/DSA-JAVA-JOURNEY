import java.util.*;

public class LCABinaryLiftingEngine {

    private final int MAX_NODES;
    private final int LOG;
    private final int[] depth;
    private final int[][] up; // The Sparse Matrix [node][jump_power]
    private final List<List<Integer>> tree;

    public LCABinaryLiftingEngine(int n) {
        this.MAX_NODES = n;
        // Calculate maximum power of 2 needed for N nodes (e.g., log2(100000) ~ 17)
        this.LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
        this.tree = new ArrayList<>();
        for (int i = 0; i < n; i++) tree.add(new ArrayList<>());
        this.depth = new int[n];
        this.up = new int[n][LOG];
    }

    public void addUndirectedEdge(int u, int v) {
        tree.get(u).add(v);
        tree.get(v).add(u);
    }

    // O(N log N) - DFS sweep to pre-calculate exponential jump pointers
    public void executePreprocessing(int node, int parent, int currentDepth) {
        depth[node] = currentDepth;
        up[node][0] = parent; // The 2^0 (1st) parent is just the direct parent

        // Dynamic Programming: The 2^i parent is the 2^(i-1) parent OF the 2^(i-1) parent
        for (int i = 1; i < LOG; i++) {
            if (up[node][i - 1] != -1) {
                up[node][i] = up[up[node][i - 1]][i - 1];
            } else {
                up[node][i] = -1;
            }
        }

        for (int neighbor : tree.get(node)) {
            if (neighbor != parent) {
                executePreprocessing(neighbor, node, currentDepth + 1);
            }
        }
    }

    // O(log N) - The Core Logarithmic Routing Engine
    public int findLowestCommonAncestor(int u, int v) {
        System.out.println("  [Network] Routing connection between Node " + u + " and Node " + v + "...");
        
        // Step 1: Force U to be the deeper node
        if (depth[u] < depth[v]) {
            int temp = u; u = v; v = temp;
        }

        // Step 2: Mathematically jump U up until it is on the exact same depth as V
        for (int i = LOG - 1; i >= 0; i--) {
            if (up[u][i] != -1 && depth[up[u][i]] >= depth[v]) {
                u = up[u][i];
            }
        }

        // If U and V are now the same node, we isolated the intersection
        if (u == v) return u;

        // Step 3: Jump both U and V upwards together, stopping just below the LCA
        for (int i = LOG - 1; i >= 0; i--) {
            if (up[u][i] != -1 && up[v][i] != -1 && up[u][i] != up[v][i]) {
                u = up[u][i];
                v = up[v][i];
            }
        }

        // The LCA is the direct parent of where they stopped
        return up[u][0];
    }

    public void initializeSystem(int root) {
        System.out.println("  [System] Forging Sparse Matrix for exponential routing...");
        for (int[] row : up) Arrays.fill(row, -1);
        executePreprocessing(root, -1, 0);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting LCA Binary Lifting Architecture ---");
        
        // A complex tree network
        LCABinaryLiftingEngine network = new LCABinaryLiftingEngine(9);
        network.addUndirectedEdge(0, 1);
        network.addUndirectedEdge(0, 2);
        network.addUndirectedEdge(1, 3);
        network.addUndirectedEdge(1, 4);
        network.addUndirectedEdge(2, 5);
        network.addUndirectedEdge(4, 6);
        network.addUndirectedEdge(4, 7);
        network.addUndirectedEdge(5, 8);

        network.initializeSystem(0);

        System.out.println("\n--- Executing O(log N) Proximity Sweeps ---");
        int lca1 = network.findLowestCommonAncestor(6, 7); // Expected: 4
        System.out.println("    -> Junction Isolated: Node " + lca1);
        
        int lca2 = network.findLowestCommonAncestor(6, 3); // Expected: 1
        System.out.println("    -> Junction Isolated: Node " + lca2);
        
        int lca3 = network.findLowestCommonAncestor(7, 8); // Expected: 0 (Root)
        System.out.println("    -> Junction Isolated: Node " + lca3);
        
        System.out.println("\nStatus: Linear node crawling mathematically eliminated via power-of-two matrix jumps.");
    }
}