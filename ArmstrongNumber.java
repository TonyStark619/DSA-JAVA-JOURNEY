public class ArmstrongNumber {
    public static void main(String[] args) {
        int originalNum = 153;
        int temp = originalNum;
        int sum = 0;

        // An Armstrong number of 3 digits is an integer such that the sum of the cubes of its digits is equal to the number itself.
        while (temp > 0) {
            int digit = temp % 10;
            sum += digit * digit * digit;
            temp /= 10;
        }

        System.out.println("Is " + originalNum + " an Armstrong number? " + (sum == originalNum));
    }
}