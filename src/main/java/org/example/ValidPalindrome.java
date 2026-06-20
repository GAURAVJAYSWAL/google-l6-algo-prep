package org.example;

public class ValidPalindrome {

    // ===== YOUR CODE GOES HERE =====
    public static boolean isPalindrome(String s) {
        int start = 0, end = s.length() - 1;
        while (start < end) {
            while (start < end && !Character.isLetterOrDigit(s.charAt(start))) start++;
            while (start < end && !Character.isLetterOrDigit(s.charAt(end))) end--;
            if (Character.toLowerCase(s.charAt(start)) != Character.toLowerCase(s.charAt(end))) {
                return false;
            }
            start++;
            end--;
        }
        return true;
    }
    // ================================

    public static void main(String[] args) {
        // Each test: input string, expected result
        String[] inputs = {
                "A man, a plan, a canal: Panama",  // true
                "race a car",                       // false
                " ",                                // true  (empty after cleaning)
                "",                                 // true
                "0P",                               // false (0 vs P)
                "a.",                               // true
                "ab_a",                             // true  (underscore is non-alphanumeric)
                "Madam"                             // true
        };
        boolean[] expected = { true, false, true, true, false, true, true, true };

        int passed = 0;
        for (int i = 0; i < inputs.length; i++) {
            boolean result = isPalindrome(inputs[i]);
            boolean ok = (result == expected[i]);
            if (ok) passed++;
            System.out.printf("Test %d: input=\"%s\" | expected=%b | got=%b | %s%n",
                    i + 1, inputs[i], expected[i], result, ok ? "PASS" : "FAIL");
        }
        System.out.printf("%n%d/%d passed%n", passed, inputs.length);
    }
}