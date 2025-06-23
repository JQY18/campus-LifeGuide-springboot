package renko.jiang.campus_life_guide.utils;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 验证码生成工具类（线程安全）
 *
 * @author 86132
 */
public class VerificationCodeUtil {

    // 允许的字符集合（去除了容易混淆的字符如0/O, 1/I/l）
    private static final String ALLOWED_CHARS = "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz";
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 生成6位字母+数字验证码（大小写敏感）
     */
    public static String generateCode() {
        return generateCode(6);
    }

    /**
     * 生成指定长度的字母+数字验证码
     *
     * @param length 验证码长度（至少4位）
     */
    public static String generateCode(int length) {
        if (length < 4) {
            throw new IllegalArgumentException("验证码长度至少为4位");
        }

        // 1. 打乱字符顺序增强随机性
        List<Character> shuffledChars = IntStream.range(0, ALLOWED_CHARS.length())
                .mapToObj(ALLOWED_CHARS::charAt)
                .collect(Collectors.toList());
        Collections.shuffle(shuffledChars, RANDOM);

        // 2. 随机选取字符
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(shuffledChars.size());
            code.append(shuffledChars.get(randomIndex));
        }

        // 3. 确保至少包含一个数字（增强可用性）
        if (code.chars().noneMatch(Character::isDigit)) {
            int replacePos = RANDOM.nextInt(length);
            char randomDigit = ALLOWED_CHARS.replaceAll("[^0-9]", "").charAt(RANDOM.nextInt(8));
            code.setCharAt(replacePos, randomDigit);
        }

        return code.toString();
    }

    // 测试用例
//    public static void main(String[] args) {
//        System.out.println("6位验证码示例: " + generateCode());
//        System.out.println("8位验证码示例: " + generateCode(10));
//    }
}