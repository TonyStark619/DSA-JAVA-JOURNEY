import java.util.*;

public class MatroidIntersectionRainbowEngine {

    static class FiberLink {
        final int id;
        final int u;
        final int v;
        final int vendorColor;

        public FiberLink(int id, int u, int v, int vendorColor) {
            this.id = id;
            this.u = u;
            this.v = v;
            this.vendorColor = vendorColor;
        }
    }

    // Lightweight Disjoint Set Union for Graphic Matroid (M1) acyclicity oracles
    static class FastDSU {
        private final int[] parent;

        public FastDSU(int n) {
            this.parent = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }

        public int find(int x) {
            while (x != parent[x]) {
                parent[x] = parent[parent[x]];
                x = parent[x];
            }
            return x;
        }

        public boolean unite(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return false;
            parent[ra] = rb;
            return true;
        }
    }

    private final int numVertices;
    private final int numColors;
    private final List<FiberLink> links = new ArrayList<>();

    public MatroidIntersectionRainbowEngine(int vertices, int colors) {
        this.numVertices = vertices;
        this.numColors = colors;
    }

    public void addCandidateLink(int u, int v, int vendorColor) {
        links.add(new FiberLink(links.size(), u, v, vendorColor));
    }

    // Resolves the exact Maximum Common Independent Set of Graphic Matroid M1 and Partition Matroid M2
    public List<FiberLink> computeMaximumRainbowForest() {
        int m = links.size();
        boolean[] inSet = new boolean[m];

        System.out.println("  [System] Executing Matroid Intersection Exchange-Graph Augmentation...");

        while (true) {
            // 1. Build current Graphic Matroid (M1) DSU and Partition Matroid (M2) color usage
            FastDSU fullDsu = new FastDSU(numVertices);
            boolean[] usedColor = new boolean[numColors];

            for (int i = 0; i < m; i++) {
                if (inSet[i]) {
                    FiberLink e = links.get(i);
                    fullDsu.unite(e.u, e.v);
                    usedColor[e.vendorColor] = true;
                }
            }

            // 2. Identify Source Set X1 (independent in M1) and Target Set X2 (independent in M2)
            boolean[] inX1 = new boolean[m];
            boolean[] inX2 = new boolean[m];
            int trivialBridge = -1;

            for (int x = 0; x < m; x++) {
                if (!inSet[x]) {
                    FiberLink e = links.get(x);
                    if (fullDsu.find(e.u) != fullDsu.find(e.v)) {
                        inX1[x] = true;
                    }
                    if (!usedColor[e.vendorColor]) {
                        inX2[x] = true;
                    }
                    if (inX1[x] && inX2[x]) {
                        trivialBridge = x;
                        break;
                    }
                }
            }

            // Fast path: An edge exists that satisfies BOTH matroids immediately without swaps
            if (trivialBridge != -1) {
                inSet[trivialBridge] = true;
                System.out.println("    -> [Direct Add] Link #" + trivialBridge + " satisfies M1 and M2 directly.");
                continue;
            }

            // 3. Construct Directed Exchange Graph
            List<List<Integer>> exchangeAdj = new ArrayList<>(m);
            for (int i = 0; i < m; i++) exchangeAdj.add(new ArrayList<>());

            for (int y = 0; y < m; y++) {
                if (!inSet[y]) continue;

                // Oracle for M1 without edge 'y': Build DSU of (I \ {y})
                FastDSU dsuWithoutY = new FastDSU(numVertices);
                for (int k = 0; k < m; k++) {
                    if (inSet[k] && k != y) {
                        FiberLink ek = links.get(k);
                        dsuWithoutY.unite(ek.u, ek.v);
                    }
                }

                int colorY = links.get(y).vendorColor;

                for (int x = 0; x < m; x++) {
                    if (inSet[x]) continue;
                    FiberLink ex = links.get(x);

                    // Directed Edge y -> x if (I \ {y}) U {x} is Acyclic (M1 Exchange)
                    if (dsuWithoutY.find(ex.u) != dsuWithoutY.find(ex.v)) {
                        exchangeAdj.get(y).add(x);
                    }

                    // Directed Edge x -> y if (I \ {y}) U {x} has Unique Colors (M2 Exchange)
                    if (!usedColor[ex.vendorColor] || ex.vendorColor == colorY) {
                        exchangeAdj.get(x).add(y);
                    }
                }
            }

            // 4. Shortest Augmenting Path BFS from X1 to X2
            int[] parent = new int[m];
            Arrays.fill(parent, -1);
            boolean[] visited = new boolean[m];
            Queue<Integer> queue = new ArrayDeque<>();

            for (int x = 0; x < m; x++) {
                if (inX1[x]) {
                    queue.add(x);
                    visited[x] = true;
                }
            }

            int targetHit = -1;
            while (!queue.isEmpty()) {
                int curr = queue.poll();
                if (inX2[curr]) {
                    targetHit = curr;
                    break;
                }
                for (int nxt : exchangeAdj.get(curr)) {
                    if (!visited[nxt]) {
                        visited[nxt] = true;
                        parent[nxt] = curr;
                        queue.add(nxt);
                    }
                }
            }

            // If no augmenting path exists, I is proven globally optimal
            if (targetHit == -1) {
                break;
            }

            // 5. Symmetric Difference Toggle along the discovered shortest exchange path
            List<Integer> path = new ArrayList<>();
            for (int cur = targetHit; cur != -1; cur = parent[cur]) {
                inSet[cur] = !inSet[cur];
                path.add(cur);
            }
            Collections.reverse(path);
            System.out.println("    -> [Cascading Swap Path Executed] Links toggled: " + path);
        }

        List<FiberLink> optimalForest = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            if (inSet[i]) optimalForest.add(links.get(i));
        }
        return optimalForest;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Edmonds' Matroid Intersection (Rainbow Forest) Engine ---");

        // 5 Data Centers (0..4) across 4 Distinct Telecom Vendors (Colors 0..3)
        // Greedy locks into {Link 0, Link 1, Link 2} and stalls at size 3.
        // Matroid Intersection discovers the 5-link cascading swap (3 -> 0 -> 4 -> 1 -> 5) to hit size 4!
        MatroidIntersectionRainbowEngine engine = new MatroidIntersectionRainbowEngine(5, 4);

        engine.addCandidateLink(0, 1, 0); // Link 0: Vendor 0
        engine.addCandidateLink(1, 2, 1); // Link 1: Vendor 1
        engine.addCandidateLink(2, 3, 2); // Link 2: Vendor 2
        engine.addCandidateLink(3, 4, 0); // Link 3: Vendor 0 (Only bridge to Node 4!)
        engine.addCandidateLink(0, 2, 1); // Link 4: Vendor 1 (Bypass bridge)
        engine.addCandidateLink(1, 2, 3); // Link 5: Vendor 3 (Parallel redundancy)

        List<FiberLink> result = engine.computeMaximumRainbowForest();

        System.out.println("\n--- Optimal Dual-Constraint Spanning Topology ---");
        System.out.println("  Maximum Rainbow Links Selected : " + result.size() + " / 4 (Complete Spanning Tree)");
        for (FiberLink link : result) {
            System.out.println(
                "    -> Link #" + link.id + ": Node " + link.u + " <---> Node " + link.v
                + " | Telecom Vendor Color #" + link.vendorColor
            );
        }

        System.out.println("\nStatus: Greedy local-optimum trap broken via shortest exchange-graph augmentation.");
    }
}