public class RecursiveFactorial {
    public static void main(String[] args) {
        int number = 5;
        int result = calculateFactorial(number);
        System.out.println("The factorial of " + number + " is: " + result);
    }

    // A method that calls itself
    static int calculateFactorial(int n) {
        // Base condition: Tells the recursion when to stop
        if (n == 0 || n == 1) {
            return 1;
        }
        // Recursive call: n * factorial of (n-1)
        return n * calculateFactorial(n - 1);
    }
}