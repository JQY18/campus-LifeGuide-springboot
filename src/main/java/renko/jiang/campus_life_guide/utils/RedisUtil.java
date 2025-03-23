package renko.jiang.campus_life_guide.utils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 86132
 */
@Slf4j
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_KEY_SEPARATOR = ".";

    public String buildKey(String... keys) {
        return String.join(CACHE_KEY_SEPARATOR, keys);
    }

}
