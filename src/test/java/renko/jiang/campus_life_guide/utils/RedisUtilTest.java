package renko.jiang.campus_life_guide.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;


@SpringBootTest
class RedisUtilTest {
    @Autowired
    private RedisUtil redisUtil;

    @Test
    void bitCount() {
        long start = System.currentTimeMillis();

        Long bitCount = redisUtil.bitCount("bittest");

        System.out.println(System.currentTimeMillis() - start);
        System.out.println(bitCount);
    }

    @Test
    void testBitCount() {
        long start = System.currentTimeMillis();

//        redisUtil.setBit("bittest", 1000, true);

        Set<Object> test = redisUtil.sMembers("test");
        System.out.println(System.currentTimeMillis() - start);

    }
    @Test
    void testGetBit() {
        long start = System.currentTimeMillis();
        List<Integer> offsets = List.of(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
        redisUtil.getBits("online.user", offsets).forEach(System.out::println);
        System.out.println(System.currentTimeMillis() - start);

    }

    @Test
    void zAdd() {
        redisUtil.zAdd("key", "test1", 1.0);
        redisUtil.zAdd("key", "test2", 2.0);
        redisUtil.zAdd("key", "test3", 3.0);
        redisUtil.zAdd("key", "test4", 4.0);
        redisUtil.zAdd("key", "test5", 5.0);
    }

    @Test
    void zSize() {
        System.out.println(redisUtil.zSize("key"));
    }

    @Test
    void zRange() {
        redisUtil.zRange("key", 0, 4).forEach(System.out::println);
    }

    @Test
    void zPoll() {
        redisUtil.zPoll("key", 0, 3).forEach(System.out::println);
    }
}