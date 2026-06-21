package org.example.bitmath;

/**
 * LC 67 — Add Binary (Easy).
 *
 * Sum two binary strings and return their binary sum, also as a string.
 */
public class AddBinary {

    /**
     * Schoolbook base-2 addition: walk both strings from the RIGHT, and at each column add
     * the two bits plus the incoming carry. The result bit is {@code sum % 2} and the carry
     * out is {@code sum / 2}; since the only state crossing columns is that single carry,
     * one right-to-left pass suffices. We append low bit first and reverse once at the end.
     * Time:  O(max(|a|, |b|)) — one pass over the longer input.
     * Space: O(max(|a|, |b|)) — the result buffer (one bit per column, plus a possible carry).
     */
    public String addBinary(String a, String b) {
        StringBuilder sb = new StringBuilder();
        int i = a.length() - 1, j = b.length() - 1, carry = 0;
        while (i >= 0 || j >= 0 || carry != 0) {            // run until both ends and carry are spent
            int sum = carry;
            if (i >= 0) sum += a.charAt(i--) - '0';        // '0'/'1' -> 0/1
            if (j >= 0) sum += b.charAt(j--) - '0';
            sb.append((char) ('0' + (sum & 1)));           // sum % 2 is the emitted bit
            carry = sum >> 1;                              // sum / 2 carries into the next column
        }
        return sb.reverse().toString();                    // built low-to-high, so flip
    }

    public static void main(String[] args) {
        AddBinary ab = new AddBinary();

        System.out.println(ab.addBinary("11", "1"));         // expected: 100
        System.out.println(ab.addBinary("1010", "1011"));    // expected: 10101
        System.out.println(ab.addBinary("0", "0"));          // expected: 0
        System.out.println(ab.addBinary("1", "111"));        // expected: 1000
    }
}
