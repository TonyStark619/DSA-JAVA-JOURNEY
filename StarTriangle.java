public class StarTriangle {
    public static void main(String[] args) {
        int rows = 5;
        
        // Outer loop controls the rows
        for (int i = 1; i <= rows; i++) {
            // Inner loop controls the columns (stars per row)
            for (int j = 1; j <= i; j++) {
                System.out.print("* ");
            }
            // Move to the next line after each row is printed
            System.out.println();
        }
    }
}