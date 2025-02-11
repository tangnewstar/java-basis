package org.tangscode.cache.impl;

import org.tangscode.cache.CacheItem;
import org.tangscode.cache.ICache;
import org.tangscode.cache.ICacheHandler;
import org.tangscode.cache.lru.LruHashMap;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/2/11
 */
public class LRUCache<K, V> implements ICache<K, V>, ICacheHandler<K> {
    private Map<K, CacheItem<V>> cache = new LruHashMap<>(100, true);

    @Override
    public Optional<V> get(K key) {
        removeIfExpired(key);
        return Optional.ofNullable(cache.get(key)).map(CacheItem::getValue);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, new CacheItem<>(value));
    }

    @Override
    public void put(K key, V value, int expireTime, TimeUnit expireTimeUnit) {
        // put and set expire time
        cache.put(key, new CacheItem<>(value, expireTime, expireTimeUnit));
    }

    @Override
    public Optional<V> remove(K key) {
        removeIfExpired(key);
        return Optional.ofNullable(cache.remove(key)).map(CacheItem::getValue);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public boolean containsKey(K key) {
        removeIfExpired(key);;
        return cache.containsKey(key);
    }

    @Override
    public Optional<Map<K, V>> getAll(Set<K> keys) {
        if (keys == null || keys.isEmpty()) {
            return Optional.empty();
        }
        Map<K, V> result = new HashMap<>();
        for (K key : keys) {
            Optional<V> value = get(key);
            value.ifPresent(v -> result.put(key, v));
        }
        return Optional.of(result);
    }

    @Override
    public void putAll(Map<K, V> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        map.forEach((k,v) -> {
            cache.put(k, new CacheItem<>(v));
        });
    }

    @Override
    public void putAll(Map<K, V> map, int expiration, TimeUnit expireTimeUnit) {
        if (map == null || map.isEmpty()) {
            return;
        }
        map.forEach((k,v) -> {
            cache.put(k, new CacheItem<>(v, expiration, expireTimeUnit));
        });
    }

    @Override
    public boolean putIfAbsent(K key, V value) {
        if (!containsKey((key))) {
            put(key, value);
            return true;
        }
        return false;
    }

    @Override
    public boolean putIfPresent(K key, V value) {
        if (containsKey(key)) {
            put(key, value);
            return true;
        }
        return false;
    }

    @Override
    public void expireAfter(K key, int expiration, TimeUnit expireTimeUnit) {
        if (containsKey(key)) {
            CacheItem<V> cacheItem = cache.get(key);
            if (cacheItem == null) {
                return;
            }
            cacheItem.setExpireAfter(expiration, expireTimeUnit);
        }
    }

    @Override
    public void removeIfExpired(K key) {
        Optional.ofNullable(cache.get(key)).ifPresent(cacheItem -> {
            if (cacheItem.isExpired()) {
                cache.remove(key);
            }
        });
    }

    @Override
    public void clearAllExpiredCaches() {
        Set<K> expiredKeys = cache.entrySet().stream()
                .filter(entry -> entry.getValue().isExpired())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        expiredKeys.forEach(cache::remove);
    }
}
