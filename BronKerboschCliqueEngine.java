import java.util.*;

public class BronKerboschCliqueEngine {

    // Adjacency list using HashSets for O(1) mathematical set intersections
    private final Map<Integer, Set<Integer>> networkGraph = new HashMap<>();

    public void addUndirectedEdge(int u, int v) {
        networkGraph.putIfAbsent(u, new HashSet<>());
        networkGraph.putIfAbsent(v, new HashSet<>());
        networkGraph.get(u).add(v);
        networkGraph.get(v).add(u);
    }

    // O(3^(N/3)) - The Core NP-Hard Recursive Optimizer
    // R: The currently forming clique
    // P: Nodes that are connected to ALL nodes in R (Potential additions)
    // X: Nodes that are connected to ALL nodes in R, but have already been explored
    private void executeBronKerbosch(Set<Integer> R, Set<Integer> P, Set<Integer> X) {
        // Base Case: If both P and X are empty, R is a Maximal Clique
        if (P.isEmpty() && X.isEmpty()) {
            System.out.println("    -> [Network Sub-Grid Isolated] Maximal Clique: " + R);
            return;
        }

        // Pivot Optimization: Choose a pivot 'u' from P Union X
        // We pick the node with the highest degree to maximize pruning
        Set<Integer> pUnionX = new HashSet<>(P);
        pUnionX.addAll(X);
        
        int pivot = -1;
        int maxDegree = -1;
        for (int node : pUnionX) {
            int degree = networkGraph.getOrDefault(node, Collections.emptySet()).size();
            if (degree > maxDegree) {
                maxDegree = degree;
                pivot = node;
            }
        }

        // P \ N(pivot): We only need to test nodes in P that are NOT neighbors of the pivot
        Set<Integer> pivotNeighbors = networkGraph.getOrDefault(pivot, Collections.emptySet());
        Set<Integer> pMinusPivotNeighbors = new HashSet<>(P);
        pMinusPivotNeighbors.removeAll(pivotNeighbors);

        for (int v : pMinusPivotNeighbors) {
            Set<Integer> neighborsOfV = networkGraph.getOrDefault(v, Collections.emptySet());

            // Next R = R Union {v}
            Set<Integer> nextR = new HashSet<>(R);
            nextR.add(v);

            // Next P = P Intersection N(v)
            Set<Integer> nextP = new HashSet<>(P);
            nextP.retainAll(neighborsOfV);

            // Next X = X Intersection N(v)
            Set<Integer> nextX = new HashSet<>(X);
            nextX.retainAll(neighborsOfV);

            // Recursive dive
            executeBronKerbosch(nextR, nextP, nextX);

            // Backtrack: Move v from P to X
            P.remove(v);
            X.add(v);
        }
    }

    public void isolateAllCliques() {
        System.out.println("  [System] Executing Bron-Kerbosch Set-Intersection Pivot Sweep...");
        Set<Integer> R = new HashSet<>();
        Set<Integer> P = new HashSet<>(networkGraph.keySet());
        Set<Integer> X = new HashSet<>();
        
        executeBronKerbosch(R, P, X);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Bron-Kerbosch Network Clique Architecture ---");
        
        BronKerboschCliqueEngine socialNetwork = new BronKerboschCliqueEngine();
        
        // Building a network where Nodes 1, 2, 3, 4 form a perfect 4-person clique,
        // and Node 5 is only connected to Node 4.
        socialNetwork.addUndirectedEdge(1, 2);
        socialNetwork.addUndirectedEdge(1, 3);
        socialNetwork.addUndirectedEdge(1, 4);
        socialNetwork.addUndirectedEdge(2, 3);
        socialNetwork.addUndirectedEdge(2, 4);
        socialNetwork.addUndirectedEdge(3, 4);
        
        // Outlier connection
        socialNetwork.addUndirectedEdge(4, 5);
        
        System.out.println("\n[Network] Mapping mathematical intersections...");
        socialNetwork.isolateAllCliques();
        
        System.out.println("\nStatus: NP-Hard combinatorial bottleneck mathematically pruned to O(3^(N/3)) via geometric pivoting.");
    }
}