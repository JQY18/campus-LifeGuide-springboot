package renko.jiang.campus_life_guide.interceptor;


import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import renko.jiang.campus_life_guide.context.UserContextHolder;
import renko.jiang.campus_life_guide.properties.JwtProperties;
import renko.jiang.campus_life_guide.utils.JwtUtil;

/**
 * @author 86132
 */

@Slf4j
@Component
public class UserContextInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if("upgrade".equalsIgnoreCase(request.getHeader("Connection"))){
//            return true;
//        }

        if (HttpMethod.OPTIONS.toString().equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null) {
            //response.setStatus(401);
            log.info("renko.jiang.campus_trade.interceptor.UserContextInterceptor:用户未登录");
            return true;
        }

        String jwtToken = token.split(" ")[1];

        //解析JWT
        try {
            log.info("renko.jiang.campus_trade.interceptor.UserContextInterceptor:用户登录-jwt校验:{}", jwtToken);

            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), jwtToken);
            Integer userId = claims.get("userId", Integer.class);
            //保存用户上下文
            UserContextHolder.setUserId(userId);
        }catch (Exception e) {
            log.error("renko.jiang.campus_trade.interceptor.UserContextInterceptor:用户登录失败", e);
            response.setStatus(401);
            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextHolder.remove();
    }
}
