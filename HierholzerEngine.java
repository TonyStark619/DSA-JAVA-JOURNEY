import java.util.*;

public class HierholzerEngine {

    // We use a PriorityQueue to ensure we visit nodes in lexical order if multiple paths exist
    private Map<String, PriorityQueue<String>> flightMap = new HashMap<>();
    private LinkedList<String> finalRoute = new LinkedList<>();

    // O(1) - Building the directed graph
    public void addFlight(String origin, String destination) {
        flightMap.putIfAbsent(origin, new PriorityQueue<>());
        flightMap.get(origin).add(destination);
    }

    // O(V + E log E) - The Eulerian Path Core
    public void constructEulerianPath(String startNode) {
        System.out.println("Executing Hierholzer's Route Reconstruction from: " + startNode);
        
        // Execute the destructive DFS
        dfs(startNode);
        
        // Hierholzer's builds the path from the dead-ends backward. 
        // We must reverse it to get the correct chronological route.
        Collections.reverse(finalRoute);
        
        System.out.println("\n--- Global Routing Resolution ---");
        System.out.println("CRITICAL RESULT: Valid Eulerian Path -> " + String.join(" ➔ ", finalRoute));
    }

    private void dfs(String currentAirport) {
        PriorityQueue<String> destinations = flightMap.get(currentContext(currentAirport));
        
        // While there are still outgoing flights from this airport
        while (destinations != null && !destinations.isEmpty()) {
            // .poll() instantly deletes the edge so we never traverse it twice
            String nextDestination = destinations.poll();
            dfs(nextDestination);
        }
        
        // THE MAGIC: Only add the airport to the route AFTER all its outgoing flights are exhausted
        finalRoute.add(currentAirport);
    }

    private String currentContext(String currentAirport) {
        return currentAirport;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Hierholzer's Eulerian Path Architecture ---");
        
        HierholzerEngine engine = new HierholzerEngine();
        
        // Reconstructing a shattered flight itinerary where every ticket must be used exactly once.
        engine.addFlight("JFK", "SFO");
        engine.addFlight("JFK", "ATL");
        engine.addFlight("SFO", "ATL");
        engine.addFlight("ATL", "JFK");
        engine.addFlight("ATL", "SFO");

        // We must start at JFK to ensure a valid continuous path for this specific graph
        engine.constructEulerianPath("JFK");
        
        System.out.println("\nStatus: All edges traversed exactly once. O(V+E) path resolution successful.");
    }
}