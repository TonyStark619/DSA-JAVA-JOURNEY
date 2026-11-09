import java.util.*;

public class DancingLinksExactCoverEngine {

    // 4-Way Toroidal Node
    static class DancingNode {
        DancingNode left, right, up, down;
        ColumnHeader column;
        int rowId;

        public DancingNode() {
            this.left = this.right = this.up = this.down = this;
        }

        public DancingNode(ColumnHeader col, int rowId) {
            this();
            this.column = col;
            this.rowId = rowId;
        }
    }

    // Specialized Column Header tracking column size for MRV heuristic
    static class ColumnHeader extends DancingNode {
        int size;
        String name;

        public ColumnHeader(String name) {
            super();
            this.size = 0;
            this.name = name;
            this.column = this;
        }
    }

    private final ColumnHeader masterHeader;
    private final ColumnHeader[] columns;
    private final List<Integer> solutionStack = new ArrayList<>();

    public DancingLinksExactCoverEngine(String[] columnNames) {
        this.masterHeader = new ColumnHeader("MASTER_ROOT");
        this.columns = new ColumnHeader[columnNames.length];

        DancingNode prev = masterHeader;
        for (int i = 0; i < columnNames.length; i++) {
            ColumnHeader col = new ColumnHeader(columnNames[i]);
            columns[i] = col;

            // Wire horizontally into the circular header ring
            col.left = prev;
            col.right = masterHeader;
            prev.right = col;
            masterHeader.left = col;
            prev = col;
        }
    }

    // Appends a row of 1-bits at the specified column indices
    public void addConstraintRow(int rowId, int[] activeColumnIndices) {
        DancingNode firstInRow = null;

        for (int colIdx : activeColumnIndices) {
            ColumnHeader col = columns[colIdx];
            DancingNode newNode = new DancingNode(col, rowId);

            // Wire vertically into the bottom of the circular column list
            newNode.up = col.up;
            newNode.down = col;
            col.up.down = newNode;
            col.up = newNode;
            col.size++;

            // Wire horizontally into the circular row list
            if (firstInRow == null) {
                firstInRow = newNode;
            } else {
                newNode.left = firstInRow.left;
                newNode.right = firstInRow;
                firstInRow.left.right = newNode;
                firstInRow.left = newNode;
            }
        }
    }

    // O(1) per node - Surgically detaches a column and all rows intersecting it
    private void cover(ColumnHeader targetCol) {
        targetCol.right.left = targetCol.left;
        targetCol.left.right = targetCol.right;

        for (DancingNode rowNode = targetCol.down; rowNode != targetCol; rowNode = rowNode.down) {
            for (DancingNode rightNode = rowNode.right; rightNode != rowNode; rightNode = rightNode.right) {
                rightNode.down.up = rightNode.up;
                rightNode.up.down = rightNode.down;
                rightNode.column.size--;
            }
        }
    }

    // O(1) per node - Exact reverse pointer restoration (Knuth's Dancing Links invariant)
    private void uncover(ColumnHeader targetCol) {
        for (DancingNode rowNode = targetCol.up; rowNode != targetCol; rowNode = rowNode.up) {
            for (DancingNode leftNode = rowNode.left; leftNode != rowNode; leftNode = leftNode.left) {
                leftNode.column.size++;
                leftNode.down.up = leftNode;
                leftNode.up.down = leftNode;
            }
        }
        targetCol.right.left = targetCol;
        targetCol.left.right = targetCol;
    }

    // Selects the column with the minimum remaining 1s to minimize branching factor
    private ColumnHeader selectMinimumBranchingColumn() {
        ColumnHeader bestCol = null;
        int minSize = Integer.MAX_VALUE;

        for (DancingNode curr = masterHeader.right; curr != masterHeader; curr = curr.right) {
            ColumnHeader col = (ColumnHeader) curr;
            if (col.size < minSize) {
                minSize = col.size;
                bestCol = col;
            }
        }
        return bestCol;
    }

    // Algorithm X Recursive Search
    public boolean executeAlgorithmX() {
        // If the master header points to itself, all constraints are satisfied!
        if (masterHeader.right == masterHeader) {
            return true;
        }

        ColumnHeader chosenCol = selectMinimumBranchingColumn();
        if (chosenCol == null || chosenCol.size == 0) {
            return false; // Dead end isolated immediately
        }

        cover(chosenCol);

        for (DancingNode r = chosenCol.down; r != chosenCol; r = r.down) {
            solutionStack.add(r.rowId);

            for (DancingNode j = r.right; j != r; j = j.right) {
                cover(j.column);
            }

            if (executeAlgorithmX()) {
                return true;
            }

            // Backtrack: Uncover in strict reverse order (Right-to-Left)
            solutionStack.remove(solutionStack.size() - 1);
            for (DancingNode j = r.left; j != r; j = j.left) {
                uncover(j.column);
            }
        }

        uncover(chosenCol);
        return false;
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Knuth's Dancing Links (DLX) Toroidal Engine ---");

        String[] constraints = {"C0", "C1", "C2", "C3", "C4", "C5", "C6"};
        DancingLinksExactCoverEngine dlx = new DancingLinksExactCoverEngine(constraints);

        // Classic Knuth Exact Cover Matrix:
        // Row 1: {C2, C4, C5}
        // Row 2: {C0, C3, C6}
        // Row 3: {C1, C2, C5}
        // Row 4: {C0, C3}
        // Row 5: {C1, C6}
        // Row 6: {C3, C4, C6}
        // Exact Cover Solution -> Rows {1, 4, 5} cover {C2,C4,C5} U {C0,C3} U {C1,C6} = {C0..C6}
        dlx.addConstraintRow(1, new int[]{2, 4, 5});
        dlx.addConstraintRow(2, new int[]{0, 3, 6});
        dlx.addConstraintRow(3, new int[]{1, 2, 5});
        dlx.addConstraintRow(4, new int[]{0, 3});
        dlx.addConstraintRow(5, new int[]{1, 6});
        dlx.addConstraintRow(6, new int[]{3, 4, 6});

        System.out.println("  [System] Executing Toroidal Pointer Cover/Uncover Sweep...");
        boolean solved = dlx.executeAlgorithmX();

        System.out.println("\n--- Exact Cover Resolution Diagnostics ---");
        System.out.println("  Satisfiable?          : " + solved);
        System.out.println("  Optimal Disjoint Rows : " + dlx.solutionStack);

        System.out.println("\nStatus: Exact Cover resolved with zero backtracking heap allocations via 4-way toroidal pointers.");
    }
}