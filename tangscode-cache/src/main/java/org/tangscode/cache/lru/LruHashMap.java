package org.tangscode.cache.lru;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/2/11
 */
public class LruHashMap<K, V> extends LinkedHashMap<K, V> {
    private int maxEntries;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public LruHashMap(int maxEntries, boolean accessOrder) {
        super(DEFAULT_INITIAL_CAPACITY, 0.75f, accessOrder);
        this.maxEntries = maxEntries;
    }

    protected boolean removeEldestEntries(Map.Entry<K, V> eldest) {
        return size() > maxEntries;
    }
}
