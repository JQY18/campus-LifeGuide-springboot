package renko.jiang.campus_life_guide.enums;

import lombok.Getter;

/**
 * @author jqy
 * @date 2025-05-27
 */

@Getter
public enum GroupRoleType {
    OWNER("OWNER"),
    ADMIN("ADMIN"),
    MEMBER("MEMBER");

    private final String roleName;
    GroupRoleType(String roleName) {
        this.roleName = roleName;
    }
}
