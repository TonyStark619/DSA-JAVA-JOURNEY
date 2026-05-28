public class StringOptimization {
    public static void main(String[] args) {
        // StringBuilder prevents memory overflow when looping
        StringBuilder builder = new StringBuilder();
        
        System.out.println("Building the alphabet...");
        
        // Appending characters one by one
        for (int i = 0; i < 26; i++) {
            char ch = (char) ('a' + i);
            builder.append(ch); // This modifies the original object directly
        }
        
        System.out.println("Final String: " + builder.toString());
        
        // StringBuilder also has powerful built-in methods
        System.out.println("Reversed instantly: " + builder.reverse().toString());
    }
}