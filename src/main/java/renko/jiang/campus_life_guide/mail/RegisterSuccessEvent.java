package renko.jiang.campus_life_guide.mail;


import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * @author 86132
 */
@Getter
public class RegisterSuccessEvent extends ApplicationEvent {
    private String email;
    private String username;

    public RegisterSuccessEvent(Object source, String email, String username) {
        super(source);
        this.email = email;
        this.username = username;
    }
}
