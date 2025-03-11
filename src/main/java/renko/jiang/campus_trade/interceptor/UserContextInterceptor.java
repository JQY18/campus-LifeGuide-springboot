package renko.jiang.campus_trade.interceptor;


import cn.hutool.core.bean.BeanUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import renko.jiang.campus_trade.context.UserContextHolder;

/**
 * @author 86132
 */

@Slf4j
@Component
public class UserContextInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HttpMethod.OPTIONS.toString().equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        String userId = request.getHeader("Authorization");
        if (userId == null) {
            //response.setStatus(401);
            log.info("renko.jiang.campus_trade.interceptor.UserContextInterceptor:用户未登录");
            return true;
        }
        log.info("renko.jiang.campus_trade.interceptor.UserContextInterceptor:用户登录:{}",userId);

        int id = Integer.parseInt(userId.split(" ")[1]);
        UserContextHolder.setUserId(id);
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextHolder.remove();
    }
}
