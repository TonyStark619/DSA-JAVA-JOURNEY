import java.util.*;

public class BipartiteGraph {
    
    // O(V + E) Time, O(V) Space - BFS Graph Coloring Engine
    public boolean checkBipartite(int vertices, List<List<Integer>> adjList) {
        // -1 means the node is completely uncolored (unvisited)
        int[] colorMap = new int[vertices];
        Arrays.fill(colorMap, -1);

        // A graph can have disconnected sub-networks, so we must check every node
        for (int i = 0; i < vertices; i++) {
            if (colorMap[i] == -1) {
                // If we find an uncolored node, launch a BFS validation sequence
                if (!bfsColoringValidator(i, adjList, colorMap)) {
                    return false; // A conflict was detected
                }
            }
        }
        return true; // The entire network was safely colored
    }

    private boolean bfsColoringValidator(int startNode, List<List<Integer>> adjList, int[] colorMap) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(startNode);
        
        // Paint the starting node with Color 0
        colorMap[startNode] = 0;

        while (!queue.isEmpty()) {
            int current = queue.poll();

            for (int neighbor : adjList.get(current)) {
                // Case 1: The neighbor has never been visited.
                if (colorMap[neighbor] == -1) {
                    // Paint it the OPPOSITE color of the current node (1 - 0 = 1, 1 - 1 = 0)
                    colorMap[neighbor] = 1 - colorMap[current];
                    queue.add(neighbor);
                } 
                // Case 2: The neighbor IS visited, and it shares our exact color. 
                else if (colorMap[neighbor] == colorMap[current]) {
                    System.out.println("CRITICAL CONFLICT: Node " + current + " and Node " + neighbor + " share Color " + colorMap[current]);
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Graph Conflict Resolution Engine ---");
        BipartiteGraph engine = new BipartiteGraph();
        
        int vertices = 4;
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < vertices; i++) graph.add(new ArrayList<>());
        
        // Constructing a valid Bipartite Network (Even cycle: 4 nodes)
        // 0-1, 1-2, 2-3, 3-0
        graph.get(0).add(1); graph.get(1).add(0);
        graph.get(1).add(2); graph.get(2).add(1);
        graph.get(2).add(3); graph.get(3).add(2);
        graph.get(3).add(0); graph.get(0).add(3);

        System.out.println("Scanning Topology 1...");
        boolean isBipartite1 = engine.checkBipartite(vertices, graph);
        System.out.println("Is Topology 1 Safely Divisible? " + (isBipartite1 ? "YES" : "NO\n"));

        // Injecting a conflict (Odd cycle: triangle between 0, 1, and 2)
        graph.get(0).add(2); graph.get(2).add(0);
        
        System.out.println("Scanning Topology 2 (Conflict Injected)...");
        boolean isBipartite2 = engine.checkBipartite(vertices, graph);
        System.out.println("Is Topology 2 Safely Divisible? " + (isBipartite2 ? "YES" : "NO"));
    }
}