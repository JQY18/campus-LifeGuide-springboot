package renko.jiang.campus_life_guide.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

/**
 * @author 86132
 */

@Slf4j
@Component
public class WebSocketEventListener {
    // 监听客户端连接事件,将用户ID放入redis中
    @EventListener
    public void handleConnect(SessionConnectEvent event) {
        Map simpSessionAttributes = event.getMessage().getHeaders().get("simpSessionAttributes", Map.class);
        if (simpSessionAttributes != null) {
            Object o = simpSessionAttributes.get("userId");
            try {
                Integer userId = (Integer) o;
                log.info("STOMP 连接认证成功，用户ID: {}", userId);
            } catch (Exception e) {
                log.error("STOMP 连接认证失败: {}", e.getMessage());
            }
        }
        //log.info("处理连接事件{}", simpSessionAttributes);
        log.info("处理连接事件{}", event);
    }

    // 监听客户端断开事件，从redis中移除用户ID
    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        log.info("处理断开事件:{}", event);
    }
}