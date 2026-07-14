import java.util.LinkedList;

public class GraphDFS {
    private int vertices;
    private LinkedList<Integer>[] adjList;

    @SuppressWarnings("unchecked")
    public GraphDFS(int vertices) {
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

    // O(V + E) Time Complexity | O(V) Space Complexity (Call Stack)
    public void traverseDFS(int startVertex) {
        boolean[] visited = new boolean[vertices];
        System.out.print("DFS Traversal Order: ");
        
        // Initiate the recursive plunge
        dfsRecursive(startVertex, visited);
        
        System.out.println("END");
    }

    private void dfsRecursive(int vertex, boolean[] visited) {
        // 1. Mark the current node as securely visited
        visited[vertex] = true;
        System.out.print(vertex + " -> ");

        // 2. Iterate through all direct neighbors
        for (int neighbor : adjList[vertex]) {
            // 3. If a neighbor is unvisited, immediately plunge deeper down that branch
            if (!visited[neighbor]) {
                dfsRecursive(neighbor, visited);
            }
        }
        // When the loop finishes, the call stack automatically backtracks
    }

    public static void main(String[] args) {
        GraphDFS graph = new GraphDFS(6);
        System.out.println("--- Booting Deep Network Penetration Engine ---");

        // Constructing a connected network topology
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(2, 4);
        graph.addEdge(3, 5);
        graph.addEdge(4, 5);

        graph.traverseDFS(0);
        
        System.out.println("\nStatus: Recursive pathfinding successfully mapped the network depth.");
    }
}