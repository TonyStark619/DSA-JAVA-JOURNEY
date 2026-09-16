public class HLDPathQueryEngine {
    
    // Mocked HLD Arrays (Populated by yesterday's DFS logic)
    private int[] parent, depth, chainHead, arrayPos;
    
    // The 1D Segment Tree built over the flattened array
    private SegmentTree st;

    public HLDPathQueryEngine(int n, int[] parent, int[] depth, int[] chainHead, int[] arrayPos, int[] flattenedWeights) {
        this.parent = parent;
        this.depth = depth;
        this.chainHead = chainHead;
        this.arrayPos = arrayPos;
        this.st = new SegmentTree(flattenedWeights);
    }

    // O(log^2 N) - The Core HLD Path Traversal Engine
    public int queryPathMax(int u, int v) {
        System.out.println("Executing O(log^2 N) Heavy-Light Path Resolution between Node " + u + " and Node " + v + "...");
        int max_val = Integer.MIN_VALUE;

        // Step 1: Jump up the heavy chains until u and v are on the SAME chain
        while (chainHead[u] != chainHead[v]) {
            // Ensure 'u' is always the node on the deeper chain
            if (depth[chainHead[u]] < depth[chainHead[v]]) {
                int temp = u; u = v; v = temp;
            }

            // Query the Segment Tree for the continuous chain segment from the chain head down to 'u'
            int uPos = arrayPos[u];
            int headPos = arrayPos[chainHead[u]];
            max_val = Math.max(max_val, st.query(Math.min(headPos, uPos), Math.max(headPos, uPos)));

            // Jump 'u' to the parent of its chain head (moving to the next heavy chain above)
            u = parent[chainHead[u]];
        }

        // Step 2: Now u and v are on the exact same heavy chain. 
        // Do one final Segment Tree query between them.
        if (depth[u] > depth[v]) {
            int temp = u; u = v; v = temp;
        }
        
        int uPos = arrayPos[u];
        int vPos = arrayPos[v];
        max_val = Math.max(max_val, st.query(Math.min(uPos, vPos), Math.max(uPos, vPos)));

        return max_val;
    }

    // --- Minimal Segment Tree Implementation for context ---
    static class SegmentTree {
        int[] tree;
        int n;
        
        public SegmentTree(int[] arr) {
            n = arr.length;
            tree = new int[2 * n];
            for (int i = 0; i < n; i++) tree[n + i] = arr[i];
            for (int i = n - 1; i > 0; --i) tree[i] = Math.max(tree[i << 1], tree[i << 1 | 1]);
        }
        
        public int query(int left, int right) {
            int res = Integer.MIN_VALUE;
            right++; // inclusive
            for (left += n, right += n; left < right; left >>= 1, right >>= 1) {
                if ((left & 1) > 0) res = Math.max(res, tree[left++]);
                if ((right & 1) > 0) res = Math.max(res, tree[--right]);
            }
            return res;
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting HLD + Segment Tree Architecture ---");
        
        // Mocking a flattened tree infrastructure of 5 nodes
        int[] parent = {0, 0, 1, 1, 2};
        int[] depth = {0, 1, 1, 2, 2};
        int[] chainHead = {0, 1, 2, 1, 2};
        int[] arrayPos = {0, 1, 3, 2, 4};
        int[] flattenedWeights = {10, 50, 30, 20, 40}; // Traffic weights at flattened positions
        
        HLDPathQueryEngine engine = new HLDPathQueryEngine(5, parent, depth, chainHead, arrayPos, flattenedWeights);
        
        int bottleneck = engine.queryPathMax(3, 4);
        
        System.out.println("\nCRITICAL RESULT: Maximum traffic bottleneck on path = " + bottleneck);
        System.out.println("Status: Chain-jumping active. O(N) linear scan mathematically bypassed.");
    }
}