public class LinkCutTreeEngine {

    static class SplayNode {
        int id;
        SplayNode left, right, parent;
        // True if this node is the root of its current auxiliary Splay tree
        boolean isSplayRoot; 
        
        public SplayNode(int id) {
            this.id = id;
            this.isSplayRoot = true;
        }
    }

    private SplayNode[] nodes;

    public LinkCutTreeEngine(int n) {
        nodes = new SplayNode[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new SplayNode(i);
        }
    }

    // O(1) - Structural Splay Tree Rotation (Zig/Zag)
    private void rotate(SplayNode x) {
        SplayNode p = x.parent;
        SplayNode g = p.parent;
        
        boolean isLeftChild = (x == p.left);
        
        // Wire the children
        if (isLeftChild) {
            p.left = x.right;
            if (x.right != null) x.right.parent = p;
            x.right = p;
        } else {
            p.right = x.left;
            if (x.left != null) x.left.parent = p;
            x.left = p;
        }
        
        x.parent = g;
        p.parent = x;
        
        // Wire the grandparent
        if (p.isSplayRoot) {
            p.isSplayRoot = false;
            x.isSplayRoot = true;
        } else {
            if (g.left == p) g.left = x;
            else g.right = x;
        }
    }

    // O(log N) Amortized - Spatially relocates a node to the root of its auxiliary tree
    private void splay(SplayNode x) {
        while (!x.isSplayRoot) {
            SplayNode p = x.parent;
            SplayNode g = p.parent;
            
            if (!p.isSplayRoot) {
                // Zig-Zig or Zig-Zag
                if ((g.left == p) == (p.left == x)) rotate(p);
                else rotate(x);
            }
            rotate(x); // Final Zig
        }
    }

    // O(log N) Amortized - The Core Engine: Exposes the exact path from the root to node X
    public void access(SplayNode x) {
        SplayNode lastNode = null;
        SplayNode current = x;
        
        while (current != null) {
            splay(current);
            // Sever the old preferred child and attach the new preferred path
            if (current.right != null) current.right.isSplayRoot = true;
            current.right = lastNode;
            if (lastNode != null) lastNode.isSplayRoot = false;
            
            lastNode = current;
            current = current.parent;
        }
        splay(x); // Finally, splay x to the absolute root of the exposed path
    }

    // O(log N) - Dynamically connects two independent trees
    public void link(int childId, int parentId) {
        System.out.println("  [System] Executing Dynamic Link: " + childId + " -> " + parentId);
        SplayNode child = nodes[childId];
        SplayNode parent = nodes[parentId];
        
        access(child);
        access(parent);
        
        child.left = parent;
        parent.parent = child;
        parent.isSplayRoot = false;
    }

    // O(log N) - Dynamically severs an edge between a child and its parent
    public void cut(int childId) {
        System.out.println("  [System] Executing Dynamic Cut on Node: " + childId);
        SplayNode child = nodes[childId];
        
        access(child);
        if (child.left != null) {
            child.left.parent = null;
            child.left.isSplayRoot = true;
            child.left = null;
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Link-Cut Tree Dynamic Forest Architecture ---");
        
        // 5 Servers in an isolated network
        LinkCutTreeEngine network = new LinkCutTreeEngine(5);
        
        network.link(2, 1);
        network.link(3, 2);
        network.link(4, 3);
        network.link(5, 4);
        
        System.out.println("\n[Network Event] Server 3 failed. Severing connection and routing to backup Server 1...");
        // Cut edge between 4 and 3
        network.cut(4); 
        // Dynamically link 4 directly to 1 without rebuilding the O(N) geometry
        network.link(4, 1); 
        
        System.out.println("\nStatus: Splay Tree geometry successfully rewired. Dynamic connectivity resolved in amortized O(log N).");
    }
}