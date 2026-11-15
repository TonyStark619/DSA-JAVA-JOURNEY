import java.util.*;

public class StoerWagnerGlobalMinCutEngine {

    private static final long INF = Long.MAX_VALUE / 4;

    private final int n;
    private final long[][] weights;

    public StoerWagnerGlobalMinCutEngine(int vertices) {
        this.n = vertices;
        this.weights = new long[n][n];
    }

    public void addUndirectedEdge(int u, int v, long capacity) {
        weights[u][v] += capacity;
        weights[v][u] += capacity;
    }

    // O(V^3) - Isolates the Global Minimum Cut across all possible 2-way network partitions
    public long computeGlobalMinimumCut() {
        System.out.println("  [System] Executing Stoer-Wagner Maximum Adjacency Search & Vertex Contraction...");

        long globalMinCut = INF;
        boolean[] contracted = new boolean[n]; // Tracks vertices merged into super-nodes
        List<Integer> bestPartitionSide = new ArrayList<>();

        // Track which original vertices belong to each super-node for forensic reporting
        List<List<Integer>> superNodeMembers = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            superNodeMembers.add(new ArrayList<>(Collections.singletonList(i)));
        }

        // Execute (V - 1) phases; each phase contracts two vertices (s and t) into one
        for (int phase = 0; phase < n - 1; phase++) {
            boolean[] inSetA = new boolean[n];
            long[] tightCouplingWeight = new long[n];

            int prevVertexS = -1;
            int lastVertexT = -1;

            // Maximum Adjacency Search: Grow set A across all (n - phase) active super-nodes
            for (int step = 0; step < n - phase; step++) {
                int mostTightlyCoupled = -1;
                long maxWeight = -1;

                for (int v = 0; v < n; v++) {
                    if (!contracted[v] && !inSetA[v] && tightCouplingWeight[v] > maxWeight) {
                        maxWeight = tightCouplingWeight[v];
                        mostTightlyCoupled = v;
                    }
                }

                prevVertexS = lastVertexT;
                lastVertexT = mostTightlyCoupled;
                inSetA[lastVertexT] = true;

                // Update coupling weights of all remaining outside vertices to Set A
                for (int v = 0; v < n; v++) {
                    if (!contracted[v] && !inSetA[v]) {
                        tightCouplingWeight[v] += weights[lastVertexT][v];
                    }
                }
            }

            // Stoer-Wagner Invariant: tightCouplingWeight[lastVertexT] is the exact min s-t cut of this phase
            long phaseCutOfT = tightCouplingWeight[lastVertexT];
            System.out.println(
                "    -> Phase " + (phase + 1) + " Cut-of-the-Phase (s=" + prevVertexS + ", t=" + lastVertexT + ") : "
                + phaseCutOfT + " Gbps"
            );

            if (phaseCutOfT < globalMinCut) {
                globalMinCut = phaseCutOfT;
                bestPartitionSide = new ArrayList<>(superNodeMembers.get(lastVertexT));
            }

            // Contract lastVertexT into prevVertexS
            contracted[lastVertexT] = true;
            superNodeMembers.get(prevVertexS).addAll(superNodeMembers.get(lastVertexT));

            for (int v = 0; v < n; v++) {
                if (!contracted[v] && v != prevVertexS) {
                    weights[prevVertexS][v] += weights[lastVertexT][v];
                    weights[v][prevVertexS] += weights[v][lastVertexT];
                }
            }
        }

        System.out.println("  [Topology] Weakest Island Partition Isolated : Nodes " + bestPartitionSide);
        return globalMinCut;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Stoer-Wagner Global Minimum Cut Architecture ---");

        // 6-Server Mesh:
        // Cluster Alpha {0, 1, 2} is heavily interconnected (10 Gbps links)
        // Cluster Beta  {3, 4, 5} is heavily interconnected (10 Gbps links)
        // Two weak cross-cluster links connect (1 - 3) with 2 Gbps and (2 - 4) with 3 Gbps (Total Cut = 5 Gbps)
        StoerWagnerGlobalMinCutEngine mesh = new StoerWagnerGlobalMinCutEngine(6);

        // Cluster Alpha internal high-capacity links
        mesh.addUndirectedEdge(0, 1, 10);
        mesh.addUndirectedEdge(1, 2, 10);
        mesh.addUndirectedEdge(0, 2, 10);

        // Cluster Beta internal high-capacity links
        mesh.addUndirectedEdge(3, 4, 10);
        mesh.addUndirectedEdge(4, 5, 10);
        mesh.addUndirectedEdge(3, 5, 10);

        // Cross-cluster bottleneck links (2 + 3 = 5 Gbps)
        mesh.addUndirectedEdge(1, 3, 2);
        mesh.addUndirectedEdge(2, 4, 3);

        long weakestCutCapacity = mesh.computeGlobalMinimumCut();

        System.out.println("\nCRITICAL RESULT: Global Minimum Structural Cut = " + weakestCutCapacity + " Gbps");
        System.out.println("Status: Global network bottleneck isolated in O(V^3) without source-sink flow assumptions.");
    }
}