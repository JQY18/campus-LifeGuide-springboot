package renko.jiang.campus_life_guide.anno;

import renko.jiang.campus_life_guide.enums.GroupRoleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jqy
 * @date 2025-05-27
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GroupRole {
    GroupRoleType value();
}
