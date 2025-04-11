package renko.jiang.campus_life_guide.interceptor;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompEncoder;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import renko.jiang.campus_life_guide.controller.chat.ChatMessage;
import renko.jiang.campus_life_guide.properties.JwtProperties;
import renko.jiang.campus_life_guide.service.UserChatService;

import java.io.IOException;
import java.nio.ByteBuffer;


/**
 * @author 86132
 */
@Slf4j
@Component
// 自定义通道拦截器
public class AuthChannelInterceptor implements ChannelInterceptor {

    // 需注入配置类
    @Resource
    private JwtProperties jwtProperties;

    @Autowired
    private UserChatService userChatService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // 过滤时保留原始STOMP头信息
        accessor.setLeaveMutable(true);

        //if (true) {
//            //            返回错误信息并终止连接
//             return buildErrorMessage(accessor, "无效的访问令牌");
//            //throw new MessagingException("无效的访问令牌");
//            //return null;
//            //sendErrorAndClose(accessor, "无效的访问令牌");
//            //return null;
//        }


        // 仅处理 CONNECT 命令（连接建立阶段）
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            log.info("STOMP 命令: {}", accessor.getCommand());

            // 测试用
//            if (true) {
//                return message;
//            }
//
//            String tokenHeader = accessor.getFirstNativeHeader("Authorization");
//
//            // 校验逻辑开始
//            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
//                log.error("STOMP 连接认证失败：Authorization 头缺失或格式错误");
//                return buildErrorMessage(accessor, "未授权的连接请求");
//            }
//
//            String jwtToken = tokenHeader.split(" ")[1];
//            try {
//                Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), jwtToken);
//                Integer userId = claims.get("userId", Integer.class);
//
//                // 绑定用户身份到 STOMP 会话（两种方式任选其一）
//                // 方式一：存入会话属性（后续通过 @Header("simpSessionAttributes") 获取）
////                accessor.getSessionAttributes().put("userId", userId);
////                accessor.setUser(userId::toString);
//                // 方式二：绑定 Spring Security Principal
//                //accessor.setUser(userId::toString);
//
//                log.info("STOMP 连接认证成功，用户ID: {}", userId);
//            } catch (Exception e) {
//                log.error("JWT 解析失败: {}", e.getMessage());
//                return buildErrorMessage(accessor, "无效的访问令牌");
//            }
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            log.info("STOMP 命令: {}", accessor.getCommand());
            // 检查用户是否有资格订阅该消息通道，防止用户恶意订阅消息
            Integer userId = (Integer) accessor.getSessionAttributes().get("userId");
            String destination = accessor.getDestination();
            // 私聊订阅自己
            // /user/12/private
            if (destination == null) {
                return buildErrorMessage(accessor, "无权订阅该消息通道");
            }

            if (destination.startsWith("/user/" + userId + "/private")) {
                log.info("STOMP 命令: {}", destination);
                // 放行
                String privateChatUserId = destination.split("/")[2];

                if (!privateChatUserId.equals(userId.toString())) {
                    log.error("STOMP 连接认证失败：用户无权订阅该消息通道userId:{} -> {}", userId, privateChatUserId);
                    return buildErrorMessage(accessor, "无权订阅该消息通道");
                }
            }

            // /topic/group/2
            if (destination.startsWith("/topic/group/")) {
                String groupId = destination.split("/")[3];
                // 检查用户是否有资格订阅该消息通道，防止用户恶意订阅消息
                // 判断用户当前订阅的groupId是否是当前用户加入的群聊
                Long chatId = null;
                try {
                    chatId = Long.parseLong(groupId);
                } catch (NumberFormatException e) {
                    return buildErrorMessage(accessor, "无权订阅该消息通道");
                }

                boolean isGroupMember = userChatService.isGroupMember(userId, chatId);

                if (BooleanUtil.isFalse(isGroupMember)) {
                    log.error("STOMP 连接认证失败：用户无权订阅该消息通道userId:{} -> chatId:{}", userId, groupId);
                    return buildErrorMessage(accessor, "无权订阅该消息通道");
                }
            }
        }
        // 设置心跳响应
        log.info("消息指令类型：{}", accessor.getCommand());
        return message;
    }

    private Message<?> buildMessage(StompHeaderAccessor accessor, String msg) {
        byte[] payload = msg.getBytes();
        return MessageBuilder.createMessage(payload, accessor.getMessageHeaders());
    }

    // 构建错误消息并终止连接
    private Message<?> buildErrorMessage(StompHeaderAccessor accessor, String errorMsg) {
        StompHeaderAccessor errorAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
        errorAccessor.setMessage(errorMsg);
        errorAccessor.setSessionId(accessor.getSessionId());
        return MessageBuilder.createMessage(new byte[0], errorAccessor.getMessageHeaders());
    }


    private void sendErrorAndClose(StompHeaderAccessor accessor, String errorMsg) {
        WebSocketSession session = (WebSocketSession) accessor.getSessionAttributes().get("WEBSOCKET_SESSION");
        if (session != null && session.isOpen()) {
            try {
                // 发送 ERROR 帧
                StompHeaderAccessor errorAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
                errorAccessor.setMessage(errorMsg);
                errorAccessor.setSessionId(accessor.getSessionId());
                Message<byte[]> errorMessage = MessageBuilder.createMessage(new byte[0], errorAccessor.getMessageHeaders());
                session.sendMessage(new TextMessage(new StompEncoder().encode(errorMessage)));
                log.info("发送 ERROR 帧: {}", errorMsg);

                // 关闭会话
                session.close(CloseStatus.NORMAL);
                log.info("会话已关闭");
            } catch (IOException e) {
                log.error("发送 ERROR 或关闭失败: {}", e.getMessage());
            }
        }
    }


    //    @Override
//    public boolean preReceive(MessageChannel channel) {
//        log.error("renko.jiang.campus_life_guide.interceptor.AuthChannelInterceptor:拦截器-preReceive");
//        return true;
//    }

//    为什么 preReceive 不触发
//    STOMP 协议特性：
//
//    STOMP 是基于帧的协议，客户端主动发送消息到服务器
//
//    服务器通常不会"接收"消息，而是"处理"客户端主动发送的帧
//
//    因此 preReceive 在 STOMP 场景下很少被调用
//
//    WebSocket 与 STOMP 区别：
//
//    在纯 WebSocket 中可能会触发 preReceive
//
//    但使用 STOMP 子协议时，消息处理流程不同
//
//    消息流方向：
//
//    preSend：处理所有出站消息（服务器→客户端）
//
//    postSend：处理所有入站消息（客户端→服务器）
//
//    preReceive 主要用于底层通道接收控制，STOMP 场景下不适用
}