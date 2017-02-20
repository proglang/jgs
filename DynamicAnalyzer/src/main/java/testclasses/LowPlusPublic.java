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
        int i = 9;      // this is going to be DYNAMIC(LOW)
        int j = 10;     // this is going to be PUBLIC
        int res = i + j;
        System.out.println(res);
    }
}
