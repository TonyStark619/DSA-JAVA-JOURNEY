import java.util.*;

public class ArticulationPointsEngine {
    private int timer = 1;

    // O(V + E) Time, O(V) Space - Isolating Critical Nodes
    public void findArticulationPoints(int vertices, List<List<Integer>> graph) {
        System.out.println("Executing O(V+E) Critical Node (Cut Vertex) Analysis...");
        
        boolean[] visited = new boolean[vertices];
        int[] discovery = new int[vertices];
        int[] low = new int[vertices];
        
        // We use a HashSet because multiple branches might flag the same node as critical
        Set<Integer> articulationPoints = new HashSet<>();

        // Assuming a connected graph, starting from Server 0
        dfs(0, -1, visited, discovery, low, graph, articulationPoints);

        System.out.println("\n--- Single Points of Failure Detected ---");
        for (int node : articulationPoints) {
            System.out.println("CRITICAL VULNERABILITY: Server " + node + " is an Articulation Point.");
        }
        if (articulationPoints.isEmpty()) {
            System.out.println("Network is highly fault-tolerant. Zero articulation points found.");
        }
    }

    private void dfs(int u, int parent, boolean[] visited, int[] discovery, int[] low, 
                     List<List<Integer>> graph, Set<Integer> ap) {
        visited[u] = true;
        discovery[u] = low[u] = timer++;
        int children = 0;

        for (int v : graph.get(u)) {
            if (v == parent) continue; // Do not traverse the immediate back-cable

            if (!visited[v]) {
                children++;
                dfs(v, u, visited, discovery, low, graph, ap);
                
                // Update lowest reachable time upon return
                low[u] = Math.min(low[u], low[v]);

                // THE MAGIC: If it's NOT the root, and the neighbor cannot reach a node discovered earlier...
                if (parent != -1 && low[v] >= discovery[u]) {
                    ap.add(u);
                }
            } else {
                // Back-edge detected
                low[u] = Math.min(low[u], discovery[v]);
            }
        }

        // EDGE CASE: If the starting root node has more than one independent DFS child branch,
        // removing the root will permanently split those branches.
        if (parent == -1 && children > 1) {
            ap.add(u);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Server Vulnerability Scanner ---");
        
        int servers = 5;
        List<List<Integer>> network = new ArrayList<>();
        for (int i = 0; i < servers; i++) network.add(new ArrayList<>());

        // Server 0, 1, 2 form a triangle. Server 0 connects to 3. Server 3 connects to 4.
        network.get(0).add(1); network.get(1).add(0);
        network.get(1).add(2); network.get(2).add(1);
        network.get(2).add(0); network.get(0).add(2);
        network.get(0).add(3); network.get(3).add(0);
        network.get(3).add(4); network.get(4).add(3);

        ArticulationPointsEngine engine = new ArticulationPointsEngine();
        engine.findArticulationPoints(servers, network);
        
        System.out.println("\nStatus: Network sweep complete. Structural dependencies verified.");
    }
}