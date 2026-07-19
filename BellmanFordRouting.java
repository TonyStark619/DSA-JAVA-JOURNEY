import java.util.*;

public class BellmanFordRouting {
    // Defines a directed network cable with a weight (which can be negative)
    static class Edge {
        int source, destination, weight;
        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    // O(V * E) Time - Negative Weight Routing and Cycle Detection
    public static void executeBellmanFord(int vertices, List<Edge> edges, int sourceNode) {
        int[] distances = new int[vertices];
        // Initialize all distances to Infinity
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[sourceNode] = 0;

        System.out.println("Executing Bellman-Ford Path Relaxation...");

        // Phase 1: Relax all edges strictly (V - 1) times
        // The shortest path in a graph with V vertices can never have more than V-1 edges
        for (int i = 1; i < vertices; i++) {
            for (Edge edge : edges) {
                if (distances[edge.source] != Integer.MAX_VALUE && 
                    distances[edge.source] + edge.weight < distances[edge.destination]) {
                    distances[edge.destination] = distances[edge.source] + edge.weight;
                }
            }
        }

        // Phase 2: The Negative Cycle Diagnostic Check
        // If we can STILL find a shorter path after (V-1) relaxations, an infinite negative loop exists.
        for (Edge edge : edges) {
            if (distances[edge.source] != Integer.MAX_VALUE && 
                distances[edge.source] + edge.weight < distances[edge.destination]) {
                System.out.println("CRITICAL ALERT: Graph contains a negative weight cycle. Routing invalid.");
                return;
            }
        }

        // Output the final optimal routing tables
        System.out.println("\n--- Optimal Routing Tables Computed ---");
        for (int i = 0; i < vertices; i++) {
            System.out.println("Cost to reach Node " + i + ": " + distances[i]);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Financial / Negative Routing Architecture ---");
        int vertices = 5;
        List<Edge> network = new ArrayList<>();

        // Constructing a network with negative weights (Source, Destination, Weight)
        network.add(new Edge(0, 1, -1));
        network.add(new Edge(0, 2, 4));
        network.add(new Edge(1, 2, 3));
        network.add(new Edge(1, 3, 2));
        network.add(new Edge(1, 4, 2));
        network.add(new Edge(3, 2, 5));
        network.add(new Edge(3, 1, 1));
        network.add(new Edge(4, 3, -3)); // Negative weight path

        executeBellmanFord(vertices, network, 0);
        System.out.println("\nStatus: Architecture successfully navigated negative weights without systemic failure.");
    }
}