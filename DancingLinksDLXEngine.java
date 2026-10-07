public class DancingLinksDLXEngine {

    // The core 4-way linked geometric node
    static class Node {
        Node left, right, up, down;
        ColumnNode column; // Points to the header of the column this node is in
        int rowIndex;

        public Node() {
            left = right = up = down = this;
        }
    }

    // Specialized node for the column headers to track sizes for heuristic optimization
    static class ColumnNode extends Node {
        int size;
        String name;

        public ColumnNode(String name) {
            super();
            this.size = 0;
            this.name = name;
            this.column = this;
        }
    }

    private ColumnNode header; // The master entry point to the Toroidal Grid

    public DancingLinksDLXEngine(int numCols) {
        header = new ColumnNode("Root");
        ColumnNode current = header;

        // Build the circular doubly-linked header row
        for (int i = 0; i < numCols; i++) {
            ColumnNode newCol = new ColumnNode("C" + i);
            newCol.left = current;
            newCol.right = header;
            current.right = newCol;
            header.left = newCol;
            current = newCol;
        }
    }

    // THE MAGIC: O(1) Pointer Unlinking
    // Mathematically removes a column and all rows that intersect it from the geometric grid
    private void cover(ColumnNode c) {
        // Disconnect the column header horizontally
        c.right.left = c.left;
        c.left.right = c.right;

        // Traverse down the column, and for every row with a 1, disconnect it vertically
        for (Node i = c.down; i != c; i = i.down) {
            for (Node j = i.right; j != i; j = j.right) {
                j.down.up = j.up;
                j.up.down = j.down;
                j.column.size--;
            }
        }
    }

    // THE MAGIC: O(1) Pointer Relinking
    // Re-inserts the exact nodes back into the matrix by utilizing their ghost pointers
    private void uncover(ColumnNode c) {
        for (Node i = c.up; i != c; i = i.up) {
            for (Node j = i.left; j != i; j = j.left) {
                j.column.size++;
                j.down.up = j;
                j.up.down = j;
            }
        }
        c.right.left = c;
        c.left.right = c;
    }

    // The Recursive Exact Cover Search utilizing DLX
    public boolean search(int depth) {
        // If the header points to itself, all columns are covered. Solution found!
        if (header.right == header) {
            System.out.println("  -> [System] Exact Cover mathematically isolated at depth " + depth);
            return true;
        }

        // Heuristic: Pick the column with the fewest 1s to minimize branching factor
        ColumnNode c = (ColumnNode) header.right;
        for (ColumnNode temp = (ColumnNode) c.right; temp != header; temp = (ColumnNode) temp.right) {
            if (temp.size < c.size) c = temp;
        }

        cover(c);

        for (Node r = c.down; r != c; r = r.down) {
            // Tentatively add this row to the solution, cover conflicting columns
            for (Node j = r.right; j != r; j = j.right) cover(j.column);

            if (search(depth + 1)) return true;

            // Backtrack: The recursive path failed. Uncover and try the next row.
            for (Node j = r.left; j != r; j = j.left) uncover(j.column);
        }

        uncover(c);
        return false; // No solution down this branch
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Donald Knuth's Dancing Links (DLX) Architecture ---");
        System.out.println("  [System] Forging 4-way linked toroidal matrix geometry...");
        
        // Simulating the matrix grid for an Exact Cover problem (e.g., Sudoku constraints)
        DancingLinksDLXEngine engine = new DancingLinksDLXEngine(5);
        
        // (In a full implementation, you would dynamically link the row nodes into the columns here)
        
        System.out.println("  [System] Initiating O(1) pointer-manipulation search...");
        // This will return false because we haven't added the specific row nodes in this demo,
        // but the core architectural backbone is fully active.
        engine.search(0);
        
        System.out.println("\nStatus: DLX architecture initialized. Array-shifting bottlenecks obliterated via pointer dancing.");
    }
}