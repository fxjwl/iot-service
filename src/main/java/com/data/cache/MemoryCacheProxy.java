package com.data.cache;

import com.data.util.ThreadPoolUtil;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryCacheProxy extends CacheProxy {

    private static Logger LOGGER = Logger.getLogger(MemoryCacheProxy.class);

    ConcurrentHashMap<String, Object> map = null;

    public MemoryCacheProxy() {
        map = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized boolean put(String key, Object value, int expirationInSeconds) {
        Date currentTime = new Date();
        long expTime = currentTime.getTime() + expirationInSeconds * 1000;
        CacheItem item = new CacheItem(key, value, new Date(expTime));
        Object returnValue = map.put(key, item);
        LOGGER.info("存入缓存  key= [" + key + "] EXPIRATION_IN_SECONDS=[" + expirationInSeconds + "]");
        return returnValue != null;
    }

    @Override
    public synchronized Object get(String key) {
        Object retunedValue = null;
        Object value = map.get(key);
        if (value != null) {
            Date currentTime = new Date();
            CacheItem item = (CacheItem) value;
            Date expTime = item.getExpireTime();
            if (currentTime.after(expTime)) {
                //expired, remove it.
                map.remove(key);
            } else {
                retunedValue = item.getValue();
            }
        }
        // 每次请求缓存时，都去扫描全部的缓存值，将过期数据清除掉
        ThreadPoolUtil.getclearCachedThreadPool().submit(new Runnable() {
            @Override
            public void run() {
                clearExpiredCachedItem();
            }
        });
        return retunedValue;
    }

    @Override
    public synchronized void clearExpiredCachedItem() {
        LOGGER.info("clearExpiredCachedItem start...");
        if (this.map == null) {
            // do nothing
        } else {
            LOGGER.info("clearExpiredCachedItem before size=[" + this.map.size() + "]");
            for (Map.Entry<String, Object> entry : this.map.entrySet()) {
                String key = entry.getKey();
                CacheItem item = (CacheItem) entry.getValue();
                Date currentTime = Calendar.getInstance().getTime();
                Date expTime = item.getExpireTime();
                if (currentTime.after(expTime)) {
                    LOGGER.info("已过期，需清除 key=[" + key + "]");
                    map.remove(key);
                }
            }
            LOGGER.info("clearExpiredCachedItem after size=[" + this.map.size() + "]");
        }
        LOGGER.info("clearExpiredCachedItem end");
    }

    class CacheItem {
        String key;
        Object value;
        Date expireTime;

        public CacheItem() {

        }

        public CacheItem(String key, Object value, Date expireTime) {
            this.key = key;
            this.value = value;
            this.expireTime = expireTime;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Date getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(Date expireTime) {
            this.expireTime = expireTime;
        }

    }

    @Override
    public void clearCache() {
        this.map.clear();
    }

}
