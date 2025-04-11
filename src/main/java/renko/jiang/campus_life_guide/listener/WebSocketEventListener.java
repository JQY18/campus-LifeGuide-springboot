package renko.jiang.campus_life_guide.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import renko.jiang.campus_life_guide.service.UserOnlineStatusService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author 86132
 */

@Slf4j
@Component
public class WebSocketEventListener {
    @Autowired
    private UserOnlineStatusService userOnlineStatusService;

    // 存储用户的在线状态key=sessionId,value=userId
    private final ConcurrentMap<String, Integer> onlineUserMap = new ConcurrentHashMap<>();

    // 监听客户端连接事件,将用户ID放入redis中
    @EventListener(SessionConnectEvent.class)
    public void handleConnect(SessionConnectEvent event) {
        // 获取websocket会话属性
        Map simpSessionAttributes = event.getMessage().getHeaders().get("simpSessionAttributes", Map.class);
        // 获取websocket会话id
        String sessionId = (String) event.getMessage().getHeaders().get("simpSessionId");
        if (simpSessionAttributes != null) {
            Integer userId = (Integer) simpSessionAttributes.get("userId");
            onlineUserMap.put(sessionId, userId);
            // 用户上线，将用户在线状态设为1
            userOnlineStatusService.setOnline(userId);
            log.info("STOMP 连接认证成功，用户ID: {}", userId);
        }

        //log.info("处理连接事件{}", simpSessionAttributes);
        log.info("处理连接事件{}", event);
    }

    // 监听客户端断开事件，从redis中移除用户ID
    @EventListener(SessionDisconnectEvent.class)
    public void handleDisconnect(SessionDisconnectEvent event) {
        // 获取websocket会话id
        String sessionId = event.getSessionId();

        if (onlineUserMap.containsKey(sessionId)) {
            //根据会话id获取用户ID
            Integer userId = onlineUserMap.get(sessionId);
            onlineUserMap.remove(sessionId);
            // 用户下线，将用户在线状态设为0
            userOnlineStatusService.setOffline(userId);
            log.info("用户ID: {} 断开连接", userId);
        }

        log.info("处理断开事件:{}", event);
    }

    // 还可以监听用户订阅事件，比如订阅了某个主题，可以记录用户订阅的主题，以便后续推送消息时，只推送给该用户订阅的主题
    @EventListener
    public void handleSubscribe(SessionSubscribeEvent event) {
        log.info("处理订阅事件{}", event);
    }

    // 可以监听用户取消订阅事件，比如取消订阅了某个主题，可以移除用户订阅的主题，以便后续推送消息时，不再推送给该用户订阅的主题
    @EventListener
    public void handleUnsubscribe(SessionUnsubscribeEvent event) {
        log.info("处理取消订阅事件{}", event);
    }
}