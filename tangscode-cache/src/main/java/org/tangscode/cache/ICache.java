package org.tangscode.cache;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author tangxinxing
 * @version 1.0
 * @description 缓存抽象
 * @param <K> key
 * @param <V> value
 * @date 2025/2/11
 */
public interface ICache<K,V> {
    Optional<V> get(K key);
    void put(K key, V value);
    void put(K key, V value, int expireTime, TimeUnit expireTimeUnit);
    Optional<V> remove(K key);
    void clear();
    boolean containsKey(K key);
    Optional<Map<K, V>> getAll(Set<K> keys);
    void putAll(Map<K, V> map);
    void putAll(Map<K, V> map, int expiration, TimeUnit expireTimeUnit);
    boolean putIfAbsent(K key, V value);
    boolean putIfPresent(K key, V value);
    void expireAfter(K key, int expiration, TimeUnit expireTimeUnit);
}
