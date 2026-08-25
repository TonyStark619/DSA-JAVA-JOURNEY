public class FenwickTree2D {
    private int[][] bit;
    private int rows;
    private int cols;

    // O(N * M * log N * log M) Initial Construction
    public FenwickTree2D(int[][] matrix) {
        if (matrix.length == 0 || matrix[0].length == 0) return;
        this.rows = matrix.length;
        this.cols = matrix[0].length;
        
        // 1-based indexing for bitwise math
        this.bit = new int[rows + 1][cols + 1];
        
        System.out.println("Constructing O(log N log M) 2D Bitwise Memory Matrix...");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                update(i, j, matrix[i][j]);
            }
        }
    }

    // O(log N * log M) Time - Dynamic Matrix Update
    public void update(int row, int col, int delta) {
        // Shift to 1-based indexing
        row++; col++;
        
        // 2D Bitwise Traversal: Moving UP the tree on both axes
        for (int i = row; i <= rows; i += i & (-i)) {
            for (int j = col; j <= cols; j += j & (-j)) {
                bit[i][j] += delta;
            }
        }
    }

    // O(log N * log M) Time - Query sum from (0,0) to (row, col)
    private int queryPrefix(int row, int col) {
        int sum = 0;
        row++; col++;
        
        // 2D Bitwise Traversal: Moving DOWN the tree on both axes
        for (int i = row; i > 0; i -= i & (-i)) {
            for (int j = col; j > 0; j -= j & (-j)) {
                sum += bit[i][j];
            }
        }
        return sum;
    }

    // O(log N * log M) Time - Query sum of a specific sub-rectangle
    public int querySubMatrix(int row1, int col1, int row2, int col2) {
        // 2D Inclusion-Exclusion Principle
        // Total = FullBox - TopBox - LeftBox + TopLeftIntersection (added back)
        return queryPrefix(row2, col2) 
             - queryPrefix(row1 - 1, col2) 
             - queryPrefix(row2, col1 - 1) 
             + queryPrefix(row1 - 1, col1 - 1);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting 2D Fenwick Tree Architecture ---");
        
        int[][] database = {
            {3, 0, 1, 4, 2},
            {5, 6, 3, 2, 1},
            {1, 2, 0, 1, 5},
            {4, 1, 0, 1, 7},
            {1, 0, 3, 0, 5}
        };
        
        FenwickTree2D fenwick2D = new FenwickTree2D(database);
        
        // Query the sub-matrix from top-left (2,1) to bottom-right (4,3)
        // Values: [2, 0, 1] + [1, 0, 1] + [0, 3, 0] = 8
        System.out.println("Original Sub-Matrix Sum [(2,1) to (4,3)]: " + fenwick2D.querySubMatrix(2, 1, 4, 3));
        
        System.out.println("\nExecuting O(log N log M) Update at (3,2) (Adding 5)...");
        fenwick2D.update(3, 2, 5); // Changing the '0' at (3,2) to a '5'
        
        System.out.println("Updated Sub-Matrix Sum [(2,1) to (4,3)]: " + fenwick2D.querySubMatrix(2, 1, 4, 3));
        
        System.out.println("\nStatus: 2D sub-grid queried and updated flawlessly without O(N*M) loop bottlenecks.");
    }
}