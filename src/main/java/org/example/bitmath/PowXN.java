package org.example.bitmath;

/**
 * LC 50. Pow(x, n).
 */
public class PowXN {

    /**
     * Key insight: x^n = product of x^(2^k) over the set bits of n (binary exponentiation),
     * so we square the base each step and multiply it in only when the current low bit is 1 —
     * O(log n) multiplications instead of n. Negative exponents become 1/x^|n|; widening n to
     * long before negating avoids the Integer.MIN_VALUE overflow (-(-2^31) is not an int).
     *
     * Time:  O(log n) — one squaring per bit of the exponent.
     * Space: O(1) — a handful of scalars.
     */
    public static double myPow(double x, int n) {
        long exp = n;                 // widen first so -exp is safe even for Integer.MIN_VALUE
        if (exp < 0) {
            x = 1 / x;
            exp = -exp;
        }
        double result = 1.0;
        while (exp > 0) {
            if ((exp & 1) == 1) result *= x; // this power-of-two factor is present
            x *= x;                          // advance to the next squared base
            exp >>= 1;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(myPow(2.0, 10));                       // expected: 1024.0
        System.out.println(myPow(2.1, 3));                        // expected: ~9.261
        System.out.println(myPow(2.0, -2));                       // expected: 0.25
        System.out.println(myPow(2.0, Integer.MIN_VALUE));        // expected: 0.0 (underflow, no overflow crash)
    }
}
