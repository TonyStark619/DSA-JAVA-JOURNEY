public class FirstUniqueChar {
    public static void main(String[] args) {
        String str = "intelligence"; // Test string
        int[] charCounts = new int[26];

        // Step 1: Count the frequencies of all characters
        for (int i = 0; i < str.length(); i++) {
            charCounts[str.charAt(i) - 'a']++;
        }

        // Step 2: Find the first character with a frequency of exactly 1
        int uniqueIndex = -1;
        for (int i = 0; i < str.length(); i++) {
            if (charCounts[str.charAt(i) - 'a'] == 1) {
                uniqueIndex = i;
                break; // Stop at the first unique character found
            }
        }

        if (uniqueIndex != -1) {
            System.out.println("First unique character is: '" + str.charAt(uniqueIndex) + "'");
        } else {
            System.out.println("No unique character found in the string.");
        }
    }
}