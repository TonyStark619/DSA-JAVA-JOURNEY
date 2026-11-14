import java.util.*;

public class EdmondsBlossomMatchingEngine {

    private final int n;
    private final List<List<Integer>> graph;
    private final int[] match;       // match[u] = partner of u (-1 if exposed)
    private final int[] parent;      // Alternating tree parent pointer
    private final int[] base;        // Representative base vertex of the contracted blossom
    private final int[] state;       // -1: Unvisited, 0: Even (Outer) node, 1: Odd (Inner) node
    private final boolean[] inQueue;
    private final boolean[] inBlossom;
    private final int[] lcaMarker;
    private int lcaTimer = 0;

    public EdmondsBlossomMatchingEngine(int vertices) {
        this.n = vertices;
        this.graph = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        this.match = new int[n];
        this.parent = new int[n];
        this.base = new int[n];
        this.state = new int[n];
        this.inQueue = new boolean[n];
        this.inBlossom = new boolean[n];
        this.lcaMarker = new int[n];
        Arrays.fill(match, -1);
    }

    public void addUndirectedEdge(int u, int v) {
        graph.get(u).add(v);
        graph.get(v).add(u);
    }

    // Finds the Lowest Common Ancestor of two Even nodes in the alternating tree
    private int findBlossomBaseLCA(int a, int b) {
        lcaTimer++;
        a = base[a];
        b = base[b];
        while (true) {
            if (a != -1) {
                if (lcaMarker[a] == lcaTimer) return a;
                lcaMarker[a] = lcaTimer;
                a = (match[a] == -1 || parent[match[a]] == -1) ? -1 : base[parent[match[a]]];
            }
            int temp = a;
            a = b;
            b = temp;
        }
    }

    // Marks one side of the odd cycle (from vertex 'v' up to 'blossomBase') for contraction
    private void markBlossomPath(int v, int blossomBase, int child) {
        while (base[v] != blossomBase) {
            inBlossom[base[v]] = inBlossom[base[match[v]]] = true;
            parent[v] = child;
            child = match[v];
            v = parent[match[v]];
        }
    }

    // BFS from an exposed root vertex with dynamic odd-cycle (Blossom) contraction
    private boolean augmentFromRoot(int root, Queue<Integer> queue) {
        Arrays.fill(state, -1);
        Arrays.fill(parent, -1);
        Arrays.fill(inQueue, false);
        for (int i = 0; i < n; i++) {
            base[i] = i;
        }

        queue.clear();
        state[root] = 0; // Root is an Even (Outer) node
        queue.add(root);
        inQueue[root] = true;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v : graph.get(u)) {
                // Ignore self-loops inside an already contracted blossom or the active matched edge
                if (base[u] == base[v] || match[u] == v) continue;

                // Case 1: Edge connects two Even nodes -> Odd Cycle (Blossom) detected!
                if (v == root || (match[v] != -1 && parent[match[v]] != -1)) {
                    int curBase = findBlossomBaseLCA(u, v);
                    Arrays.fill(inBlossom, false);
                    markBlossomPath(u, curBase, v);
                    markBlossomPath(v, curBase, u);

                    // Contract all vertices in the odd cycle into 'curBase' and enqueue newly Even nodes
                    for (int i = 0; i < n; i++) {
                        if (inBlossom[base[i]]) {
                            base[i] = curBase;
                            if (!inQueue[i]) {
                                state[i] = 0;
                                queue.add(i);
                                inQueue[i] = true;
                            }
                        }
                    }
                } 
                // Case 2: Unvisited tree vertex encountered
                else if (parent[v] == -1) {
                    parent[v] = u;
                    state[v] = 1; // Odd (Inner) node

                    // If 'v' is completely exposed, we found an augmenting path! Flip edges along the path.
                    if (match[v] == -1) {
                        int curr = v;
                        while (curr != -1) {
                            int p = parent[curr];
                            int nextCurr = (p == -1) ? -1 : match[p];
                            match[curr] = p;
                            if (p != -1) match[p] = curr;
                            curr = nextCurr;
                        }
                        return true;
                    } 
                    // Otherwise extend the alternating tree through v's current partner
                    else {
                        int partner = match[v];
                        state[partner] = 0;
                        queue.add(partner);
                        inQueue[partner] = true;
                    }
                }
            }
        }
        return false;
    }

    // O(V^3) - Computes Maximum Cardinality Matching on an arbitrary non-bipartite graph
    public int computeMaximumGeneralMatching() {
        System.out.println("  [System] Executing Edmonds' Blossom Odd-Cycle Contraction Sweep...");
        int matchedPairs = 0;
        Queue<Integer> queue = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            if (match[i] == -1) {
                if (augmentFromRoot(i, queue)) {
                    matchedPairs++;
                }
            }
        }
        return matchedPairs;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Jack Edmonds' Blossom General Graph Matching Engine ---");

        // Non-Bipartite 6-Node Mesh containing a 5-Cycle Blossom (0-1-2-3-4-0) plus stem (2-5)
        // Bipartite algorithms fail here because 0-1-2-3-4 is an odd cycle of length 5!
        EdmondsBlossomMatchingEngine blossom = new EdmondsBlossomMatchingEngine(6);
        blossom.addUndirectedEdge(0, 1);
        blossom.addUndirectedEdge(1, 2);
        blossom.addUndirectedEdge(2, 3);
        blossom.addUndirectedEdge(3, 4);
        blossom.addUndirectedEdge(4, 0); // Closes the 5-node odd cycle (Blossom)
        blossom.addUndirectedEdge(2, 5); // Stem edge allowing all 6 nodes (3 pairs) to match!

        int maxPairs = blossom.computeMaximumGeneralMatching();

        System.out.println("\n--- General Graph Matching Diagnostics ---");
        System.out.println("  Maximum Disjoint Pairs Isolated : " + maxPairs + " (Covers " + (maxPairs * 2) + "/6 nodes)");
        for (int u = 0; u < 6; u++) {
            if (u < blossom.match[u]) {
                System.out.println("    -> Peer Node " + u + " <===> Peer Node " + blossom.match[u]);
            }
        }

        System.out.println("\nStatus: Odd-cycle parity traps neutralized in O(V^3) via dynamic Blossom base contraction.");
    }
}