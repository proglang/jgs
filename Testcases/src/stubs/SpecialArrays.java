package stubs;

import static security.Definition.*;

public class SpecialArrays {

    @FieldSecurity({ "low", "low" })
    public int[] lowLow;

    @FieldSecurity({ "low", "high" })
    public int[] lowHigh;

    @FieldSecurity({ "high", "high" })
    public int[] highHigh;

    @FieldSecurity({ "low", "low", "low" })
    public int[][] lowLowLow;

    @FieldSecurity({ "low", "low", "high" })
    public int[][] lowLowHigh;

    @FieldSecurity({ "low", "high", "high" })
    public int[][] lowHighHigh;

    @FieldSecurity({ "high", "high", "high" })
    public int[][] highHighHigh;

}
