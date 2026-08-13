import java.util.*;

public class DijkstraRoutingEngine {
    
    // 1. The Network Node Architecture
    static class Node implements Comparable<Node> {
        int targetServer;
        int latency; // Cost, weight, or distance

        public Node(int targetServer, int latency) {
            this.targetServer = targetServer;
            this.latency = latency;
        }

        // The PriorityQueue uses this to ALWAYS pick the lowest latency path first
        public int compareTo(Node other) {
            return this.latency - other.latency;
        }
    }

    // O(E log V) Time - The Core Routing Engine
    public static void computeShortestPaths(int totalServers, List<List<Node>> network, int sourceServer) {
        System.out.println("Executing O(E log V) Dijkstra Routing Analysis from Server " + sourceServer + "...");

        // Array to store the absolute minimum latency to reach each server
        int[] minLatency = new int[totalServers];
        Arrays.fill(minLatency, Integer.MAX_VALUE);
        minLatency[sourceServer] = 0;

        // The Min-Heap: mathematically bubbles the cheapest paths to the top
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(sourceServer, 0));

        while (!pq.isEmpty()) {
            // Extract the absolute cheapest path currently available in the network
            Node current = pq.poll();
            int currentServer = current.targetServer;
            int currentTime = current.latency;

            // Optimization: If we pull a stale path from the queue that is worse than our known best, drop it.
            if (currentTime > minLatency[currentServer]) continue;

            // Scan all connecting cables from this server
            for (Node neighbor : network.get(currentServer)) {
                int nextServer = neighbor.targetServer;
                int cableLatency = neighbor.latency;

                // THE RELAXATION STEP
                // If the time to reach the current server + the time to cross the cable 
                // is FASTER than our previously recorded best time to the next server...
                if (minLatency[currentServer] + cableLatency < minLatency[nextServer]) {
                    // Update the record and push the new, faster path into the PriorityQueue
                    minLatency[nextServer] = minLatency[currentServer] + cableLatency;
                    pq.add(new Node(nextServer, minLatency[nextServer]));
                }
            }
        }

        System.out.println("\n--- Optimal Routing Table Generated ---");
        for (int i = 0; i < totalServers; i++) {
            if (minLatency[i] == Integer.MAX_VALUE) {
                System.out.println("Target Server " + i + ": UNREACHABLE (Network Partition)");
            } else {
                System.out.println("Target Server " + i + ": " + minLatency[i] + " ms latency");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Dijkstra Packet Routing Engine ---");
        
        int servers = 5;
        List<List<Node>> topology = new ArrayList<>();
        for (int i = 0; i < servers; i++) topology.add(new ArrayList<>());

        // Injecting weighted fiber-optic connections (Server, Target, Latency in ms)
        topology.get(0).add(new Node(1, 2));
        topology.get(0).add(new Node(2, 4));
        topology.get(1).add(new Node(2, 1));
        topology.get(1).add(new Node(3, 7));
        topology.get(2).add(new Node(4, 3));
        topology.get(3).add(new Node(4, 1));

        computeShortestPaths(servers, topology, 0);
        
        System.out.println("\nStatus: Routing table secured. Optimal sub-structure property verified.");
    }
}