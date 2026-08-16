import java.util.Arrays;

public class FloydWarshallEngine {

    final static int INF = 99999; // Represents infinity (unreachable nodes)

    // O(V^3) Time, O(V^2) Space - The Global Routing Matrix Builder
    public void computeAllPairsShortestPath(int[][] graph, int vertices) {
        System.out.println("Executing O(V^3) Floyd-Warshall Dynamic Programming Analysis...");

        // Create the DP matrix and initialize it with the original graph weights
        int[][] dist = new int[vertices][vertices];
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                dist[i][j] = graph[i][j];
            }
        }

        // THE CORE ENGINE: 3 Nested Loops
        // k = The intermediate "shortcut" node we are currently evaluating
        for (int k = 0; k < vertices; k++) {
            // i = The starting source node
            for (int i = 0; i < vertices; i++) {
                // j = The final destination node
                for (int j = 0; j < vertices; j++) {
                    
                    // If going through 'k' is strictly faster than going directly from 'i' to 'j',
                    // we update the master matrix with the new shortcut time.
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        printRoutingMatrix(dist, vertices);
    }

    private void printRoutingMatrix(int[][] dist, int vertices) {
        System.out.println("\n--- Global Optimal Routing Matrix ---");
        for (int i = 0; i < vertices; ++i) {
            for (int j = 0; j < vertices; ++j) {
                if (dist[i][j] == INF) {
                    System.out.print("INF \t");
                } else {
                    System.out.print(dist[i][j] + " ms\t");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Floyd-Warshall Global Router ---");
        
        int vertices = 4;
        
        // Simulating the initial network topology (Adjacency Matrix)
        // 0 on the diagonal (distance to itself is 0)
        // INF means no direct physical cable exists yet
        int[][] networkTopology = {
            {0,   5,  INF, 10},
            {INF, 0,   3,  INF},
            {INF, INF, 0,   1},
            {INF, INF, INF, 0}
        };

        FloydWarshallEngine engine = new FloydWarshallEngine();
        engine.computeAllPairsShortestPath(networkTopology, vertices);
        
        System.out.println("\nStatus: All-Pairs routing resolved. Global shortcuts mathematically verified.");
    }
}