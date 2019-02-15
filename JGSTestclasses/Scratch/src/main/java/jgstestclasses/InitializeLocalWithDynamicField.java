package jgstestclasses;

import de.unifreiburg.cs.proglang.jgs.support.*;

/**
 * Returning a high-security value from a method.
 * Created by fennell on 25.09.17.
 */
public class InitializeLocalWithDynamicField {

    @Constraints("? <= @ret")
    static String theAnswer() {
        return DynamicLabel.makeHigh("42");
    }

    @Sec("?")
    static String theAnswer = DynamicLabel.makeHigh("42");

    @Constraints("? <= @0")
    @Effects({"LOW", "?"})
    public static void main(String ... args) {
       String answer;
       answer = theAnswer;
       String answer2 = answer;
       IOUtils.printPublicDynamic(answer2); // should throw
    }
}

/* theAnswer has level H due to makeHigh() which makes level of answer H in line 23 and thus the level of answer2 in line 24
  Hence printPublicDynamic fails
  Changing printPublicDynamic to printSecret() or removing makeHigh() for theAnswer solves the problem */