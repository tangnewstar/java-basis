package org.tangscode.cache;

/**
 * @author tangxinxing
 * @version 1.0
 * @description 缓存数据管理和维护
 * @date 2025/2/11
 */
public interface ICacheHandler<K> {
    void removeIfExpired(K key);
    void clearAllExpiredCaches();
}
