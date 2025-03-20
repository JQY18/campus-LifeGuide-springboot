package renko.jiang.campus_life_guide.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginDTO {

    private String nickname;
    @Schema(description = "用户名",title = "username-title",defaultValue = "admin",requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    @Schema(description = "密码", defaultValue = "111111",requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

}
