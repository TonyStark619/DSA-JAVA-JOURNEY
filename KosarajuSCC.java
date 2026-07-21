import java.util.*;

public class KosarajuSCC {
    private int vertices;
    private List<List<Integer>> adjList;
    private List<List<Integer>> transposedList;

    public KosarajuSCC(int vertices) {
        this.vertices = vertices;
        adjList = new ArrayList<>();
        transposedList = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            adjList.add(new ArrayList<>());
            transposedList.add(new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjList.get(source).add(destination);
        // Simultaneously build the reversed network for Step 2
        transposedList.get(destination).add(source); 
    }

    // Step 1: Standard DFS to determine the processing order
    private void fillOrderDFS(int node, boolean[] visited, Stack<Integer> stack) {
        visited[node] = true;
        for (int neighbor : adjList.get(node)) {
            if (!visited[neighbor]) {
                fillOrderDFS(neighbor, visited, stack);
            }
        }
        // Push to stack ONLY when the branch is completely exhausted
        stack.push(node);
    }

    // Step 3: DFS on the reversed graph to isolate the clusters
    private void isolateClusterDFS(int node, boolean[] visited) {
        visited[node] = true;
        System.out.print(node + " ");
        for (int neighbor : transposedList.get(node)) {
            if (!visited[neighbor]) {
                isolateClusterDFS(neighbor, visited);
            }
        }
    }

    // O(V + E) Time - The Core Engine
    public void findSCCs() {
        Stack<Integer> stack = new Stack<>();
        boolean[] visited = new boolean[vertices];

        // Pass 1: Map the topological structure
        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                fillOrderDFS(i, visited, stack);
            }
        }

        // Reset memory for the second pass
        Arrays.fill(visited, false);
        System.out.println("--- Booting O(V+E) Kosaraju Cluster Analysis ---");
        System.out.println("Isolated Strongly Connected Components found:");

        // Pass 2: Extract the clusters using the transposed graph
        int clusterCount = 1;
        while (!stack.isEmpty()) {
            int node = stack.pop();
            // If it's unvisited, it's the root of a new isolated cluster
            if (!visited[node]) {
                System.out.print("Cluster " + clusterCount + ": [ ");
                isolateClusterDFS(node, visited);
                System.out.println("]");
                clusterCount++;
            }
        }
    }

    public static void main(String[] args) {
        KosarajuSCC network = new KosarajuSCC(5);
        
        // Constructing a directed network with 2 distinct clusters
        network.addEdge(1, 0);
        network.addEdge(0, 2);
        network.addEdge(2, 1); // Forms a cycle (SCC) between 0, 1, 2
        network.addEdge(0, 3); // One-way bridge to the second cluster
        network.addEdge(3, 4); // Forms a smaller SCC (just node 3 and node 4 don't cycle back, wait)
        network.addEdge(4, 3); // Forms a cycle (SCC) between 3, 4

        network.findSCCs();
        System.out.println("\nStatus: Transposition and extraction complete. Sub-graphs identified.");
    }
}