import java.util.*;

public class NetworkBridgesEngine {
    private int timer = 1;

    // O(V + E) Time - Identifying Single Points of Failure
    public List<List<Integer>> findCriticalConnections(int n, List<List<Integer>> connections) {
        System.out.println("Executing O(V+E) Critical Pathway Analysis...");
        
        // Step 1: Build the Adjacency List (Graph)
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
        for (List<Integer> conn : connections) {
            graph.get(conn.get(0)).add(conn.get(1));
            graph.get(conn.get(1)).add(conn.get(0));
        }

        int[] discovery = new int[n];
        int[] low = new int[n];
        boolean[] visited = new boolean[n];
        List<List<Integer>> bridges = new ArrayList<>();

        // Step 2: Launch the DFS
        // We assume a fully connected network for this simulation, starting at Node 0
        dfs(0, -1, visited, discovery, low, graph, bridges);

        return bridges;
    }

    private void dfs(int current, int parent, boolean[] visited, int[] discovery, int[] low, 
                     List<List<Integer>> graph, List<List<Integer>> bridges) {
        visited[current] = true;
        discovery[current] = low[current] = timer++;

        for (int neighbor : graph.get(current)) {
            if (neighbor == parent) continue; // Do not go immediately backward on the same cable

            if (!visited[neighbor]) {
                dfs(neighbor, current, visited, discovery, low, graph, bridges);
                
                // When the recursive call returns, update the lowest reachable node
                low[current] = Math.min(low[current], low[neighbor]);

                // THE MAGIC: Is this a bridge?
                // If the neighbor's lowest reachable node is strictly AFTER the current node was discovered,
                // it means the neighbor has no back-edge to an earlier part of the network.
                if (low[neighbor] > discovery[current]) {
                    bridges.add(Arrays.asList(current, neighbor));
                }
            } else {
                // Back-edge detected: update the low time, but this edge is NOT a bridge.
                low[current] = Math.min(low[current], discovery[neighbor]);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Infrastructure Vulnerability Scanner ---");
        
        int servers = 4;
        List<List<Integer>> cables = new ArrayList<>();
        // Server 0, 1, 2 form a redundant triangle. Server 3 is hanging off Server 1.
        cables.add(Arrays.asList(0, 1));
        cables.add(Arrays.asList(1, 2));
        cables.add(Arrays.asList(2, 0));
        cables.add(Arrays.asList(1, 3)); 

        NetworkBridgesEngine engine = new NetworkBridgesEngine();
        List<List<Integer>> vulnerabilities = engine.findCriticalConnections(servers, cables);
        
        System.out.println("\n--- Critical Connections (Bridges) Detected ---");
        for (List<Integer> bridge : vulnerabilities) {
            System.out.println("CRITICAL VULNERABILITY: Cable connecting Server " + bridge.get(0) + " <---> Server " + bridge.get(1));
        }
        
        System.out.println("Status: Network sweep complete. Single points of failure isolated.");
    }
}