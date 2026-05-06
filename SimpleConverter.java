import java.util.Scanner;

public class SimpleConverter {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("--- INR to JPY Converter ---");
        System.out.print("Enter amount in Rupees (₹): ");
        
        float rupees = in.nextFloat();
        
        // As of today, 1 INR is approx 1.85 JPY
        float yen = rupees * 1.85f;
        
        System.out.println("Amount in Japanese Yen: ¥" + yen);
        System.out.println("Conversion Successful!");
        
        in.close();
    }
}