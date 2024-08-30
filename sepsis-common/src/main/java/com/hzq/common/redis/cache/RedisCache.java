package com.hzq.common.redis.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author gc
 * @class com.hua.hzq.common.redis RedisCache
 * @date 2024/8/16 15:46
 * @description Redis缓存工具类
 */
@Component
public class RedisCache {
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <T> void setCacheObject(String key, T value) {
        if (key == null || key.isEmpty()) return;
        this.redisTemplate.opsForValue().set(key, value);
    }

    public <T> void setCacheObject(String key, T value, Integer timeout, TimeUnit timeUnit) {
        if (key == null || key.isEmpty()) return;
        this.redisTemplate.opsForValue().set(key, value, (long) timeout, timeUnit);
    }

    @SuppressWarnings("unchecked")
    public <T> T getCacheObject(String key) {
        if (key == null || key.isEmpty()) return null;
        return (T) this.redisTemplate.opsForValue().get(key);
    }

    public boolean deleteCacheObject(String key) {
        if (key == null || key.isEmpty()) return false;
        return Boolean.TRUE.equals(this.redisTemplate.delete(key));
    }

    public boolean expire(String key, long timeout) {
        if (key == null || key.isEmpty()) return false;
        return this.expire(key, timeout, TimeUnit.SECONDS);
    }

    public boolean expire(String key, long timeout, TimeUnit unit) {
        if (key == null || key.isEmpty()) return false;
        return Boolean.TRUE.equals(this.redisTemplate.expire(key, timeout, unit));
    }

    public long getExpire(String key) {
        if (key == null || key.isEmpty()) return 0L;
        return Optional.ofNullable(this.redisTemplate.getExpire(key)).orElse(0L);
    }

    public Boolean hasKey(String key) {
        if (key == null || key.isEmpty()) return false;
        return this.redisTemplate.hasKey(key);
    }

    public <T> long setCacheList(String key, List<T> dataList) {
        if (key == null || key.isEmpty()) return 0L;
        Long count = this.redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0L : count;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getCacheList(String key) {
        if (key == null || key.isEmpty()) return new ArrayList<>();
        return (List<T>) this.redisTemplate.opsForList().range(key, 0L, -1L);
    }

    public <T> void setCacheSet(String key, Set<T> dataSet) {
        if (key == null || key.isEmpty()) return;
        BoundSetOperations<String, Object> setOperation = this.redisTemplate.boundSetOps(key);
        for (T t : dataSet) setOperation.add(t);
    }

    @SuppressWarnings("unchecked")
    public <T> Set<T> getCacheSet(String key) {
        if (key == null || key.isEmpty()) return new HashSet<>();
        return (Set<T>) this.redisTemplate.opsForSet().members(key);
    }

    public <T> void setCacheMap(String key, Map<String, T> dataMap) {
        if (key == null || key.isEmpty()) return;
        if (dataMap != null) this.redisTemplate.opsForHash().putAll(key, dataMap);
    }

    @SuppressWarnings("unchecked")
    public <T> Map<Object, T> getCacheMap(String key) {
        if (key == null || key.isEmpty()) return new HashMap<>();
        return (Map<Object, T>) this.redisTemplate.opsForHash().entries(key);
    }

    public <T> void setCacheMapValue(String key, String hKey, T value) {
        if (key == null || key.isEmpty()) return;
        this.redisTemplate.opsForHash().put(key, hKey, value);
    }

    public <T> T getCacheMapValue(String key, String hKey) {
        if (key == null || key.isEmpty()) return null;
        HashOperations<String, String, T> opsForHash = this.redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    public void delCacheMapValue(String key, String hKey) {
        if (key == null || key.isEmpty()) return;
        this.redisTemplate.opsForHash().delete(key, hKey);
    }
}
