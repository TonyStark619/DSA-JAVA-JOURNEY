public class VanEmdeBoasTreeEnginess {

    static class VEBNode {
        int universeSize;
        int min;
        int max;
        VEBNode summary;
        VEBNode[] clusters;
        int lowerSqrt;
        int upperSqrt;

        public VEBNode(int u) {
            this.universeSize = u;
            this.min = -1;
            this.max = -1;

            // Base case: A universe of size 2 requires no child clusters or summary
            if (u > 2) {
                int bits = Integer.numberOfTrailingZeros(u);
                this.lowerSqrt = 1 << (bits / 2);
                this.upperSqrt = 1 << ((bits + 1) / 2);

                this.summary = new VEBNode(upperSqrt);
                this.clusters = new VEBNode[upperSqrt];
                for (int i = 0; i < upperSqrt; i++) {
                    this.clusters[i] = new VEBNode(lowerSqrt);
                }
            }
        }

        // Extracts the high bits (Cluster Number)
        int high(int x) {
            return x / lowerSqrt;
        }

        // Extracts the low bits (Position inside the Cluster)
        int low(int x) {
            return x % lowerSqrt;
        }

        // Reconstructs the global integer from Cluster Number and Offset
        int generateIndex(int cluster, int offset) {
            return (cluster * lowerSqrt) + offset;
        }
    }

    private final VEBNode root;

    public VanEmdeBoasTreeEnginess(int universePowerOfTwo) {
        System.out.println("  [System] Forging Van Emde Boas Square-Root Hierarchy (U = " + universePowerOfTwo + ")...");
        this.root = new VEBNode(universePowerOfTwo);
    }

    private void emptyInsert(VEBNode node, int x) {
        node.min = x;
        node.max = x;
    }

    // O(log log U) - Recursive Universe Insertion
    public void insert(int x) {
        insertRecursive(root, x);
    }

    private void insertRecursive(VEBNode node, int x) {
        // Case 1: Node is completely empty. Store in O(1) without recursing.
        if (node.min == -1) {
            emptyInsert(node, x);
            return;
        }

        // Case 2: New element is smaller than current min. Swap them!
        // The old min gets pushed down into the clusters; the new min stays at the top.
        if (x < node.min) {
            int temp = x;
            x = node.min;
            node.min = temp;
        }

        if (node.universeSize > 2) {
            int clusterIndex = node.high(x);
            int offset = node.low(x);

            // If the target cluster is currently empty, we update the summary tree
            // and initialize the cluster's min/max in O(1) time (only ONE recursive call happens!)
            if (node.clusters[clusterIndex].min == -1) {
                insertRecursive(node.summary, clusterIndex);
                emptyInsert(node.clusters[clusterIndex], offset);
            } else {
                // Cluster already active; recurse into the cluster without touching summary
                insertRecursive(node.clusters[clusterIndex], offset);
            }
        }

        if (x > node.max) {
            node.max = x;
        }
    }

    // O(log log U) - Isolates the smallest element strictly greater than X
    public int findSuccessor(int x) {
        return successorRecursive(root, x);
    }

    private int successorRecursive(VEBNode node, int x) {
        // Base Case: Universe of size 2
        if (node.universeSize == 2) {
            if (x == 0 && node.max == 1) return 1;
            return -1;
        }

        // If X is strictly less than the minimum of this node, the minimum IS the successor
        if (node.min != -1 && x < node.min) {
            return node.min;
        }

        int clusterIndex = node.high(x);
        int offset = node.low(x);
        int maxLow = node.clusters[clusterIndex].max;

        // Case A: The successor exists inside the exact same cluster as X
        if (maxLow != -1 && offset < maxLow) {
            int successorOffset = successorRecursive(node.clusters[clusterIndex], offset);
            return node.generateIndex(clusterIndex, successorOffset);
        } 
        // Case B: We must jump to the next active cluster using the Summary Tree
        else {
            int nextActiveCluster = successorRecursive(node.summary, clusterIndex);
            if (nextActiveCluster == -1) {
                return -1; // No successor exists in the universe
            } else {
                int minOffset = node.clusters[nextActiveCluster].min;
                return node.generateIndex(nextActiveCluster, minOffset);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Van Emde Boas (vEB) O(log log U) Engine ---");

        // Universe size 16 (Must be a power of 2; supports keys 0 to 15)
        VanEmdeBoasTreeEnginess veb = new VanEmdeBoasTreeEnginess(16);

        int[] networkPorts = {2, 3, 4, 5, 7, 14, 15};
        System.out.println("  [System] Populating Universe with active keys: {2, 3, 4, 5, 7, 14, 15}");
        for (int port : networkPorts) {
            veb.insert(port);
        }

        System.out.println("\n--- Executing O(log log U) Successor Queries ---");
        System.out.println("Successor of 0  -> " + veb.findSuccessor(0));  // Expected: 2
        System.out.println("Successor of 4  -> " + veb.findSuccessor(4));  // Expected: 5
        System.out.println("Successor of 5  -> " + veb.findSuccessor(5));  // Expected: 7
        System.out.println("Successor of 7  -> " + veb.findSuccessor(7));  // Expected: 14
        System.out.println("Successor of 15 -> " + veb.findSuccessor(15)); // Expected: -1 (None)

        System.out.println("\nStatus: O(log N) comparison barrier shattered via recursive square-root bit-slicing.");
    }
}