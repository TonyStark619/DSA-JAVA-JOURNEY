import java.util.*;

public class HierholzersEulerianEngine {

    private int vertices;
    private Map<Integer, Stack<Integer>> adjacencyList;
    private int[] inDegree;
    private int[] outDegree;

    public HierholzersEulerianEngine(int v) {
        this.vertices = v;
        adjacencyList = new HashMap<>();
        for (int i = 0; i < v; i++) {
            adjacencyList.put(i, new Stack<>());
        }
        inDegree = new int[v];
        outDegree = new int[v];
    }

    public void addDirectedEdge(int u, int v) {
        adjacencyList.get(u).push(v);
        outDegree[u]++;
        inDegree[v]++;
    }

    // O(V) - Mathematically verifies if traversing every edge exactly once is possible
    private boolean hasEulerianCircuit() {
        for (int i = 0; i < vertices; i++) {
            if (inDegree[i] != outDegree[i]) {
                return false;
            }
        }
        return true;
    }

    // O(E) - The Core Graph Sweeper
    public List<Integer> findEulerianCircuit(int startNode) {
        System.out.println("  [System] Executing O(E) Hierholzer's Sweep...");
        
        if (!hasEulerianCircuit()) {
            System.out.println("  [ALERT] Network geometry mathematically prohibits a perfect Eulerian Circuit.");
            return new ArrayList<>();
        }

        Stack<Integer> currentPath = new Stack<>();
        List<Integer> finalCircuit = new ArrayList<>();
        
        currentPath.push(startNode);
        int currentNode = startNode;

        while (!currentPath.isEmpty()) {
            // If the current node has unvisited outgoing edges, we keep driving forward
            if (!adjacencyList.get(currentNode).isEmpty()) {
                currentPath.push(currentNode);
                // We consume the edge so we never traverse it again
                int nextNode = adjacencyList.get(currentNode).pop(); 
                currentNode = nextNode;
            } 
            // If we hit a dead end, we have completed a loop. 
            // We lock this node into our final path and backtrack to stitch other loops.
            else {
                finalCircuit.add(currentNode);
                currentNode = currentPath.pop();
            }
        }

        // The algorithm generates the path in reverse order from the dead ends
        Collections.reverse(finalCircuit);
        return finalCircuit;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Hierholzer's Eulerian Circuit Architecture ---");
        
        HierholzersEulerianEngine network = new HierholzersEulerianEngine(5);
        
        // A complex interconnected graph with overlapping loops
        network.addDirectedEdge(0, 1);
        network.addDirectedEdge(1, 2);
        network.addDirectedEdge(2, 0);
        
        network.addDirectedEdge(1, 3);
        network.addDirectedEdge(3, 4);
        network.addDirectedEdge(4, 1);
        
        System.out.println("\n[Network] Initializing continuous edge-routing payload...");
        
        List<Integer> optimalPath = network.findEulerianCircuit(0);
        
        if (!optimalPath.isEmpty()) {
            System.out.println("\nCRITICAL RESULT: Eulerian Circuit successfully isolated:");
            for (int i = 0; i < optimalPath.size(); i++) {
                System.out.print(optimalPath.get(i) + (i == optimalPath.size() - 1 ? "" : " -> "));
            }
            System.out.println("\n\nStatus: Every network edge traversed exactly once in strict linear time.");
        }
    }
}