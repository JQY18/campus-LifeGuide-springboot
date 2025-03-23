package renko.jiang.campus_life_guide.interceptor;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import renko.jiang.campus_life_guide.properties.JwtProperties;
import renko.jiang.campus_life_guide.utils.JwtUtil;

import java.util.Map;


/**
 * @author 86132
 */

@Slf4j
@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    @Resource
    private JwtProperties jwtProperties;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
        String jwtToken = servletServerHttpRequest.getServletRequest().getParameter("token");
        log.info("挥手前token:{}", jwtToken);

        if (StrUtil.isBlank(jwtToken)) {
            response.setStatusCode(HttpStatusCode.valueOf(401));
            log.info("renko.jiang.campus_life_guide.interceptor.AuthHandshakeInterceptor:用户未登录");
            return false;
        }

        Integer userId;
        //解析JWT
        try {
            log.info("renko.jiang.campus_life_guide.interceptor.AuthHandshakeInterceptor:用户登录-jwt校验:{}", jwtToken);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), jwtToken);
            userId = claims.get("userId", Integer.class);
        } catch (Exception e) {
            log.error("renko.jiang.campus_life_guide.interceptor.AuthHandshakeInterceptor:用户登录认证失败");
            response.setStatusCode(HttpStatusCode.valueOf(401));
            return false;
        }
        // 从请求参数或 Header 中提取用户 ID（例如 JWT 令牌）
        attributes.put("userId", userId);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        log.error("挥手后");

    }
}