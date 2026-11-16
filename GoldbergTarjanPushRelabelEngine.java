import java.util.*;

public class GoldbergTarjanPushRelabelEngine {

    private static final long INF = Long.MAX_VALUE / 4;

    private final int n;
    private final long[][] capacity;
    private final long[][] flow;
    private final long[] excess;
    private final int[] height;
    private final int[] heightCount; // Tracks vertex count per elevation for the Gap Heuristic
    private final int[] nextNeighborCursor;

    public GoldbergTarjanPushRelabelEngine(int vertices) {
        this.n = vertices;
        this.capacity = new long[n][n];
        this.flow = new long[n][n];
        this.excess = new long[n];
        this.height = new int[n];
        this.heightCount = new int[2 * n + 2];
        this.nextNeighborCursor = new int[n];
    }

    public void addDirectedLink(int u, int v, long cap) {
        capacity[u][v] += cap;
    }

    // O(1) - Pushes excess fluid downhill from u to v along an admissible edge (height[u] == height[v] + 1)
    private void pushFluid(int u, int v, Queue<Integer> activeQueue, boolean[] inQueue, int source, int sink) {
        long residualCap = capacity[u][v] - flow[u][v];
        long sendDelta = Math.min(excess[u], residualCap);
        if (sendDelta <= 0) return;

        flow[u][v] += sendDelta;
        flow[v][u] -= sendDelta;
        excess[u] -= sendDelta;
        excess[v] += sendDelta;

        if (v != source && v != sink && !inQueue[v] && excess[v] > 0) {
            activeQueue.add(v);
            inQueue[v] = true;
        }
    }

    // Raises the elevation of overflowing vertex 'u' and triggers the Gap Heuristic if a height layer empties
    private void relabelVertex(int u) {
        int oldHeight = height[u];
        int minNeighborHeight = 2 * n;

        for (int v = 0; v < n; v++) {
            if (capacity[u][v] - flow[u][v] > 0) {
                minNeighborHeight = Math.min(minNeighborHeight, height[v]);
            }
        }

        heightCount[oldHeight]--;
        height[u] = minNeighborHeight + 1;
        heightCount[height[u]]++;

        // THE GAP HEURISTIC:
        // If no vertices remain at 'oldHeight' (and 0 < oldHeight < n), every vertex with
        // (oldHeight < height[v] < n) is mathematically severed from the sink!
        // Catapult them immediately above the source (n + 1) so excess drains back to source.
        if (oldHeight > 0 && oldHeight < n && heightCount[oldHeight] == 0) {
            for (int v = 0; v < n; v++) {
                if (v != u && height[v] > oldHeight && height[v] <= n) {
                    heightCount[height[v]]--;
                    height[v] = n + 1;
                    heightCount[height[v]]++;
                }
            }
        }
    }

    // Discharges all excess flow from vertex 'u' via alternating Push and Relabel operations
    private void dischargeOverflowingNode(int u, Queue<Integer> activeQueue, boolean[] inQueue, int source, int sink) {
        while (excess[u] > 0) {
            int v = nextNeighborCursor[u];
            if (v < n) {
                if (capacity[u][v] - flow[u][v] > 0 && height[u] == height[v] + 1) {
                    pushFluid(u, v, activeQueue, inQueue, source, sink);
                } else {
                    nextNeighborCursor[u]++;
                }
            } else {
                relabelVertex(u);
                nextNeighborCursor[u] = 0;
            }
        }
    }

    // Computes Maximum s-t Flow using FIFO Preflow-Push + Gap Heuristic
    public long computeMaximumThroughput(int source, int sink) {
        System.out.println("  [System] Initializing Hydrostatic Preflow Saturation from Source " + source + "...");

        height[source] = n;
        heightCount[n] = 1;
        heightCount[0] = n - 1;
        excess[source] = INF;

        Queue<Integer> activeQueue = new ArrayDeque<>();
        boolean[] inQueue = new boolean[n];

        // Saturate all outgoing links from Source immediately
        for (int v = 0; v < n; v++) {
            if (capacity[source][v] > 0) {
                pushFluid(source, v, activeQueue, inQueue, source, sink);
            }
        }

        System.out.println("  [System] Executing Local Push-Relabel Discharge with Gap Pruning...");
        while (!activeQueue.isEmpty()) {
            int u = activeQueue.poll();
            inQueue[u] = false;
            dischargeOverflowingNode(u, activeQueue, inQueue, source, sink);
        }

        return excess[sink];
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Goldberg-Tarjan Push-Relabel Max-Flow Engine ---");

        // 6-Node Optical Switching Fabric (Source = 0, Sink = 5)
        GoldbergTarjanPushRelabelEngine fabric = new GoldbergTarjanPushRelabelEngine(6);
        fabric.addDirectedLink(0, 1, 16);
        fabric.addDirectedLink(0, 2, 13);
        fabric.addDirectedLink(1, 2, 10);
        fabric.addDirectedLink(1, 3, 12);
        fabric.addDirectedLink(2, 1, 4);
        fabric.addDirectedLink(2, 4, 14);
        fabric.addDirectedLink(3, 2, 9);
        fabric.addDirectedLink(3, 5, 20);
        fabric.addDirectedLink(4, 3, 7);
        fabric.addDirectedLink(4, 5, 4);

        long maxThroughput = fabric.computeMaximumThroughput(0, 5);

        System.out.println("\n--- Hydrostatic Equilibrium Telemetry ---");
        System.out.println("  Final Vertex Elevations (height) : " + Arrays.toString(fabric.height));
        System.out.println("  Maximum Sustained Fabric Flow    : " + maxThroughput + " Tbps"); // Expected: 23 Tbps
        System.out.println("\nStatus: Global path searches eliminated via local preflow-push and O(1) gap-layer pruning.");
    }
}