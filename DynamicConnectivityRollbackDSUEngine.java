import java.util.*;

public class DynamicConnectivityRollbackDSUEngine {

    static class Edge {
        final int u;
        final int v;

        public Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    // Rollback Disjoint Set Union: Union-by-Rank + Explicit Mutation Undo Stack
    static class RollbackDSU {
        private final int[] parent;
        private final int[] rank;
        private int activeComponents;

        // Stack stores [childNode, oldParentRank, previousComponentCount]
        private final Deque<int[]> historyStack = new ArrayDeque<>();

        public RollbackDSU(int n) {
            this.parent = new int[n];
            this.rank = new int[n];
            this.activeComponents = n;
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        // O(log N) - Traverses parent pointers WITHOUT path compression to preserve rollback integrity
        public int findRoot(int x) {
            while (x != parent[x]) {
                x = parent[x];
            }
            return x;
        }

        // O(log N) - Merges two sets and pushes the exact delta onto the undo stack
        public void union(int u, int v) {
            int rootU = findRoot(u);
            int rootV = findRoot(v);
            if (rootU == rootV) {
                return;
            }

            // Ensure rootU is always the higher-or-equal rank root
            if (rank[rootU] < rank[rootV]) {
                int temp = rootU;
                rootU = rootV;
                rootV = temp;
            }

            // Record state prior to pointer mutation
            historyStack.push(new int[]{rootV, rootU, rank[rootU]});

            parent[rootV] = rootU;
            if (rank[rootU] == rank[rootV]) {
                rank[rootU]++;
            }
            activeComponents--;
        }

        public int captureCheckpoint() {
            return historyStack.size();
        }

        // O(1) per undone union - Restores DSU pointers to an exact historical checkpoint
        public void rollbackTo(int targetCheckpointSize) {
            while (historyStack.size() > targetCheckpointSize) {
                int[] record = historyStack.pop();
                int child = record[0];
                int par = record[1];
                int oldRank = record[2];

                parent[child] = child;
                rank[par] = oldRank;
                activeComponents++;
            }
        }

        public int getActiveComponents() {
            return activeComponents;
        }
    }

    private final int timeHorizon;
    private final List<Edge>[] timeTree;
    private final RollbackDSU dsu;
    private final int[] componentCountAtTime;

    @SuppressWarnings("unchecked")
    public DynamicConnectivityRollbackDSUEngine(int numVertices, int maxTimeSteps) {
        this.timeHorizon = maxTimeSteps;
        this.timeTree = new ArrayList[4 * maxTimeSteps];
        for (int i = 0; i < 4 * maxTimeSteps; i++) {
            timeTree[i] = new ArrayList<>();
        }
        this.dsu = new RollbackDSU(numVertices);
        this.componentCountAtTime = new int[maxTimeSteps];
    }

    // O(log T) - Registers an edge as active across the inclusive time window [startTime, endTime]
    public void addEdgeLifespan(int u, int v, int startTime, int endTime) {
        insertInterval(1, 0, timeHorizon - 1, startTime, endTime, new Edge(u, v));
    }

    private void insertInterval(int node, int l, int r, int ql, int qr, Edge edge) {
        if (ql > r || qr < l) return;
        if (ql <= l && r <= qr) {
            timeTree[node].add(edge);
            return;
        }
        int mid = l + (r - l) / 2;
        insertInterval(node << 1, l, mid, ql, qr, edge);
        insertInterval((node << 1) | 1, mid + 1, r, ql, qr, edge);
    }

    // O((N + Q log Q) log N) - Sweeps the Time Segment Tree with DSU Checkpointing
    public int[] resolveTimelineConnectivity() {
        System.out.println("  [System] Executing Time-Segment Tree DFS with Rollback DSU Checkpoints...");
        sweepTimeTree(1, 0, timeHorizon - 1);
        return componentCountAtTime;
    }

    private void sweepTimeTree(int node, int l, int r) {
        int checkpoint = dsu.captureCheckpoint();

        // 1. Activate all edges covering this canonical time interval
        for (Edge edge : timeTree[node]) {
            dsu.union(edge.u, edge.v);
        }

        // 2. If leaf timestamp reached, record the exact connected component count
        if (l == r) {
            componentCountAtTime[l] = dsu.getActiveComponents();
        } else {
            int mid = l + (r - l) / 2;
            sweepTimeTree(node << 1, l, mid);
            sweepTimeTree((node << 1) | 1, mid + 1, r);
        }

        // 3. Roll back DSU state to the exact checkpoint before entering this node
        dsu.rollbackTo(checkpoint);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Offline Dynamic Connectivity (Rollback DSU) Engine ---");

        // 5 Servers (0..4) tracked across 6 discrete time epochs (t = 0..5)
        DynamicConnectivityRollbackDSUEngine timeline = new DynamicConnectivityRollbackDSUEngine(5, 6);

        // Edge (0 - 1) is online from t = 0 to t = 4
        timeline.addEdgeLifespan(0, 1, 0, 4);
        // Edge (1 - 2) is online from t = 1 to t = 3
        timeline.addEdgeLifespan(1, 2, 1, 3);
        // Edge (3 - 4) is online from t = 2 to t = 5
        timeline.addEdgeLifespan(3, 4, 2, 5);
        // Bridge Edge (2 - 3) briefly links both clusters only at t = 2
        timeline.addEdgeLifespan(2, 3, 2, 2);

        int[] clusterCounts = timeline.resolveTimelineConnectivity();

        System.out.println("\n--- Timeline Connectivity Telemetry ---");
        // Expected:
        // t=0: {0,1}, {2}, {3}, {4}         -> 4 clusters
        // t=1: {0,1,2}, {3}, {4}            -> 3 clusters
        // t=2: {0,1,2,3,4} (Bridge active!) -> 1 cluster
        // t=3: {0,1,2}, {3,4}               -> 2 clusters
        // t=4: {0,1}, {2}, {3,4}            -> 3 clusters
        // t=5: {0}, {1}, {2}, {3,4}         -> 4 clusters
        for (int t = 0; t < clusterCounts.length; t++) {
            System.out.println("  Epoch t = " + t + " -> Active Isolated Server Clusters: " + clusterCounts[t]);
        }

        System.out.println("\nStatus: Dynamic edge deletions resolved in O(log N) without Link-Cut overhead via Rollback DSU.");
    }
}