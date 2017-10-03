package utils;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.*;

public class HashListedMapTest {

    @Test
    public void size() throws Exception {
        HashListedMap<String, String> foo = new HashListedMap<>();
        assertEquals(0, foo.size());
        assertTrue(foo.isEmpty());

        foo.put("Manfred", Collections.emptyList());
        assertEquals(1, foo.size());
        foo.add("Manfred", "Was geht");
        assertEquals(1, foo.size());
        assertEquals(1, foo.count());
        foo.add("Manfred", "Som");
        assertEquals(1, foo.size());
        assertEquals(2, foo.count());
    }

    @Test
    public void containsKey() throws Exception {
        HashListedMap<String, String> foo = new HashListedMap<>();
        assertFalse(foo.containsKey(""));
        assertFalse(foo.containsKey("Manfred"));

        foo.add("Theo", "");
        assertTrue(foo.containsKey("Theo"));
    }

    @Test
    public void containsValue() throws Exception {
        HashListedMap<String, String> foo = new HashListedMap<>();
        assertFalse(foo.containsValue(""));
        assertFalse(foo.containsValue("Manfred"));

        foo.add("Theo", "");
        assertTrue(foo.containsValue(""));
        foo.add("Theo", "As");
        assertTrue(foo.containsValue(Arrays.asList("", "As")));
    }

    @Test
    public void get() throws Exception {
        HashListedMap<String, String> foo = new HashListedMap<>();
        String val = "Value";
        foo.add("Key1", val);
        foo.put("Key2", Arrays.asList(val, val));
        assertEquals(val, foo.get("Key1", 0));
        assertEquals(val, foo.get("Key2", 0));
        assertEquals(val, foo.get("Key2", 1));
        assertNull( foo.get("Olga"));
        assertNull( foo.get("Olga", 6));

        try {
            foo.get("Key1", 1);
            fail("Expected an IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException anIndexOutOfBoundsException) {
            assertEquals(anIndexOutOfBoundsException.getMessage(), "Index: 1, Size: 1");
        }
        assertEquals(Arrays.asList(val, val), foo.get("Key2"));
    }

    @Test
    public void keySet() throws Exception {
        HashListedMap<String, String> foo = new HashListedMap<>();
        foo.add("Some", "thing");
        foo.add("Some", "things");
        foo.add("Foo", "things");
        foo.add("Sue", "things");
        assertEquals(Arrays.asList("Some", "Foo", "Sue"), foo.getSortedKeys());
    }

    @Test
    public void add() throws Exception {
        HashListedMap<String, String> foo = new HashListedMap<>();
        assertTrue(foo.isEmpty());
        assertFalse(foo.add("Something"));
        foo.add("Manny", "Something");
        assertTrue(foo.add("Something else"));
        assertTrue(foo.add("Something else again"));
        assertFalse(foo.isEmpty());
        assertEquals(1, foo.size());
        assertEquals(3, foo.count());
        foo.clear();

        assertTrue(foo.isEmpty());
        assertFalse(foo.add("Something"));
        assertFalse(foo.addAll(Arrays.asList("Some", "Thing")));
        foo.put("Hallo", Arrays.asList("Elton", "John"));
        assertTrue(foo.add("Something"));
        assertTrue(foo.addAll(Arrays.asList("Some", "Thing")));
        assertFalse(foo.isEmpty());
        assertEquals(1, foo.size());
        assertEquals(5, foo.count());

        foo.add("Ciao", "Elton");
        assertEquals(2, foo.size());
        assertTrue(foo.addAll(Arrays.asList("Some", "Thing")));
        assertEquals("Some", foo.get("Ciao", 1));
        assertEquals("Some", foo.get("Hallo", 3));

        assertEquals("{ Hallo -> [Elton, John, Something, Some, Thing],"
                     + " Ciao -> [Elton, Some, Thing] }",
                     foo.toString());
    }
}