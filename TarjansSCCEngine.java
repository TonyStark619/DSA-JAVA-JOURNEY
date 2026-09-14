import java.util.*;

public class TarjansSCCEngine {
    private int V;
    private List<List<Integer>> adj;
    private int time = 0;

    public TarjansSCCEngine(int v) {
        this.V = v;
        adj = new ArrayList<>();
        for (int i = 0; i < v; ++i) {
            adj.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v) {
        adj.get(u).add(v);
    }

    // O(V + E) Time - The Core DFS Engine
    private void sccDFS(int u, int[] low, int[] disc, boolean[] stackMember, Stack<Integer> st) {
        // Initialize discovery time and low value
        disc[u] = time;
        low[u] = time;
        time++;
        stackMember[u] = true;
        st.push(u);

        for (int v : adj.get(u)) {
            // If v is not visited yet, recurse for it
            if (disc[v] == -1) {
                sccDFS(v, low, disc, stackMember, st);
                // Check if the subtree rooted at v has a connection back to an ancestor of u
                low[u] = Math.min(low[u], low[v]);
            } 
            // If v is already on the stack, it's a back-edge (we found a cycle)
            else if (stackMember[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        // If the lowest node reachable from 'u' is 'u' itself, it is the root of an SCC
        if (low[u] == disc[u]) {
            System.out.print("Strongly Connected Cluster found: [ ");
            int w = -1;
            while (w != u) {
                w = st.pop();
                System.out.print(w + " ");
                stackMember[w] = false;
            }
            System.out.println("]");
        }
    }

    public void executeTarjan() {
        System.out.println("Executing O(V+E) Tarjan's Cluster Resolution...\n");
        int[] disc = new int[V];
        int[] low = new int[V];
        boolean[] stackMember = new boolean[V];
        Stack<Integer> st = new Stack<>();

        Arrays.fill(disc, -1);
        Arrays.fill(low, -1);
        Arrays.fill(stackMember, false);

        // Call the recursive helper function for all unvisited vertices
        for (int i = 0; i < V; i++) {
            if (disc[i] == -1) {
                sccDFS(i, low, disc, stackMember, st);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Tarjan's SCC Architecture ---");
        
        TarjansSCCEngine engine = new TarjansSCCEngine(5);
        
        // Creating a directed network with clusters
        engine.addEdge(1, 0);
        engine.addEdge(0, 2);
        engine.addEdge(2, 1); // 0, 1, 2 form a cycle (Cluster 1)
        engine.addEdge(0, 3);
        engine.addEdge(3, 4); // 3 and 4 are isolated (Clusters 2 and 3)
        
        engine.executeTarjan();
        
        System.out.println("\nStatus: Directed graph parsed. O(V+E) cyclic cluster resolution complete.");
    }
}