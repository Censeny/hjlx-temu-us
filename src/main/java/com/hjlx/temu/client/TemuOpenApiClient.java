package com.hjlx.temu.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hjlx.temu.config.TemuProperties;
import com.hjlx.temu.enums.TemuApiType;
import com.hjlx.temu.exception.BusinessException;
import com.hjlx.temu.util.TemuSignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Temu 开放平台直连客户端（海外部署）。
 *
 * <p>本服务持有 app_key / app_secret 并完成签名，调用方仅需提供
 * 店铺的 access_token、区域及业务参数。</p>
 */
@Component
public class TemuOpenApiClient {

    private static final Logger log = LoggerFactory.getLogger(TemuOpenApiClient.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final TemuProperties properties;
    private final HttpClient httpClient;

    public TemuOpenApiClient(TemuProperties properties) {
        this.properties = properties;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(properties.getTimeoutMs()))
                .build();
    }

    /**
     * 通用调用方法。
     *
     * @param type        Temu OpenAPI 方法名
     * @param accessToken Temu access_token（获取 token 接口不需要传）
     * @param request     业务参数（会平铺到请求体顶层）
     * @param region      区域：US / EU / GLOBAL，为空时使用默认区域
     * @return Temu 原始响应
     */
    public Map<String, Object> call(String type, String accessToken, Map<String, Object> request, String region) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("type", type);
        params.put("app_key", properties.getAppKey());
        params.put("timestamp", String.valueOf(Instant.now().getEpochSecond()));
        params.put("data_type", "JSON");
        if (accessToken != null && !accessToken.isEmpty()) {
            params.put("access_token", accessToken);
        }
        if (request != null) {
            for (Map.Entry<String, Object> entry : request.entrySet()) {
                if (entry.getValue() != null) {
                    params.put(entry.getKey(), entry.getValue());
                }
            }
        }

        params.put("sign", TemuSignUtil.sign(params, properties.getAppSecret()));

        String url = getBaseUrl(region) + properties.getApiRouter();
        return doPost(url, params);
    }

    /**
     * 获取面单等文档：先查询下载地址，再下载文件字节。
     *
     * @param accessToken access_token
     * @param request     业务参数（documentType、packageSnList 等）
     * @param region      区域
     * @return 文件字节
     */
    @SuppressWarnings("unchecked")
    public byte[] documentDownload(String accessToken, Map<String, Object> request, String region) {
        Map<String, Object> response = call(
                TemuApiType.BG_LOGISTICS_SHIPMENT_DOCUMENT_GET.getCode(), accessToken, request, region);
        if (!Boolean.TRUE.equals(response.get("success"))) {
            throw new BusinessException("获取面单下载地址失败: " + response.get("errorMsg"));
        }

        Object resultObj = response.get("result");
        if (!(resultObj instanceof Map)) {
            throw new BusinessException("获取面单下载地址失败: result 为空");
        }
        Object listObj = ((Map<String, Object>) resultObj).get("shippingLabelUrlList");
        if (!(listObj instanceof List) || ((List<?>) listObj).isEmpty()) {
            throw new BusinessException("未获取到面单下载地址");
        }
        Object first = ((List<?>) listObj).get(0);
        if (!(first instanceof Map) || ((Map<String, Object>) first).get("url") == null) {
            throw new BusinessException("未获取到面单下载地址");
        }
        String url = String.valueOf(((Map<String, Object>) first).get("url"));
        return downloadUrl(url);
    }

    private byte[] downloadUrl(String url) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(url))
                    .timeout(Duration.ofMillis(properties.getTimeoutMs()))
                    .GET()
                    .build();
            HttpResponse<byte[]> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() != 200) {
                throw new BusinessException("下载面单失败，HTTP状态码: " + response.statusCode());
            }
            return response.body();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("下载面单文件异常: {}", url, e);
            throw new BusinessException("下载面单失败: " + e.getMessage());
        }
    }

    private Map<String, Object> doPost(String url, Map<String, Object> params) {
        try {
            String jsonBody = MAPPER.writeValueAsString(params);
            log.info("Temu API 请求 - URL: {}, Body: {}", url, jsonBody);

            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .timeout(Duration.ofMillis(properties.getTimeoutMs()))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            log.info("Temu API 响应 - Status: {}, Body: {}", response.statusCode(), response.body());

            if (response.statusCode() == 200) {
                return MAPPER.readValue(response.body(), new TypeReference<Map<String, Object>>() {
                });
            }
            return errorResult("HTTP_" + response.statusCode(),
                    "HTTP状态码: " + response.statusCode() + ", body: " + response.body());
        } catch (Exception e) {
            log.error("Temu API 请求异常", e);
            return errorResult("REQUEST_EXCEPTION", e.getMessage());
        }
    }

    private Map<String, Object> errorResult(String errorCode, String errorMsg) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", false);
        result.put("errorCode", errorCode);
        result.put("errorMsg", errorMsg);
        result.put("result", null);
        return result;
    }

    private String getBaseUrl(String region) {
        String target = (region == null || region.isEmpty()) ? properties.getDefaultRegion() : region;
        if ("EU".equalsIgnoreCase(target)) {
            return properties.getBaseUrlEu();
        }
        if ("GLOBAL".equalsIgnoreCase(target)) {
            return properties.getBaseUrlGlobal();
        }
        return properties.getBaseUrlUs();
    }
}
