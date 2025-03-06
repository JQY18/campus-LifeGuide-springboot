package renko.jiang.campus_trade.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import renko.jiang.campus_trade.config.json.JacksonObjectMapper;
import renko.jiang.campus_trade.interceptor.PVCountInterceptor;
import renko.jiang.campus_trade.interceptor.UserContextInterceptor;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private PVCountInterceptor pvCountInterceptor;

    @Autowired
    private UserContextInterceptor userContextInterceptor;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userContextInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/admin/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/register");

        registry.addInterceptor(pvCountInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/admin/**");
    }

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }


    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建一个消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //为消息转换器设置一个对象转换器,对象转换器可以将Java对象序列化为json格式的数据
        converter.setObjectMapper(new JacksonObjectMapper());
        //将消息转换器加入到容器中
        converters.add(0, converter);
    }

}