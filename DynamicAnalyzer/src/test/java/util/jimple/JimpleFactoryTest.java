package util.jimple;

import org.junit.Test;
import soot.*;

import java.util.Arrays;
import java.util.Collections;


import static org.junit.Assert.*;

public class JimpleFactoryTest {
    @Test
    public void calcKey() throws Exception {
        assertEquals("int, java.lang.String",
                     JimpleFactory.calcKey(Arrays.asList(IntType.v(), RefType.v("java.lang.String"))));
        assertEquals("int, long, short",
                     JimpleFactory.calcKey(Arrays.asList(IntType.v(), LongType.v(), ShortType.v())));

        assertEquals("", JimpleFactory.calcKey(Collections.emptyList()));

    }

}