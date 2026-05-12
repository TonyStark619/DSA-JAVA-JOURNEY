public class CountDigits {
    public static void main(String[] args) {
        int number = 84592;
        int count = 0;
        
        int temp = number;
        while (temp > 0) {
            count++;
            temp /= 10; // Removes the last digit
        }
        
        System.out.println("The number " + number + " has " + count + " digits.");
    }
}