package utils.jimple;

import org.junit.Test;
import soot.*;
import testclasses.utils.SimpleObject;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class SootsTest {
    @Test
    public void toSootType() throws Exception {
        assertEquals(BooleanType.v(), Soots.toSootType(boolean.class));
        assertEquals(RefType.v("java.lang.Boolean"), Soots.toSootType(Boolean.class));
        assertEquals(IntType.v(), Soots.toSootType(int.class));
        assertEquals(RefType.v("java.lang.Integer"), Soots.toSootType(Integer.class));
        assertEquals(NullType.v(), Soots.toSootType( null));
        String[][] foo = new String[5][9];
        assertEquals(ArrayType.v(RefType.v("java.lang.String"), 2),
                     Soots.toSootType(foo.getClass()));
        String[][][] bar = new String[5][3][4];
        assertEquals(ArrayType.v(RefType.v("java.lang.String"),3),
                     Soots.toSootType(bar.getClass()));
    }

    @Test
    public void createParameters() throws Exception {
        assertEquals(Arrays.asList(BooleanType.v(), IntType.v()),
                     Soots.createParameters(boolean.class, int.class));

        SimpleObject[][] objects = new SimpleObject[0][85];
        assertEquals(Arrays.asList(NullType.v(), RefType.v("java.lang.String"),
                                   ArrayType.v(RefType.v("testclasses.utils.SimpleObject"), 2)),

                     Soots.createParameters(null, String.class, objects.getClass()));

        assertEquals(Collections.emptyList(), Soots.createParameters());
    }
}