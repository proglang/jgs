package stubs;

import static security.Definition.*;


public class SpecialArrays {

    @FieldSecurity({ "low", "low" })
    public int[] lowLow;

    @FieldSecurity({ "high", "high" })
    public int[] highHigh;

    @FieldSecurity({ "low", "low", "low"})
    public int[][] lowLowLow;

    @FieldSecurity({ "low", "low", "high" })
    public int[][] lowLowHigh;

    @FieldSecurity({ "high", "high", "high" })
    public int[][] highHighHigh;


}
