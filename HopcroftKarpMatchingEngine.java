import java.util.*;

public class HopcroftKarpMatchingEngine {
    private final int NIL = 0;
    private final int INF = Integer.MAX_VALUE;

    private int numDrivers; // Set U
    private int numRiders;  // Set V
    private List<List<Integer>> adjacencyList;

    private int[] pairU;
    private int[] pairV;
    private int[] dist;

    public HopcroftKarpMatchingEngine(int drivers, int riders) {
        this.numDrivers = drivers;
        this.numRiders = riders;
        
        adjacencyList = new ArrayList<>();
        // 1-based indexing for mathematical simplicity (0 is reserved for NIL/Unmatched)
        for (int i = 0; i <= drivers; i++) {
            adjacencyList.add(new ArrayList<>());
        }
        
        pairU = new int[drivers + 1];
        pairV = new int[riders + 1];
        dist = new int[drivers + 1];
    }

    public void addPossibleMatch(int driver, int rider) {
        adjacencyList.get(driver).add(rider);
    }

    // Phase 1: BFS creates the Layered Geometry of all shortest augmenting paths
    private boolean bfs() {
        Queue<Integer> queue = new LinkedList<>();

        for (int u = 1; u <= numDrivers; u++) {
            if (pairU[u] == NIL) {
                dist[u] = 0;
                queue.add(u); // Add unmatched drivers to the initial BFS frontier
            } else {
                dist[u] = INF;
            }
        }

        dist[NIL] = INF;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            if (dist[u] < dist[NIL]) {
                for (int v : adjacencyList.get(u)) {
                    // If the rider's current driver hasn't been visited in this BFS phase
                    if (dist[pairV[v]] == INF) {
                        dist[pairV[v]] = dist[u] + 1;
                        queue.add(pairV[v]);
                    }
                }
            }
        }
        // If dist[NIL] is no longer infinity, we successfully found at least one augmenting path
        return dist[NIL] != INF; 
    }

    // Phase 2: DFS commits the matches utilizing the BFS Layer Graph
    private boolean dfs(int u) {
        if (u != NIL) {
            for (int v : adjacencyList.get(u)) {
                // Strictly follow the layered geometry laid down by the BFS
                if (dist[pairV[v]] == dist[u] + 1) {
                    if (dfs(pairV[v])) {
                        pairV[v] = u;
                        pairU[u] = v;
                        return true;
                    }
                }
            }
            // Dead end optimization
            dist[u] = INF; 
            return false;
        }
        return true;
    }

    // O(E * sqrt(V)) - The Core Execution Engine
    public int executeMaxMatching() {
        System.out.println("  [System] Executing Hopcroft-Karp BFS/DFS Sweep...");
        
        Arrays.fill(pairU, NIL);
        Arrays.fill(pairV, NIL);

        int maxMatches = 0;

        // Keep running parallel BFS layers until no more augmenting paths exist
        while (bfs()) {
            for (int u = 1; u <= numDrivers; u++) {
                if (pairU[u] == NIL && dfs(u)) {
                    maxMatches++;
                }
            }
        }
        return maxMatches;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Bipartite Matching Architecture ---");
        
        // 4 Drivers, 4 Riders
        HopcroftKarpMatchingEngine routingSystem = new HopcroftKarpMatchingEngine(4, 4);
        
        // Driver 1 can take Rider 2 or 3
        routingSystem.addPossibleMatch(1, 2);
        routingSystem.addPossibleMatch(1, 3);
        // Driver 2 can only take Rider 1
        routingSystem.addPossibleMatch(2, 1);
        // Driver 3 can take Rider 1 or 4
        routingSystem.addPossibleMatch(3, 1);
        routingSystem.addPossibleMatch(3, 4);
        // Driver 4 can take Rider 2
        routingSystem.addPossibleMatch(4, 2);

        int optimalMatches = routingSystem.executeMaxMatching();
        
        System.out.println("\nCRITICAL RESULT: Maximum System Matches Achieved -> " + optimalMatches);
        System.out.println("Status: O(E * sqrt(V)) bipartite geometry successfully resolved.");
    }
}