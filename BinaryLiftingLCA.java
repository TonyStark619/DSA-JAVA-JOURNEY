import java.util.*;

public class BinaryLiftingLCA {
    private int[] depth;
    private int[][] up;
    private int LOG;
    private List<List<Integer>> tree;

    // FIX: Updated parameter to List<int[]> to match the array iteration
    public BinaryLiftingLCA(int n, List<int[]> edges, int root) {
        tree = new ArrayList<>();
        for (int i = 0; i < n; i++) tree.add(new ArrayList<>());
        
        for (int[] edge : edges) {
            tree.get(edge[0]).add(edge[1]);
            tree.get(edge[1]).add(edge[0]);
        }

        // Calculate the maximum possible power of 2 needed for this tree size
        LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
        up = new int[n][LOG];
        depth = new int[n];

        // Run a single DFS to map depths and the 1st (2^0) parent for every node
        dfs(root, root, 0);

        System.out.println("Executing DP to populate Binary Lifting Matrix...");
        // Dynamic Programming: The 2^j ancestor is the 2^(j-1) ancestor of your 2^(j-1) ancestor
        for (int j = 1; j < LOG; j++) {
            for (int i = 0; i < n; i++) {
                up[i][j] = up[up[i][j - 1]][j - 1];
            }
        }
    }

    private void dfs(int node, int parent, int d) {
        up[node][0] = parent;
        depth[node] = d;
        for (int neighbor : tree.get(node)) {
            if (neighbor != parent) {
                dfs(neighbor, node, d + 1);
            }
        }
    }

    // O(log N) Time - High-Speed Lowest Common Ancestor Query
    public int getLCA(int u, int v) {
        // Step 1: Force node 'u' to be the deeper node
        if (depth[u] < depth[v]) {
            int temp = u; u = v; v = temp;
        }

        // Step 2: Jump 'u' up in powers of 2 until it is at the exact same depth as 'v'
        int depthDiff = depth[u] - depth[v];
        for (int j = LOG - 1; j >= 0; j--) {
            if ((depthDiff & (1 << j)) != 0) {
                u = up[u][j];
            }
        }

        // If they are now the same node, we found the LCA
        if (u == v) return u;

        // Step 3: Jump them BOTH up simultaneously in powers of 2, 
        // stopping right before they hit the common ancestor.
        for (int j = LOG - 1; j >= 0; j--) {
            if (up[u][j] != up[v][j]) {
                u = up[u][j];
                v = up[v][j];
            }
        }

        // The LCA is the direct parent of where they stopped
        return up[u][0];
    }

    public static void main(String[] args) {
        System.out.println("--- Booting O(log N) Binary Lifting Architecture ---");
        
        int n = 7;
        List<int[]> edges = Arrays.asList(
            new int[]{0, 1}, new int[]{0, 2}, 
            new int[]{1, 3}, new int[]{1, 4}, 
            new int[]{2, 5}, new int[]{2, 6}
        );

        // FIX: Clean instantiation without forcing a generic type cast
        BinaryLiftingLCA engine = new BinaryLiftingLCA(n, edges, 0);
        
        int node1 = 3, node2 = 4;
        System.out.println("CRITICAL INSIGHT: Lowest Common Ancestor of " + node1 + " and " + node2 + " is Node " + engine.getLCA(node1, node2));
        
        node1 = 3; node2 = 6;
        System.out.println("CRITICAL INSIGHT: Lowest Common Ancestor of " + node1 + " and " + node2 + " is Node " + engine.getLCA(node1, node2));
        
        System.out.println("Status: O(log N) logarithmic jumps executed flawlessly.");
    }
}