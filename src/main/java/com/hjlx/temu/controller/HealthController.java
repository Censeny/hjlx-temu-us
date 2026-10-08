package com.hjlx.temu.controller;

import com.hjlx.temu.client.TemuOpenApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 健康检查（无需鉴权）。
 */
@RestController
public class HealthController {
    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "UP");
        log.info(result.toString());
        return result;
    }
}
