package demo;

import de.unifreiburg.cs.proglang.jgs.support.*;

public class FieldAccess_Update {

    @Sec("?")
    static String a = "v";

    @Constraints("LOW <= @0")
    @Effects({"LOW", "?"})
    public static void main(String[] args) {
        //FieldAccess_Update fieldAccess_update = new FieldAccess_Update();

        //int h = fieldAccess_update.a;
        String h = a;
        IOUtils.printSecret(h);


        /*fieldAccess_update.a = Casts.cast("? ~> HIGH", 5);
        IOUtils.printSecret(fieldAccess_update.a);*/
    }
}
