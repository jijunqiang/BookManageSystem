package com.bysj.booksystem.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RegisterInterceptor implements WebMvcConfigurer {

    @Bean
    public HandlerInterceptor getLoginHandleInterceptor(){
        return new LoginHandlerInterceptor();
    }

    @Bean
    public HandlerInterceptor getRememberMeInterceptor(){
        return new RememberMeInterceptor();
    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(getLoginHandleInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/userController/login","/","login","/login","/page/register","/css/**","/img/**","/js/**","/layui/**");

            registry.addInterceptor(getRememberMeInterceptor())
                    .addPathPatterns("/userController/login");

    }
}
