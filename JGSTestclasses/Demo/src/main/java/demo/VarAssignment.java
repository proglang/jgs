package demo;

import de.unifreiburg.cs.proglang.jgs.support.*;

public class VarAssignment {

    @Constraints("LOW <= @0")
    @Effects({"HIGH", "LOW", "?"})
    public static void main(String[] args) {

        /* int x = Casts.cast("? ~> LOW", 5);
        int y = x;

        int x1 = Casts.cast("? ~> HIGH", 5);
        IOUtils.printSecret(x1); */

        double a = 5.0;
        double x2 = Casts.cast("LOW ~> ?", a);
        //int y2 = x2;
        IOUtils.printSecret(x2);

        String s = "dfjgsf";
        //System.out.println(s.toString());


        /*int x3 = Casts.cast("LOW ~> ?", 5);
        IOUtils.printSecret(x3);*/

        /*int x = DynamicLabel.makeHigh(5);
        int y = DynamicLabel.makeHigh(x);
        int z = y;
        IOUtils.printSecret(y);*/
        //String z = DynamicLabel.makeHigh(y);
        //String z = DynamicLabel.makeHigh("hello");
        //String z = y;
        //IOUtils.printSecret(x);
        //IOUtils.printSecret(y);
        //IOUtils.printSecret(z);




        //String a = DynamicLabel.makeLow("a");
        //String b = DynamicLabel.makeHigh(a);
        //String g = DynamicLabel.makeHigh(b);
        //String c = g;
        //IOUtils.printSecret(g);
        /*Boolean i = DynamicLabel.makeLow(false);
        Boolean j = DynamicLabel.makeHigh(i);
        Boolean k = DynamicLabel.makeHigh(j);
        Boolean m = k;*/
        //IOUtils.printSecret(i);


        /* String a = DynamicLabel.makeLow("hi");
        String y = DynamicLabel.makeHigh(a);
        String z = y;
        IOUtils.printSecret(z); */

        /*  String a = DynamicLabel.makeLow("hi");
        String y = DynamicLabel.makeHigh(a);
        String z = y;
        IOUtils.printSecret(a); */
    }
}
