public class LargestOfThree {
    public static void main(String[] args) {
        int a = 45;
        int b = 89;
        int c = 23;

        int max = a;
        if (b > max) {
            max = b;
        }
        if (c > max) {
            max = c;
        }

        System.out.println("The largest number is: " + max);
    }
}