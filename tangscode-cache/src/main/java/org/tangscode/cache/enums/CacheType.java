package org.tangscode.cache.enums;

import org.tangscode.cache.impl.DefaultCache;
import org.tangscode.cache.ICache;
import org.tangscode.cache.impl.LRUCache;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/2/11
 */
public enum CacheType {
    DEFAULT(DefaultCache.class),
    LRU(LRUCache.class);

    private Class<? extends ICache> cacheClass;
    CacheType(Class<? extends ICache> cacheClass) {
        this.cacheClass = cacheClass;
    }
    public Class<? extends ICache> getCacheClass() {
        return cacheClass;
    }
}
