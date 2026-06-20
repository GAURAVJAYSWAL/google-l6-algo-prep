package org.example.trie;

import java.util.HashMap;
import java.util.Map;

/**
 * LC 359. Logger Rate Limiter.
 */
public class LoggerRateLimiter {

    /**
     * Key insight: a message is allowed iff at least 10 seconds have passed since it
     * was last printed. Rather than store the last-printed time, store the EARLIEST
     * timestamp at which the message may print again (lastPrinted + 10); then the
     * check is a single comparison and the bookkeeping is one map write. Timestamps
     * arrive non-decreasing, so the stored value is always a valid future gate.
     *
     * Time:  O(1) per call — one hash lookup and at most one insert.
     * Space: O(D) — one entry per distinct message ever printed.
     */
    static class Logger {
        private final Map<String, Integer> nextAllowed = new HashMap<>();

        public Logger() {
        }

        public boolean shouldPrintMessage(int timestamp, String message) {
            Integer allowedAt = nextAllowed.get(message);
            if (allowedAt != null && timestamp < allowedAt) {
                return false;   // still inside the 10s cooldown window
            }
            nextAllowed.put(message, timestamp + 10);
            return true;
        }
    }

    public static void main(String[] args) {
        Logger logger = new Logger();
        System.out.println(logger.shouldPrintMessage(1, "foo"));    // expected: true
        System.out.println(logger.shouldPrintMessage(2, "bar"));    // expected: true
        System.out.println(logger.shouldPrintMessage(3, "foo"));    // expected: false (foo gated until 11)
        System.out.println(logger.shouldPrintMessage(8, "bar"));    // expected: false (bar gated until 12)
        System.out.println(logger.shouldPrintMessage(10, "foo"));   // expected: false (still < 11)
        System.out.println(logger.shouldPrintMessage(11, "foo"));   // expected: true (exactly 10s later)
    }
}
