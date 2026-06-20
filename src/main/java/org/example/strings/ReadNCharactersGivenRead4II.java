package org.example.strings;

public class ReadNCharactersGivenRead4II {

    // ----- Stub for the provided API: read4 fills up to 4 chars from a hidden source. -----
    private String source = "";   // settable backing file, swappable from main for demos
    private int sourcePos = 0;    // how far read4 has consumed the source

    public void setSource(String file) {
        this.source = file;
        this.sourcePos = 0;
    }

    private int read4(char[] buf4) {
        int n = Math.min(4, source.length() - sourcePos);
        for (int i = 0; i < n; i++) buf4[i] = source.charAt(sourcePos++);
        return n;
    }

    // ----- Carried-over state between read() calls. -----
    private final char[] buf4 = new char[4]; // leftover chars from the previous read4 burst
    private int buf4Size = 0;                 // how many valid chars currently sit in buf4
    private int buf4Pos = 0;                  // index of the next unconsumed char in buf4

    /**
     * read4 only hands back chars in chunks of 4, so a single read(n) may grab fewer
     * than 4 and must REMEMBER the unconsumed tail for the next call — that internal
     * buffer is the whole trick of the "call multiple times" variant. Each read first
     * drains any leftover chars in buf4, then pulls fresh chunks via read4 until it
     * has n chars or the source is exhausted (a short read4 signals EOF). Any chars
     * fetched beyond n stay parked in buf4 for the following call.
     *
     * Time:  O(n)  — amortized across calls; each source char is moved into buf once.
     * Space: O(1)  — a fixed 4-char carry buffer regardless of n.
     */
    public int read(char[] buf, int n) {
        int written = 0;
        while (written < n) {
            if (buf4Pos == buf4Size) {           // buffer empty -> fetch the next chunk
                buf4Size = read4(buf4);
                buf4Pos = 0;
                if (buf4Size == 0) break;        // short/empty read4 => source fully drained
            }
            // Copy as much of the buffered chunk as the request still needs.
            while (written < n && buf4Pos < buf4Size) {
                buf[written++] = buf4[buf4Pos++];
            }
        }
        return written;   // may be < n only at end of source
    }

    public static void main(String[] args) {
        // Demo: same reader, multiple read() calls — leftover chars must carry across calls.
        ReadNCharactersGivenRead4II r = new ReadNCharactersGivenRead4II();
        r.setSource("abcdefg");
        char[] buf = new char[10];

        int n1 = r.read(buf, 1);
        System.out.println(n1 + ":" + new String(buf, 0, n1)); // expected: 1:a   (3 chars carried in buf4)
        int n2 = r.read(buf, 3);
        System.out.println(n2 + ":" + new String(buf, 0, n2)); // expected: 3:bcd (drains carry, no new read4)
        int n3 = r.read(buf, 5);
        System.out.println(n3 + ":" + new String(buf, 0, n3)); // expected: 3:efg (only 3 left in source)
        int n4 = r.read(buf, 2);
        System.out.println(n4 + ":" + new String(buf, 0, n4)); // expected: 0:    (source exhausted)

        // Fresh source resets state cleanly.
        r.setSource("leetcode");
        int m1 = r.read(buf, 5);
        System.out.println(m1 + ":" + new String(buf, 0, m1)); // expected: 5:leetc
        int m2 = r.read(buf, 5);
        System.out.println(m2 + ":" + new String(buf, 0, m2)); // expected: 3:ode
    }
}
