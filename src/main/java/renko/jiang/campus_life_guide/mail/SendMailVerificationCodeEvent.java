package renko.jiang.campus_life_guide.mail;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author 86132
 */
@Getter
public class SendMailVerificationCodeEvent extends ApplicationEvent {
    private String email;

    public SendMailVerificationCodeEvent(Object source, String email) {
        super(source);
        this.email = email;
    }

}
