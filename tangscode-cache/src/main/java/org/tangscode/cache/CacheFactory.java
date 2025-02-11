package org.tangscode.cache;

import org.tangscode.cache.enums.CacheType;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/2/11
 */
public class CacheFactory {

    public static ICache createCache(CacheType cacheType) {
        try {
            return cacheType.getCacheClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("failed to instantiate cache for class:" + cacheType.getCacheClass().getName());
        }
    }
}
