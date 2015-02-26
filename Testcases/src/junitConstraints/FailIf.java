package junitConstraints;

import static security.Definition.*;
import security.Definition.Constraints;

public class FailIf extends stubs.MinimalFields {

    public static void main(String[] args) {
    }

    @Constraints("@pc <= low")
    public void failIf1() {
        if (lowIField == 0) {
            // @security("Illegal flow from high to low")
            lowIField = mkHigh(42);
        }
    }

    @Constraints("@pc <= low")
    public void failIf2() {
        if (highIField == 0) {
            // @security("Illegal flow from high to low")
            lowIField = 42;
        }
    }

    @Constraints("@pc <= low")
    public void failIf3() {
        if (highIField == 0) {
            // @security("Illegal flow from high to low")
            lowIField = mkHigh(42);
        }
    }

    @Constraints("@pc <= low")
    public void failIf4() {
        if (lowIField == 0)
            // @security("Illegal flow from high to low")
            lowIField = mkHigh(42);
    }

    @Constraints("@pc <= low")
    public void failIf5() {     
        if (highIField == 0)
        	// @security("Illegal flow from high to low")
            lowIField = 42;
    }

    @Constraints("@pc <= low")
    public void failIf6() {        
        if (highIField == 0)
        	// @security("Illegal flow from high to low")
            lowIField = mkHigh(42);
    }

    @Constraints("low <= @return")
    public int failIf7() {
        int l = lowIField;
        int h = highIField;
        // @security("weaker return expected")
        return l == 0 ? h : l;
    }

    @Constraints("low <= @return")
    public int failIf8() {
        int l = lowIField;
        int h = highIField;
        // @security("weaker return expected")
        return l == 0 ? l : h;
    }

    @Constraints("low <= @return")
    public int failIf9() {
        int l = lowIField;
        int h = highIField;
        // @security("weaker return expected")
        return l == 0 ? h : h;
    }

    @Constraints("low <= @return")
    public int failIf10() {
        int l = lowIField;
        int h = highIField;
        // @security("weaker return expected")
        return h == 0 ? l : l;
    }

    @Constraints("low <= @return")
    public int failIf11() {
        int l = lowIField;
        int h = highIField;
        // @security("weaker return expected")
        return h == 0 ? h : l;
    }

    @Constraints("low <= @return")
    public int failIf12() {
        int l = lowIField;
        int h = highIField;
        // @security("weaker return expected")
        return h == 0 ? l : h;
    }

    @Constraints("low <= @return")
    public int failIf13() {
        int h = highIField;
        // @security("weaker return expected")
        return h == 0 ? h : h;
    }

    @Constraints("low <= @return")
    public int failIf14() {
        int h = highIField;
        int l = lowIField;
        int i = h == 0 ? l : l;
        // @security("weaker return expected")
        return i;
    }

    @Constraints("low <= @return")
    public int failIf15() {
        if (lowIField == 0) {
            return lowIField;
        } else {
            // @security("weaker return expected")
            return highIField;
        }
    }

    @Constraints("low <= @return")
    public int failIf16() {
        if (lowIField == 0) {
            // @security("weaker return expected")
            return highIField;
        } else {
            return lowIField;
        }
    }

    @Constraints("low <= @return")
    public int failIf17() {
        if (lowIField == 0) {
            // @security("weaker return expected")
            return highIField;
        } else {
            // @security("weaker return expected")
            return highIField;
        }
    }

    @Constraints("low <= @return")
    public int failIf18() {
        if (highIField == 0) {
            // @security("weaker return expected")
            return highIField;
        } else {
            // @security("weaker return expected")
            return highIField;
        }
    }

    @Constraints("low <= @return")
    public int failIf19() {
        if (highIField == 0) {
            // @security("weaker return expected")
            return lowIField;
        } else {
            // @security("weaker return expected")
            return highIField;
        }
    }

    @Constraints("low <= @return")
    public int failIf20() {
        if (highIField == 0) {
            // @security("weaker return expected")
            return highIField;
        } else {
            // @security("weaker return expected")
            return lowIField;
        }
    }

    @Constraints("low <= @return")
    public int failIf21() {
        if (highIField == 0) {
            // @security("weaker return expected")
            return lowIField;
        } else {
            // @security("weaker return expected")
            return lowIField;
        }
    }

    @Constraints("@pc <= low")
    public void failIf22() {
        if (lowIField == 0) {
            // @security("Invalid assignment")
            lowIField = mkHigh(42);
        } else {
            // Also invalid assignment - but analysis interrupted
            lowIField = mkHigh(42);
        }
    }

    @Constraints("@pc <= low")
    public void failIf23() {
        if (lowIField == 0) {
            lowIField = 42;
        } else {
            // @security("Invalid assignment")
            lowIField = mkHigh(42);
        }
    }

    @Constraints("@pc <= low")
    public void failIf24() {
        if (lowIField == 0) {
            // @security("Invalid assignment")
            lowIField = mkHigh(42);
        } else {
            lowIField = 42;
        }
    }

    @Constraints("@pc <= low")
    public void failIf25() {
        if (highIField == 0) {
            // @security("Illegal flow from high to low")
            lowIField = mkHigh(42);
        } else {
            // Also invalid assignment - but analysis interrupted
            lowIField = mkHigh(42);
        }
    }

