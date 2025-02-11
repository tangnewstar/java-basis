package org.tangscode.cache;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/2/11
 */
public class CacheItem<V> {
    private V value;
    private long expireTimeStamp;

    public CacheItem(V value) {
        this(value, -1);
    }

    public CacheItem(V value, long expireTimeStamp) {
        this.value =value;
        this.expireTimeStamp = expireTimeStamp;
    }

    public CacheItem(V value, int expiration, TimeUnit timeUnit) {
        this.value = value;
        setExpireAfter(expiration, timeUnit);
    }

    public void setExpireAfter(int expiration, TimeUnit timeUnit) {
        if (expiration <= 0) {
            throw new IllegalArgumentException("invalid expiration");
        }
        long expireTimeStamp = System.currentTimeMillis() + timeUnit.toMillis(expiration);
        this.expireTimeStamp = expireTimeStamp;
    }

    public V getValue() {
        return value;
    }

    public boolean isExpired() {
        return expireTimeStamp > 0 && System.currentTimeMillis() > expireTimeStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheItem<?> cacheItem = (CacheItem<?>) o;
        return Objects.equals(value, cacheItem.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
