import java.util.*;

public class CriticalBridgeIsolationEngine {
    
    private final int vertices;
    private final List<List<Integer>> networkGraph;
    
    // Geometry trackers for Tarjan's DFS sweep
    private int timer = 0;
    private final int[] discoveryTime;
    private final int[] lowestReachable;
    private final int[] parent;
    
    // Output matrix of isolated vulnerabilities
    private final List<String> criticalBridges;

    public CriticalBridgeIsolationEngine(int v) {
        this.vertices = v;
        this.networkGraph = new ArrayList<>();
        for (int i = 0; i < v; i++) {
            networkGraph.add(new ArrayList<>());
        }
        this.discoveryTime = new int[v];
        this.lowestReachable = new int[v];
        this.parent = new int[v];
        this.criticalBridges = new ArrayList<>();
        
        Arrays.fill(discoveryTime, -1);
        Arrays.fill(parent, -1);
    }

    public void addUndirectedEdge(int u, int v) {
        networkGraph.get(u).add(v);
        networkGraph.get(v).add(u);
    }

    // O(V + E) - The Core DFS Vulnerability Sweeper
    private void executeTarjanSweep(int u) {
        discoveryTime[u] = lowestReachable[u] = ++timer;

        for (int v : networkGraph.get(u)) {
            // Case 1: Node V is unvisited. We dive deeper.
            if (discoveryTime[v] == -1) {
                parent[v] = u;
                executeTarjanSweep(v);
                
                // As the recursion unwinds, we update the lowest reachable node
                lowestReachable[u] = Math.min(lowestReachable[u], lowestReachable[v]);
                
                // THE CRITICAL MATHEMATICAL PROOF:
                // If the lowest node V can reach is still deeper/later than U's discovery time,
                // V has no "back-edge" to safety. The edge U-V is a Single Point of Failure.
                if (lowestReachable[v] > discoveryTime[u]) {
                    criticalBridges.add("Server_" + u + " <====> Server_" + v);
                }
            } 
            // Case 2: Node V is visited and is NOT the parent (It's a Back-Edge / Loop)
            else if (v != parent[u]) {
                lowestReachable[u] = Math.min(lowestReachable[u], discoveryTime[v]);
            }
        }
    }

    public void isolateVulnerabilities() {
        System.out.println("  [System] Executing Tarjan's Geometry Sweep for Single Points of Failure...");
        for (int i = 0; i < vertices; i++) {
            if (discoveryTime[i] == -1) {
                executeTarjanSweep(i);
            }
        }
        
        System.out.println("\n--- Critical Infrastructure Vulnerabilities ---");
        if (criticalBridges.isEmpty()) {
            System.out.println("  -> Network is absolutely redundant. Zero critical bridges detected.");
        } else {
            for (String bridge : criticalBridges) {
                System.out.println("  [ALERT] Critical Connection Isolated: " + bridge);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Tarjan's Bridge-Finding Architecture ---");
        
        // A 5-Server Network Grid
        CriticalBridgeIsolationEngine network = new CriticalBridgeIsolationEngine(5);
        
        // Servers 0, 1, and 2 form a highly redundant triangle (No bridges here)
        network.addUndirectedEdge(0, 1);
        network.addUndirectedEdge(1, 2);
        network.addUndirectedEdge(2, 0);
        
        // Server 0 connects to Server 3. If this breaks, 3 and 4 are completely severed.
        network.addUndirectedEdge(0, 3);
        
        // Server 3 connects to Server 4. If this breaks, 4 is isolated.
        network.addUndirectedEdge(3, 4);
        
        network.isolateVulnerabilities();
        
        System.out.println("\nStatus: O(V+E) linear sweep complete. Structural network weaknesses mathematically proven.");
    }
}