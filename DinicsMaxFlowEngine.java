import java.util.*;

public class DinicsMaxFlowEngine {

    static class Edge {
        int to;
        int flow;
        int capacity;
        int reverseEdgeIndex; // Pointer to the residual (backward) edge

        public Edge(int to, int capacity, int reverseEdgeIndex) {
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
            this.reverseEdgeIndex = reverseEdgeIndex;
        }
    }

    private int V;
    private List<List<Edge>> graph;
    private int[] level; // Holds the BFS Level Graph geometry
    private int[] pointer; // Dead-end optimization for DFS

    public DinicsMaxFlowEngine(int vertices) {
        this.V = vertices;
        graph = new ArrayList<>();
        for (int i = 0; i < V; i++) graph.add(new ArrayList<>());
        level = new int[V];
        pointer = new int[V];
    }

    public void addDirectedEdge(int from, int to, int capacity) {
        // Forward edge
        graph.get(from).add(new Edge(to, capacity, graph.get(to).size()));
        // Residual (backward) edge initialized with 0 capacity
        graph.get(to).add(new Edge(from, 0, graph.get(from).size() - 1));
    }

    // Phase 1: BFS builds the Level Graph (Shortest paths from Source)
    private boolean buildLevelGraph(int source, int sink) {
        Arrays.fill(level, -1);
        level[source] = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (Edge edge : graph.get(current)) {
                // If the node is unvisited AND the edge has remaining capacity
                if (level[edge.to] < 0 && edge.flow < edge.capacity) {
                    level[edge.to] = level[current] + 1;
                    queue.add(edge.to);
                }
            }
        }
        // If the sink was reached, a path exists.
        return level[sink] >= 0;
    }

    // Phase 2: DFS blasts a blocking flow strictly following the Level Graph
    private int sendBlockingFlow(int current, int sink, int pushedFlow) {
        if (pushedFlow == 0 || current == sink) return pushedFlow;

        // The 'pointer' array ensures we don't re-explore dead ends in the same BFS phase
        for (int cid = pointer[current]; cid < graph.get(current).size(); cid++, pointer[current]++) {
            Edge edge = graph.get(current).get(cid);
            int residualCapacity = edge.capacity - edge.flow;

            // Only push flow if it's moving forward in the Level Graph AND has capacity
            if (level[current] + 1 == level[edge.to] && residualCapacity > 0) {
                int flowToPush = sendBlockingFlow(edge.to, sink, Math.min(pushedFlow, residualCapacity));

                if (flowToPush > 0) {
                    // Augment the flow on the forward edge
                    edge.flow += flowToPush;
                    // Mathematically subtract the flow from the residual backward edge
                    graph.get(edge.to).get(edge.reverseEdgeIndex).flow -= flowToPush;
                    return flowToPush;
                }
            }
        }
        return 0;
    }

    // O(V^2 * E) - Core Execution Engine
    public int executeMaxFlow(int source, int sink) {
        System.out.println("  [System] Executing BFS/DFS Network Flow Resolution...");
        
        if (source == sink) return -1;
        int totalFlow = 0;

        // Loop until BFS can no longer reach the Sink (Network is completely saturated)
        while (buildLevelGraph(source, sink)) {
            Arrays.fill(pointer, 0); // Reset dead-end pointers for the new phase
            
            while (true) {
                int flow = sendBlockingFlow(source, sink, Integer.MAX_VALUE);
                if (flow == 0) break; // No more flow can be pushed in this level graph
                totalFlow += flow;
            }
        }
        return totalFlow;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Dinic's Algorithm (Max Flow Architecture) ---");
        
        // 0 = Source, 5 = Sink
        DinicsMaxFlowEngine network = new DinicsMaxFlowEngine(6);
        network.addDirectedEdge(0, 1, 16);
        network.addDirectedEdge(0, 2, 13);
        network.addDirectedEdge(1, 2, 10);
        network.addDirectedEdge(1, 3, 12);
        network.addDirectedEdge(2, 1, 4);
        network.addDirectedEdge(2, 4, 14);
        network.addDirectedEdge(3, 2, 9);
        network.addDirectedEdge(3, 5, 20);
        network.addDirectedEdge(4, 3, 7);
        network.addDirectedEdge(4, 5, 4);

        int source = 0;
        int sink = 5;
        int maxThroughput = network.executeMaxFlow(source, sink);
        
        System.out.println("\nCRITICAL RESULT: Maximum Network Throughput -> " + maxThroughput); // Expected: 23
        System.out.println("Status: Network bottleneck completely saturated via Level Graph geometry.");
    }
}