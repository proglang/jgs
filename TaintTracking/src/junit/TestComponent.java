package junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import security.ALevel;
import security.ILevel;
import constraints.ComponentProgramCounterRef;
import constraints.ComponentParameterRef;
import constraints.ComponentReturnRef;
import constraints.IComponent;

public class TestComponent {

    private static class SLevel extends ALevel {

        private final String level;

        private SLevel(String level) {
            this.level = level;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((level == null) ? 0 : level.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SLevel other = (SLevel) obj;
            if (level == null) {
                if (other.level != null)
                    return false;
            } else if (!level.equals(other.level))
                return false;
            return true;
        }

        @Override
        public String getName() {
            return level;
        }

        @Override
        public String toString() {
            return "SLevel: " + level;
        }

    }

    @Test
    public final void testEqualsLevel() {
        ILevel level1 = new SLevel("low");
        ILevel level2 = new SLevel("high");
        IComponent level3 = new SLevel("low");
        assertTrue("Same levels", level1.equals(level1));
        assertTrue("Equal levels", level1.equals(new SLevel("low")));
        assertTrue("Equal levels", level1.equals(level3));
        assertTrue("Equal levels", new SLevel("low").equals(level1));
        assertFalse("Not equal levels", level1.equals(level2));
        assertFalse("Not equal levels", level2.equals(level1));
        assertFalse("Not equal levels", level1.equals(new SLevel("high")));
        assertFalse("Not equal levels", new SLevel("high").equals(level1));
        assertFalse("Not equal levels",
                    level1.equals(new ComponentParameterRef(0, "test")));
    }

    @Test
    public final void testEqualsParamRef() {
        ComponentParameterRef p1 = new ComponentParameterRef(0, "test");
        ComponentParameterRef p2 = new ComponentParameterRef(1, "test");
        ComponentParameterRef p3 = new ComponentParameterRef(1, "test-b");
        ComponentParameterRef p4 = new ComponentParameterRef(0, "test-b");
        IComponent c1 = new ComponentParameterRef(0, "test");
        IComponent c2 = new ComponentParameterRef(1, "test");
        IComponent c3 = new ComponentParameterRef(1, "test-b");
        IComponent c4 = new ComponentParameterRef(0, "test-b");
        assertTrue("Same param refs", p1.equals(p1));
        assertTrue("Equal param refs",
                   p1.equals(new ComponentParameterRef(0, "test")));
        assertTrue("Equal param refs",
                   new ComponentParameterRef(0, "test").equals(p1));
        assertFalse("Not equal param refs", p1.equals(p2));
        assertFalse("Not equal param refs", p2.equals(p1));
        assertFalse("Not equal param refs", p1.equals(p3));
        assertFalse("Not equal param refs", p3.equals(p1));
        assertFalse("Not equal param refs", p1.equals(p4));
        assertFalse("Not equal param refs", p4.equals(p1));
        assertTrue("Same param refs", p1.equals(c1));
        assertFalse("Not equal param refs", p1.equals(c2));
        assertFalse("Not equal param refs", c2.equals(p1));
        assertFalse("Not equal param refs", p1.equals(c3));
        assertFalse("Not equal param refs", c3.equals(p1));
        assertFalse("Not equal param refs", p1.equals(c4));
        assertFalse("Not equal param refs", c4.equals(p1));
        assertFalse("Not equal param refs",
                    p1.equals(new ComponentParameterRef(0, "testb")));
        assertFalse("Not equal param refs",
                    new ComponentParameterRef(1, "test").equals(p1));
    }

    @Test
    public final void testEqualsReturnRef() {
        ComponentReturnRef r1 = new ComponentReturnRef("test");
        ComponentReturnRef r2 = new ComponentReturnRef("test-b");
        IComponent c1 = new ComponentReturnRef("test");
        IComponent c2 = new ComponentReturnRef("test-b");
        assertTrue("Same return refs", r1.equals(r1));
        assertTrue("Equal return refs",
                   r1.equals(new ComponentReturnRef("test")));
        assertTrue("Equal return refs",
                   new ComponentReturnRef("test").equals(r1));
        assertFalse("Not equal return refs", r1.equals(r2));
        assertFalse("Not equal return refs", r2.equals(r1));
        assertTrue("Same return refs", r1.equals(c1));
        assertFalse("Not equal return refs", r1.equals(c2));
        assertFalse("Not equal return refs", c2.equals(r1));
        assertFalse("Not equal return refs",
                    r1.equals(new ComponentReturnRef("testb")));
        assertFalse("Not equal return refs",
                    new ComponentReturnRef("testb").equals(r1));
    }

    @Test
    public final void testEqualsPCRef() {
        ComponentProgramCounterRef pc1 = new ComponentProgramCounterRef("a");
        IComponent c1 = new ComponentProgramCounterRef("a");
        assertTrue("Same pc refs", pc1.equals(pc1));
        assertTrue("Equal pc refs",
                   pc1.equals(new ComponentProgramCounterRef("a")));
        assertTrue("Equal pc refs",
                   new ComponentProgramCounterRef("a").equals(pc1));
        assertTrue("Same pc refs", pc1.equals(c1));
    }

}
