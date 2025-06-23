package renko.jiang.campus_life_guide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import renko.jiang.campus_life_guide.utils.RedisUtil;

/**
 * @author 86132
 */
@Service
public class UserOnlineStatusService {
    @Autowired
    private RedisUtil redisUtil;
    private final String ONLINE_USER_KEY = "online.user";

    /**
     * 设置用户在线状态
     * @param userId
     */
    public void setOnline(Integer userId) {
        redisUtil.setBit(ONLINE_USER_KEY, userId, true);
    }

    /**
     *  设置用户离线状态
     * @param userId
     */
    public void setOffline(Integer userId) {
        redisUtil.setBit(ONLINE_USER_KEY, userId, false);
    }

    /**
     * 判断用户是否在线
     * @param userId
     * @return
     */
    public Boolean isOnline(Integer userId) {
        return redisUtil.getBit(ONLINE_USER_KEY, userId);
    }

    public Boolean clearOnlineUserBitMap() {
        return redisUtil.delete(ONLINE_USER_KEY);
    }
}
