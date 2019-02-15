package pkg2;

import de.unifreiburg.cs.proglang.jgs.support.*;
import static de.unifreiburg.cs.proglang.jgs.support.StringUtil.*;

public class Examples {

    static class Logger {
        @Sec("LOW")
        String buf;

        @Sec("HIGH")
        String hbuf;

        @Sec("?")
        String dbuf;
    }

    @Constraints({"@0 <= @ret", "@1 <= @ret"})
    static int max(int x, int y) {
        if (x <= y) { x = y; }
        return x;
    }

    @Constraints({"@1 <= @ret", "@2 <= @ret", "@0 <= LOW"})
    @Effects({"LOW"})
    static int maxMsg(Logger log, int x, int y) {
        if (x <= y) { x = y; }
        log.buf = "max was called";
        return x;
    }

    @Sec("HIGH")
    static int secret;
    @Sec("LOW")
    static int fortyTwo = 42;

    @Effects({"LOW"})
    static void callingPolymorphicMethods() {
        Logger log = new Logger();
        int x = Casts.castHighToDyn(secret);
        int y = Casts.castLowToDyn(fortyTwo);
        int r;

        r = maxMsg(log, secret, fortyTwo);
        r = maxMsg(log, x, y);

        // uncomment for type error
        // r = maxMsg(log, secret, y);

        if (x == 42) {
            // uncomment for type error
            // r = maxMsg(log, x, y);
        }

    }

    @Constraints({"@1 <= @ret", "@2 <= @ret", "@0 <= ?"})
    @Effects({"?"})
    static int maxMsgDyn(Logger log, int x, int y) {
        if (x <= y) { x = y; }
        log.dbuf = "max was called";
        return x;
    }

    @Constraints({"HIGH <= @2", "@3 <= LOW", "@1 <= ?", "@1 <= LOW", "@0 <= LOW", "@0 <= ?"})
    @Effects({"?", "LOW"})
    static void logResults(Logger log, boolean privMode, String h, String l) {
        log.dbuf = append("public result ", Casts.castLowToDyn(l));
        if (privMode) {
            log.dbuf = append("secret result ", Casts.castHighToDyn(h));
        }
        if (!privMode) {
            log.buf = Casts.castDynToLow(log.dbuf);
        }
    }

    interface Modifier {
        @Constraints({"@0 <= @ret"})
        int modify(int x);
    }

    @Constraints({"@0 <= @ret", "@1 <= @ret", "@2 <= @ret"})
    static int maxMod(int x, int y, Modifier ymod) {
        int z = ymod.modify(y);
        if (x <= z) { return z; } else { return x; }
    }

    static class Id implements Modifier {

        @Constraints({"@0 <= @ret"})
        public int modify(int x) {
            return x;
        }
    }


    @Constraints({"@0 <= @ret", "@1 <= @ret"})
    static int max2(int x, int y) {
        return maxMod(x, y, new Id());
    }

    static class Erase implements Modifier {

        @Sec("pub")
        private int def;

        @Constraints({"@0 <= pub"})
        public Erase(int def) {
            this.def = def;
        }

        public int modify(int x) {
            return def;
        }
    }

    @Sec("LOW")
    static int lowField;
    @Sec("HIGH")
    static int highField;

    @Effects({"LOW"})
    static void m() {
        // does not typecheck, although sound
        // lowField = maxMod(lowField, highField, new Erase(0));
        // using dynamic types is allowed
        lowField = Casts.castDynToLow(maxMod(Casts.castLowToDyn(lowField), Casts.castHighToDyn(highField), new Erase(0)));
    }

    public static void main(String args[]) {
        m();
        max(5, 7);
        Logger log = new Logger();
        maxMsg(log, 5, 7);
        maxMsgDyn(log, 5, 7);
        callingPolymorphicMethods();
        logResults(log, true, "a", "b");
        maxMod(5, 7, new Id());
        max2(5, 8);
    }

}
