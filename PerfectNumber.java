public class PerfectNumber {
    public static void main(String[] args) {
        int number = 28; // 28 is a perfect number (1 + 2 + 4 + 7 + 14)
        int sum = 0;

        // Find all divisors and add them to the sum
        for (int i = 1; i <= number / 2; i++) {
            if (number % i == 0) {
                sum += i;
            }
        }

        if (sum == number && number > 0) {
            System.out.println(number + " is a Perfect Number.");
        } else {
            System.out.println(number + " is NOT a Perfect Number.");
        }
    }
}