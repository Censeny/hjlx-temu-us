package com.hjlx.temu.interceptor;

import com.hjlx.temu.config.TemuProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * 国内调用方的固定 API Key 校验。
 */
public class ApiKeyInterceptor implements HandlerInterceptor {

    public static final String API_KEY_HEADER = "x-api-key";

    private final TemuProperties properties;

    public ApiKeyInterceptor(TemuProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String apiKey = request.getHeader(API_KEY_HEADER);
        if (properties.getApiKey() != null && !properties.getApiKey().isEmpty()
                && properties.getApiKey().equals(apiKey)) {
            return true;
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(
                "{\"success\":false,\"errorCode\":\"UNAUTHORIZED\",\"errorMsg\":\"无效的 API Key\",\"result\":null}");
        return false;
    }
}
