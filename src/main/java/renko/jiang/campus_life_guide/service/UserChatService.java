package renko.jiang.campus_life_guide.service;

/**
 * (UserChat)表服务接口
 *
 * @author makejava
 * @since 2025-03-17 22:25:53
 */
public interface UserChatService {
    // 查询指定userId是否是chatId的群成员
    boolean isGroupMember(Integer userId, Long chatId);
}
