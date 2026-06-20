package org.example.dp;

public class PoorPigs {

    /**
     * Information-theoretic counting, not search. In one round a pig either dies or
     * survives; with t = floor(minutesToTest / minutesToDie) rounds available, a single
     * pig's outcome can be "died in round 1, 2, ..., t, or never died" = (t + 1)
     * distinguishable states. Pigs act independently, so p pigs encode (t + 1)^p joint
     * states, and we need at least one state per bucket: (t + 1)^p >= buckets. Hence the
     * minimum p = ceil(log_{t+1}(buckets)). We compute it by multiplying (t+1) until the
     * product covers all buckets — integer-only, dodging log/float rounding errors.
     * Time:  O(log_{t+1}(buckets)) — one multiply per pig added.
     * Space: O(1) — a running product and counter.
     */
    public static int poorPigs(int buckets, int minutesToDie, int minutesToTest) {
        int statesPerPig = minutesToTest / minutesToDie + 1;   // t + 1 outcomes per pig
        int pigs = 0;
        long reachable = 1;                         // (statesPerPig)^pigs so far
        while (reachable < buckets) {               // need (t+1)^pigs >= buckets
            reachable *= statesPerPig;
            pigs++;
        }
        return pigs;
    }

    public static void main(String[] args) {
        System.out.println(poorPigs(1000, 15, 60));
        // expected: 5 (5^5 = 3125 >= 1000, 4 pigs give only 625)

        System.out.println(poorPigs(4, 15, 15));
        // expected: 2 (2^2 = 4)

        System.out.println(poorPigs(1, 15, 15));
        // expected: 0 (the single bucket needs no test)

        System.out.println(poorPigs(1000, 15, 30));
        // expected: 7 (3 states per pig, 3^7 = 2187 >= 1000, 3^6 = 729 too few)
    }
}
