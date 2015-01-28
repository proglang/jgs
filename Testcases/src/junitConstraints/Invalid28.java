package junitConstraints;

import static security.Definition.*;

public class Invalid28 {

    public static void main(String[] args) {
    }

    @Constraints({ "low <= @return", "low = @return[", "low = @return[[" })
    public int[] invalid30() {
        return arrayIntLow(23);
    }

}
