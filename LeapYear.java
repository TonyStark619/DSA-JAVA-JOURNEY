public class LeapYear {
    public static void main(String[] args) {
        int year = 2024; // You can change this to test
        boolean isLeap = false;

        // A year is a leap year if it is divisible by 4
        // But if it's a century year (divisible by 100), it must also be divisible by 400
        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
            isLeap = true;
        }

        if (isLeap) {
            System.out.println(year + " is a Leap Year.");
        } else {
            System.out.println(year + " is NOT a Leap Year.");
        }
    }
}