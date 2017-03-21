package testclasses;

import testclasses.utils.C;

/**
 * Created by Nicolas Müller on 07.02.17.
 */
public class NoNSU3_Fields {

    /**
     *          C b = new C();
     *          C c = DynamicLabel.makeHigh(b);
     *          c.f = true;
     *
     *          This code throws a NSU error, since we access f through high sec-object c
     *          The following code reconstructs a similar scenario, where NSU check is triggered,
     *          but there's no invalid access
     */
   public static void main(String[] args) {
       C b = new C();
       b.f = true;          // should trigger NSU, but no Invalid Flow!
   }
}
