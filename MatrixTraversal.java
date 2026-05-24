public class MatrixTraversal {
    public static void main(String[] args) {
        // A 2D array representing a 3x3 matrix 
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };

        System.out.println("2D Matrix Elements:");
        // Outer loop handles the rows
        for (int row = 0; row < matrix.length; row++) {
            // Inner loop handles the columns in that specific row
            for (int col = 0; col < matrix[row].length; col++) {
                System.out.print(matrix[row][col] + " ");
            }
            System.out.println(); // Move to the next line after a row finishes
        }
    }
}