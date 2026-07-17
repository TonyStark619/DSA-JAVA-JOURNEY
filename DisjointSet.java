public class DisjointSet {
    private int[] parent;
    private int[] rank;

    // Initialize the architecture: Every node starts as its own independent boss
    public DisjointSet(int size) {
        parent = new int[size];
        rank = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i; 
            rank[i] = 0;   // Tree height starts at 0
        }
    }

    // O(1) Amortized Time - Path Compression
    public int findUltimateParent(int node) {
        // Base case: If the node is its own boss, we found the ultimate parent
        if (node == parent[node]) {
            return node;
        }
        // Path Compression: During the recursive return, we directly attach the node 
        // to the ultimate parent, flattening the tree and accelerating future lookups.
        return parent[node] = findUltimateParent(parent[node]);
    }

    // O(1) Amortized Time - Union by Rank
    public void unionByRank(int nodeU, int nodeV) {
        int ultimateParentU = findUltimateParent(nodeU);
        int ultimateParentV = findUltimateParent(nodeV);

        // If they share the same ultimate parent, they are already connected. A cycle exists.
        if (ultimateParentU == ultimateParentV) return;

        // Optimization: Always attach the smaller tree under the taller tree to keep the architecture flat
        if (rank[ultimateParentU] < rank[ultimateParentV]) {
            parent[ultimateParentU] = ultimateParentV;
        } 
        else if (rank[ultimateParentV] < rank[ultimateParentU]) {
            parent[ultimateParentV] = ultimateParentU;
        } 
        else {
            // If they are the same height, attach one to the other and increase the new boss's rank
            parent[ultimateParentV] = ultimateParentU;
            rank[ultimateParentU]++;
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting O(1) Network Cluster Engine ---");
        DisjointSet network = new DisjointSet(8);

        // Wiring the network clusters
        network.unionByRank(1, 2);
        network.unionByRank(2, 3);
        network.unionByRank(4, 5);
        network.unionByRank(6, 7);
        network.unionByRank(5, 6);

        // Checking connectivity dynamically without DFS
        System.out.println("Are nodes 1 and 3 in the same cluster? " + 
            (network.findUltimateParent(1) == network.findUltimateParent(3) ? "YES" : "NO"));
            
        System.out.println("Are nodes 1 and 5 in the same cluster? " + 
            (network.findUltimateParent(1) == network.findUltimateParent(5) ? "YES" : "NO"));

        // Connecting the two massive independent clusters
        System.out.println("\nExecuting cross-cluster Union(3, 4)...");
        network.unionByRank(3, 4);

        System.out.println("Are nodes 1 and 5 in the same cluster now? " + 
            (network.findUltimateParent(1) == network.findUltimateParent(5) ? "YES" : "NO"));
            
        System.out.println("\nStatus: Dynamic connectivity tracked with strict O(1) lookup efficiency.");
    }
}