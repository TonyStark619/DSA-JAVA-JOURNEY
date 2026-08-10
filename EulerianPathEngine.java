import java.util.*;

public class EulerianPathEngine {

    // O(V + E) Time - Eulerian Path Reconstruction
    public static List<String> reconstructItinerary(String[][] tickets) {
        // We use a PriorityQueue to ensure we always pick the alphabetically smallest destination first
        Map<String, PriorityQueue<String>> flightGraph = new HashMap<>();
        
        System.out.println("Ingesting randomized flight data...");
        for (String[] ticket : tickets) {
            flightGraph.putIfAbsent(ticket[0], new PriorityQueue<>());
            flightGraph.get(ticket[0]).add(ticket[1]);
        }

        LinkedList<String> optimalItinerary = new LinkedList<>();
        
        // Start the traversal from the standard origin airport
        dfs("JFK", flightGraph, optimalItinerary);
        
        return optimalItinerary;
    }

    private static void dfs(String airport, Map<String, PriorityQueue<String>> graph, LinkedList<String> itinerary) {
        PriorityQueue<String> destinations = graph.get(airport);

        // While there are still outgoing flights from this airport, take them
        while (destinations != null && !destinations.isEmpty()) {
            String nextDestination = destinations.poll();
            dfs(nextDestination, graph, itinerary);
        }

        // THE MAGIC: We only add the airport to the itinerary when we are completely out of options.
        // Because of the recursive stack, this builds the route perfectly in reverse order.
        itinerary.addFirst(airport);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Hierholzer's Graph Reconstruction Engine ---");
        
        // Simulating a scrambled stack of boarding passes
        String[][] boardingPasses = {
            {"MUC", "LHR"}, 
            {"JFK", "MUC"}, 
            {"SFO", "SJC"}, 
            {"LHR", "SFO"}
        };
        
        List<String> finalRoute = reconstructItinerary(boardingPasses);
        
        System.out.println("\nExecuting O(V+E) Eulerian Path Traversal...");
        System.out.println("CRITICAL INSIGHT: Reconstructed Sequence -> " + String.join(" -> ", finalRoute));
        System.out.println("Status: Graph edges saturated. Dead-end traps mathematically avoided.");
    }
}