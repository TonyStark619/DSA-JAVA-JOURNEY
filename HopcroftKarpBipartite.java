import java.util.*;

public class HopcroftKarpBipartite {
    private static final int NIL = 0;
    private static final int INF = Integer.MAX_VALUE;

    private int numRiders;
    private int numDrivers;
    private List<List<Integer>> adj;
    private int[] pairU, pairV, dist;

    // O(E * sqrt(V)) Time - Apex Bipartite Matching
    public HopcroftKarpBipartite(int u, int v) {
        this.numRiders = u;
        this.numDrivers = v;
        adj = new ArrayList<>();
        // 1-based indexing for the algorithm to use 0 as NIL
        for (int i = 0; i <= u; i++) {
            adj.add(new ArrayList<>());
        }
        pairU = new int[u + 1];
        pairV = new int[v + 1];
        dist = new int[u + 1];
    }

    public void addEdge(int rider, int driver) {
        adj.get(rider).add(driver);
    }

    // BFS to find the shortest augmenting paths in parallel
    private boolean bfs() {
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 1; i <= numRiders; i++) {
            if (pairU[i] == NIL) {
                dist[i] = 0; // Unmatched nodes are the starting points
                queue.add(i);
            } else {
                dist[i] = INF;
            }
        }
        dist[NIL] = INF;

        while (!queue.isEmpty()) {
            int rider = queue.poll();
            if (dist[rider] < dist[NIL]) {
                for (int driver : adj.get(rider)) {
                    if (dist[pairV[driver]] == INF) {
                        dist[pairV[driver]] = dist[rider] + 1;
                        queue.add(pairV[driver]);
                    }
                }
            }
        }
        // If dist[NIL] is no longer INF, we found at least one augmenting path
        return dist[NIL] != INF;
    }

    // DFS to commit the exact paths discovered by BFS
    private boolean dfs(int rider) {
        if (rider != NIL) {
            for (int driver : adj.get(rider)) {
                if (dist[pairV[driver]] == dist[rider] + 1) {
                    if (dfs(pairV[driver])) {
                        pairV[driver] = rider;
                        pairU[rider] = driver;
                        return true;
                    }
                }
            }
            dist[rider] = INF; // Dead end
            return false;
        }
        return true;
    }

    public int getMaxMatching() {
        System.out.println("Executing O(E * sqrt(V)) Hopcroft-Karp Optimization...");
        int matches = 0;
        
        // While there is an augmenting path, find it and augment the matching
        while (bfs()) {
            for (int i = 1; i <= numRiders; i++) {
                if (pairU[i] == NIL && dfs(i)) {
                    matches++;
                }
            }
        }
        return matches;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Bipartite Assignment Engine ---");
        
        // 4 Riders, 4 Drivers
        HopcroftKarpBipartite engine = new HopcroftKarpBipartite(4, 4);
        
        // Rider 1 can go with Driver 2 or 3
        engine.addEdge(1, 2);
        engine.addEdge(1, 3);
        // Rider 2 can go with Driver 1
        engine.addEdge(2, 1);
        // Rider 3 can go with Driver 1 or 4
        engine.addEdge(3, 1);
        engine.addEdge(3, 4);
        // Rider 4 can go with Driver 2
        engine.addEdge(4, 2);

        int maxPairings = engine.getMaxMatching();
        
        System.out.println("\n--- Global Assignment Resolution ---");
        System.out.println("CRITICAL RESULT: Maximum Optimal Pairings = " + maxPairings);
        
        for (int i = 1; i <= 4; i++) {
            if (engine.pairU[i] != NIL) {
                System.out.println("Rider " + i + " ➔ Driver " + engine.pairU[i]);
            }
        }
        
        System.out.println("\nStatus: Bipartite graph saturated. Bottleneck resolution successful.");
    }
}