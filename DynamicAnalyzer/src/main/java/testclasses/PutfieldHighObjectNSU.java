package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;
import testclasses.utils.C;

public class PutfieldHighObjectNSU {

    public static void main(String[] args) {
        C c = new C();
        C d = DynamicLabel.makeHigh(c);
        d.f = true;
        System.out.println(c.f);
    }
}
