package junitConstraints;

import static security.Definition.*;
import security.Definition.Constraints;
import security.Definition.FieldSecurity;

public class SuccessWriteEffect extends stubs.Fields {

    @FieldSecurity("low")
    public stubs.PCI pci;

    public static void main(String[] args) {
    }

    @Constraints("@pc <= low")
    public void successWriteEffect1() {
        lowIField = 42;
    }

    @Constraints("@pc <= high")
    public void successWriteEffect2() {
        highIField = 42;
    }

    public void successWriteEffect3() {
        highIField = 42;
    }

    @Constraints("@pc <= low")
    public void successWriteEffect4() {
        highIField = 42;
    }

    @Constraints("@pc <= low")
    public void successWriteEffect5() {
        lowLowIField = arrayIntLow(42);
    }

    @Constraints("@pc <= low")
    public void successWriteEffect6() {
        lowLowIField[23] = 42;
    }

    @Constraints("@pc <= low")
    public void successWriteEffect7() {
        lowHighIField = arrayIntHigh(42);
    }

    public void successWriteEffect8() {
        lowHighIField[23] = 42;
    }

    @Constraints("@pc <= high")
    public void successWriteEffect9() {
        lowHighIField[23] = 42;
    }

    public void successWriteEffect10() {
        highHighIField = arrayIntHigh(42);
    }

    @Constraints("@pc <= high")
    public void successWriteEffect11() {
        highHighIField = arrayIntHigh(42);
    }

    public void successWriteEffect12() {
        highHighIField[23] = 42;
    }

    @Constraints("@pc <= high")
    public void successWriteEffect13() {
        highHighIField[23] = 42;
    }

    @Constraints("@pc <= low")
    public void successWriteEffect14() {
        int[] arr = arrayIntLow(42);
        arr[23] = 42;
    }

    @Constraints("@pc <= high")
    public void successWriteEffect15() {
        int[] arr = arrayIntHigh(42);
        arr[23] = 42;
    }

    public void successWriteEffect16() {
        int[] arr = arrayIntHigh(42);
        arr[23] = 42;
    }

    @Constraints("@pc <= low")
    public void successWriteEffect17() {
        pci.lowPC();
    }

    @Constraints("@pc <= low")
    public void successWriteEffect18() {
        stubs.PCSHigh.lowPC();
    }

    @Constraints("@pc <= low")
    public void successWriteEffect19() {
        stubs.PCSLow.lowPC();
    }

    @Constraints("@pc <= low")
    public void successWriteEffect20() {
        stubs.PCSLow.highPC();
    }

    @SuppressWarnings("unused")
    @Constraints("@pc <= low")
    public void successWriteEffect21() {
        int i = stubs.PCSLow.lowSField;
    }

    @SuppressWarnings("unused")
    @Constraints("@pc <= low")
    public void successWriteEffect22() {
        int i = stubs.PCSLow.highSField;
    }

    @SuppressWarnings("unused")
    @Constraints("@pc <= high")
    public void successWriteEffect23() {
        int i = stubs.PCSHigh.highSField;
    }

    @Constraints("@pc <= high")
    public void successWriteEffect24() {
        stubs.PCSHigh.highPC();
    }

    @Constraints("@pc <= high")
    public void successWriteEffect25() {
        pci.highPC();
    }

}
