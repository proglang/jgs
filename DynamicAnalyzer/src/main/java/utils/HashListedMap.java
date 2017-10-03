package utils;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * This Map is a very useful implementation, that are provides operations of
 * storing a List of Values, that are mapped of a certain key.
 * @param <K> The Key Type of this Map
 * @param <V> The Value Type of the List, that are used internal.
 * @author Karsten Fix, 30.09.17
 */
public class HashListedMap<K, V> implements Map<K, List<V>> {

    // <editor-fold desc="Internal Representation ">

    /** An intern used implementation of Map */
    private Map<K, List<V>> content;

    /** An internal used implementation, that allows to
     * iterate over the Keys in insertion order */
    private List<K> sortedKeys;

    /** The last used key or null */
    private K lastKey = null;

    // </editor-fold>

    // <editor-fold desc="Constructors">

    /** Creates a new HashListedMap */
    public HashListedMap() {
        content = new HashMap<>();
        sortedKeys = new ArrayList<>();
    }

    // </editor-fold>

    // <editor-fold desc="Size Operations">

    /**
     * Tells the Size of the Map
     * @return The number of Keys stored.
     */
    @Override
    public int size() { return content.size(); }

    /**
     * Specifies the count of Values stored in the Map.
     * @return The Sum of all Values stored in the Map.
     */
    public int count() {
        int count = 0;
        for (List<V> values : content.values()) { count += values.size(); }
        return count;
    }

    /**
     * Specifies, if the Map is empty.
     * @return True, if and only if there are no keys in the map.
     */
    @Override
    public boolean isEmpty() { return content.isEmpty(); }

    // </editor-fold>

    // <editor-fold desc="Contains Specifications">

    /**
     * Tells if the given Key is within the Key Set.
     * @param o The Object, that could be a Key.
     * @return True, if and only if, the given Key is within the Key Set.
     */
    @Override
    public boolean containsKey(Object o) { return content.containsKey(o); }

    /**
     * Tells if the given Object is a Value, that is stored in this map.
     * That could be a List of Values or a Value it self.
     * @param o The Object, that could be a stored value.
     * @return True, if the given object is stored within this map.
     */
    @Override
    public boolean containsValue(Object o) {
        if (o instanceof  List) return content.containsValue(o);
        else {
            for (List<V> values: content.values()) {
                for (V val : values) {
                    if (val.equals(o)) return true;
                }
            }
        }
        return false;
    }

    // </editor-fold>

    // <editor-fold desc="Get Elements">

    /**
     * Gets the List of Values, that are stored under the given Key.
     * @param o The Key, that allows access to the stored Values
     * @return The List of Values Stored under the given key <br>
     *     <b>or null</b> if the Key does not exist.
     */
    @Override
    public List<V> get(Object o) { return content.get(o); }

    /**
     * Gets the Value, that is stored under the given key, at the given
     * position.
     * @param key The Key, that has stored a List of Values
     * @param index The index of the Value within the List, that is stored under
     *              the given key.
     * @return The Value, that is stored there <b>or null</b> if the key does not
     * exist.
     * @throws IndexOutOfBoundsException if the given index is out of Bounds.
     */
    public V get(K key, int index) {
        List<V> l = content.get(key);
        return l == null ? null : l.get(index);
    }

    // </editor-fold>

    // <editor-fold desc="Adding Operations">

    /**
     * Tries to add the given Value to the last used keys list.
     * @param v The Value, that shall be added.
     * @return true, if the adding was successful or
     * false, if the last used key is unspecified.
     */
    public boolean add(V v) {
        if (lastKey == null) return false;
        add(lastKey, v);
        return true;
    }

    /**
     * Adds the given Value to the list, that is stored under the given key.
     * @param k The Key, that shall store a List of Values, if it does not contain
     *          the key it creates a new one.
     * @param vs The value, that is added to the List of Values, that are stored
     *           under the key.
     */
    public void add(K k, V vs) {
        if (!sortedKeys.contains(k)) sortedKeys.add(k);
        List<V> list = content.containsKey(k) ? content.get(k) : new ArrayList<>();
        list.add(vs);
        content.put(k, list);
        lastKey = k;
    }

    /**
     * Tries to add the given List of Values to the last used keys list.
     * @param vs The List of Values, that shall be added.
     * @return true, if the adding was successful or
     * false, if the last used key is unspecified.
     */
    public boolean addAll(List<V> vs) {
        if (lastKey == null) return false;
        addAll(lastKey, vs);
        return true;
    }

    /**
     * Adds the given List of Values that are stored under the given Key.
     * @param k The key, that shall store the given Values.
     * @param vs A list of Values, that shall be stored under the given key.
     */
    public void addAll(K k, List<V> vs) {
        if (!sortedKeys.contains(k)) sortedKeys.add(k);
        List<V> list = content.containsKey(k) ? content.get(k) : new ArrayList<>();
        list.addAll(vs);
        content.put(k, list);
        lastKey = k;
    }

    /**
     * Stores the given List for the given Key. <br>
     *     <b>NOTE:</b> This method <i>overwrites</i> all values stored for the key.
     * @param k The key, that shall store the given list
     * @param vs The List of Values, that shall be stored by the given key
     * @return The previous List of Value, that was stored under the key before.
     */
    @Override
    public List<V> put(K k, List<V> vs) { lastKey = k; return content.put(k, new ArrayList<>(vs)); }

    /**
     * Replaces the Map with the given Map.
     * @param map The new Map.
     */
    @Override
    public void putAll(Map<? extends K, ? extends List<V>> map) { lastKey = null; content.putAll(map); }

    // </editor-fold>

    // <editor-fold desc="Remove Elements">

    /**
     * Removes the List of Values, that are accessible with the given key.
     * @param o The key, that may store a List of Values
     * @return The List of values, that have been stored under the given key.
     */
    @Override
    public List<V> remove(Object o) { return content.remove(o); }

    /** Clears the HashListedMap, means it erases all the content*/
    @Override
    public void clear() { content.clear(); sortedKeys.clear(); lastKey = null; }

    // </editor-fold>

    // <editor-fold desc="Iterable">

    /**
     * Gets a Set of all the keys. This set is not chained with the
     * keys. Means changing in the Set of Key do not influence the
     * Map.
     * @return A Set of all Keys.
     */
    @Override @Nonnull
    public Set<K> keySet() { return new HashSet<>(sortedKeys); }

    /**
     * Gets a List of Keys sorted in the order of adding Values to
     * the Key.
     * @return A list of Keys, that are sorted in the order of adding.
     */
    public List<K> getSortedKeys() { return sortedKeys; }

    /**
     * Returns a Collection of List of Values.
     * @return The Collection of List of Values is not sorted.
     */
    @Override @Nonnull
    public Collection<List<V>> values() { return content.values(); }

    /**
     * Returns a Set of Entries.
     * @return A Set of Entries, that is linked with the Map. Changes of
     * the entries value will be synchronized with the map.
     */
    @Override @Nonnull
    public Set<Entry<K, List<V>>> entrySet() { return content.entrySet(); }

    // </editor-fold>

    @Override
    public String toString() {
        StringBuilder repr = new StringBuilder();
        repr.append("{ ");
        for (K k : sortedKeys) {
            repr.append(k  + " -> " + content.get(k) + ", ");
        }
        repr.replace(repr.length() - 2, repr.length(), " }");
        return repr.toString();
    }

}