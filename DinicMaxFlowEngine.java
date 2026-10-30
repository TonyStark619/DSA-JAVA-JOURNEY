import java.util.*;

public class DinicMaxFlowEngine {

    static class Edge {
        int to;
        int capacity;
        int flow;
        int reverseEdgeIndex; // Pointer to the residual (backward) edge

        public Edge(int to, int capacity, int reverseEdgeIndex) {
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
            this.reverseEdgeIndex = reverseEdgeIndex;
        }
    }

    private final int numVertices;
    private final List<List<Edge>> networkGraph;
    private final int[] levelGraph; // BFS distance from the source
    private final int[] nextEdgePtr; // DFS dead-end pruning tracker

    public DinicMaxFlowEngine(int vertices) {
        this.numVertices = vertices;
        this.networkGraph = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            networkGraph.add(new ArrayList<>());
        }
        this.levelGraph = new int[vertices];
        this.nextEdgePtr = new int[vertices];
    }

    // O(1) - Geometrically wires a forward capacity edge and a backward residual edge
    public void addDirectedEdge(int from, int to, int capacity) {
        networkGraph.get(from).add(new Edge(to, capacity, networkGraph.get(to).size()));
        networkGraph.get(to).add(new Edge(from, 0, networkGraph.get(from).size() - 1));
    }

    // O(E) - Phase 1: BFS constructs the geometric Level Graph
    // Ensures we only ever push flow strictly FORWARD toward the sink
    private boolean constructLevelGraph(int source, int sink) {
        Arrays.fill(levelGraph, -1);
        levelGraph[source] = 0;
        
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            for (Edge edge : networkGraph.get(current)) {
                // If the edge has available capacity and the target node is unvisited
                if (edge.capacity - edge.flow > 0 && levelGraph[edge.to] == -1) {
                    levelGraph[edge.to] = levelGraph[current] + 1;
                    queue.add(edge.to);
                }
            }
        }
        // If the sink was reached, a valid path exists
        return levelGraph[sink] != -1;
    }

    // O(V*E) per phase - Phase 2: DFS Blocking Flow with Dead-End Pruning
    private int pushBlockingFlow(int current, int sink, int pushedFlow) {
        if (pushedFlow == 0 || current == sink) return pushedFlow;

        // THE CRITICAL OPTIMIZATION: 
        // nextEdgePtr prevents the DFS from repeatedly re-evaluating dead ends
        for (int i = nextEdgePtr[current]; i < networkGraph.get(current).size(); i++, nextEdgePtr[current]++) {
            Edge edge = networkGraph.get(current).get(i);

            // Only push flow if the neighbor is exactly one level deeper
            if (levelGraph[current] + 1 != levelGraph[edge.to]) continue;

            int availableCapacity = edge.capacity - edge.flow;
            if (availableCapacity == 0) continue;

            // Recursively push flow down the pipeline
            int bottleneck = pushBlockingFlow(edge.to, sink, Math.min(pushedFlow, availableCapacity));

            if (bottleneck == 0) continue; // Hit a dead end downstream

            // Commit the flow and update the residual backward edge
            edge.flow += bottleneck;
            networkGraph.get(edge.to).get(edge.reverseEdgeIndex).flow -= bottleneck;
            
            return bottleneck;
        }
        return 0;
    }

    // O(V^2 E) - The Absolute Max Flow Engine
    public int calculateMaximumFlow(int source, int sink) {
        System.out.println("  [System] Executing Dinic's Algorithm (Level Graph + Blocking Flow)...");
        int absoluteMaxFlow = 0;

        // Keep building level graphs until the sink is mathematically unreachable
        while (constructLevelGraph(source, sink)) {
            // Reset the dead-end pruning pointers for the new DFS phase
            Arrays.fill(nextEdgePtr, 0);
            
            int flow;
            // Push parallel blocking flows until this specific level graph is exhausted
            while ((flow = pushBlockingFlow(source, sink, Integer.MAX_VALUE)) > 0) {
                absoluteMaxFlow += flow;
            }
        }
        return absoluteMaxFlow;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Dinic's Network Flow Architecture ---");
        
        // 6-Node AWS Server Grid (Node 0 is Source, Node 5 is Sink)
        DinicMaxFlowEngine network = new DinicMaxFlowEngine(6);
        
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

        int maxBandwidth = network.calculateMaximumFlow(0, 5);
        
        System.out.println("\nCRITICAL RESULT: Absolute Maximum Network Bandwidth -> " + maxBandwidth + " Gbps");
        System.out.println("Status: O(V^2 E) bottleneck mathematically shattered via dead-end pointer pruning.");
    }
}