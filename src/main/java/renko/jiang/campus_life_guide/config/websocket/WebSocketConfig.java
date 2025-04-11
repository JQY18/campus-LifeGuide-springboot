package renko.jiang.campus_life_guide.config.websocket;


import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import renko.jiang.campus_life_guide.interceptor.AuthChannelInterceptor;
import renko.jiang.campus_life_guide.interceptor.AuthHandshakeInterceptor;
import renko.jiang.campus_life_guide.interceptor.MessageFilterChannelInterceptor;

/**
 * @author 86132
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Resource
    private AuthHandshakeInterceptor authHandshakeInterceptor;


    @Resource
    private AuthChannelInterceptor authChannelInterceptor;

    @Resource
    private MessageFilterChannelInterceptor messageFilterChannelInterceptor;


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authChannelInterceptor)
                .interceptors(messageFilterChannelInterceptor);
    }


    @Resource(name = "heartBeatTaskScheduler")
    private TaskScheduler heartBeatTaskScheduler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 设置消息代理前缀、心跳
        config.enableSimpleBroker("/topic", "/user", "/queue")
                .setHeartbeatValue(new long[]{20000, 20000})
                .setTaskScheduler(heartBeatTaskScheduler);
        config.setUserDestinationPrefix("/user/");
        // 应用程序目的地前缀
        config.setApplicationDestinationPrefixes("/app");
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册STOMP端点并允许跨域访问
        registry.addEndpoint("/ws/chat")
                .addInterceptors(authHandshakeInterceptor)
                .setAllowedOrigins("*");

        registry.addEndpoint("/ws/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}