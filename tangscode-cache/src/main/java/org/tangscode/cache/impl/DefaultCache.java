package org.tangscode.cache.impl;

import org.tangscode.cache.ICache;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/2/11
 */
public class DefaultCache<K, V> implements ICache<K,V> {
    @Override
    public Optional<V> get(K key) {
        return null;
    }

    @Override
    public void put(K key, V value) {

    }

    @Override
    public void put(K key, V value, int expireTime, TimeUnit expireTimeUnit) {

    }

    @Override
    public Optional<V> remove(K key) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean containsKey(K key) {
        return false;
    }

    @Override
    public Optional<Map<K, V>> getAll(Set<K> keys) {
        return Optional.of(Collections.emptyMap());
    }

    @Override
    public void putAll(Map<K, V> map) {

    }

    @Override
    public void putAll(Map<K, V> map, int expiration, TimeUnit expireTimeUnit) {

    }

    @Override
    public boolean putIfAbsent(K key, V value) {
        return false;
    }

    @Override
    public boolean putIfPresent(K key, V value) {
        return false;
    }

    @Override
    public void expireAfter(K key, int expiration, TimeUnit expireTimeUnit) {

    }
}
