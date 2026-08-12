public class DisjointSetUnion {
    private int[] parent;
    private int[] rank;

    // Initialize the architecture: Every node starts as its own independent network
    public DisjointSetUnion(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i; 
            rank[i] = 0;   
        }
    }

    // Optimization 1: Path Compression
    // As we search for the absolute root of the network, we instantly attach 
    // every node we pass directly to the root, flattening the structure.
    public int findUltimateParent(int node) {
        if (node == parent[node]) {
            return node;
        }
        // The recursive return assigns the ultimate root to the current node
        return parent[node] = findUltimateParent(parent[node]); 
    }

    // Optimization 2: Union by Rank
    // Connects two independent sub-networks in near O(1) time
    public void unionByRank(int u, int v) {
        int ultimateParentU = findUltimateParent(u);
        int ultimateParentV = findUltimateParent(v);

        // If they share the same root, they are already in the same network
        if (ultimateParentU == ultimateParentV) return;

        // Attach the smaller network under the larger network to prevent long chains
        if (rank[ultimateParentU] < rank[ultimateParentV]) {
            parent[ultimateParentU] = ultimateParentV;
        } else if (rank[ultimateParentV] < rank[ultimateParentU]) {
            parent[ultimateParentV] = ultimateParentU;
        } else {
            // If they are exactly the same height, pick one and increase its rank
            parent[ultimateParentV] = ultimateParentU;
            rank[ultimateParentU]++;
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting O(1) Dynamic Connectivity Engine (DSU) ---");
        
        int totalServers = 7;
        DisjointSetUnion network = new DisjointSetUnion(totalServers + 1); // 1-based indexing
        
        System.out.println("Executing Network Topology Merges...");
        network.unionByRank(1, 2);
        network.unionByRank(2, 3);
        network.unionByRank(4, 5);
        network.unionByRank(6, 7);
        network.unionByRank(5, 6);
        
        // Check connectivity between Server 3 and 7
        System.out.println("\nQuery: Are Server 3 and Server 7 connected?");
        if (network.findUltimateParent(3) == network.findUltimateParent(7)) {
            System.out.println("Result: YES - Data flow is possible.");
        } else {
            System.out.println("Result: NO - Network partition detected.");
        }
        
        // Merge the two isolated clusters
        System.out.println("\nExecuting Critical Infrastructure Merge (Server 3 -> Server 7)...");
        network.unionByRank(3, 7);
        
        // Re-check connectivity
        System.out.println("Query: Are Server 3 and Server 7 connected?");
        if (network.findUltimateParent(3) == network.findUltimateParent(7)) {
            System.out.println("Result: YES - Full network synchronization achieved.");
        }
    }
}