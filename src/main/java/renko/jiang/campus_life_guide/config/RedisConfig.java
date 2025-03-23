package renko.jiang.campus_life_guide.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author 86132
 */
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 创建redisTemplate对象
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 获取字符串序列化器
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 设置redis连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置key序列化器
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // 设置hashKey的序列化器
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        //设置value序列化器
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer());
        // 设置hashValue的序列化器
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer());
        // 初始化redisTemplate
        redisTemplate.afterPropertiesSet();
        // 返回redisTemplate
        return redisTemplate;
    }

    private Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {


        // 创建一个ObjectMapper实例，用于定义序列化和反序列化的行为
        ObjectMapper objectMapper = new ObjectMapper();

        // 设置ObjectMapper的可见性，使得所有属性都可以被序列化和反序列化
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        // 配置ObjectMapper在遇到未知属性时不会抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 激活默认的类型信息，以支持序列化和反序列化时自动识别类型
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY);
        // 配置ObjectMapper在遇到未知属性时不会抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 将自定义的ObjectMapper设置到Jackson2JsonRedisSerializer中
        // 创建一个用于序列化和反序列化的Jackson2JsonRedisSerializer实例
        return new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
    }
}
