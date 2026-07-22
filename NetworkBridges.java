import java.util.*;

public class NetworkBridges {
    private int timer = 0;

    // O(V + E) Time, O(V) Space - Single Pass Vulnerability Scanner
    public List<List<Integer>> criticalConnections(int vertices, List<List<Integer>> connections) {
        // 1. Build the Adjacency List
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < vertices; i++) graph.add(new ArrayList<>());
        
        for (List<Integer> edge : connections) {
            graph.get(edge.get(0)).add(edge.get(1));
            graph.get(edge.get(1)).add(edge.get(0)); // Undirected network
        }

        int[] discoveryTime = new int[vertices];
        int[] lowestReachable = new int[vertices];
        boolean[] visited = new boolean[vertices];
        List<List<Integer>> bridges = new ArrayList<>();

        // 2. Initiate DFS from Node 0 (Assuming a connected graph)
        dfs(0, -1, visited, discoveryTime, lowestReachable, graph, bridges);
        return bridges;
    }

    private void dfs(int current, int parent, boolean[] visited, int[] disc, int[] low, 
                     List<List<Integer>> graph, List<List<Integer>> bridges) {
        
        visited[current] = true;
        // Stamp the current time. Initially, the lowest reachable is just its own time.
        disc[current] = low[current] = ++timer;

        for (int neighbor : graph.get(current)) {
            // Optimization: Don't go immediately back to the node we just came from
            if (neighbor == parent) continue;

            if (!visited[neighbor]) {
                // Plunge deeper into the network
                dfs(neighbor, current, visited, disc, low, graph, bridges);
                
                // As the recursive call returns, update the current node's lowest reachable time
                low[current] = Math.min(low[current], low[neighbor]);

                // THE BRIDGE CONDITION: 
                // If the lowest time reachable by the neighbor is STRICTLY GREATER than 
                // when we discovered the current node, the neighbor has no back-route. 
                // This cable is the only way in or out.
                if (low[neighbor] > disc[current]) {
                    bridges.add(Arrays.asList(current, neighbor));
                }
            } else {
                // We hit an already visited node (a Back-Edge). We found a loop!
                // Update the lowest reachable time.
                low[current] = Math.min(low[current], disc[neighbor]);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Network Vulnerability Scanner ---");
        NetworkBridges scanner = new NetworkBridges();
        
        int servers = 4;
        List<List<Integer>> cables = new ArrayList<>();
        // A loop between 0, 1, and 2. 
        cables.add(Arrays.asList(0, 1));
        cables.add(Arrays.asList(1, 2));
        cables.add(Arrays.asList(2, 0));
        // A single cable extending out to server 3
        cables.add(Arrays.asList(1, 3)); 

        System.out.println("Scanning Infrastructure Topology for Single Points of Failure...");
        List<List<Integer>> criticalCables = scanner.criticalConnections(servers, cables);

        for (List<Integer> bridge : criticalCables) {
            System.out.println("CRITICAL FAILURE POINT DETECTED: Cable between Server " + bridge.get(0) + " and Server " + bridge.get(1));
        }
        System.out.println("Status: Tarjan's O(V+E) Bridge diagnostic complete.");
    }
}