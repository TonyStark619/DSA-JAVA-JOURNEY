import java.util.*;

public class GrahamScanConvexHull {

    static class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // A utility function to find the next-to-top element in the stack
    private static Point peekNextToTop(Stack<Point> stack) {
        Point top = stack.pop();
        Point nextToTop = stack.peek();
        stack.push(top);
        return nextToTop;
    }

    // The Cross Product Math: 
    // 0 --> Collinear, 1 --> Clockwise (Right Turn), 2 --> Counterclockwise (Left Turn)
    private static int orientation(Point p, Point q, Point r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0;
        return (val > 0) ? 1 : 2;
    }

    private static int distSq(Point p1, Point p2) {
        return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
    }

    // O(N log N) - The Core Computational Geometry Engine
    public static void computeConvexHull(Point[] points, int n) {
        System.out.println("Executing O(N log N) Graham Scan Boundary Resolution...");
        if (n < 3) {
            System.out.println("Result: A convex hull requires at least 3 points.");
            return;
        }

        // Step 1: Find the bottom-most point (or left-most if tied)
        int minY = points[0].y, min = 0;
        for (int i = 1; i < n; i++) {
            int y = points[i].y;
            if ((y < minY) || (minY == y && points[i].x < points[min].x)) {
                minY = points[i].y;
                min = i;
            }
        }

        // Place the bottom-most point at the 0th position
        Point temp = points[0];
        points[0] = points[min];
        points[min] = temp;

        // Step 2: Sort the remaining points by Polar Angle with respect to points[0]
        final Point p0 = points[0];
        Arrays.sort(points, 1, n, new Comparator<Point>() {
            public int compare(Point p1, Point p2) {
                int orient = orientation(p0, p1, p2);
                if (orient == 0) {
                    return (distSq(p0, p2) >= distSq(p0, p1)) ? -1 : 1;
                }
                return (orient == 2) ? -1 : 1; // Prefer counter-clockwise (Left turns)
            }
        });

        // Step 3: Traverse and build the boundary stack
        Stack<Point> hull = new Stack<>();
        hull.push(points[0]);
        hull.push(points[1]);
        hull.push(points[2]);

        for (int i = 3; i < n; i++) {
            // THE MAGIC: If the next point makes a RIGHT TURN (Clockwise = 1), 
            // the boundary is caving inward. Pop the bad point off the stack.
            while (hull.size() > 1 && orientation(peekNextToTop(hull), hull.peek(), points[i]) != 2) {
                hull.pop();
            }
            hull.push(points[i]);
        }

        System.out.println("\n--- Global Boundary Computed (Coordinates) ---");
        while (!hull.isEmpty()) {
            Point p = hull.pop();
            System.out.println("(" + p.x + ", " + p.y + ")");
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Computational Geometry Engine ---");
        
        Point[] dataset = {
            new Point(0, 3), new Point(1, 1), new Point(2, 2), new Point(4, 4),
            new Point(0, 0), new Point(1, 2), new Point(3, 1), new Point(3, 3)
        };
        
        computeConvexHull(dataset, dataset.length);
        
        System.out.println("\nStatus: Convex Hull perimeter locked. Internal outliers successfully discarded.");
    }
}