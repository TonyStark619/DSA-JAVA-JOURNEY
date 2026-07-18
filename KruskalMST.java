import java.util.*;

public class KruskalMST {
    // 1. Define the Network Cable (Edge)
    static class Edge implements Comparable<Edge> {
        int source, destination, weight;
        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
        // Sort from cheapest to most expensive
        public int compareTo(Edge compareEdge) {
            return this.weight - compareEdge.weight;
        }
    }

    // 2. The O(1) Cycle Detection Engine (From Yesterday)
    static class DisjointSet {
        int[] parent, rank;
        public DisjointSet(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) parent[i] = i;
        }
        public int find(int i) {
            if (parent[i] == i) return i;
            return parent[i] = find(parent[i]); // Path Compression
        }
        public void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);
            if (rootI != rootJ) {
                if (rank[rootI] < rank[rootJ]) parent[rootI] = rootJ;
                else if (rank[rootI] > rank[rootJ]) parent[rootJ] = rootI;
                else {
                    parent[rootJ] = rootI;
                    rank[rootI]++;
                }
            }
        }
    }

    // 3. The Core Algorithm: O(E log E) Time
    public static void buildMinimumSpanningTree(int vertices, List<Edge> edges) {
        // Step A: Sort all cables by cost (Greedy Approach)
        Collections.sort(edges);
        
        DisjointSet dsu = new DisjointSet(vertices);
        int totalCost = 0;
        int edgesUsed = 0;

        System.out.println("Executing Kruskal's Network Optimization...");
        
        // Step B: Iterate through the sorted cables
        for (Edge edge : edges) {
            int rootSource = dsu.find(edge.source);
            int rootDest = dsu.find(edge.destination);

            // Step C: If their ultimate parents are different, adding this cable will NOT create a cycle
            if (rootSource != rootDest) {
                System.out.println("Connected City " + edge.source + " to City " + edge.destination + " [Cost: " + edge.weight + "]");
                totalCost += edge.weight;
                dsu.union(rootSource, rootDest);
                edgesUsed++;
            }

            // A tree connecting V vertices always has exactly V - 1 edges
            if (edgesUsed == vertices - 1) break; 
        }
        System.out.println("Total Minimum Infrastructure Cost: " + totalCost);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Kruskal's MST Architecture ---");
        int vertices = 4;
        List<Edge> networkEdges = new ArrayList<>();

        // (Source, Destination, Cost)
        networkEdges.add(new Edge(0, 1, 10));
        networkEdges.add(new Edge(0, 2, 6));
        networkEdges.add(new Edge(0, 3, 5));
        networkEdges.add(new Edge(1, 3, 15));
        networkEdges.add(new Edge(2, 3, 4));

        buildMinimumSpanningTree(vertices, networkEdges);
        System.out.println("\nStatus: Optimal, cycle-free network topology successfully deployed.");
    }
}