import java.util.*;

public class LiChaoConvexHullEngine {

    private static final long INF = Long.MAX_VALUE / 4;

    // Immutable Linear Function: f(x) = slope * x + intercept
    static class Line {
        final long slope;
        final long intercept;
        final String label;

        public Line(long slope, long intercept, String label) {
            this.slope = slope;
            this.intercept = intercept;
            this.label = label;
        }

        public long eval(long x) {
            return slope * x + intercept;
        }
    }

    // Sparse Dynamic Tree Node (Avoids allocating 4 * Domain arrays for large coordinate spaces)
    static class Node {
        Line bestAtMid;
        Node left;
        Node right;

        public Node(Line line) {
            this.bestAtMid = line;
        }
    }

    private final long domainMin;
    private final long domainMax;
    private Node root;

    public LiChaoConvexHullEngine(long domainMin, long domainMax) {
        this.domainMin = domainMin;
        this.domainMax = domainMax;
        this.root = null;
    }

    // O(log(domainMax - domainMin)) - Inserts an arbitrary-slope line into the lower envelope
    public void insertLine(long slope, long intercept, String label) {
        Line incoming = new Line(slope, intercept, label);
        root = insertRecursive(root, domainMin, domainMax, incoming);
    }

    private Node insertRecursive(Node node, long l, long r, Line candidate) {
        if (node == null) {
            return new Node(candidate);
        }

        long mid = l + (r - l) / 2;
        boolean candidateWinsLeft = candidate.eval(l) < node.bestAtMid.eval(l);
        boolean candidateWinsMid = candidate.eval(mid) < node.bestAtMid.eval(mid);

        // Ensure node.bestAtMid always holds the strictly better line at 'mid'
        if (candidateWinsMid) {
            Line displaced = node.bestAtMid;
            node.bestAtMid = candidate;
            candidate = displaced;
        }

        // Base case: Domain collapsed to a single integer coordinate
        if (l == r) {
            return node;
        }

        // Single-Intersection Invariant:
        // If the winner changed between 'l' and 'mid', the intersection lies in [l, mid] -> push loser Left.
        // Otherwise, the losing line can only overtake on the Right half [mid + 1, r] -> push loser Right.
        if (candidateWinsLeft != candidateWinsMid) {
            node.left = insertRecursive(node.left, l, mid, candidate);
        } else {
            node.right = insertRecursive(node.right, mid + 1, r, candidate);
        }
        return node;
    }

    // O(log(domainMax - domainMin)) - Evaluates the exact minimum cost across all inserted lines at x
    public Line queryOptimalLine(long x) {
        return queryRecursive(root, domainMin, domainMax, x);
    }

    private Line queryRecursive(Node node, long l, long r, long x) {
        if (node == null) {
            return new Line(0, INF, "NONE");
        }

        Line currentBest = node.bestAtMid;
        if (l == r) {
            return currentBest;
        }

        long mid = l + (r - l) / 2;
        Line childBest = (x <= mid)
                ? queryRecursive(node.left, l, mid, x)
                : queryRecursive(node.right, mid + 1, r, x);

        return (childBest.eval(x) < currentBest.eval(x)) ? childBest : currentBest;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Sparse Li Chao Segment Tree (Dynamic Convex Hull) Engine ---");

        // Coordinate domain [0 .. 1,000,000] representing batch token workload size 'x'
        LiChaoConvexHullEngine envelope = new LiChaoConvexHullEngine(0, 1_000_000);

        // Insert heterogeneous compute pricing tiers in completely UNSORTED slope order:
        // Total Cost(x) = perTokenSlope * x + fixedProvisioningOverhead
        List<Line> pricingTiers = Arrays.asList(
            new Line(4, 1200, "SPOT_CLUSTER_B"),     // Low slope, moderate fixed overhead
            new Line(12, 100, "SERVERLESS_EDGE"),    // Steep slope, tiny fixed overhead
            new Line(2, 5000, "DEDICATED_H200_POD"), // Ultra-low slope, high fixed overhead
            new Line(7, 600,  "REGIONAL_STANDARD"),  // Mid-tier curve
            new Line(1, 14000,"RESERVED_SUPERPOD")   // Lowest slope, highest fixed commitment
        );

        System.out.println("  [System] Inserting 5 unsorted linear cost curves into Sparse Li Chao Tree...");
        for (Line tier : pricingTiers) {
            envelope.insertLine(tier.slope, tier.intercept, tier.label);
        }

        System.out.println("\n--- Executing O(log X) Lower-Envelope Queries & Brute-Force Verification ---");
        long[] testWorkloads = {50, 150, 500, 2500, 12000};

        for (long x : testWorkloads) {
            Line liChaoWinner = envelope.queryOptimalLine(x);
            long liChaoCost = liChaoWinner.eval(x);

            // Cross-verify against direct O(M) linear scan over all pricing curves
            long bruteMin = INF;
            for (Line tier : pricingTiers) {
                bruteMin = Math.min(bruteMin, tier.eval(x));
            }

            System.out.printf(
                "  Workload x = %5d -> Optimal Tier: %-19s | Cost: %6d | Exact Match: %b%n",
                x, liChaoWinner.label, liChaoCost, (liChaoCost == bruteMin)
            );
        }

        System.out.println("\nStatus: Arbitrary-slope dynamic convex hull queries resolved in O(log X) with sparse node allocation.");
    }
}