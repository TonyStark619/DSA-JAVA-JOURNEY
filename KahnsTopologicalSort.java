import java.util.*;

public class KahnsTopologicalSort {

    // O(V + E) Time, O(V) Space - Dependency Resolution Engine
    public static List<Integer> resolveDependencies(int vertices, List<List<Integer>> graph) {
        int[] inDegree = new int[vertices];

        System.out.println("Executing O(V+E) Kahn's Algorithm Dependency Scan...");
        
        // Step 1: Calculate the In-Degree (incoming edges) for every single node
        for (int i = 0; i < vertices; i++) {
            for (int neighbor : graph.get(i)) {
                inDegree[neighbor]++;
            }
        }

        // Step 2: Push all independent nodes (0 dependencies) into a Queue
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < vertices; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        List<Integer> executionOrder = new ArrayList<>();

        // Step 3: Process nodes and cascade the dependency unlocks
        while (!queue.isEmpty()) {
            int current = queue.poll();
            executionOrder.add(current);

            // "Delete" the outgoing edges from the processed node
            for (int neighbor : graph.get(current)) {
                inDegree[neighbor]--;
                
                // If the neighbor now has 0 dependencies, it is unlocked for processing
                if (inDegree[neighbor] == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // Step 4: Circular Dependency (Deadlock) Check
        if (executionOrder.size() != vertices) {
            System.out.println("CRITICAL FAILURE: Circular dependency (Cycle) detected.");
            System.out.println("System mathematically cannot be resolved.");
            return new ArrayList<>(); // Return empty to indicate failure
        }

        return executionOrder;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Topological Sort (Dependency Resolver) ---");
        
        int totalPackages = 6;
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < totalPackages; i++) graph.add(new ArrayList<>());

        // Simulating Dependencies: 
        // 5 depends on 2 and 0
        // 4 depends on 0 and 1
        // 2 depends on 3, 3 depends on 1
        graph.get(5).add(2);
        graph.get(5).add(0);
        graph.get(4).add(0);
        graph.get(4).add(1);
        graph.get(2).add(3);
        graph.get(3).add(1);

        List<Integer> optimalOrder = resolveDependencies(totalPackages, graph);
        
        if (!optimalOrder.isEmpty()) {
            System.out.println("\n--- Optimal Execution Sequence Generated ---");
            System.out.println("Compile Order: " + optimalOrder);
            System.out.println("Status: Deadlocks avoided. Execution topology secured.");
        }
    }
}