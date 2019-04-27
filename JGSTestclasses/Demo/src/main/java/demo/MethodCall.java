package demo;

import de.unifreiburg.cs.proglang.jgs.support.*;

public class MethodCall {

    @Constraints("LOW <= @0")
    @Effects({"LOW", "HIGH", "?"})
    public static void main(String[] args) {
        int a = DynamicLabel.makeLow(5);
        int b = DynamicLabel.makeLow(7);
        int z = add(a, b);
        //int z = add(5, 7);
        //int r = z;
        IOUtils.printSecret(z);
    }


    @Constraints({"@0 <= ?", "@1 <= ?", "@ret <= ?", "@0 <= @ret", "@1 <= @ret"})
    @Effects({"HIGH", "LOW", "?"})
    public static int add(int x, int y){
        return x + y;
    }

}
