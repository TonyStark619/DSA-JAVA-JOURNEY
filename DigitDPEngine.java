import java.util.Arrays;

public class DigitDPEngine {

    // Memoization table: dp[index][current_sum][is_tight]
    // 20 digits is enough for a 64-bit Long (up to 10^18)
    private static long[][][] dp = new long[20][200][2];
    private static String limitNumber;
    private static int targetDigitSum;

    // O(log10(N) * targetSum * 2) Time - The Digit DP Core
    private static long calculateDigitDP(int idx, int currentSum, int tight) {
        // Base Case: We have constructed a full number. Does its digit sum match the target?
        if (idx == limitNumber.length()) {
            return (currentSum == targetDigitSum) ? 1 : 0;
        }

        // Return memoized result if this exact state has been solved before
        if (dp[idx][currentSum][tight] != -1) {
            return dp[idx][currentSum][tight];
        }

        long count = 0;
        // If tight is 1, our max digit is restricted by the actual upper limit number.
        // If tight is 0, we can use any digit from 0 to 9.
        int limitDigit = (tight == 1) ? (limitNumber.charAt(idx) - '0') : 9;

        // Try placing every valid digit at the current position
        for (int digit = 0; digit <= limitDigit; digit++) {
            int newTight = (tight == 1 && digit == limitDigit) ? 1 : 0;
            count += calculateDigitDP(idx + 1, currentSum + digit, newTight);
        }

        // Memoize and return the subproblem result
        dp[idx][currentSum][tight] = count;
        return count;
    }

    public static long executeCount(String R, int targetSum) {
        System.out.println("Executing O(log N) Digit DP Execution...");
        limitNumber = R;
        targetDigitSum = targetSum;
        
        // Reset the DP table with -1
        for (long[][] 2dArray : dp) {
            for (long[] row : 2dArray) {
                Arrays.fill(row, -1);
            }
        }
        
        // Start from index 0, initial sum 0, and tight flag = 1 (we are strictly bound by R)
        return calculateDigitDP(0, 0, 1);
    }

    public static void main(String[] args) {
        System.out.println("--- Booting Digit DP Combinatorics Engine ---");
        
        // Example: Count how many numbers from 1 to 1,000,000,000,000,000 (10^15) 
        // have a digit sum of exactly 45.
        String upperBound = "1000000000000000"; 
        int sumToFind = 45;
        
        System.out.println("Upper Bound: " + upperBound);
        System.out.println("Target Digit Sum: " + sumToFind);
        
        long result = executeCount(upperBound, sumToFind);
        
        System.out.println("\nCRITICAL RESULT: " + result + " valid numbers discovered.");
        System.out.println("Status: Combinatorial explosion bypassed. DP memoization completely decoupled from N.");
    }
}