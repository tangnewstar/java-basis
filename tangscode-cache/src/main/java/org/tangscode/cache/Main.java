package org.tangscode.cache;

import org.tangscode.cache.enums.CacheType;

import java.util.concurrent.TimeUnit;

/**
 * @author tangxinxing
 * @version 1.0
 * @description
 * @date 2025/2/11
 */
public class Main {
    public static void main(String[] args) {
        CacheManager cacheManager = new CacheManager();
        cacheManager.createCache("user", CacheType.LRU);
        ICache<String, String> userCache = cacheManager.getCache("user", String.class, String.class);
        userCache.put("user1", "user1");
        userCache.expireAfter("user1", 1, TimeUnit.SECONDS);
        userCache.get("user1").ifPresent(value -> System.out.println("found user: " + value));
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean isPresent = userCache.get("user1").isPresent();
        if (!isPresent) {
            System.out.println("user1 is expired");
        }
    }
}
