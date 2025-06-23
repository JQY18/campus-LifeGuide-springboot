package renko.jiang.campus_life_guide.mail;


import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author 86132
 */
@Getter
public class LoginSuccessEvent extends ApplicationEvent {
    private String email;
    private String username;
    public LoginSuccessEvent(Object source,String email,String username) {
        super(source);
        this.email = email;
        this.username = username;
    }
}
