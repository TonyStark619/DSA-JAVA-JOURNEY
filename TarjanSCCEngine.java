import java.util.*;

public class TarjanSCCEngine {
    private int time = 0;
    private int sccCount = 0;

    // O(V + E) Time, O(V) Space - Single-Pass SCC Extraction
    public void findSCCs(int vertices, List<List<Integer>> graph) {
        System.out.println("Executing O(V+E) Tarjan's Single-Pass DFS Analysis...");

        int[] discovery = new int[vertices];
        int[] low = new int[vertices];
        boolean[] inStack = new boolean[vertices];
        Stack<Integer> stack = new Stack<>();

        // Initialize discovery times to -1 (unvisited)
        Arrays.fill(discovery, -1);

        for (int i = 0; i < vertices; i++) {
            if (discovery[i] == -1) {
                dfs(i, discovery, low, stack, inStack, graph);
            }
        }
        
        System.out.println("\nCRITICAL INSIGHT: Total Strongly Connected Components (SCCs) isolated: " + sccCount);
    }

    private void dfs(int u, int[] discovery, int[] low, Stack<Integer> stack, boolean[] inStack, List<List<Integer>> graph) {
        discovery[u] = low[u] = ++time;
        stack.push(u);
        inStack[u] = true;

        // Traverse all outgoing edges
        for (int v : graph.get(u)) {
            // Case 1: Node 'v' has never been visited
            if (discovery[v] == -1) {
                dfs(v, discovery, low, stack, inStack, graph);
                // Update the lowest reachable node for 'u'
                low[u] = Math.min(low[u], low[v]);
            } 
            // Case 2: Node 'v' is already in our current DFS stack (Back-edge detected!)
            else if (inStack[v]) {
                low[u] = Math.min(low[u], discovery[v]);
            }
        }

        // THE MAGIC: If 'u' is the root of an SCC, pop all nodes in its cluster off the stack
        if (low[u] == discovery[u]) {
            sccCount++;
            System.out.print("SCC Cluster " + sccCount + " Extracted: [ ");
            
            while (true) {
                int poppedNode = stack.pop();
                inStack[poppedNode] = false;
                System.out.print(poppedNode + " ");
                if (poppedNode == u) break;
            }
            System.out.println("]");
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Tarjan's SCC Architecture ---");
        
        int vertices = 5;
        List<List<Integer>> network = new ArrayList<>();
        for (int i = 0; i < vertices; i++) network.add(new ArrayList<>());

        // Defining the directed network topology
        // Cluster 1: 1 -> 0 -> 2 -> 1 (Cycle)
        network.get(1).add(0);
        network.get(0).add(2);
        network.get(2).add(1);
        
        // Link to Cluster 2
        network.get(0).add(3);
        
        // Cluster 2: 3 -> 4 -> 3 (Cycle)
        network.get(3).add(4);
        network.get(4).add(3);

        TarjanSCCEngine engine = new TarjanSCCEngine();
        engine.findSCCs(vertices, network);
        
        System.out.println("Status: Network segmented into mathematically independent clusters.");
    }
}