public class RecursiveFibonacci {
    public static void main(String[] args) {
        int n = 7; // Let's find the 7th number in the sequence
        int result = fibo(n);
        System.out.println("The " + n + "th Fibonacci number is: " + result);
    }

    // A method that calls itself twice
    static int fibo(int n) {
        // Base condition: The 0th and 1st numbers are 0 and 1
        if (n < 2) {
            return n;
        }
        // Recursive call: The sum of the previous two numbers
        return fibo(n - 1) + fibo(n - 2);
    }
}