public class FloydWarshallRouting {
    // We use a large number to represent Infinity (unconnected paths)
    // We don't use Integer.MAX_VALUE to prevent integer overflow during addition
    final static int INF = 99999; 

    // O(V^3) Time, O(V^2) Space - Dynamic Programming Graph Engine
    public void executeAllPairsShortestPath(int[][] graph, int vertices) {
        int[][] distance = new int[vertices][vertices];

        // Step 1: Initialize the DP Matrix with the original network layout
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                distance[i][j] = graph[i][j];
            }
        }

        // Step 2: The Core DP Engine
        // We systematically try to route traffic through every possible intermediate node 'k'
        for (int k = 0; k < vertices; k++) {
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    // If routing from i -> k -> j is faster than the current known path i -> j, update it.
                    if (distance[i][k] + distance[k][j] < distance[i][j]) {
                        distance[i][j] = distance[i][k] + distance[k][j];
                    }
                }
            }
        }

        // Output the final computed matrix
        System.out.println("--- Global Optimal Routing Matrix Computed ---");
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (distance[i][j] == INF) {
                    System.out.print("INF\t");
                } else {
                    System.out.print(distance[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        FloydWarshallRouting engine = new FloydWarshallRouting();
        int vertices = 4;

        // Constructing the initial network as a 2D Adjacency Matrix
        // A value of INF means no direct cable exists between those two servers.
        int[][] networkMatrix = { 
            { 0,   5,  INF, 10 },
            { INF, 0,   3,  INF },
            { INF, INF, 0,   1 },
            { INF, INF, INF, 0 } 
        };

        System.out.println("Ingesting Network Topology...\n");
        engine.executeAllPairsShortestPath(networkMatrix, vertices);
        
        System.out.println("\nStatus: O(V^3) Dynamic Programming execution complete. All paths optimized.");
    }
}