    @Constraints("@pc <= low")
    public void failIf26() {
        if (highIField == 0) {
            // @security("Illegal flow from high to low")
            lowIField = 42;
        } else {
            // Also invalid assignment - but analysis interrupted
            lowIField = 42;
        }
    }

    @Constraints("@pc <= low")
    public void failIf27() {
        if (highIField == 0) {
            highIField = 42;
        } else {
            // @security("Illegal flow from high to low")
            lowIField = 42;
        }
    }

    @Constraints("low <= @return")
    public int failIf28() {
        if (lowIField == 0) {
            // @security("weaker return expected")
            return highIField;
        }
        return lowIField;
    }

    @Constraints("low <= @return")
    public int failIf29() {
        if (lowIField == 0) {
            return lowIField;
        }
        // @security("weaker return expected")
        return highIField;
    }

    @Constraints("low <= @return")
    public int failIf30() {
        if (lowIField == 0) {
            // @security("weaker return expected")
            return highIField;
        }
        // @security("weaker return expected")
        return highIField;
    }

    @Constraints("low <= @return")
    public int failIf31() {
        if (highIField == 0) {
            // @security("weaker return expected")
            return lowIField;
        }
        // @security("weaker return expected")
        return lowIField;
    }

    @Constraints("low <= @return")
    public int failIf32() {
        if (highIField == 0) {
            // @security("weaker return expected")
            return highIField;
        }
        // @security("weaker return expected")
        return lowIField;
    }

    @Constraints("low <= @return")
    public int failIf33() {
        if (highIField == 0) {
            // @security("weaker return expected")
            return lowIField;
        }
        // @security("weaker return expected")
        return highIField;
    }

    @Constraints("low <= @return")
    public int failIf34() {
        if (highIField == 0) {
            // @security("weaker return expected")
            return highIField;
        }
        // @security("weaker return expected")
        return highIField;
    }

    @Constraints("low <= @return")
    public int failIf35() {
        int result = 0;
        if (lowIField == 0) {
            result = highIField;
        }
        // @security("weaker return expected")
        return result;
    }

    @Constraints("low <= @return")
    public int failIf36() {
        int result = 0;
        if (highIField == 0) {
            result = lowIField;
        }
        // @security("weaker return expected")
        return result;
    }

    @Constraints("low <= @return")
    public int failIf37() {
        int result = 0;
        if (highIField == 0) {
            result = highIField;
        }
        // @security("weaker return expected")
        return result;
    }

    @Constraints("low <= @return")
    public int failIf38() {
        if (lowIField == 0) {
            if (highIField == 0) {
                // @security("weaker return expected")
                return 0;
            } else {
                // @security("weaker return expected")
                return 0;
            }
        } else {
            if (lowIField == 0) {
                return 0;
            } else {
                return 0;
            }
        }
    }

    @Constraints("low <= @return")
    public int failIf39() {
        if (lowIField == 0) {
            if (lowIField == 0) {
                return 0;
            } else {
                return 0;
            }
        } else {
            if (highIField == 0) {
                // @security("weaker return expected")
                return 0;
            } else {
                // @security("weaker return expected")
                return 0;
            }
        }
    }

    @Constraints("low <= @return")
    public int failIf40() {
        if (lowIField == 0) {
            if (highIField == 0) {
                // @security("weaker return expected")
                return 0;
            } else {
                // @security("weaker return expected")
                return 0;
            }
        } else {
            if (highIField == 0) {
                // @security("weaker return expected")
                return 0;
            } else {
                // @security("weaker return expected")
                return 0;
            }
        }
    }

    @Constraints("low <= @return")
    public int failIf41() {
        if (highIField == 0) {
            if (lowIField == 0) {
                // @security("weaker return expected")
                return 0;
            } else {
                // @security("weaker return expected")
                return 0;
            }
        } else {
            if (lowIField == 0) {
                // @security("weaker return expected")
                return 0;
            } else {
                // @security("weaker return expected")
                return 0;
            }
        }
    }

    @Constraints("low <= @return")
    public int failIf42() {
        if (highIField == 0) {
            if (highIField == 0) {
                // @security("weaker return expected")
                return 0;
            } else {
                // @security("weaker return expected")
                return 0;
            }
        } else {
            if (lowIField == 0) {
                // @security("weaker return expected")
                return 0;
            } else {
                // @security("weaker return expected")
                return 0;
            }
        }
    }

    @Constraints("low <= @return")
    public int failIf43() {
        if (highIField == 0) {
            if (highIField == 0) {
                // @security("weaker return expected")
                return 0;
            } else {
                // @security("weaker return expected")
                return 0;
            }
        } else {
            if (highIField == 0) {
                // @security("weaker return expected")
                return 0;
            } else {
                // @security("weaker return expected")
                return 0;
            }
        }
    }

    @Constraints("low <= @return")
    public int failIf44() {
        if (highIField == 0) {
            if (lowIField == 0) {
                // @security("weaker return expected")
                return 0;
            } else {
                // @security("weaker return expected")
                return 0;
            }
        } else {
            if (highIField == 0) {
                // @security("weaker return expected")
                return 0;
            } else {
                // @security("weaker return expected")
                return 0;
            }
        }
    }

    @Constraints({ "low <= @return" })
    public int successIf100() {
        int i = mkLow(42);
        if (mkHigh(true)) {
            i = 42;
        }
        // @security("weaker return expected")
        return i;
    }

}
