package com.data.cache;

public abstract class CacheProxy {

    static CacheProxy instance = new MemoryCacheProxy();

    public static CacheProxy getProxy() {
        return instance;
    }

    public abstract boolean put(String key, Object value, int expireTime);

    public abstract Object get(String key);

    /**
     * Clear cached objects. This SHOULD be called by administration operation.
     */
    public abstract void clearCache();

    public abstract void clearExpiredCachedItem();
}
