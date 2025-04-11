package renko.jiang.campus_life_guide.service.impl;

import jakarta.annotation.Resource;
import renko.jiang.campus_life_guide.mapper.UserChatMapper;
import renko.jiang.campus_life_guide.service.UserChatService;
import org.springframework.stereotype.Service;


/**
 * (UserChat)表服务实现类
 *
 * @author makejava
 * @since 2025-03-17 22:25:53
 */
@Service("userChatService")
public class UserChatServiceImpl implements UserChatService {
    @Resource
    private UserChatMapper userChatMapper;

    @Override
    public boolean isGroupMember(Integer userId, Long chatId) {
        long count = userChatMapper.isGroupMember(userId, chatId);
        return count > 0;
    }

}
