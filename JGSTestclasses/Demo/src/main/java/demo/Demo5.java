package demo;

import de.unifreiburg.cs.proglang.jgs.support.Casts;
import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import de.unifreiburg.cs.proglang.jgs.support.Effects;
import de.unifreiburg.cs.proglang.jgs.support.Sec;

/**
 * An example of a different security domain
 */
public class Demo5 {

    @Sec("alice")
    static String aliceField;

    @Sec("bob")
    static String bobField;

    @Sec("charlie")
    static String charlieField;

    @Constraints({"@0 ~ ?", "@1 <= pub"})
    @Effects({"alice", "bob", "charlie"})
    static void sendTo(String s, int dest) {
        if (dest == 0) {
            aliceField = Casts.cast("? ~> alice", s);
        } else if (dest == 1) {
            bobField = Casts.cast("? ~> bob", s);
        } else if (dest == 2) {
                charlieField = Casts.cast("? ~> charlie", s);
        }
    }

    @Constraints({"@0 ~ ?"})
    @Effects({"alice", "bob", "charlie"})
    public static void main(String[] args) {
        String foralice = Casts.cast("alice ~> ?", "for alice");
        String forbob = Casts.cast("bob ~> ?", "for bob");
        String forcharlie = Casts.cast("charlie ~> ?", "for charlie");
        sendTo(foralice, 0);
        sendTo(forbob, 0);
        sendTo(forcharlie, 1);
    }

}

/* ?? */