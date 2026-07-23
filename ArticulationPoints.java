import java.util.*;

public class ArticulationPoints {
    private int timer = 0;

    // O(V + E) Time, O(V) Space - Critical Server Identification
    public void findCriticalServers(int vertices, List<List<Integer>> connections) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < vertices; i++) graph.add(new ArrayList<>());
        
        for (List<Integer> edge : connections) {
            graph.get(edge.get(0)).add(edge.get(1));
            graph.get(edge.get(1)).add(edge.get(0));
        }

        int[] discoveryTime = new int[vertices];
        int[] lowestReachable = new int[vertices];
        boolean[] visited = new boolean[vertices];
        
        // We use a boolean array because a server might trigger the condition multiple times, 
        // but we only want to list it once.
        boolean[] isArticulationPoint = new boolean[vertices];

        // Assuming a fully connected network, start DFS from Node 0
        dfs(0, -1, visited, discoveryTime, lowestReachable, graph, isArticulationPoint);

        System.out.println("--- Booting O(V+E) Server Vulnerability Scanner ---");
        System.out.println("Scanning topology for Single Point of Failure (SPOF) Routers...");
        
        for (int i = 0; i < vertices; i++) {
            if (isArticulationPoint[i]) {
                System.out.println("CRITICAL VULNERABILITY: Server " + i + " is an Articulation Point.");
            }
        }
    }

    private void dfs(int current, int parent, boolean[] visited, int[] disc, int[] low, 
                     List<List<Integer>> graph, boolean[] isArticulationPoint) {
        
        visited[current] = true;
        disc[current] = low[current] = ++timer;
        int independentChildren = 0;

        for (int neighbor : graph.get(current)) {
            if (neighbor == parent) continue;

            if (!visited[neighbor]) {
                independentChildren++;
                dfs(neighbor, current, visited, disc, low, graph, isArticulationPoint);
                
                low[current] = Math.min(low[current], low[neighbor]);

                // Condition 1: If we are NOT the starting root, and the neighbor has no back-route
                if (parent != -1 && low[neighbor] >= disc[current]) {
                    isArticulationPoint[current] = true;
                }
            } else {
                // Back-edge found: update the lowest reachable time
                low[current] = Math.min(low[current], disc[neighbor]);
            }
        }

        // Condition 2: If we ARE the starting root, and we have more than 1 disconnected branch
        if (parent == -1 && independentChildren > 1) {
            isArticulationPoint[current] = true;
        }
    }

    public static void main(String[] args) {
        ArticulationPoints scanner = new ArticulationPoints();
        int servers = 5;
        List<List<Integer>> cables = new ArrayList<>();
        
        // Cluster 1 (Servers 0, 1, 2)
        cables.add(Arrays.asList(0, 1));
        cables.add(Arrays.asList(1, 2));
        cables.add(Arrays.asList(2, 0));
        
        // The bottleneck connection
        cables.add(Arrays.asList(0, 3)); 
        
        // Cluster 2 (Servers 3, 4)
        cables.add(Arrays.asList(3, 4));

        // In this topology, if Server 0 dies, Cluster 1 is broken and Cluster 2 is stranded.
        // If Server 3 dies, Server 4 is stranded.
        scanner.findCriticalServers(servers, cables);
        System.out.println("Status: Articulation Point mapping complete.");
    }
}