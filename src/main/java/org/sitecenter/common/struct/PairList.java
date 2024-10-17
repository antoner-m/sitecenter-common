package org.sitecenter.common.struct;

import java.io.Serializable;
import java.util.*;

/**
 * PairList is a specialized ArrayList that stores PairString objects,
 * allowing store multiple items with same key via addByKey method.
 * Or it can act as unique key list if you work via putByKey method.
 */
public class PairList extends ArrayList<PairString> implements Serializable {
    public PairList(int initialCapacity) {
        super(initialCapacity);
    }

    public PairList() {
    }

    public PairList(Collection<? extends PairString> c) {
        super(c);
    }

    /** multiple items with same key allowed
     *
     * @param key
     * @param value
     */
    public void addByKey(String key, String value) {
        this.add(new PairString(key, value));
    }
    public void removeByKey(String key) {
        this.removeIf(p -> p.getKey().equals(key));
    }

    /** Behave like hashmap - only one key allowed in list
     *
     * @param key
     * @param value
     */
    public void putByKey(String key, String value) {
        removeByKey(key);
        addByKey(key, value);
    }

    public Optional<String> findByKey(String key) {
        for (PairString p : this) {
            if (p.getKey().equals(key)) {
                return Optional.of(p.getValue());
            }
        }
        return Optional.empty();
    }

    public List<String> findValuesByKey(String key) {
        ArrayList<String> values = new ArrayList<>();
        for (PairString p : this) {
            if (p.getKey().equals(key))
                values.add(p.getValue());
        }
        if (!values.isEmpty())
            return values;
        else
            return Collections.emptyList();
    }

}
