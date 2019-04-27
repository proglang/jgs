package demo;

import de.unifreiburg.cs.proglang.jgs.support.*;


public class WhileLoop {

    @Constraints("LOW <= @0")
    @Effects({"LOW", "?"})
    public static void main(String[] args) {

        int x = Casts.cast("HIGH ~> ?", 7);

        while (x < 10) {
            x++;
        }


        //IOUtils.printSecret(x);
    }

}
