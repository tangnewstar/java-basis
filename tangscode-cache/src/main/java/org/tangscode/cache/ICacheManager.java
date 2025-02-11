package org.tangscode.cache;

import org.tangscode.cache.enums.CacheType;

import java.util.Set;

/**
 * @author tangxinxing
 * @version 1.0
 * @description 缓存g
 * @date 2025/2/11
 */
public interface ICacheManager {
    <K,V> ICache<K,V> getCache(String cacheName, Class<K> keyType, Class<V> valueType);
    void createCache(String cacheName, CacheType cacheType);
    void destroyCache(String cacheName);
    void destroyAllCaches();
    Set<String> getCacheNames();
}
