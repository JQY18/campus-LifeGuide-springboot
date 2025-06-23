package renko.jiang.campus_life_guide.config.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.*;
import renko.jiang.campus_life_guide.config.json.JacksonObjectMapper;
import renko.jiang.campus_life_guide.interceptor.PVCountInterceptor;
import renko.jiang.campus_life_guide.interceptor.UserContextInterceptor;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private PVCountInterceptor pvCountInterceptor;

    @Autowired
    private UserContextInterceptor userContextInterceptor;


    //添加统一路径前缀/api
//    @Override
//    public void configurePathMatch(PathMatchConfigurer configurer) {
//        configurer.addPathPrefix("/api", c -> c.isAnnotationPresent(RestController.class));
//    }


    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api", c ->
                c.isAnnotationPresent(RestController.class) &&
                        !c.getPackageName().contains("springfox") && // 排除 Springfox 相关的包
                        !c.getPackageName().contains("springdoc")    // 排除 Springdoc 相关的包
        );
    }
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


    private final String[] EXCLUDE_PATH = {
            "/doc.html",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "webjars/**"
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userContextInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/admin/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/register")
                .excludePathPatterns(EXCLUDE_PATH);

        registry.addInterceptor(pvCountInterceptor)
                .addPathPatterns("/locations");

//        registry.addInterceptor(pvCountInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/admin/**")
//                .excludePathPatterns(EXCLUDE_PATH);
    }

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:" + uploadPath + "/");

        // 添加静态资源映射规则
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        //配置 knife4j 的静态资源请求映射地址
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }


    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建一个消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //为消息转换器设置一个对象转换器,对象转换器可以将Java对象序列化为json格式的数据
        converter.setObjectMapper(new JacksonObjectMapper());
        //将消息转换器加入到容器中
        converters.add(converters.size() - 1, converter);
    }

}