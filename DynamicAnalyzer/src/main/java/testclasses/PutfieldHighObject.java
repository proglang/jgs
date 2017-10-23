package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;
import testclasses.utils.C;

public class PutfieldHighObject {

    public static void main(String[] args) {
        C c = new C();
        C d = DynamicLabel.makeHigh(c);
        c.f = DynamicLabel.makeHigh(false);
        d.f = true;
        System.out.println(c.f);
    }
}
