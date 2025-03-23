package renko.jiang.campus_life_guide.pojo.vo;

import lombok.Data;

/**
 * @author 86132
 */

@Data
public class GroupMember {
    private Integer id;
    private String nickname;
    private String avatar;
    // 群主(owner)、管理员(admin)、普通成员(member)
    private String role;
}
