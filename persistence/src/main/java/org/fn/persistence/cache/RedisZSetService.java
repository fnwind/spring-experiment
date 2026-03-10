package org.fn.persistence.cache;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

public class RedisZSetService extends BaseRedisService {

    public RedisZSetService(RedisDbManager redisDbManager) {
        super(redisDbManager);
    }

    private ZSetOperations<String, Object> ops() {
        return template().opsForZSet();
    }

    public Boolean add(String key, Object value, double score) {
        return ops().add(key, value, score);
    }

    public Long add(String key, Set<ZSetOperations.TypedTuple<Object>> tuples) {
        return ops().add(key, tuples);
    }

    public Long remove(String key, Object... values) {
        return ops().remove(key, values);
    }

    public Double score(String key, Object value) {
        return ops().score(key, value);
    }

    public Long rank(String key, Object value) {
        return ops().rank(key, value);
    }

    public Long reverseRank(String key, Object value) {
        return ops().reverseRank(key, value);
    }

    public Set<Object> range(String key, long start, long end) {
        return ops().range(key, start, end);
    }

    public Set<Object> reverseRange(String key, long start, long end) {
        return ops().reverseRange(key, start, end);
    }

    public Set<ZSetOperations.TypedTuple<Object>> rangeWithScores(String key, long start, long end) {
        return ops().rangeWithScores(key, start, end);
    }

    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeWithScores(String key, long start, long end) {
        return ops().reverseRangeWithScores(key, start, end);
    }

    public Long size(String key) {
        return ops().size(key);
    }

    public Long removeRange(String key, long start, long end) {
        return ops().removeRange(key, start, end);
    }

    public Long removeRangeByScore(String key, double min, double max) {
        return ops().removeRangeByScore(key, min, max);
    }

    public Double incrementScore(String key, Object value, double delta) {
        return ops().incrementScore(key, value, delta);
    }

    public Set<Object> rangeByScore(String key, double min, double max) {
        return ops().rangeByScore(key, min, max);
    }

    public Set<ZSetOperations.TypedTuple<Object>> rangeByScoreWithScores(String key, double min, double max) {
        return ops().rangeByScoreWithScores(key, min, max);
    }
}