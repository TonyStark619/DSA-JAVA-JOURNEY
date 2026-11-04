public class LiChaoTreeEngine {

    private static final long INF = (long) 4e18;

    // Represents a linear cost function: y = m*x + c
    static class Line {
        long m;
        long c;

        public Line(long m, long c) {
            this.m = m;
            this.c = c;
        }

        long evaluate(long x) {
            return (m * x) + c;
        }
    }

    static class Node {
        Line dominantLine;
        Node left;
        Node right;

        public Node(Line line) {
            this.dominantLine = line;
        }
    }

    private Node root;
    private final long domainMin;
    private final long domainMax;

    public LiChaoTreeEngine(long domainMin, long domainMax) {
        this.domainMin = domainMin;
        this.domainMax = domainMax;
        // Initialize root with a sentinel line at infinity
        this.root = new Node(new Line(0, INF));
    }

    // O(log X) - Dynamically inserts a new linear function into the coordinate domain
    public void addLine(long slope, long intercept) {
        insertRecursive(root, domainMin, domainMax, new Line(slope, intercept));
    }

    private void insertRecursive(Node node, long l, long r, Line newLine) {
        long mid = l + (r - l) / 2;

        boolean newWinsAtLeft = newLine.evaluate(l) < node.dominantLine.evaluate(l);
        boolean newWinsAtMid = newLine.evaluate(mid) < node.dominantLine.evaluate(mid);

        // If the new line beats the current line at the midpoint, it claims dominance over this interval.
        // We swap them so 'newLine' now holds the loser that needs to be pushed into a sub-interval.
        if (newWinsAtMid) {
            Line temp = node.dominantLine;
            node.dominantLine = newLine;
            newLine = temp;
        }

        // Base case: Interval is a single point, no further intersection possible
        if (l == r) return;

        // THE GEOMETRIC INVARIANT: Two lines intersect at most once.
        // The losing line can only overtake the winning line in ONE of the two halves.
        if (newWinsAtLeft != newWinsAtMid) {
            // Intersection lies in the left half [l, mid]
            if (node.left == null) node.left = new Node(new Line(0, INF));
            insertRecursive(node.left, l, mid, newLine);
        } else {
            // Intersection lies in the right half [mid + 1, r]
            if (node.right == null) node.right = new Node(new Line(0, INF));
            insertRecursive(node.right, mid + 1, r, newLine);
        }
    }

    // O(log X) - Queries the absolute minimum value among all registered lines at coordinate X
    public long queryMinimum(long x) {
        return queryRecursive(root, domainMin, domainMax, x);
    }

    private long queryRecursive(Node node, long l, long r, long x) {
        if (node == null) return INF;

        long currentMin = node.dominantLine.evaluate(x);
        if (l == r) return currentMin;

        long mid = l + (r - l) / 2;
        if (x <= mid) {
            return Math.min(currentMin, queryRecursive(node.left, l, mid, x));
        } else {
            return Math.min(currentMin, queryRecursive(node.right, mid + 1, r, x));
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Li Chao Segment Tree (Geometric CHT) Engine ---");

        // Coordinate domain covering [-100,000 to +100,000]
        LiChaoTreeEngine optimizer = new LiChaoTreeEngine(-100_000, 100_000);

        System.out.println("  [System] Injecting non-monotonic linear cost functions...");
        // Line 1: y = 3x + 10
        optimizer.addLine(3, 10);
        // Line 2: y = -2x + 25
        optimizer.addLine(-2, 25);
        // Line 3: y = 1x + 5
        optimizer.addLine(1, 5);

        System.out.println("\n--- Executing O(log X) Lower-Envelope Queries ---");
        // At x = 1: L1 = 13, L2 = 23, L3 = 6  -> Min is 6
        System.out.println("Optimal Cost at x = 1  -> " + optimizer.queryMinimum(1));
        // At x = 10: L1 = 40, L2 = 5, L3 = 15 -> Min is 5
        System.out.println("Optimal Cost at x = 10 -> " + optimizer.queryMinimum(10));
        // At x = -5: L1 = -5, L2 = 35, L3 = 0 -> Min is -5
        System.out.println("Optimal Cost at x = -5 -> " + optimizer.queryMinimum(-5));

        System.out.println("\nStatus: O(N^2) dynamic programming envelope bottleneck reduced to strict O(N log X).");
    }
}