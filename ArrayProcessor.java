// Java: Array Operations & Strict Exception Handling
public class ArrayProcessor {
    public static void main(String[] args) {
        // Fixed-size, strongly-typed array
        int[] dataStream = {10, 20, 0, 40};
        
        System.out.println("--- Java Execution ---");
        
        // Intentionally pushing out of bounds to trigger an exception
        for (int i = 0; i <= dataStream.length; i++) { 
            try {
                int result = 100 / dataStream[i]; 
                System.out.println("Processed: " + result);
            } catch (ArithmeticException e) {
                System.out.println("Error: Cannot divide by zero.");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error: Array index out of bounds.");
            }
        }
    }
}