public class LineIntersectionEngine {

    static class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // Mathematically checks if Point q lies directly on the segment pr
    private static boolean onSegment(Point p, Point q, Point r) {
        return q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
               q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y);
    }

    // Cross Product Evaluation
    // 0 = Collinear, 1 = Clockwise, 2 = Counterclockwise
    private static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0;
        return (val > 0) ? 1 : 2;
    }

    // O(1) Time, O(1) Space - The Core Geometric Engine
    public static boolean doIntersect(Point p1, Point q1, Point p2, Point q2) {
        System.out.println("Executing O(1) Geometric Cross-Product Verification...");
        
        // Find the 4 orientations required for general and special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General Case: The endpoints of one segment straddle the other segment
        if (o1 != o2 && o3 != o4) {
            return true;
        }

        // Special Cases: Collinear overlaps
        // p1, q1 and p2 are collinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;
        // p1, q1 and q2 are collinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;
        // p2, q2 and p1 are collinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;
        // p2, q2 and q1 are collinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2)) return true;

        return false; // Does not intersect
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Line Segment Intersection Engine ---");
        
        Point p1 = new Point(1, 1), q1 = new Point(10, 1);
        Point p2 = new Point(1, 2), q2 = new Point(10, 2);
        
        boolean result1 = doIntersect(p1, q1, p2, q2);
        System.out.println("\nSegment 1 [(1,1) to (10,1)] and Segment 2 [(1,2) to (10,2)]");
        System.out.println("Intersection Status: " + (result1 ? "COLLISION DETECTED" : "CLEAR"));

        Point p3 = new Point(10, 0), q3 = new Point(0, 10);
        Point p4 = new Point(0, 0), q4 = new Point(10, 10);
        
        boolean result2 = doIntersect(p3, q3, p4, q4);
        System.out.println("\nSegment 3 [(10,0) to (0,10)] and Segment 4 [(0,0) to (10,10)]");
        System.out.println("Intersection Status: " + (result2 ? "COLLISION DETECTED" : "CLEAR"));
    }
}