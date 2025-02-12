package org.tangscode.cache;

import org.tangscode.cache.enums.CacheType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/2/11
 */
public class CacheManager implements ICacheManager {

    private Map<String ,ICache> caches = new ConcurrentHashMap<>();

    public CacheManager() {
        timelyCleanExpiredItems();
    }

    @Override
    public <K, V> ICache<K, V> getCache(String cacheName, Class<K> keyType, Class<V> valueType) {
        try {
            return (ICache<K, V>) caches.get(cacheName);
        } catch (Exception e) {
            throw new RuntimeException("Cache not found");
        }
    }

    @Override
    public void createCache(String cacheName, CacheType cacheType) {
        ICache cache = CacheFactory.createCache(cacheType);
        caches.put(cacheName, cache);
        if (cache instanceof ICacheHandler) {
            addHandler((ICacheHandler) cache);
        }
    }

    @Override
    public void destroyCache(String cacheName) {
        if (caches.containsKey(cacheName)) {
            caches.remove(cacheName);
        }
    }

    @Override
    public void destroyAllCaches() {
        caches.clear();
    }

    @Override
    public Set<String> getCacheNames() {
        return caches.keySet();
    }

    private List<ICacheHandler> handlers = new ArrayList<>();
    public void addHandler(ICacheHandler handler) {
        handlers.add(handler);
    }

    private void timelyCleanExpiredItems() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("start clean expired data timely");
                handlers.forEach(ICacheHandler::clearAllExpiredCaches);
            }
        }, 3000L, 1000L * 2);
    }
}
