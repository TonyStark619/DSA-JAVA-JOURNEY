import java.util.LinkedList;

public class GraphFoundation {
    // The core architecture: An array of Linked Lists
    private LinkedList<Integer>[] adjList;
    private int vertices;

    @SuppressWarnings("unchecked")
    public GraphFoundation(int vertices) {
        this.vertices = vertices;
        adjList = new LinkedList[vertices];
        
        // Initialize an empty Linked List for every single node in the network
        for (int i = 0; i < vertices; i++) {
            adjList[i] = new LinkedList<>();
        }
    }

    // O(1) Time - Adding an undirected connection between two nodes
    public void addEdge(int source, int destination) {
        adjList[source].add(destination);
        adjList[destination].add(source); // Remove this line if the graph is strictly one-way (Directed)
    }

    // Diagnostic tool to verify the network topology
    public void displayArchitecture() {
        System.out.println("--- Booting Network Node Architecture ---");
        for (int i = 0; i < vertices; i++) {
            System.out.print("Node " + i + " is directly connected to: ");
            for (Integer neighbor : adjList[i]) {
                System.out.print(neighbor + " -> ");
            }
            System.out.println("END");
        }
    }

    public static void main(String[] args) {
        // Booting a network of 5 computers (0 through 4)
        GraphFoundation network = new GraphFoundation(5);
        
        // Wiring the network connections
        network.addEdge(0, 1);
        network.addEdge(0, 4);
        network.addEdge(1, 2);
        network.addEdge(1, 3);
        network.addEdge(1, 4);
        network.addEdge(2, 3);
        network.addEdge(3, 4);

        network.displayArchitecture();
        System.out.println("\nStatus: Adjacency List network mapped dynamically without O(V^2) matrix overhead.");
    }
}