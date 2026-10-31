public class LinkCuttingTreeEngine {

    static class Node {
        int id;
        Node left, right, parent;
        boolean reverseFlag; // Used to flip the path direction geometrically

        public Node(int id) {
            this.id = id;
        }

        // Checks if this node is the root of its specific Splay Tree path
        boolean isRoot() {
            return parent == null || (parent.left != this && parent.right != this);
        }
    }

    private final Node[] nodes;

    public LinkCuttingTreeEngine(int n) {
        nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node(i);
        }
    }

    // O(1) - Pushes the lazy reversal flag down to children
    private void pushDown(Node x) {
        if (x.reverseFlag) {
            Node temp = x.left;
            x.left = x.right;
            x.right = temp;
            if (x.left != null) x.left.reverseFlag ^= true;
            if (x.right != null) x.right.reverseFlag ^= true;
            x.reverseFlag = false;
        }
    }

    // O(1) - Mathematically rotates a node upwards in the Splay Tree geometry
    private void rotate(Node x) {
        Node y = x.parent, z = y.parent;
        if (!y.isRoot()) {
            if (y == z.left) z.left = x;
            else z.right = x;
        }
        x.parent = z;
        
        if (x == y.left) {
            y.left = x.right;
            if (x.right != null) x.right.parent = y;
            x.right = y;
        } else {
            y.right = x.left;
            if (x.left != null) x.left.parent = y;
            x.left = y;
        }
        y.parent = x;
    }

    // Amortized O(log N) - Pulls a node to the absolute root of its Splay Tree
    private void splay(Node x) {
        while (!x.isRoot()) {
            Node y = x.parent, z = y.parent;
            if (!y.isRoot()) pushDown(z);
            pushDown(y);
            pushDown(x);
            
            if (!y.isRoot()) {
                if ((x == y.left) == (y == z.left)) rotate(y); // Zig-Zig
                else rotate(x); // Zig-Zag
            }
            rotate(x);
        }
        pushDown(x);
    }

    // Amortized O(log N) - Forges a continuous preferred path from the root down to X
    private void access(Node x) {
        Node last = null;
        for (Node y = x; y != null; y = y.parent) {
            splay(y);
            y.right = last; // Attach the lower part of the preferred path
            last = y;
        }
        splay(x);
    }

    // Amortized O(log N) - Makes X the absolute root of the entire represented network tree
    private void makeRoot(Node x) {
        access(x);
        x.reverseFlag ^= true;
    }

    // Amortized O(log N) - Dynamically connects two isolated network segments
    public void link(int uId, int vId) {
        Node u = nodes[uId];
        Node v = nodes[vId];
        makeRoot(u);
        u.parent = v;
        System.out.println("  [Network] Edge linked: Server " + uId + " <---> Server " + vId);
    }

    // Amortized O(log N) - Dynamically severs a physical connection in the network
    public void cut(int uId, int vId) {
        Node u = nodes[uId];
        Node v = nodes[vId];
        makeRoot(u);
        access(v);
        if (v.left == u && u.right == null) {
            v.left = null;
            u.parent = null;
            System.out.println("  [Network] Edge severed: Server " + uId + " -/ /- Server " + vId);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Link-Cut Dynamic Forest Architecture ---");
        
        LinkCuttingTreeEngine network = new LinkCuttingTreeEngine(5);
        
        System.out.println("  [System] Forging initial network topology...");
        network.link(1, 2);
        network.link(2, 3);
        network.link(3, 4);
        
        System.out.println("\n  [System] Executing dynamic topology mutation...");
        network.cut(2, 3); // Severing the network in half
        network.link(1, 5); // Re-routing through a new node
        
        System.out.println("\nStatus: Dynamic network edges linked and cut in strict O(log N) via Splay Tree geometric rotation.");
    }
}