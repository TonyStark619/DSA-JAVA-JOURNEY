import java.util.*;

public class TopologicalSort {
    
    // O(V + E) Time - Dependency Resolution Engine
    public static void resolveDependencies(int vertices, int[][] edges) {
        List<List<Integer>> adjList = new ArrayList<>();
        int[] inDegree = new int[vertices];

        // 1. Initialize the architecture
        for (int i = 0; i < vertices; i++) {
            adjList.add(new ArrayList<>());
        }

        // 2. Build the Directed Graph and calculate In-Degrees (Prerequisites)
        for (int[] edge : edges) {
            int prereq = edge[0];
            int course = edge[1];
            adjList.get(prereq).add(course);
            inDegree[course]++; // This course has one more prerequisite
        }

        // 3. Load all tasks that require ZERO prerequisites into the Queue
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < vertices; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        List<Integer> executionOrder = new ArrayList<>();

        // 4. Execute Kahn's BFS Traversal
        while (!queue.isEmpty()) {
            int current = queue.poll();
            executionOrder.add(current);

            // Since we completed this task, remove it as a requirement for its neighbors
            for (int dependentCourse : adjList.get(current)) {
                inDegree[dependentCourse]--;
                
                // If a neighbor now has 0 remaining prerequisites, it is ready to execute
                if (inDegree[dependentCourse] == 0) {
                    queue.add(dependentCourse);
                }
            }
        }

        // 5. Verification: Did we complete all tasks, or is there a cyclic deadlock?
        if (executionOrder.size() == vertices) {
            System.out.println("Valid Execution Sequence: " + executionOrder);
        } else {
            System.out.println("CRITICAL FAILURE: Cyclic dependency detected. Deadlock.");
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Kahn's Dependency Resolution Protocol ---");
        
        // Simulating 6 Tasks (0 through 5)
        int vertices = 6;
        
        // Task 0 must happen before 2 and 3. Task 3 must happen before 1, etc.
        int[][] dependencies = {
            {5, 2}, {5, 0}, {4, 0}, {4, 1}, {2, 3}, {3, 1}
        };

        System.out.println("Ingesting Directed Acyclic Graph (DAG)...");
        resolveDependencies(vertices, dependencies);
        
        System.out.println("Status: Compilation order successfully mapped.");
    }
}