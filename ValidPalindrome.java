public class ValidPalindrome {
    public static void main(String[] args) {
        String str = "racecar"; // Test with other words
        boolean isPalindrome = true;
        
        // Place one pointer at the start and one at the end
        int start = 0;
        int end = str.length() - 1;
        
        while (start < end) {
            // If the characters don't match, it's not a palindrome
            if (str.charAt(start) != str.charAt(end)) {
                isPalindrome = false;
                break;
            }
            // Move pointers toward the center
            start++;
            end--;
        }
        
        System.out.println("Is '" + str + "' a palindrome? " + isPalindrome);
    }
}