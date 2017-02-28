package testclasses;

import com.sun.tools.corba.se.idl.toJavaPortable.Helper;
import utils.analyzer.HelperClass;

/**
 * Created by Nicolas Müller on 07.02.17.
 */
public class NoNSU1 {
    public static void main(String[] args) {
        int x = 3;
        int y = HelperClass.makeHigh(7);
        int z = x + y;
        System.out.println(z);
    }
}
