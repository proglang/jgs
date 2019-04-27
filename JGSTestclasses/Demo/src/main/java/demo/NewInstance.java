package demo;

import de.unifreiburg.cs.proglang.jgs.support.*;

public class NewInstance {

    @Sec("?")
    int a;
    @Sec("?")
    double b;

    @Constraints({"@0 <= ?", "@1 <= ?"})
    NewInstance(int a, double b){
        this.a = a;
        this.b = b;
    }

    @Constraints("LOW <= @0")
    @Effects({"LOW", "?"})
    public static void main(String[] args) {
        int x = Casts.cast("LOW ~> ?", 5);
        double y = Casts.cast("HIGH ~> ?", 7.0);

        NewInstance newInstance = new NewInstance(x, y);
        //NewInstance newInstance = new NewInstance();
        newInstance = Casts.cast("LOW ~> ?", newInstance);
        IOUtils.printSecret(newInstance);
    }
}
