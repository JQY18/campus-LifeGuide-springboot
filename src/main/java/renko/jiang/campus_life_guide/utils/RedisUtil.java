package renko.jiang.campus_life_guide.utils;

import io.lettuce.core.RedisException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * @author 86132
 */
@Slf4j
@Component
public class RedisUtil {

    public static final String VERIFICATION_CODE_KEY = "register.verify";
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_KEY_SEPARATOR = ".";

    /**
     * 构建 Redis key
     */
    public String buildKey(String... keys) {
        return String.join(CACHE_KEY_SEPARATOR, keys);
    }

    // String 操作

    /**
     * 设置值
     */
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            log.error("Redis set error: key={}, value={}, error={}", key, value, e.getMessage());
            throw e;
        }
    }

    /**
     * 设置值并指定过期时间
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
        } catch (Exception e) {
            log.error("Redis set with timeout error: key={}, value={}, error={}", key, value, e.getMessage());
            throw e;
        }
    }

    /**
     * 设置值(如果不存在)并指定过期时间
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @return
     */
    public Boolean setnx(String key, Object value, long timeout, TimeUnit unit) {
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
        } catch (Exception e) {
            log.error("Redis setnx error: key={}, value={}, error={}", key, value, e.getMessage());
            throw e;
        }
    }

    /**
     * 获取值
     */
    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Redis get error: key={}, error={}", key, e.getMessage());
            throw e;
        }
    }

    // BitMap操作


    /**
     * 设置 Bitmap 指定偏移量的值
     *
     * @param key    Redis key
     * @param offset 偏移量（用户 ID）
     * @param value  true 表示 1，false 表示 0
     */
    public void setBit(String key, long offset, boolean value) {
        try {
            redisTemplate.opsForValue().setBit(key, offset, value);
        } catch (Exception e) {
            log.error("Redis setBit error: key={}, offset={}, value={}, error={}",
                    key, offset, value, e.getMessage());
            throw e;
        }
    }

    /**
     * 获取 Bitmap 指定偏移量的值
     *
     * @param key    Redis key
     * @param offset 偏移量（用户 ID）
     * @return true 表示 1，false 表示 0
     */
    public Boolean getBit(String key, long offset) {
        try {
            return redisTemplate.opsForValue().getBit(key, offset);
        } catch (Exception e) {
            log.error("Redis getBit error: key={}, offset={}, error={}",
                    key, offset, e.getMessage());
            throw e;
        }
    }

    /**
     * 统计 Bitmap 中 1 的数量
     *
     * @param key Redis key
     * @return 1 的总数
     */
    public Long bitCount(String key) {
        try {
            return redisTemplate.execute((RedisCallback<Long>) connection ->
                    connection.bitCount(key.getBytes()));
        } catch (Exception e) {
            log.error("Redis bitCount error: key={}, error={}", key, e.getMessage());
            throw e;
        }
    }

    public List<Boolean> getBitsTest(String key, List<Integer> offsets) {
        List<Object> statusList = redisTemplate.executePipelined(
                (RedisCallback<Object>) connection -> {
                    for (Integer userId : offsets) {
                        connection.getBit(key.getBytes(), userId);
                    }
                    return null;
                }
        );
        return statusList.stream().map(Boolean.class::cast).toList();
    }

    /**
     * 批量获取 Bitmap 中指定偏移量的值
     *
     * @param key
     * @param offsets
     * @return
     */
    public List<Boolean> getBits(String key, List<Integer> offsets) {
        List<Object> statusList = redisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                for (Integer userId : offsets) {
                    operations.opsForValue().getBit(key, userId);
                }
                return null;
            }
        });
        return statusList.stream().map(Boolean.class::cast).toList();
    }


    // Hash 操作

    /**
     * Hash 设置值
     */
    public void hSet(String key, String field, Object value) {
        try {
            redisTemplate.opsForHash().put(key, field, value);
        } catch (Exception e) {
            log.error("Redis hSet error: key={}, field={}, value={}, error={}", key, field, value, e.getMessage());
            throw e;
        }
    }

    /**
     * Hash 获取值
     */
    public Object hGet(String key, String field) {
        try {
            return redisTemplate.opsForHash().get(key, field);
        } catch (Exception e) {
            log.error("Redis hGet error: key={}, field={}, error={}", key, field, e.getMessage());
            throw e;
        }
    }

    /**
     * Hash 删除字段
     */
    public Long hDel(String key, String... fields) {
        try {
            return redisTemplate.opsForHash().delete(key, (Object[]) fields);
        } catch (Exception e) {
            log.error("Redis hDel error: key={}, fields={}, error={}", key, fields, e.getMessage());
            throw e;
        }
    }

    // List 操作

    /**
     * 左推入列表
     */
    public Long lPush(String key, Object value) {
        try {
            return redisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            log.error("Redis lPush error: key={}, value={}, error={}", key, value, e.getMessage());
            throw e;
        }
    }

    /**
     * 右弹出列表
     */
    public Object rPop(String key) {
        try {
            return redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            log.error("Redis rPop error: key={}, error={}", key, e.getMessage());
            throw e;
        }
    }

    // PileLine批处理
    public void executePipeline(SessionCallback<Object> callback) {
        try {
            redisTemplate.executePipelined(callback);
        } catch (Exception e) {
            log.error("Redis executePipeline error: error={}", e.getMessage());
            throw e;
        }
    }


    // Set 操作

    /**
     * 添加到集合
     */
    public Long sAdd(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("Redis sAdd error: key={}, values={}, error={}", key, values, e.getMessage());
            throw e;
        }
    }

    /**
     * 获取集合成员
     */
    public Set<Object> sMembers(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("Redis sMembers error: key={}, error={}", key, e.getMessage());
            throw e;
        }
    }

    /**
     * 删除集合成员
     */
    public Long sDel(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            log.error("Redis sDel error: key={}, values={}, error={}", key, values, e.getMessage());
            throw e;
        }
    }

    /**
     * 是否存在集合中
     */
    public Boolean sIsMember(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error("Redis sIsMember error: key={}, value={}, error={}", key, value, e.getMessage());
            throw e;
        }
    }


    // ZSet 操作

    /**
     * 添加到有序集合
     */
    public Boolean zAdd(String key, Object value, double score) {
        try {
            // 有新人匹配则重置过期时间
            Boolean add = redisTemplate.opsForZSet().add(key, value, score);
            redisTemplate.expire(key, 2, TimeUnit.MINUTES);
            return add;
        } catch (Exception e) {
            log.error("Redis zAdd error: key={}, value={}, score={}, error={}", key, value, score, e.getMessage());
            throw e;
        }
    }
    /**
     * ZSet的长度
     */
    public Long zSize(String key) {
        try {
            return redisTemplate.opsForZSet().zCard(key);
        } catch (Exception e) {
            log.error("Redis zSize error: key={}, error={}", key, e.getMessage());
            throw e;
        }
    }

    /**
     * 获取有序集合范围内的成员
     */
    public Set<Object> zRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            log.error("Redis zRange error: key={}, start={}, end={}, error={}", key, start, end, e.getMessage());
            throw e;
        }
    }


    private static final String ZPOLL_SCRIPT = """
    local result = redis.call('ZRANGE', KEYS[1], ARGV[1], ARGV[2])
    if #result > 0 then
        redis.call('ZREMRANGEBYRANK', KEYS[1], ARGV[1], ARGV[2])
    end
    return result
    """;
    public List<Object> zPoll(String key, long start, long end) {
        try {
            DefaultRedisScript<List> script = new DefaultRedisScript<>();
            script.setScriptText(ZPOLL_SCRIPT);
            script.setResultType(List.class);

            List result = redisTemplate.execute(script, List.of(key), start, end);
            return result != null ? result : Collections.emptyList();
        } catch (Exception e) {
            log.error("Redis zPoll error: key={}, start={}, end={}, error={}", key, start, end, e.getMessage());
            throw new RedisException("Failed to poll ZSet: " + e.getMessage(), e);
        }
    }

    // 通用操作

    /**
     * 删除 key
     */
    public Boolean delete(String key) {
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Redis delete error: key={}, error={}", key, e.getMessage());
            throw e;
        }
    }

    /**
     * 设置过期时间
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        try {
            return redisTemplate.expire(key, timeout, unit);
        } catch (Exception e) {
            log.error("Redis expire error: key={}, timeout={}, unit={}, error={}", key, timeout, unit, e.getMessage());
            throw e;
        }
    }

    /**
     * 检查 key 是否存在
     */
    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("Redis hasKey error: key={}, error={}", key, e.getMessage());
            throw e;
        }
    }

    public Boolean zDel(String key, Integer userId) {
        try {
            return redisTemplate.opsForZSet().remove(key, userId) > 0;
        } catch (Exception e) {
            log.error("Redis zDel error: key={}, userId={}, error={}", key, userId, e.getMessage());
            throw e;
        }
    }
}