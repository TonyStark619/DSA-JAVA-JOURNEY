import java.util.*;

public class CentroidDecompositionEngine {
    private int n;
    private List<Set<Integer>> tree;
    private int[] subtreeSize;
    private int[] centroidParent;

    public CentroidDecompositionEngine(int n) {
        this.n = n;
        tree = new ArrayList<>();
        for (int i = 0; i < n; i++) tree.add(new HashSet<>());
        
        subtreeSize = new int[n];
        centroidParent = new int[n];
        Arrays.fill(centroidParent, -1);
    }

    public void addEdge(int u, int v) {
        tree.get(u).add(v);
        tree.get(v).add(u);
    }

    // O(N) - Calculate the size of every subtree in the current component
    private int dfsSize(int u, int p) {
        subtreeSize[u] = 1;
        for (int v : tree.get(u)) {
            if (v != p) {
                subtreeSize[u] += dfsSize(v, u);
            }
        }
        return subtreeSize[u];
    }

    // O(N) - Find the exact Centroid node of the current component
    private int getCentroid(int u, int p, int totalNodes) {
        for (int v : tree.get(u)) {
            if (v != p && subtreeSize[v] > totalNodes / 2) {
                // If a child has more than half the nodes, the centroid MUST be in that subtree
                return getCentroid(v, u, totalNodes);
            }
        }
        // If no child has > N/2 nodes, the current node is the mathematical centroid
        return u;
    }

    // O(N log N) - The Core Recursive Decomposition Engine
    private void decompose(int u, int p) {
        // 1. Calculate subtree sizes for the current component
        int totalNodes = dfsSize(u, -1);

        // 2. Find the centroid of this component
        int centroid = getCentroid(u, -1, totalNodes);

        // 3. Link it to the Centroid Tree structure
        if (p != -1) {
            centroidParent[centroid] = p;
        }

        // 4. Destroy the edges connected to the centroid to isolate the sub-components
        // We copy the neighbors to a list to avoid ConcurrentModificationException during deletion
        List<Integer> neighbors = new ArrayList<>(tree.get(centroid));
        for (int v : neighbors) {
            tree.get(centroid).remove(v);
            tree.get(v).remove(centroid);
            
            // 5. Recursively decompose the isolated pieces
            decompose(v, centroid);
        }
    }

    public void buildCentroidTree(int root) {
        System.out.println("Executing O(N log N) Centroid Decomposition...");
        decompose(root, -1);
        
        System.out.println("\n--- Flattened Centroid Hierarchy ---");
        System.out.println("Node | Centroid Parent");
        for (int i = 0; i < n; i++) {
            System.out.printf(" %2d  |      %2d\n", i, centroidParent[i]);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Centroid Architecture Engine ---");
        
        CentroidDecompositionEngine engine = new CentroidDecompositionEngine(7);
        
        // Constructing a highly unbalanced, line-like tree
        engine.addEdge(0, 1);
        engine.addEdge(1, 2);
        engine.addEdge(2, 3);
        engine.addEdge(3, 4);
        engine.addEdge(4, 5);
        engine.addEdge(5, 6);
        
        // Decomposing starting from node 0
        engine.buildCentroidTree(0);
        
        System.out.println("\nStatus: Network perfectly balanced. Depth mathematically restricted to O(log N).");
    }
}import java.util.*;

public class CentroidDecompositionEngine {
    private int n;
    private List<Set<Integer>> tree;
    private int[] subtreeSize;
    private int[] centroidParent;

    public CentroidDecompositionEngine(int n) {
        this.n = n;
        tree = new ArrayList<>();
        for (int i = 0; i < n; i++) tree.add(new HashSet<>());
        
        subtreeSize = new int[n];
        centroidParent = new int[n];
        Arrays.fill(centroidParent, -1);
    }

    public void addEdge(int u, int v) {
        tree.get(u).add(v);
        tree.get(v).add(u);
    }

    // O(N) - Calculate the size of every subtree in the current component
    private int dfsSize(int u, int p) {
        subtreeSize[u] = 1;
        for (int v : tree.get(u)) {
            if (v != p) {
                subtreeSize[u] += dfsSize(v, u);
            }
        }
        return subtreeSize[u];
    }

    // O(N) - Find the exact Centroid node of the current component
    private int getCentroid(int u, int p, int totalNodes) {
        for (int v : tree.get(u)) {
            if (v != p && subtreeSize[v] > totalNodes / 2) {
                // If a child has more than half the nodes, the centroid MUST be in that subtree
                return getCentroid(v, u, totalNodes);
            }
        }
        // If no child has > N/2 nodes, the current node is the mathematical centroid
        return u;
    }

    // O(N log N) - The Core Recursive Decomposition Engine
    private void decompose(int u, int p) {
        // 1. Calculate subtree sizes for the current component
        int totalNodes = dfsSize(u, -1);

        // 2. Find the centroid of this component
        int centroid = getCentroid(u, -1, totalNodes);

        // 3. Link it to the Centroid Tree structure
        if (p != -1) {
            centroidParent[centroid] = p;
        }

        // 4. Destroy the edges connected to the centroid to isolate the sub-components
        // We copy the neighbors to a list to avoid ConcurrentModificationException during deletion
        List<Integer> neighbors = new ArrayList<>(tree.get(centroid));
        for (int v : neighbors) {
            tree.get(centroid).remove(v);
            tree.get(v).remove(centroid);
            
            // 5. Recursively decompose the isolated pieces
            decompose(v, centroid);
        }
    }

    public void buildCentroidTree(int root) {
        System.out.println("Executing O(N log N) Centroid Decomposition...");
        decompose(root, -1);
        
        System.out.println("\n--- Flattened Centroid Hierarchy ---");
        System.out.println("Node | Centroid Parent");
        for (int i = 0; i < n; i++) {
            System.out.printf(" %2d  |      %2d\n", i, centroidParent[i]);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Centroid Architecture Engine ---");
        
        CentroidDecompositionEngine engine = new CentroidDecompositionEngine(7);
        
        // Constructing a highly unbalanced, line-like tree
        engine.addEdge(0, 1);
        engine.addEdge(1, 2);
        engine.addEdge(2, 3);
        engine.addEdge(3, 4);
        engine.addEdge(4, 5);
        engine.addEdge(5, 6);
        
        // Decomposing starting from node 0
        engine.buildCentroidTree(0);
        
        System.out.println("\nStatus: Network perfectly balanced. Depth mathematically restricted to O(log N).");
    }
}