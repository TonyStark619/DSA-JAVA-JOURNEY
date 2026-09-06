import java.util.*;

public class DPTreesEngine {

    // DP Table: dp[node][0] = Not Included, dp[node][1] = Included
    private static int[][] dp;
    private static List<List<Integer>> tree;

    // O(V) Time, O(V) Space - Hierarchical Dynamic Programming
    public static void executeTreeDP(int node, int parent) {
        // Base initialization
        dp[node][0] = 0; // If we don't include this node, we get 0 from it
        dp[node][1] = 1; // If we include this node, we get 1 (itself)

        for (int child : tree.get(node)) {
            // Prevent infinite loops back to the parent in an undirected tree
            if (child == parent) continue;

            // Post-Order Traversal: Solve the children first
            executeTreeDP(child, node);

            // THE MAGIC: State Transitions
            // Case 1: If current node is NOT included, we can either include or exclude the child (pick the max)
            dp[node][0] += Math.max(dp[child][0], dp[child][1]);

            // Case 2: If current node IS included, we CANNOT include the child (must take the 'Not Included' state)
            dp[node][1] += dp[child][0];
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting DP on Trees (Maximum Independent Set) Architecture ---");

        int n = 7; // Total nodes (Employees 0 to 6)
        tree = new ArrayList<>();
        for (int i = 0; i < n; i++) tree.add(new ArrayList<>());
        dp = new int[n][2];

        // Building the Corporate Hierarchy (Undirected Edges)
        // 0 is the CEO. 1 and 2 are VPs. 3,4 report to 1. 5,6 report to 2.
        tree.get(0).add(1); tree.get(1).add(0);
        tree.get(0).add(2); tree.get(2).add(0);
        tree.get(1).add(3); tree.get(3).add(1);
        tree.get(1).add(4); tree.get(4).add(1);
        tree.get(2).add(5); tree.get(5).add(2);
        tree.get(2).add(6); tree.get(6).add(2);

        // Start DFS from the root (CEO - Node 0), with parent as -1
        executeTreeDP(0, -1);

        int maxPartySize = Math.max(dp[0][0], dp[0][1]);
        
        System.out.println("\n--- Optimization Complete ---");
        System.out.println("CRITICAL RESULT: Maximum independent employees that can be invited = " + maxPartySize);
        System.out.println("Status: O(V) hierarchical state resolution successful. Adjacency conflicts mathematically prevented.");
    }
}