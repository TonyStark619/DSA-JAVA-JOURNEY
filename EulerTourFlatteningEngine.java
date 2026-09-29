import java.util.*;

public class EulerTourFlatteningEngine {
    private int V;
    private List<List<Integer>> tree;
    
    // The mathematical timestamps that flatten the hierarchy
    private int timer = 0;
    private int[] entryTime;
    private int[] exitTime;
    private int[] flattenedArray; // Maps the timer index back to the actual node ID

    public EulerTourFlatteningEngine(int vertices) {
        this.V = vertices;
        tree = new ArrayList<>();
        for (int i = 0; i < V; i++) tree.add(new ArrayList<>());
        
        entryTime = new int[V];
        exitTime = new int[V];
        flattenedArray = new int[V];
    }

    public void addEdge(int u, int v) {
        tree.get(u).add(v);
        tree.get(v).add(u);
    }

    // O(N) - Execute the Euler Tour exactly once to map the hierarchy
    public void buildFlatArchitecture(int root) {
        System.out.println("  [System] Executing DFS Euler Tour to flatten geometry...");
        dfs(root, -1);
    }

    private void dfs(int current, int parent) {
        // Record the moment we enter the node
        entryTime[current] = timer;
        flattenedArray[timer] = current;
        timer++;

        // Process all children
        for (int child : tree.get(current)) {
            if (child != parent) {
                dfs(child, current);
            }
        }

        // Record the moment we completely finish processing the node and all its children
        // The exit time is the last valid timer index that belongs to this node's subtree
        exitTime[current] = timer - 1;
    }

    // O(1) - Check if 'u' is an ancestor of 'v'
    // In a flattened tree, a node is an ancestor if and only if it was entered BEFORE 'v' and exited AFTER 'v'
    public boolean isAncestor(int u, int v) {
        return entryTime[u] <= entryTime[v] && exitTime[u] >= exitTime[v];
    }

    // O(1) - Get the exact 1D contiguous array boundaries for any node's entire subtree
    public void getSubtreeRange(int node) {
        int start = entryTime[node];
        int end = exitTime[node];
        
        System.out.println("\n  -> Subtree for Node " + node + " maps to 1D Array Indices: [" + start + " to " + end + "]");
        
        System.out.print("  -> Nodes strictly inside this contiguous block: ");
        for (int i = start; i <= end; i++) {
            System.out.print(flattenedArray[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Euler Tour Flattening Architecture ---");
        
        // Tree Structure: 
        // 0 is CEO. 
        // 1 and 2 report to 0. 
        // 3 and 4 report to 1. 
        // 5 reports to 2.
        EulerTourFlatteningEngine engine = new EulerTourFlatteningEngine(6);
        engine.addEdge(0, 1); engine.addEdge(0, 2);
        engine.addEdge(1, 3); engine.addEdge(1, 4);
        engine.addEdge(2, 5);
        
        engine.buildFlatArchitecture(0);
        
        System.out.println("\n--- Querying Flattened Subtree Memory Blocks ---");
        // We can now pass this flat [start, end] range directly into a Fenwick or Segment Tree!
        engine.getSubtreeRange(1); // Should contain 1, 3, 4
        engine.getSubtreeRange(2); // Should contain 2, 5
        
        System.out.println("\n--- Executing O(1) Hierarchy Verification ---");
        System.out.println("Is Node 1 the boss (ancestor) of Node 4? " + engine.isAncestor(1, 4));
        System.out.println("Is Node 2 the boss (ancestor) of Node 3? " + engine.isAncestor(2, 3));
        
        System.out.println("\nStatus: Hierarchical tree structure mathematically collapsed into O(1) contiguous memory blocks.");
    }
}