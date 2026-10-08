package com.hjlx.temu.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 统一异常处理，返回与 Temu 一致的结构：success / errorCode / errorMsg。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> handleBusiness(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return body(false, e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> handleException(Exception e) {
        log.error("系统异常", e);
        return body(false, "SYSTEM_ERROR", e.getMessage());
    }

    private Map<String, Object> body(boolean success, Object errorCode, String errorMsg) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", success);
        result.put("errorCode", errorCode);
        result.put("errorMsg", errorMsg);
        result.put("result", null);
        return result;
    }
}
