package demo;

import de.unifreiburg.cs.proglang.jgs.support.Constraints;
import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;
import de.unifreiburg.cs.proglang.jgs.support.Effects;
import de.unifreiburg.cs.proglang.jgs.support.IOUtils;
import util.staticResults.implementation.Dynamic;

public class ArithmeticExpr {

    @Constraints("LOW <= @0")
    @Effects({"LOW", "?"})
    public static void main(String[] args) {
        /*int a = 9;
        //int x = DynamicLabel.makeLow(5);
        int y = DynamicLabel.makeLow(7);
        int x = DynamicLabel.makeLow(15) + 5;
        x = x * 2;
        y = y + a - 6;
        int z = x + y;
        IOUtils.printSecret(z);*/

        double a = DynamicLabel.makeLow(4.0);
        //int a = 3;
        int b = DynamicLabel.makeLow(5);
        //int b = 9;
        double c = a - b;
        IOUtils.printSecret(c);

    }
}
