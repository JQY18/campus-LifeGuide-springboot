package renko.jiang.campus_life_guide.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import renko.jiang.campus_life_guide.service.UserOnlineStatusService;



/**
 * @author 86132
 */
@Slf4j
@Component
public class CacheWarmer implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private UserOnlineStatusService userOnlineStatusService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("ApplicationReadyEvent:开始缓存预热。。。");
        // 清理用户登陆状态的BitMap
         userOnlineStatusService.clearOnlineUserBitMap();
    }
}
