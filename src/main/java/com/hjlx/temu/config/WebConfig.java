package com.hjlx.temu.config;

import com.hjlx.temu.interceptor.ApiKeyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置：注册 API Key 拦截器。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TemuProperties properties;

    public WebConfig(TemuProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiKeyInterceptor(properties))
                .addPathPatterns("/api/**");
    }
}
