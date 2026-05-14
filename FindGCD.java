public class FindGCD {
    public static void main(String[] args) {
        int a = 36;
        int b = 60;
        int gcd = 1;

        // Loop runs until the smaller of the two numbers
        for (int i = 1; i <= a && i <= b; i++) {
            // Check if 'i' divides both numbers perfectly
            if (a % i == 0 && b % i == 0) {
                gcd = i;
            }
        }

        System.out.println("The GCD of " + a + " and " + b + " is: " + gcd);
    }
}