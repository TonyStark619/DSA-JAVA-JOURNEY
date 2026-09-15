import java.util.*;

public class HeavyLightDecomposition {
    private int n;
    private List<List<Integer>> tree;
    
    // HLD specific arrays
    private int[] parent, depth, subtreeSize, heavyChild;
    private int[] chainHead; // The node at the very top of a heavy chain
    private int[] arrayPos;  // The position of the node in the flattened 1D array
    private int currentPos = 0;

    public HeavyLightDecomposition(int n) {
        this.n = n;
        tree = new ArrayList<>();
        for (int i = 0; i < n; i++) tree.add(new ArrayList<>());
        
        parent = new int[n];
        depth = new int[n];
        subtreeSize = new int[n];
        heavyChild = new int[n];
        chainHead = new int[n];
        arrayPos = new int[n];
        Arrays.fill(heavyChild, -1);
    }

    public void addEdge(int u, int v) {
        tree.get(u).add(v);
        tree.get(v).add(u);
    }

    // DFS 1: O(N) - Calculate Subtree Sizes and identify the Heavy Child
    private void dfsSize(int u, int p, int d) {
        parent[u] = p;
        depth[u] = d;
        subtreeSize[u] = 1;
        int maxSubtree = 0;

        for (int v : tree.get(u)) {
            if (v != p) {
                dfsSize(v, u, d + 1);
                subtreeSize[u] += subtreeSize[v];
                
                // If this child's subtree is the biggest we've seen, mark it as the Heavy Child
                if (subtreeSize[v] > maxSubtree) {
                    maxSubtree = subtreeSize[v];
                    heavyChild[u] = v;
                }
            }
        }
    }

    // DFS 2: O(N) - Construct the Heavy Chains and flatten the Tree
    private void dfsHLD(int u, int p, int head) {
        chainHead[u] = head;
        arrayPos[u] = currentPos++; // Assigning a position in the flattened array

        // 1. Follow the Heavy Child first to keep the chain continuous in the array
        if (heavyChild[u] != -1) {
            dfsHLD(heavyChild[u], u, head);
        }

        // 2. Iterate through Light Children (each starts a brand new heavy chain)
        for (int v : tree.get(u)) {
            if (v != p && v != heavyChild[u]) {
                dfsHLD(v, u, v);
            }
        }
    }

    public void buildHLD(int root) {
        System.out.println("Executing O(N) Heavy-Light Decomposition Analysis...");
        // Step 1: Telemetry sweep for subtree weights
        dfsSize(root, root, 0);
        
        // Step 2: Chain construction
        dfsHLD(root, root, root);
        
        System.out.println("\n--- Flattened Topological Map ---");
        System.out.println("Node | Chain Head | Flattened Array Position");
        for (int i = 0; i < n; i++) {
            System.out.printf(" %2d  |     %2d     |          %2d\n", i, chainHead[i], arrayPos[i]);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting HLD Architectural Flattener ---");
        
        HeavyLightDecomposition hld = new HeavyLightDecomposition(9);
        
        // Constructing an unbalanced network tree
        hld.addEdge(0, 1);
        hld.addEdge(0, 2);
        hld.addEdge(1, 3);
        hld.addEdge(1, 4);
        hld.addEdge(2, 5);
        hld.addEdge(4, 6);
        hld.addEdge(4, 7);
        hld.addEdge(7, 8);
        
        hld.buildHLD(0);
        
        System.out.println("\nStatus: Tree flattened successfully. Ready for O(log^2 N) Segment Tree queries.");
    }
}