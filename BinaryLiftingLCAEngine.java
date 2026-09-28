import java.util.*;

public class BinaryLiftingLCAEngine {
    private int maxNodes;
    private int maxLog;
    private List<List<Integer>> tree;
    
    // up[i][j] stores the (2^j)-th ancestor of node i
    private int[][] up;
    private int[] depth;

    public BinaryLiftingLCAEngine(int nodes) {
        this.maxNodes = nodes;
        // Calculate the maximum power of 2 needed for the given number of nodes
        this.maxLog = (int) (Math.log(nodes) / Math.log(2)) + 2; 
        
        tree = new ArrayList<>();
        for (int i = 0; i < nodes; i++) {
            tree.add(new ArrayList<>());
        }
        
        up = new int[nodes][maxLog];
        depth = new int[nodes];
    }

    public void addEdge(int u, int v) {
        tree.get(u).add(v);
        tree.get(v).add(u);
    }

    // O(N log N) - Precompute the depth and the ancestor matrix using DFS
    public void buildArchitecture(int root) {
        System.out.println("  [System] Executing DFS to build 2^i Ancestor Matrix...");
        dfs(root, root, 0);
    }

    private void dfs(int current, int parent, int currentDepth) {
        depth[current] = currentDepth;
        
        // The 2^0 (1st) ancestor is just the direct parent
        up[current][0] = parent;
        
        // Dynamic Programming: The 2^j ancestor is the 2^(j-1) ancestor OF the 2^(j-1) ancestor
        for (int j = 1; j < maxLog; j++) {
            up[current][j] = up[up[current][j - 1]][j - 1];
        }

        // Traverse children
        for (int child : tree.get(current)) {
            if (child != parent) {
                dfs(child, current, currentDepth + 1);
            }
        }
    }

    // O(log N) - The Core Time-Bending Query
    public int getLCA(int u, int v) {
        // Step 1: Ensure u is the deeper node to simplify logic
        if (depth[u] < depth[v]) {
            int temp = u; u = v; v = temp;
        }

        // Step 2: "Lift" u up until it is at the exact same depth as v
        for (int j = maxLog - 1; j >= 0; j--) {
            // If jumping 2^j steps doesn't overshoot v's depth, take the jump
            if (depth[u] - (1 << j) >= depth[v]) {
                u = up[u][j];
            }
        }

        // If they are now the same node, v was a direct ancestor of u
        if (u == v) return u;

        // Step 3: Lift BOTH nodes up simultaneously using the largest possible leaps
        for (int j = maxLog - 1; j >= 0; j--) {
            if (up[u][j] != up[v][j]) {
                u = up[u][j];
                v = up[v][j];
            }
        }

        // We stopped exactly one level below the LCA
        return up[u][0];
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Binary Lifting LCA Architecture ---");
        
        // Tree: 0 is root. 0->1, 0->2. 1->3, 1->4. 2->5. 4->6.
        BinaryLiftingLCAEngine engine = new BinaryLiftingLCAEngine(7);
        engine.addEdge(0, 1); engine.addEdge(0, 2);
        engine.addEdge(1, 3); engine.addEdge(1, 4);
        engine.addEdge(2, 5); engine.addEdge(4, 6);
        
        engine.buildArchitecture(0);
        
        System.out.println("\n--- Querying Live Hierarchy ---");
        System.out.println("Lowest Common Ancestor of 6 and 3: Node " + engine.getLCA(6, 3)); // Expected: 1
        System.out.println("Lowest Common Ancestor of 6 and 5: Node " + engine.getLCA(6, 5)); // Expected: 0
        System.out.println("Lowest Common Ancestor of 4 and 1: Node " + engine.getLCA(4, 1)); // Expected: 1
        
        System.out.println("\nStatus: O(N) traversal bottlenecks completely bypassed via exponential geometric lifting.");
    }
}