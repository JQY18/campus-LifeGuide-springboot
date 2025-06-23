package renko.jiang.campus_life_guide.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import renko.jiang.campus_life_guide.enums.GroupRoleType;
import renko.jiang.campus_life_guide.anno.GroupRole;
import renko.jiang.campus_life_guide.context.UserContextHolder;
import renko.jiang.campus_life_guide.mapper.UserChatMapper;
import renko.jiang.campus_life_guide.pojo.result.Result;


/**
 * @author jqy
 * @date 2025-05-27
 */

@Component
@Aspect
public class GroupPermissionControl {

    @Autowired
    private UserChatMapper userChatMapper;

    @Around("@annotation(anno)")
    public Object groupRoleControl(ProceedingJoinPoint joinPoint, GroupRole anno) throws Throwable {
        // 获取当前用户id
        Integer userId = UserContextHolder.getUserId();
        // 获取当前群聊id
        Object[] args = joinPoint.getArgs();
        Long chatId = (Long) args[0];
        // 获取当前群聊的群角色
        String roleInChatRoom = userChatMapper.getRoleInChatRoom(chatId, userId);
        // 检查是否与注解的群角色匹配
        GroupRoleType groupRole = anno.value();
        if (!roleInChatRoom.equals(groupRole.getRoleName())) {
            return Result.error("无权限访问");
        }

        Object result = joinPoint.proceed(args);

        return result;
    }

//    @Pointcut("execution(* renko.jiang.campus_life_guide.controller.chat.*.*(..)) && @annotation(renko.jiang.campus_life_guide.anno.GroupRole)")
//    private void groupRolePointCut(){}
//
//
//    // 群聊权限控制切面
//    @Around("groupRolePointCut() && @annotation(a)")
//    private Object groupRoleControl(ProceedingJoinPoint joinPoint, GroupRole a){
//        // 能够访问目标接方法的群角色类型
//        GroupRoleType value = a.value();
//        // 当前登录的用户id
//        UserContextHolder.getUserId();
//        // 查看当前用户在当前群聊中的角色
//        return null;
//    }
}
