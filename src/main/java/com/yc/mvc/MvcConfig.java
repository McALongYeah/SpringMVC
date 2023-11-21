package com.yc.mvc;

import com.yc.mvc.web.loginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@ComponentScan("com.yc.mvc")
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
//    @Override
//    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
////        WebMvcConfigurer.super.configureDefaultServletHandling(configurer);
//        configurer.enable();
//    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("file:E:\\software\\workspace\\untitled\\SpringMVC-demo\\src\\main\\webapp\\");
    }

    @Bean
    public StandardServletMultipartResolver a(){
        return new StandardServletMultipartResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(new loginInterceptor())
              .addPathPatterns("/a.html","/b.html","/a/b/c/sign");
    }
}
