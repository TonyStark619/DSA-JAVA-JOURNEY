import java.util.*;

public class StoerWagnerMinCutEngine {

    private int[][] adjacencyMatrix;
    private int numVertices;

    public StoerWagnerMinCutEngine(int v) {
        this.numVertices = v;
        this.adjacencyMatrix = new int[v][v];
    }

    // Undirected edge representing physical bandwidth or cable capacity
    public void addUndirectedEdge(int u, int v, int weight) {
        adjacencyMatrix[u][v] += weight;
        adjacencyMatrix[v][u] += weight;
    }

    // O(V^3) - The Core Global Matrix Collapser
    public int findGlobalMinimumCut() {
        System.out.println("  [System] Executing Stoer-Wagner Global Cut Analysis...");
        
        int globalMinCut = Integer.MAX_VALUE;
        
        // Keeps track of which nodes have been mathematically merged into super-nodes
        boolean[] merged = new boolean[numVertices];
        
        // We will collapse the graph V-1 times until only a single super-node remains
        for (int phase = 1; phase < numVertices; phase++) {
            
            // Array to track the tightest connections to our growing cluster
            int[] weights = new int[numVertices];
            boolean[] inCluster = new boolean[numVertices];
            
            int prevNode = -1, currNode = -1;
            
            // In each phase, we pull all unmerged nodes into a single cluster one by one
            for (int i = 0; i < numVertices - phase + 1; i++) {
                int nextNode = -1;
                int maxWeight = -1;
                
                // Find the node most tightly connected to the current cluster
                for (int j = 0; j < numVertices; j++) {
                    if (!merged[j] && !inCluster[j] && weights[j] > maxWeight) {
                        nextNode = j;
                        maxWeight = weights[j];
                    }
                }
                
                if (i == numVertices - phase) {
                    // The cut of this phase is the connection weight of the VERY LAST node added
                    System.out.println("    -> Phase " + phase + " Local Cut Isolated: " + maxWeight);
                    globalMinCut = Math.min(globalMinCut, maxWeight);
                    
                    // Mathematically merge the last two nodes (currNode and nextNode) into one
                    merged[nextNode] = true;
                    for (int j = 0; j < numVertices; j++) {
                        if (!merged[j]) {
                            adjacencyMatrix[currNode][j] += adjacencyMatrix[nextNode][j];
                            adjacencyMatrix[j][currNode] += adjacencyMatrix[nextNode][j];
                        }
                    }
                } else {
                    inCluster[nextNode] = true;
                    // Update the connection weights for the remaining nodes
                    for (int j = 0; j < numVertices; j++) {
                        if (!merged[j] && !inCluster[j]) {
                            weights[j] += adjacencyMatrix[nextNode][j];
                        }
                    }
                    prevNode = currNode;
                    currNode = nextNode;
                }
            }
        }
        return globalMinCut;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Stoer-Wagner Network Severing Architecture ---");
        
        // An interconnected 5-node server grid
        StoerWagnerMinCutEngine network = new StoerWagnerMinCutEngine(5);
        
        network.addUndirectedEdge(0, 1, 2);
        network.addUndirectedEdge(0, 2, 3);
        network.addUndirectedEdge(1, 2, 4);
        network.addUndirectedEdge(1, 3, 2);
        network.addUndirectedEdge(2, 4, 2);
        network.addUndirectedEdge(3, 4, 3);

        System.out.println("\n[Network] Initializing V^3 super-node geometric collapses...");
        int ultimateCut = network.findGlobalMinimumCut();
        
        // The weakest link is severing Node 0 from the rest of the network (Weight 5) 
        // OR severing Node 3 and 4 together (Weight 4). Minimum is 4.
        System.out.println("\nCRITICAL RESULT: Absolute Global Minimum Cut Capacity -> " + ultimateCut);
        System.out.println("Status: Network vulnerabilities mapped without requiring source/sink definitions.");
    }
}