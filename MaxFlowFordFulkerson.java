import java.util.*;

public class MaxFlowFordFulkerson {
    
    // Breadth-First Search to find if a path exists from Source to Sink in the Residual Graph
    private static boolean bfs(int[][] residualGraph, int source, int sink, int[] parent) {
        int vertices = residualGraph.length;
        boolean[] visited = new boolean[vertices];
        Queue<Integer> queue = new LinkedList<>();
        
        queue.add(source);
        visited[source] = true;
        parent[source] = -1;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v = 0; v < vertices; v++) {
                // If unvisited and there is available residual capacity
                if (!visited[v] && residualGraph[u][v] > 0) {
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }
        // Returns true if we successfully reached the sink node
        return visited[sink];
    }

    // O(Max_Flow * E) Time - Maximum Network Flow Engine
    public static int calculateMaxFlow(int[][] graph, int source, int sink) {
        int vertices = graph.length;
        int u, v;

        // Create a residual graph to track remaining capacities dynamically
        int[][] residualGraph = new int[vertices][vertices];
        for (i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                residualGraph[i][j] = graph[i][j];
            }
        }

        int[] parent = new int[vertices];
        int maxFlow = 0;

        System.out.println("Executing Edmonds-Karp Residual Flow Analysis...");

        // Augment the flow while there is a path from source to sink
        while (bfs(residualGraph, source, sink, parent)) {
            // Find the maximum flow through the path found by BFS
            int pathFlow = Integer.MAX_VALUE;
            for (v = sink; v != source; v = parent[v]) {
                u = parent[v];
                pathFlow = Math.min(pathFlow, residualGraph[u][v]);
            }

            // Update residual capacities of the edges and reverse edges along the path
            for (v = sink; v != source; v = parent[v]) {
                u = parent[v];
                residualGraph[u][v] -= pathFlow;
                residualGraph[v][u] += pathFlow; // Allow flow reversal
            }

            maxFlow += pathFlow;
        }

        return maxFlow;
    }

    private static int i = 0; // Helper counter for loop scope

    public static void main(String[] args) {
        System.out.println("--- Booting Ford-Fulkerson Max-Flow Architecture ---");
        
        // Simulating a network of 6 servers (0 is Source, 5 is Sink)
        int[][] capacityMatrix = {
            {0, 16, 13, 0, 0, 0},
            {0, 0, 10, 12, 0, 0},
            {0, 4, 0, 0, 14, 0},
            {0, 0, 9, 0, 0, 20},
            {0, 0, 0, 7, 0, 4},
            {0, 0, 0, 0, 0, 0}
        };

        int sourceNode = 0;
        int sinkNode = 5;

        int maxBandwidth = calculateMaxFlow(capacityMatrix, sourceNode, sinkNode);

        System.out.println("\nCRITICAL INSIGHT: Maximum Network Flow capacity is " + maxBandwidth + " Gbps");
        System.out.println("Status: Residual graph saturated. Optimal throughput computed.");
    }
}