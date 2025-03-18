package renko.jiang.campus_life_guide.pojo.vo;

import lombok.Data;


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
