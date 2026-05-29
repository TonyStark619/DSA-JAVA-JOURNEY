public class ValidAnagram {
    public static void main(String[] args) {
        String str1 = "listen";
        String str2 = "silent";

        // If lengths are different, they cannot be anagrams
        if (str1.length() != str2.length()) {
            System.out.println("Not anagrams.");
            return;
        }

        // Create an array to count character frequencies (assuming lowercase English letters)
        int[] charCounts = new int[26];

        // Increment for str1, decrement for str2
        for (int i = 0; i < str1.length(); i++) {
            charCounts[str1.charAt(i) - 'a']++;
            charCounts[str2.charAt(i) - 'a']--;
        }

        // If they are anagrams, all counts will have returned to zero
        boolean isAnagram = true;
        for (int count : charCounts) {
            if (count != 0) {
                isAnagram = false;
                break;
            }
        }

        System.out.println("Are '" + str1 + "' and '" + str2 + "' anagrams? " + isAnagram);
    }
}