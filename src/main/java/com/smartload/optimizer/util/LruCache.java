package com.smartload.optimizer.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simple thread-unsafe LRU cache. Stateless service so this is fine.
 */
public class LruCache<K, V> extends LinkedHashMap<K, V> {
    private final int maxSize;

    public LruCache(int maxSize) {
        super(Math.max(16, maxSize), 0.75f, true);
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}
