public class SearchIn2DArray {
    public static void main(String[] args) {
        int[][] matrix = {
            {10, 20, 30},
            {40, 50, 60},
            {70, 80, 90}
        };
        int target = 60;
        boolean found = false;

        System.out.println("Searching for " + target + " in the matrix...");
        
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                if (matrix[row][col] == target) {
                    System.out.println("Element found at Row: " + row + ", Column: " + col);
                    found = true;
                    break;
                }
            }
        }
        
        if (!found) {
            System.out.println("Element not found in the matrix.");
        }
    }
}