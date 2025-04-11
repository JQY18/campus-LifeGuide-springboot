package renko.jiang.campus_life_guide.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author 86132
 */
@Data
public class LoginDTO {

    private String nickname;
    @Schema(description = "用户名",title = "username-title",defaultValue = "admin",requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    @Schema(description = "密码", defaultValue = "111111",requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
    @Schema(description = "邮箱", defaultValue = "111111@qq.com",requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
    @Schema(description = "验证码", defaultValue = "111111",requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

}
