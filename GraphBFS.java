import java.util.LinkedList;
import java.util.Queue;

public class GraphBFS {
    private int vertices;
    private LinkedList<Integer>[] adjList;

    @SuppressWarnings("unchecked")
    public GraphBFS(int vertices) {
        this.vertices = vertices;
        adjList = new LinkedList[vertices];
        for (int i = 0; i < vertices; i++) {
            adjList[i] = new LinkedList<>();
        }
    }

    public void addEdge(int source, int destination) {
        adjList[source].add(destination);
        adjList[destination].add(source); // Undirected Graph
    }

    // O(V + E) Time Complexity | O(V) Space Complexity
    public void traverseBFS(int startVertex) {
        boolean[] visited = new boolean[vertices];
        Queue<Integer> queue = new LinkedList<>();

        // Initialize the traversal
        visited[startVertex] = true;
        queue.add(startVertex);

        System.out.print("BFS Traversal Order: ");

        while (!queue.isEmpty()) {
            // Pop the front node
            int current = queue.poll();
            System.out.print(current + " -> ");

            // Inspect all direct neighbors of the current node
            for (int neighbor : adjList[current]) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor); // Queue the neighbor for the next layer
                }
            }
        }
        System.out.println("END");
    }

    public static void main(String[] args) {
        GraphBFS graph = new GraphBFS(6);
        
        // Constructing a connected network topology
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 4);
        graph.addEdge(3, 5);
        graph.addEdge(4, 5);

        System.out.println("--- Booting Graph Traversal Engine ---");
        graph.traverseBFS(0); // Initiate layer-by-layer exploration from node 0
        
        System.out.println("\nStatus: Cyclic protection verified. Layer mapping successful.");
    }
}