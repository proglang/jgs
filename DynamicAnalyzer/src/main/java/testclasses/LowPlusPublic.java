package testclasses;

/**
 * Created by Nicolas Müller on 20.02.17.
 * Purpose of this is to check that
 *
 * res =    i      +     j
 *          |            |
 *        DYN(LOW)     PUBLIC
 *
 * returns DYN(LOW). See {@link utils.staticResults.CustomTyping}
 */
public class LowPlusPublic {
    public static void main(String[] args) {
        int i = 9;              // b0: this is going to be DYNAMIC(LOW)
        int j = 10;             // b1: this is going to be PUBLIC
        int res = i + j;        // i2: the join of b0 and b1
        System.out.println(res);
    }
}
