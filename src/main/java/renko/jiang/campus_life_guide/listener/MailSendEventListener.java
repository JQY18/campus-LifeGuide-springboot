package renko.jiang.campus_life_guide.listener;


import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import renko.jiang.campus_life_guide.mail.LoginSuccessEvent;
import renko.jiang.campus_life_guide.mail.MailService;
import renko.jiang.campus_life_guide.mail.RegisterSuccessEvent;
import renko.jiang.campus_life_guide.mail.SendMailVerificationCodeEvent;
import renko.jiang.campus_life_guide.utils.RedisUtil;
import renko.jiang.campus_life_guide.utils.VerificationCodeUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author 86132
 */
@Slf4j
@Component
public class MailSendEventListener {
    @Autowired
    private MailService mailService;

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 异步发送邮件验证码
     *
     * @param event
     */
    @Async("eventExecutor")
    @EventListener(SendMailVerificationCodeEvent.class)
    public void handleSendMailVerificationCodeEvent(SendMailVerificationCodeEvent event) {
        // 获取验证码发送事件的邮箱
        String email = event.getEmail();
        // 生成验证码
        String verificationCode = VerificationCodeUtil.generateCode();

        // 设计缓存key
        String key = redisUtil.buildKey(RedisUtil.VERIFICATION_CODE_KEY, email);
        String lockKey = redisUtil.buildKey(RedisUtil.VERIFICATION_CODE_KEY, email, "lock");
        // 判断距离上一次发送邮件的间隔是否小于60秒，即lockKey是否存在
        Boolean setnx = redisUtil.setnx(lockKey, true, 1, TimeUnit.MINUTES);
        if (!setnx) {
            log.warn("邮件发送过于频繁，请稍后再试");
            return;
        }

        try {
            // 将验证码缓存至redis，五分钟有效，只能使用一次
            redisUtil.set(key, verificationCode, 5, TimeUnit.MINUTES);
            // 使用setnx简易分布式锁，防止盗刷，1分钟以内只能发送一次
            redisUtil.setnx(lockKey, true, 1, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("Redis set error: key={}, value={}, error={}", key, verificationCode, e.getMessage());
            throw e;
        }

        // 缓存写入成功，发送邮件
        // 发送验证码
        try {
            mailService.sendVerificationEmail(email, verificationCode);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 注册成功发送邮件
     * @param event
     */
    @Async("eventExecutor")
    @EventListener(RegisterSuccessEvent.class)
    public void handleRegisterSuccessEvent(RegisterSuccessEvent event) {
        log.info("注册成功，发送邮件");
        String to = event.getEmail();
        String username = event.getUsername();

        try {
            mailService.sendRegisterSuccessEmail(to, username);
        } catch (Exception e) {
            log.error("发送邮件失败", e);
        }
    }

    /**
     * 登录成功发送邮件
     * @param event
     */
    @Async("eventExecutor")
    @EventListener(LoginSuccessEvent.class)
    public void handleLoginSuccessEvent(LoginSuccessEvent event) {
        log.info("登录成功，发送邮件");
        String to = event.getEmail();
        String username = event.getUsername();

        try {
            mailService.sendLoginSuccessEmail(to, username);
        } catch (Exception e) {
            log.error("发送邮件失败", e);
        }
    }
}
