package renko.jiang.campus_life_guide.mail;


import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class MailUtilTest {
    @Autowired
    private MailService mailService;

    @Test
    void sendVerificationEmail() throws MessagingException {
        long start = System.currentTimeMillis();
        mailService.sendVerificationEmail("3165973561@qq.com", "654321");
        System.out.println((System.currentTimeMillis() - start));
    }

    @Test
    void sendVerificationEmailSimple() {
        long start = System.currentTimeMillis();

        mailService.sendVerificationEmailSimple("3165973561@qq.com", "123456");
        System.out.println((System.currentTimeMillis() - start));
    }

    @Test
    void sendSimpleEmail() {
        String to = "3165973561@qq.com";
        String subject = "测试邮件";
        String text = "这是一封测试邮件";
        mailService.sendGeneralEmail(to, subject, text);
    }

    @Test
    void sendHtmlEmail() {
        String to = "3165973561@qq.com";
        String subject = "注册验证";
        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>邮件</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<h3>这是一封HTML邮件！</h3>\n" +
                "</body>\n" +
                "</html>";
        mailService.sendHtmlMailMessage(to, subject, content);
    }
}