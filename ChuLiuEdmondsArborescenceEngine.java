import java.util.*;

public class ChuLiuEdmondsArborescenceEngine {

    private static final long INF = Long.MAX_VALUE / 4;

    static class DirectedEdge {
        final int u;
        final int v;
        final long weight;

        public DirectedEdge(int u, int v, long weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }
    }

    // O(V * E) - Computes exact Minimum Spanning Arborescence cost rooted at 'root'
    // Returns -1 if no directed spanning tree reaches all vertices from 'root'
    public static long computeMinimumArborescence(int numVertices, int root, List<DirectedEdge> initialEdges) {
        System.out.println("  [System] Executing Chu-Liu / Edmonds Directed Cycle-Contraction Sweep...");

        long totalCost = 0L;
        int currentN = numVertices;
        int currentRoot = root;
        List<DirectedEdge> currentEdges = new ArrayList<>(initialEdges);
        int phase = 0;

        while (true) {
            phase++;
            long[] minInCost = new long[currentN];
            int[] parent = new int[currentN];
            Arrays.fill(minInCost, INF);
            Arrays.fill(parent, -1);

            // Step 1: Greedily pick the minimum-cost incoming edge for every non-root vertex
            for (DirectedEdge e : currentEdges) {
                if (e.u != e.v && e.weight < minInCost[e.v]) {
                    minInCost[e.v] = e.weight;
                    parent[e.v] = e.u;
                }
            }

            minInCost[currentRoot] = 0L;
            for (int v = 0; v < currentN; v++) {
                if (v != currentRoot && minInCost[v] == INF) {
                    return -1L; // Vertex 'v' is unreachable from the root
                }
            }

            // Step 2: Detect directed cycles among the greedily selected incoming edges
            int[] visitedStamp = new int[currentN];
            int[] cycleId = new int[currentN];
            Arrays.fill(visitedStamp, -1);
            Arrays.fill(cycleId, -1);

            int cycleCount = 0;
            long phaseBaseCost = 0L;

            for (int i = 0; i < currentN; i++) {
                phaseBaseCost += minInCost[i];
                int curr = i;

                while (visitedStamp[curr] != i && cycleId[curr] == -1 && curr != currentRoot) {
                    visitedStamp[curr] = i;
                    curr = parent[curr];
                }

                // A brand-new directed cycle was closed at 'curr'
                if (curr != currentRoot && cycleId[curr] == -1) {
                    for (int u = parent[curr]; u != curr; u = parent[u]) {
                        cycleId[u] = cycleCount;
                    }
                    cycleId[curr] = cycleCount;
                    cycleCount++;
                }
            }

            totalCost += phaseBaseCost;
            System.out.printf(
                "    -> Phase %d | Active Nodes: %d | Phase Base Cost: %d | Directed Cycles Found: %d%n",
                phase, currentN, phaseBaseCost, cycleCount
            );

            // Base Case: Zero cycles -> Optimal Directed Arborescence isolated!
            if (cycleCount == 0) {
                return totalCost;
            }

            // Step 3: Assign unique super-vertex IDs to all non-cycle vertices
            for (int i = 0; i < currentN; i++) {
                if (cycleId[i] == -1) {
                    cycleId[i] = cycleCount++;
                }
            }

            // Step 4: Contract cycles and reweight cross-cycle edges to marginal swap cost (w - minIn[v])
            List<DirectedEdge> contractedEdges = new ArrayList<>();
            for (DirectedEdge e : currentEdges) {
                int superU = cycleId[e.u];
                int superV = cycleId[e.v];
                if (superU != superV) {
                    contractedEdges.add(new DirectedEdge(superU, superV, e.weight - minInCost[e.v]));
                }
            }

            currentN = cycleCount;
            currentRoot = cycleId[currentRoot];
            currentEdges = contractedEdges;
        }
    }

    // Exhaustive O(E^(V-1)) small-graph oracle to verify mathematical optimality
    private static long bruteForceVerify(int n, int root, List<DirectedEdge> edges) {
        List<List<DirectedEdge>> incoming = new ArrayList<>(n);
        for (int i = 0; i < n; i++) incoming.add(new ArrayList<>());
        for (DirectedEdge e : edges) {
            if (e.v != root && e.u != e.v) incoming.get(e.v).add(e);
        }
        return dfsVerify(1, n, root, incoming, new int[n], 0L);
    }

    private static long dfsVerify(
            int node, int n, int root, List<List<DirectedEdge>> incoming, int[] chosenParent, long costSoFar) {
        if (node == n) {
            // Confirm no directed cycle exists (every vertex reaches 'root' within n steps)
            for (int v = 0; v < n; v++) {
                int curr = v;
                int steps = 0;
                while (curr != root && steps <= n) {
                    curr = chosenParent[curr];
                    steps++;
                }
                if (curr != root) return INF;
            }
            return costSoFar;
        }
        long best = INF;
        for (DirectedEdge e : incoming.get(node)) {
            chosenParent[node] = e.u;
            best = Math.min(best, dfsVerify(node + 1, n, root, incoming, chosenParent, costSoFar + e.weight));
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Chu-Liu / Edmonds Directed Spanning Arborescence Engine ---");

        // 5-Node Asymmetric Broadcast Graph (Root = 0)
        // Trap: Raw edge (0 -> 1) costs 11 (cheaper than 0 -> 2 at 12),
        // yet entering Cycle {1 -> 2 -> 3 -> 1} at Node 2 eliminates the cost-4 edge (1 -> 2),
        // yielding marginal cost (12 - 4 = 8) vs entering at Node 1 (11 - 1 = 10)!
        List<DirectedEdge> relayEdges = Arrays.asList(
            new DirectedEdge(0, 1, 11),
            new DirectedEdge(0, 2, 12),
            new DirectedEdge(1, 2, 4),
            new DirectedEdge(2, 3, 3),
            new DirectedEdge(3, 1, 1),
            new DirectedEdge(3, 4, 6),
            new DirectedEdge(1, 4, 2)
        );

        long edmondsCost = computeMinimumArborescence(5, 0, relayEdges);
        long verifiedCost = bruteForceVerify(5, 0, relayEdges);

        System.out.println("\n--- Directed Arborescence Verification ---");
        System.out.println("  Chu-Liu / Edmonds Optimal Cost : " + edmondsCost);
        System.out.println("  Exhaustive Oracle Verification : " + verifiedCost);
        System.out.println("  Exact Mathematical Match?      : " + (edmondsCost == verifiedCost));
        System.out.println("  Optimal Directed Tree          : 0 -> 2 (12), 2 -> 3 (3), 3 -> 1 (1), 1 -> 4 (2) = 18");

        System.out.println("\nStatus: Directed cyclic dependency traps resolved in O(V * E) via marginal edge reweighting.");
    }
}