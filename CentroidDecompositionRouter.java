import java.util.*;

public class CentroidDecompositionRouter {

    private static final int INF = 1_000_000_000;

    private final int n;
    private final List<List<Integer>> originalTree;

    // Centroid Decomposition Hierarchy
    private final boolean[] removedAsCentroid;
    private final int[] subtreeSize;
    private final int[] centroidParent;

    // distToCentroidAncestor[u] stores exact distances from u to each of its <= log2(N) centroid ancestors
    private final List<List<Integer>> distToCentroidAncestor;

    // Dynamic query state: minDistInCentroidSubtree[c] holds the closest active node inside c's centroid region
    private final int[] minDistInCentroidSubtree;

    public CentroidDecompositionRouter(int vertices) {
        this.n = vertices;
        this.originalTree = new ArrayList<>(n);
        this.distToCentroidAncestor = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            originalTree.add(new ArrayList<>());
            distToCentroidAncestor.add(new ArrayList<>());
        }

        this.removedAsCentroid = new boolean[n];
        this.subtreeSize = new int[n];
        this.centroidParent = new int[n];
        this.minDistInCentroidSubtree = new int[n];

        Arrays.fill(centroidParent, -1);
        Arrays.fill(minDistInCentroidSubtree, INF);
    }

    public void addUndirectedEdge(int u, int v) {
        originalTree.get(u).add(v);
        originalTree.get(v).add(u);
    }

    // DFS 1: Computes exact subtree sizes within the unremoved component
    private int computeSubtreeSizes(int u, int parent) {
        subtreeSize[u] = 1;
        for (int v : originalTree.get(u)) {
            if (v != parent && !removedAsCentroid[v]) {
                subtreeSize[u] += computeSubtreeSizes(v, u);
            }
        }
        return subtreeSize[u];
    }

    // DFS 2: Locates the Centroid (node whose removal leaves no component > totalSize / 2)
    private int locateCentroid(int u, int parent, int totalComponentSize) {
        for (int v : originalTree.get(u)) {
            if (v != parent && !removedAsCentroid[v] && subtreeSize[v] > totalComponentSize / 2) {
                return locateCentroid(v, u, totalComponentSize);
            }
        }
        return u;
    }

    // DFS 3: Records the exact distance from every node in the component to the current centroid
    private void recordDistancesToCentroid(int u, int parent, int depthFromCentroid) {
        distToCentroidAncestor.get(u).add(depthFromCentroid);
        for (int v : originalTree.get(u)) {
            if (v != parent && !removedAsCentroid[v]) {
                recordDistancesToCentroid(v, u, depthFromCentroid + 1);
            }
        }
    }

    // O(N log N) - Recursively decomposes the tree into a balanced Centroid Hierarchy of height <= log2(N)
    private void decomposeRecursive(int entryNode, int prevCentroid) {
        int totalSize = computeSubtreeSizes(entryNode, -1);
        int centroid = locateCentroid(entryNode, -1, totalSize);

        // Record distances from this centroid to all nodes in its current component
        recordDistancesToCentroid(centroid, -1, 0);

        removedAsCentroid[centroid] = true;
        centroidParent[centroid] = prevCentroid;

        for (int neighbor : originalTree.get(centroid)) {
            if (!removedAsCentroid[neighbor]) {
                decomposeRecursive(neighbor, centroid);
            }
        }
    }

    public void buildCentroidHierarchy() {
        System.out.println("  [System] Executing O(N log N) Centroid Decomposition across network topology...");
        decomposeRecursive(0, -1);
        // Reverse each node's ancestor-distance list so index 0 is the node itself (bottom-up order)
        for (int i = 0; i < n; i++) {
            Collections.reverse(distToCentroidAncestor.get(i));
        }
    }

    // O(log N) - Dynamically activates a server node and updates its <= log2(N) centroid ancestors
    public void activateServerReplica(int u) {
        int currCentroid = u;
        int ancestorLevel = 0;

        while (currCentroid != -1) {
            int dist = distToCentroidAncestor.get(u).get(ancestorLevel);
            minDistInCentroidSubtree[currCentroid] = Math.min(minDistInCentroidSubtree[currCentroid], dist);
            currCentroid = centroidParent[currCentroid];
            ancestorLevel++;
        }
        System.out.println("  [Router] Activated replica on Server " + u + " (Updated " + ancestorLevel + " centroid levels)");
    }

    // O(log N) - Queries the shortest hop distance from 'u' to ANY active server replica
    public int queryNearestActiveReplica(int u) {
        int shortestDist = INF;
        int currCentroid = u;
        int ancestorLevel = 0;

        while (currCentroid != -1) {
            int distFromUToCentroid = distToCentroidAncestor.get(u).get(ancestorLevel);
            shortestDist = Math.min(
                shortestDist,
                distFromUToCentroid + minDistInCentroidSubtree[currCentroid]
            );
            currCentroid = centroidParent[currCentroid];
            ancestorLevel++;
        }
        return shortestDist;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Centroid Decomposition Dynamic Proximity Router ---");

        // Degenerate 8-node chain/tree topology: 0 - 1 - 2 - 3 - 4 - 5 - 6, plus branch 3 - 7
        CentroidDecompositionRouter router = new CentroidDecompositionRouter(8);
        router.addUndirectedEdge(0, 1);
        router.addUndirectedEdge(1, 2);
        router.addUndirectedEdge(2, 3);
        router.addUndirectedEdge(3, 4);
        router.addUndirectedEdge(4, 5);
        router.addUndirectedEdge(5, 6);
        router.addUndirectedEdge(3, 7);

        router.buildCentroidHierarchy();

        System.out.println("\n--- Executing O(log N) Dynamic Replica Activations & Proximity Queries ---");
        // Initially activate only Server 0
        router.activateServerReplica(0);
        System.out.println("  Nearest Active Replica to Server 6 -> " + router.queryNearestActiveReplica(6) + " hops (via Server 0)");

        // Dynamically activate Server 7 (attached to node 3)
        router.activateServerReplica(7);
        // Path 6 -> 5 -> 4 -> 3 -> 7 is exactly 4 hops
        System.out.println("  Nearest Active Replica to Server 6 -> " + router.queryNearestActiveReplica(6) + " hops (via Server 7)");

        // Dynamically activate Server 5
        router.activateServerReplica(5);
        // Path 6 -> 5 is 1 hop
        System.out.println("  Nearest Active Replica to Server 6 -> " + router.queryNearestActiveReplica(6) + " hop  (via Server 5)");

        System.out.println("\nStatus: O(N) tree path scans compressed into O(log N) Centroid LCA lookups.");
    }
}