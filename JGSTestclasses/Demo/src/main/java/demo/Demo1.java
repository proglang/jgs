package demo;

import de.unifreiburg.cs.proglang.jgs.support.*;

/**
 * Simple JGS demo.
 *
 * Security lattice: LOW < MIDDLE < HIGH
 */
public class Demo1 {

    static int z = 7;
    @Sec("?")
    static String dynField;

    @Sec("HIGH")
    static String staticField;

    @Constraints("LOW <= @0")
    @Effects({"LOW", "?"})
    public static void main(String[] args) {
        int a = z;
        int x = 7;
        int e = x;
        int y = 5;
        int g = x + y;

        String check = new String("");

        int c = Casts.cast("? ~> MEDIUM", x);

        Demo1 demo1 = new Demo1();
        demo1.dynField = "2";
        dynField = "5";

        Float f = 5.0f;
        String.valueOf(f);
        int u = f.intValue();


        String secret = IOUtils.readSecret(); // <- library method
        /* secret has level H as it is read using readSecret() */
        IOUtils.printSecret(secret);          // <- no leak

        /* secret has level H, hence println() causes an error */
        // System.out.println(secret);        // <- static leak

        /* dynField has level ? and it cannot be cast to HIGH which is secret's level, hence causes an error */
        // dynField = Casts.cast("HIGH ~> ?", secret);             // <- journey through dynamic code

        /* ? cannot flow into HIGH type */
        // IOUtils.printSecret(Casts.cast("? ~> HIGH", dynField)); // <-

        /* ? can flow into LOW type */
        // IOUtils.printSecret(Casts.cast("? ~> LOW", dynField)); // <- dynamically detected leak


        /* staticField has level H like secret, so assignment and printSecret cause no error */
        /* staticField = secret;
        IOUtils.printSecret(staticField); */
    }

    public void test(){
        this.dynField = "fgdhc";
    }

}
