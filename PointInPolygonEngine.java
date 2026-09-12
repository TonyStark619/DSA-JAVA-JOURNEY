public class PointInPolygonEngine {

    static class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // Helper: Checks if q lies directly on the line segment pr
    private static boolean onSegment(Point p, Point q, Point r) {
        return q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
               q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y);
    }

    // Helper: Cross-Product Orientation
    private static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0;
        return (val > 0) ? 1 : 2;
    }

    // Helper: Recycled from Day 146 Line Intersection
    private static boolean doIntersect(Point p1, Point q1, Point p2, Point q2) {
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        if (o1 != o2 && o3 != o4) return true;
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;
        if (o4 == 0 && onSegment(p2, q1, q2)) return true;

        return false;
    }

    // O(N) Time, O(1) Space - The Ray Casting Engine
    public static boolean isInside(Point polygon[], int n, Point p) {
        System.out.println("Executing O(N) Ray Casting Geofence Verification...");
        
        // A polygon must have at least 3 vertices
        if (n < 3) return false;

        // Create a point representing the mathematical ray extending to infinity on the X-axis
        Point extreme = new Point(1000000, p.y);

        int count = 0;
        int i = 0;
        
        do {
            int next = (i + 1) % n;

            // Check if the line segment from 'p' to 'extreme' intersects with the polygon edge
            if (doIntersect(polygon[i], polygon[next], p, extreme)) {
                // If the point is collinear with the edge, check if it actually lies ON the edge
                if (orientation(polygon[i], p, polygon[next]) == 0) {
                    return onSegment(polygon[i], p, polygon[next]);
                }
                count++;
            }
            i = next;
        } while (i != 0);

        // TRUE if count is odd, FALSE if count is even
        return (count % 2 != 0);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Geofence Coordinate Engine ---");
        
        Point polygon[] = {new Point(0, 0), new Point(10, 0), new Point(10, 10), new Point(0, 10)};
        int n = polygon.length;
        
        Point target1 = new Point(5, 5);
        System.out.println("\nChecking Target 1 (5, 5)...");
        System.out.println("Status: " + (isInside(polygon, n, target1) ? "INSIDE DELIVERY ZONE" : "OUTSIDE ZONE"));

        Point target2 = new Point(15, 5);
        System.out.println("\nChecking Target 2 (15, 5)...");
        System.out.println("Status: " + (isInside(polygon, n, target2) ? "INSIDE DELIVERY ZONE" : "OUTSIDE ZONE"));
    }
}