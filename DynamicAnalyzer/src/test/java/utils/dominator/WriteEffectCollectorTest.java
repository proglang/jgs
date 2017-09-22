package utils.dominator;

import de.unifreiburg.cs.proglang.jgs.examples.BodyBuilder;
import org.junit.Before;
import org.junit.Test;
import soot.*;
import soot.jimple.*;
import utils.BodyGenerator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class WriteEffectCollectorTest {

    /**
     * Test for a simple if program: <br>
     * <code>
     *     a = 5;
     *     b = 4;
     *     if (a == 2) {
     *         b = 3;
     *     } else {
     *         a = 42;
     *     }
     * </code> <br/>
     * <b>Write Effect</b> is then {a, b}
     * @throws Exception
     */
    @Test
    public void get() throws Exception {
        BodyGenerator bGen = new BodyGenerator("tests.testclasses.iftests.IfTestSimple");
        // Know I have only one: <init>
        Body b = bGen.getBodies().get(1);
        System.out.println(b.getMethod().getName());

        // Getting the Locales, that are effected, in this case all
        Set<Local> effects = new HashSet<>();
        effects.addAll(b.getLocals());

        WriteEffectCollector<Local> dom = new WriteEffectCollector<>(Local.class, b);
        dom.printBodyGraph();
        assertEquals(effects, dom.getAll());
    }

    @Test
    public void get1() throws Exception {
        BodyGenerator bGen = new BodyGenerator("testclasses.IfStmtFail");
        for (Body b : bGen.getBodies()) System.out.println("Got one: "+b.getMethod().getName());
    }
}