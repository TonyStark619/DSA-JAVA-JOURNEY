import java.util.*;

public class NetworkRouting {
    // Defines a network connection with latency (weight)
    static class Edge {
        int targetNode;
        int weight;

        Edge(int targetNode, int weight) {
            this.targetNode = targetNode;
            this.weight = weight;
        }
    }

    // O(E log V) Time - High-Speed Priority Routing
    public void dijkstra(List<List<Edge>> graph, int source) {
        int vertices = graph.size();
        int[] distances = new int[vertices];
        
        // Assume all paths take infinite time initially
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[source] = 0; // The distance to the starting node is 0

        // Priority Queue to always expand the mathematically fastest available path first
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        pq.add(new Edge(source, 0));

        while (!pq.isEmpty()) {
            Edge current = pq.poll();
            int currentNode = current.targetNode;

            // Inspect all connected servers/nodes
            for (Edge neighbor : graph.get(currentNode)) {
                int newDistance = distances[currentNode] + neighbor.weight;
                
                // If we found a faster route, update the ledger and queue it up
                if (newDistance < distances[neighbor.targetNode]) {
                    distances[neighbor.targetNode] = newDistance;
                    pq.add(new Edge(neighbor.targetNode, newDistance));
                }
            }
        }

        System.out.println("--- Booting O(E log V) Routing Protocol ---");
        for (int i = 0; i < vertices; i++) {
            System.out.println("Minimum latency to Node " + i + ": " + distances[i] + "ms");
        }
    }

    public static void main(String[] args) {
        int vertices = 5;
        List<List<Edge>> network = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            network.add(new ArrayList<>());
        }

        // Constructing a weighted network (Source, Destination, Latency)
        network.get(0).add(new Edge(1, 2));
        network.get(0).add(new Edge(4, 8));
        network.get(1).add(new Edge(2, 3));
        network.get(1).add(new Edge(4, 2));
        network.get(2).add(new Edge(3, 1));
        network.get(3).add(new Edge(4, 1));

        NetworkRouting engine = new NetworkRouting();
        System.out.println("Initiating Pathfinding Diagnostics from Node 0...");
        engine.dijkstra(network, 0);
        
        System.out.println("\nStatus: Optimal network pathways successfully calculated.");
    }
}