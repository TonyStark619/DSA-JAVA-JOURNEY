public class LinkCutDynamicTreeEngine {

    static class Node {
        final int id;
        long val;
        long sum;
        boolean rev; // Lazy path-reversal flag for O(log N) makeRoot()
        Node left;
        Node right;
        Node parent; // Either a Splay-Tree parent (solid) or a Path-Parent pointer (dashed)

        public Node(int id, long val) {
            this.id = id;
            this.val = val;
            this.sum = val;
        }
    }

    private final Node[] nodes;

    public LinkCutDynamicTreeEngine(long[] initialNodeWeights) {
        int n = initialNodeWeights.length;
        this.nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i, initialNodeWeights[i]);
        }
    }

    // Returns true if 'x' is the root of its auxiliary Splay Tree (its parent pointer is a dashed path-parent)
    private boolean isSplayRoot(Node x) {
        return x.parent == null || (x.parent.left != x && x.parent.right != x);
    }

    // Recomputes subtree path aggregate sum from solid Splay children
    private void pullUp(Node x) {
        x.sum = x.val
                + (x.left != null ? x.left.sum : 0L)
                + (x.right != null ? x.right.sum : 0L);
    }

    // Lazily reverses the depth ordering of an auxiliary Splay subtree
    private void applyReverse(Node x) {
        if (x == null) return;
        Node temp = x.left;
        x.left = x.right;
        x.right = temp;
        x.rev = !x.rev;
    }

    private void pushDown(Node x) {
        if (x.rev) {
            applyReverse(x.left);
            applyReverse(x.right);
            x.rev = false;
        }
    }

    // Flushes lazy reversal tags top-down from the Splay Root to 'x' prior to rotations
    private void pushPathFromSplayRoot(Node x) {
        if (!isSplayRoot(x)) {
            pushPathFromSplayRoot(x.parent);
        }
        pushDown(x);
    }

    // Standard Splay Tree Zig Rotation preserving dashed path-parent pointers
    private void rotate(Node x) {
        Node p = x.parent;
        Node g = p.parent;
        boolean isLeftChild = (p.left == x);

        if (!isSplayRoot(p)) {
            if (g.left == p) g.left = x;
            else g.right = x;
        }
        x.parent = g;

        if (isLeftChild) {
            p.left = x.right;
            if (x.right != null) x.right.parent = p;
            x.right = p;
        } else {
            p.right = x.left;
            if (x.left != null) x.left.parent = p;
            x.left = p;
        }
        p.parent = x;

        pullUp(p);
        pullUp(x);
    }

    // Amortized O(log N) - Brings 'x' to the root of its auxiliary Splay Tree
    private void splay(Node x) {
        pushPathFromSplayRoot(x);
        while (!isSplayRoot(x)) {
            Node p = x.parent;
            Node g = p.parent;
            if (!isSplayRoot(p)) {
                boolean zigZig = (g.left == p) == (p.left == x);
                if (zigZig) rotate(p);
                else rotate(x);
            }
            rotate(x);
        }
    }

    // Core Link-Cut Primitive: Exposes the root-to-x path as a single preferred Splay Tree
    private Node access(Node x) {
        Node lastPreferred = null;
        for (Node curr = x; curr != null; curr = curr.parent) {
            splay(curr);
            curr.right = lastPreferred; // Detaches old preferred child into a dashed edge
            pullUp(curr);
            lastPreferred = curr;
        }
        splay(x);
        return lastPreferred;
    }

    // Re-roots the represented tree so 'x' becomes its shallowest vertex
    public void makeRoot(int u) {
        Node nu = nodes[u];
        access(nu);
        applyReverse(nu);
    }

    // Locates the canonical root of the represented tree containing 'u'
    public Node findRoot(Node x) {
        access(x);
        while (true) {
            pushDown(x);
            if (x.left == null) break;
            x = x.left;
        }
        splay(x);
        return x;
    }

    // Amortized O(log N) - Online connectivity check
    public boolean isConnected(int u, int v) {
        if (u == v) return true;
        return findRoot(nodes[u]) == findRoot(nodes[v]);
    }

    // Amortized O(log N) - Online edge insertion between two disconnected trees
    public boolean link(int u, int v) {
        Node nu = nodes[u];
        Node nv = nodes[v];
        makeRoot(u);
        if (findRoot(nv) == nu) {
            return false; // Already connected; linking would violate tree acyclicity
        }
        nu.parent = nv; // Attach as a dashed child
        return true;
    }

    // Amortized O(log N) - Online edge deletion between directly connected vertices
    public boolean cut(int u, int v) {
        Node nu = nodes[u];
        Node nv = nodes[v];
        makeRoot(u);
        access(nv);
        // If (u, v) is a direct edge, 'nu' is strictly the immediate left child of 'nv' with no right subtree
        if (nv.left == nu && nu.right == null) {
            nv.left = null;
            nu.parent = null;
            pullUp(nv);
            return true;
        }
        return false;
    }

    // Amortized O(log N) - Mutates a single node's latency weight online
    public void updateNodeWeight(int u, long newWeight) {
        Node nu = nodes[u];
        splay(nu);
        nu.val = newWeight;
        pullUp(nu);
    }

    // Amortized O(log N) - Computes exact aggregate weight along the unique path from u to v
    public long queryPathSum(int u, int v) {
        makeRoot(u);
        access(nodes[v]);
        return nodes[v].sum;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Sleator-Tarjan Link-Cut Dynamic Tree Engine ---");

        // 6 Compute Nodes (0..5) with individual hop latencies in ms
        long[] hopLatencies = {10, 20, 30, 40, 50, 60};
        LinkCutDynamicTreeEngine lct = new LinkCutDynamicTreeEngine(hopLatencies);

        System.out.println("  [Online Topology] Linking initial backbone: 0-1-2-3-4 and branch 2-5...");
        lct.link(0, 1);
        lct.link(1, 2);
        lct.link(2, 3);
        lct.link(3, 4);
        lct.link(2, 5);

        // Initial path 0 -> 1 -> 2 -> 3 -> 4 = 10 + 20 + 30 + 40 + 50 = 150 ms
        System.out.println("  → Path Latency (0 -> 4) Initial          : " + lct.queryPathSum(0, 4) + " ms");

        System.out.println("\n--- Executing Real-Time Online Cut & Reroute Surgery ---");
        boolean severed = lct.cut(2, 3);
        System.out.println("  → Severed Bridge (2 - 3) Online?         : " + severed);
        System.out.println("  → Are Nodes 0 and 4 still connected?     : " + lct.isConnected(0, 4));

        // Reroute Node 4 online through Backup Node 5
        lct.link(5, 4);
        System.out.println("  → Linked Backup Bridge (5 - 4) Online! Connected? : " + lct.isConnected(0, 4));

        // New path 0 -> 1 -> 2 -> 5 -> 4 = 10 + 20 + 30 + 60 + 50 = 170 ms
        System.out.println("  → Rerouted Path Latency (0 -> 4)         : " + lct.queryPathSum(0, 4) + " ms");

        // Dynamically optimize Node 5 latency from 60 ms down to 15 ms
        lct.updateNodeWeight(5, 15);
        // Updated path 0 -> 1 -> 2 -> 5 -> 4 = 10 + 20 + 30 + 15 + 50 = 125 ms
        System.out.println("  → Post-Optimization Latency (0 -> 4)     : " + lct.queryPathSum(0, 4) + " ms");

        System.out.println("\nStatus: Online link, cut, re-root, and path-sum queries resolved in amortized O(log N) time.");
    }
}