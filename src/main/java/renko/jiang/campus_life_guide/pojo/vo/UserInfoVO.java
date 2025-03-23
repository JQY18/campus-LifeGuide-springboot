package renko.jiang.campus_life_guide.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


/**
 * @author 86132
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserInfoVO {
    private Integer id;
    private String username;
    private String nickname;
    private String gender;
    private Integer age;
    private String avatar;
    private String school;
    private Integer collections;
}
