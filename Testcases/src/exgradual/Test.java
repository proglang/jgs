package exgradual;

import static security.Definition.*;

public class Test extends stubs.Fields {

    @FieldSecurity({ "high", "low" })
    public int[] field;

    @Constraints("")
    public void test() {
        field[2] = 42;
    }

    public static void main(String[] args) {
    }

}
