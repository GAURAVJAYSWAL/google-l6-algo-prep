package org.example.bitmath;

/**
 * LC 204. Count Primes.
 */
public class CountPrimes {

    /**
     * Key insight: Sieve of Eratosthenes. Assume every number is prime, then for each prime i
     * strike out its multiples. Crossing can start at i*i (not 2*i) because any smaller multiple
     * k*i with k &lt; i was already crossed when the smaller factor k was processed; this is what
     * brings the work down to O(n log log n).
     *
     * Time:  O(n log log n) — the harmonic-over-primes sum of multiples crossed.
     * Space: O(n) — one boolean per candidate below n.
     */
    public static int countPrimes(int n) {
        if (n < 3) return 0;                          // no primes below 2
        boolean[] composite = new boolean[n];         // composite[k] == true means k is not prime
        int count = 0;
        for (int i = 2; i < n; i++) {
            if (composite[i]) continue;
            count++;
            if ((long) i * i >= n) continue;          // avoid int overflow on the start index
            for (int j = i * i; j < n; j += i) {      // start at i*i; smaller multiples already crossed
                composite[j] = true;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(countPrimes(10));   // expected: 4  (2,3,5,7)
        System.out.println(countPrimes(0));    // expected: 0
        System.out.println(countPrimes(1));    // expected: 0
        System.out.println(countPrimes(2));    // expected: 0  (primes strictly less than 2)
        System.out.println(countPrimes(100));  // expected: 25
    }
}
