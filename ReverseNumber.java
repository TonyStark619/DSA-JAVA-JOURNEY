import java.util.Scanner;

public class ReverseNumber {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter a number to reverse: ");
        int num = in.nextInt();
        
        int ans = 0;
        
        // Core Logic: Extracting digits from right to left
        while (num > 0) {
            int rem = num % 10;       // Get the last digit
            num = num / 10;           // Remove the last digit from original number
            ans = ans * 10 + rem;     // Append digit to the new reversed number
        }
        
        System.out.println("Reversed number: " + ans);
    }
}