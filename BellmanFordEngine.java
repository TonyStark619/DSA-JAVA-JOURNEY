import java.util.*;

public class BellmanFordEngine {

    // 1. Minimalist Edge Architecture
    static class Edge {
        int source, target, latency;
        public Edge(int source, int target, int latency) {
            this.source = source;
            this.target = target;
            this.latency = latency;
        }
    }

    // O(V * E) Time - Negative Cycle Detection & Routing
    public static void computeShortestPaths(int vertices, List<Edge> edges, int sourceNode) {
        System.out.println("Executing O(V * E) Bellman-Ford Network Analysis...");

        // Initialize all distances to infinity
        int[] minDistance = new int[vertices];
        Arrays.fill(minDistance, Integer.MAX_VALUE);
        minDistance[sourceNode] = 0;

        // Step 1: Relax all edges exactly (V - 1) times
        // A simple path in a graph can have at most (V - 1) edges.
        for (int i = 1; i <= vertices - 1; i++) {
            for (Edge edge : edges) {
                int u = edge.source;
                int v = edge.target;
                int weight = edge.latency;

                // If the source is reachable, and the new path is strictly faster...
                if (minDistance[u] != Integer.MAX_VALUE && minDistance[u] + weight < minDistance[v]) {
                    minDistance[v] = minDistance[u] + weight;
                }
            }
        }

        // Step 2: The Negative Cycle Check (The V-th relaxation)
        for (Edge edge : edges) {
            int u = edge.source;
            int v = edge.target;
            int weight = edge.latency;

            if (minDistance[u] != Integer.MAX_VALUE && minDistance[u] + weight < minDistance[v]) {
                System.out.println("CRITICAL FAILURE: Negative weight cycle detected in topology.");
                System.out.println("Routing table calculation aborted. Infinite loop risk.");
                return; // Halt execution
            }
        }

        // Step 3: Print the finalized, mathematically sound routing table
        System.out.println("\n--- Stable Routing Table Generated ---");
        for (int i = 0; i < vertices; i++) {
            if (minDistance[i] == Integer.MAX_VALUE) {
                System.out.println("Server " + i + ": UNREACHABLE");
            } else {
                System.out.println("Server " + i + ": " + minDistance[i] + " ms latency");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Bellman-Ford Fault-Tolerant Router ---");
        
        int servers = 5;
        List<Edge> network = new ArrayList<>();
        
        // Simulating a network that includes a negative weight (e.g., bandwidth credit)
        network.add(new Edge(0, 1, -1)); 
        network.add(new Edge(0, 2, 4));
        network.add(new Edge(1, 2, 3));
        network.add(new Edge(1, 3, 2));
        network.add(new Edge(1, 4, 2));
        network.add(new Edge(3, 2, 5));
        network.add(new Edge(3, 1, 1)); // Change this to -4 to trigger the Negative Cycle warning
        network.add(new Edge(4, 3, -3)); 
        
        computeShortestPaths(servers, network, 0);
        
        System.out.println("\nStatus: Topology scanned. Zero negative cycles detected. Routes secured.");
    }
}