package renko.jiang.campus_life_guide.interceptor;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
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
import renko.jiang.campus_life_guide.properties.JwtProperties;

import java.io.IOException;


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

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);


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
        }
        log.info("STOMP 命令: {}", accessor.getCommand());
        return message;
    }


    // 构建错误消息并终止连接
    private Message<?> buildErrorMessage(StompHeaderAccessor accessor, String errorMsg) {
        StompHeaderAccessor errorAccessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
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

}