public class SpatialKDTreeEngine {

    static class Point {
        int[] coords; // Supports X, Y, Z, etc. (K-dimensions)
        public Point(int... coords) {
            this.coords = coords;
        }
    }

    static class KDNode {
        Point point;
        KDNode left, right;
        public KDNode(Point point) {
            this.point = point;
        }
    }

    private KDNode root;
    private final int K; // Number of dimensions (e.g., 2 for GPS coordinates)

    public SpatialKDTreeEngine(int kDimensions) {
        this.K = kDimensions;
    }

    // O(log N) - Geometrically inserts a point by alternating axes
    public void insert(Point p) {
        root = insertRec(root, p, 0);
    }

    private KDNode insertRec(KDNode node, Point p, int depth) {
        if (node == null) return new KDNode(p);

        // Calculate which geometric axis we are splitting on at this depth
        int axis = depth % K;

        // Route left or right based strictly on the current axis
        if (p.coords[axis] < node.point.coords[axis]) {
            node.left = insertRec(node.left, p, depth + 1);
        } else {
            node.right = insertRec(node.right, p, depth + 1);
        }
        return node;
    }

    // Mathematical Euclidean Distance between two K-dimensional points
    private double distance(Point p1, Point p2) {
        double sum = 0;
        for (int i = 0; i < K; i++) {
            sum += Math.pow(p1.coords[i] - p2.coords[i], 2);
        }
        return Math.sqrt(sum);
    }

    // O(log N) - The Core Proximity Engine (Nearest Neighbor)
    private KDNode bestNode;
    private double bestDistance;

    public Point findNearestNeighbor(Point target) {
        bestNode = null;
        bestDistance = Double.MAX_VALUE;
        System.out.println("  [System] Executing O(log N) Spatial Pruning Search...");
        searchNearest(root, target, 0);
        return bestNode.point;
    }

    private void searchNearest(KDNode node, Point target, int depth) {
        if (node == null) return;

        double d = distance(node.point, target);
        if (d < bestDistance) {
            bestDistance = d;
            bestNode = node;
        }

        int axis = depth % K;
        KDNode goodSide, badSide;

        // Determine which bounding box the target naturally falls into
        if (target.coords[axis] < node.point.coords[axis]) {
            goodSide = node.left;
            badSide = node.right;
        } else {
            goodSide = node.right;
            badSide = node.left;
        }

        // 1. Search the geometric box the target is inside
        searchNearest(goodSide, target, depth + 1);

        // 2. Mathematical Pruning: Do we even NEED to check the other box?
        // If the straight-line distance to the axis boundary is GREATER than our best distance,
        // it is mathematically impossible for a closer point to exist on the other side. We prune it.
        if (Math.abs(target.coords[axis] - node.point.coords[axis]) < bestDistance) {
            searchNearest(badSide, target, depth + 1);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting K-Dimensional (KD-Tree) Spatial Engine ---");
        
        // 2D Space (e.g., Uber Map grid coordinates)
        SpatialKDTreeEngine mapGrid = new SpatialKDTreeEngine(2);
        
        // Ingesting driver locations
        mapGrid.insert(new Point(3, 6));
        mapGrid.insert(new Point(17, 15));
        mapGrid.insert(new Point(13, 15));
        mapGrid.insert(new Point(6, 12));
        mapGrid.insert(new Point(9, 1));
        mapGrid.insert(new Point(2, 7));
        mapGrid.insert(new Point(10, 19));

        // User requests a ride at coordinate (10, 14)
        Point userLocation = new Point(10, 14);
        System.out.println("\n[Network] User requested driver at GPS (10, 14).");
        
        Point nearestDriver = mapGrid.findNearestNeighbor(userLocation);
        
        System.out.println("\nCRITICAL RESULT: Nearest driver isolated at GPS (" + nearestDriver.coords[0] + ", " + nearestDriver.coords[1] + ")");
        System.out.println("Status: O(N) spatial scan completely bypassed via multidimensional geometric pruning.");
    }
}