import java.util.*;

public class HeavyLightDecompositionEngine {
    private int n;
    private List<List<Integer>> tree;
    
    // HLD Architectural Arrays
    private int[] parent, depth, subtreeSize, heavyChild;
    private int[] chainHead; // The node at the very top of the current heavy chain
    private int[] flatPosition; // The mapped 1D array position for the Segment Tree
    
    private int currentPosition = 0;

    public HeavyLightDecompositionEngine(int nodes) {
        this.n = nodes;
        tree = new ArrayList<>();
        for (int i = 0; i < n; i++) tree.add(new ArrayList<>());
        
        parent = new int[n];
        depth = new int[n];
        subtreeSize = new int[n];
        heavyChild = new int[n];
        chainHead = new int[n];
        flatPosition = new int[n];
        
        Arrays.fill(heavyChild, -1);
    }

    public void addEdge(int u, int v) {
        tree.get(u).add(v);
        tree.get(v).add(u);
    }

    // Sweep 1: O(N) - Calculate subtree sizes and identify the heaviest child for every node
    public void executeStructuralSweep(int current, int p, int d) {
        parent[current] = p;
        depth[current] = d;
        subtreeSize[current] = 1;
        
        int maxSubtree = 0;
        
        for (int child : tree.get(current)) {
            if (child != p) {
                executeStructuralSweep(child, current, d + 1);
                subtreeSize[current] += subtreeSize[child];
                
                // If this child is the heaviest we've seen, it becomes the continuous Heavy Chain
                if (subtreeSize[child] > maxSubtree) {
                    maxSubtree = subtreeSize[child];
                    heavyChild[current] = child;
                }
            }
        }
    }

    // Sweep 2: O(N) - Connect the Heavy Chains and map them to a flattened 1D array
    public void executeChainDecomposition(int current, int p, int head) {
        chainHead[current] = head;
        flatPosition[current] = currentPosition++;
        
        // Priority 1: Continue the Heavy Chain down the tree so it stays contiguous in memory
        if (heavyChild[current] != -1) {
            executeChainDecomposition(heavyChild[current], current, head);
        }
        
        // Priority 2: Traverse the Light Edges. Every light edge starts a brand new Heavy Chain.
        for (int child : tree.get(current)) {
            if (child != p && child != heavyChild[current]) {
                executeChainDecomposition(child, current, child);
            }
        }
    }

    public void runDiagnostics() {
        System.out.println("\n--- Heavy-Light Decomposition Memory Map ---");
        System.out.println("Node | Chain Head | 1D Array Position | Heavy Child");
        for (int i = 0; i < n; i++) {
            System.out.printf("%4d | %10d | %17d | %11d\n", 
                i, chainHead[i], flatPosition[i], heavyChild[i]);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Heavy-Light Decomposition (HLD) Engine ---");
        
        // Tree Structure:
        //       0
        //      / \
        //     1   2
        //    / \   \
        //   3   4   5
        //      /
        //     6
        HeavyLightDecompositionEngine hld = new HeavyLightDecompositionEngine(7);
        hld.addEdge(0, 1); hld.addEdge(0, 2);
        hld.addEdge(1, 3); hld.addEdge(1, 4);
        hld.addEdge(2, 5); hld.addEdge(4, 6);
        
        System.out.println("  [System] Executing Pass 1: Subtree Weight Analysis...");
        hld.executeStructuralSweep(0, 0, 0);
        
        System.out.println("  [System] Executing Pass 2: Heavy Chain Memory Flattening...");
        hld.executeChainDecomposition(0, 0, 0);
        
        hld.runDiagnostics();
        
        System.out.println("\nStatus: Geometric tree constraints flattened into contiguous heavy paths for O(log^2 N) Segment Tree querying.");
    }
}