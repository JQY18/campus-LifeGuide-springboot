package renko.jiang.campus_life_guide.mail;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Objects;

/**
 * @author 86132
 */
@Component
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;


    @Autowired
    private TemplateEngine templateEngine;

    // 发送验证码邮件,HTML格式

    public void sendVerificationEmail(String to, String verificationCode) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // 设置邮件内容（通过Thymeleaf渲染）
        Context context = new Context();
        context.setVariable("username", "用户");
        context.setVariable("verificationCode", verificationCode);
        String htmlContent = templateEngine.process("email/verification-email", context);

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject("【师大生活指南】注册验证码");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }


    // 发送注册后的欢迎邮件
    public void sendRegisterSuccessEmail(String to, String username) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        // 设置邮件内容
        Context context = new Context();
        context.setVariable("username", username);
        String htmlContent = templateEngine.process("email/register-success", context);

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject("【师大生活指南】注册成功!!!");
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

    // 发送登录成功通知
    public void sendLoginSuccessEmail(String to, String username) throws MessagingException {
        if(to == null){
            return;
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        // 设置邮件内容
        Context context = new Context();
        context.setVariable("username", username);
        String htmlContent = templateEngine.process("email/login-success", context);

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject("【师大生活指南】登录成功!!!");
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

    // 直接拼接替换HTML内的字符串
    @SneakyThrows
    public void sendVerificationEmailSimple(String to,String code) {
        // 读取HTML文件
        Resource resource = new FileSystemResourceLoader().getResource("classpath:templates/email/verification-email.html");
        Path path = resource.getFile().toPath();
        String htmlTemplate = Files.readString(path);
        String htmlContent = htmlTemplate
                .replace("username", "用户")
                .replace("verificationCode", code);

        MimeMessage message = mailSender.createMimeMessage();
        // 发送逻辑同上...
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject("【师大生活指南】注册验证码");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }


    // 发送HTML邮件（带附件）
    public void sendHtmlEmail(String to, String subject, String htmlContent, String attachmentPath)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        // true表示支持附件
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("your-qq@qq.com");
        helper.setTo(to);
        helper.setSubject(subject);
        // true表示内容为HTML
        helper.setText(htmlContent, true);

        // 添加附件
        if (attachmentPath != null) {
            FileSystemResource file = new FileSystemResource(new File(attachmentPath));
            helper.addAttachment("附件名称.pdf", file);
        }

        mailSender.send(message);
    }


    /////

    /**
     * 发送纯文本的邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     * @return 是否成功
     */
    @SneakyThrows(Exception.class)
    public boolean sendGeneralEmail(String subject, String content, String... to) {
        // 创建邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        // 设置收件人
        message.setTo(to);
        // 设置邮件主题
        message.setSubject(subject);
        // 设置邮件内容
        message.setText(content);

        // 发送邮件
        mailSender.send(message);

        return true;
    }

    /**
     * 发送html邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    public void sendHtmlMailMessage(String to, String subject, String content) {

        try {
            //true 代表支持复杂的类型
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
            //邮件发信人
            mimeMessageHelper.setFrom(from);
            //邮件收信人  1或多个
            mimeMessageHelper.setTo(to.split(","));
            //邮件主题
            mimeMessageHelper.setSubject(subject);
            //邮件内容   true 代表支持html
            mimeMessageHelper.setText(content, true);
            //邮件发送时间
            mimeMessageHelper.setSentDate(new Date());

            //发送邮件
            mailSender.send(mimeMessageHelper.getMimeMessage());
            System.out.println("发送邮件成功：" + from + "->" + to);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("发送邮件失败：" + e.getMessage());
        }
    }

    /**
     * 发送html的邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     * @return 是否成功
     */
    @SneakyThrows(Exception.class)
    public boolean sendHtmlEmail(String subject, String content, String... to) {
        // 创建邮件消息
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        // 设置收件人
        helper.setTo(to);
        // 设置邮件主题
        helper.setSubject(subject);
        // 设置邮件内容
        helper.setText(content, true);

        // 发送邮件
        mailSender.send(mimeMessage);

        return true;

    }

    /**
     * 发送带附件的邮件
     *
     * @param to        收件人
     * @param subject   主题
     * @param content   内容
     * @param filePaths 附件路径
     * @return 是否成功
     */
    @SneakyThrows(Exception.class)
    public boolean sendAttachmentsEmail(String subject, String content, String[] to, String[] filePaths) {
        // 创建邮件消息
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        // 设置收件人
        helper.setTo(to);
        // 设置邮件主题
        helper.setSubject(subject);
        // 设置邮件内容
        helper.setText(content, true);

        // 添加附件
        if (filePaths != null) {
            for (String filePath : filePaths) {
                FileSystemResource file = new FileSystemResource(new File(filePath));
                helper.addAttachment(Objects.requireNonNull(file.getFilename()), file);

            }
        }
        // 发送邮件
        mailSender.send(mimeMessage);
        return true;
    }

    /**
     * 发送带静态资源的邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     * @param rscPath 静态资源路径
     * @param rscId   静态资源id
     * @return 是否成功
     */
    @SneakyThrows(Exception.class)
    public boolean sendInlineResourceEmail(String subject, String content, String to, String rscPath, String rscId) {
        // 创建邮件消息
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        // 设置发件人
        helper.setFrom(from);
        // 设置收件人
        helper.setTo(to);
        // 设置邮件主题
        helper.setSubject(subject);

        //html内容图片
        String contentHtml = "<html><body>这是邮件的内容，包含一个图片：<img src='cid:" + rscId + "'>" + content + "</body></html>";

        helper.setText(contentHtml, true);
        //指定讲资源地址
        FileSystemResource res = new FileSystemResource(new File(rscPath));
        helper.addInline(rscId, res);

        mailSender.send(mimeMessage);
        return true;
    }

}
