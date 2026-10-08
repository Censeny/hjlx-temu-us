package com.hjlx.temu.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Temu 网关配置。
 *
 * <p>海外服务持有 Temu 开放平台的应用凭证（appKey / appSecret），
 * 国内业务方只需传入各店铺的 access_token 与业务参数即可。</p>
 */
@Component
@ConfigurationProperties(prefix = "temu")
public class TemuProperties {

    /**
     * 国内服务调用本服务时携带的固定 API Key（请求头 x-api-key）。
     */
    private String apiKey;

    /**
     * Temu 开放平台 app_key。
     */
    private String appKey;

    /**
     * Temu 开放平台 app_secret。
     */
    private String appSecret;

    /**
     * 默认区域：US / EU / GLOBAL。
     */
    private String defaultRegion = "US";

    /**
     * 美区接入地址。
     */
    private String baseUrlUs = "https://openapi-b-us.temu.com";

    /**
     * 欧区接入地址。
     */
    private String baseUrlEu = "https://openapi-b-eu.temu.com";

    /**
     * 全球区接入地址。
     */
    private String baseUrlGlobal = "https://openapi-b-global.temu.com";

    /**
     * 通用代理接口路径。
     */
    private String apiRouter = "/openapi/router";

    /**
     * HTTP 超时时间（毫秒）。
     */
    private int timeoutMs = 30000;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getDefaultRegion() {
        return defaultRegion;
    }

    public void setDefaultRegion(String defaultRegion) {
        this.defaultRegion = defaultRegion;
    }

    public String getBaseUrlUs() {
        return baseUrlUs;
    }

    public void setBaseUrlUs(String baseUrlUs) {
        this.baseUrlUs = baseUrlUs;
    }

    public String getBaseUrlEu() {
        return baseUrlEu;
    }

    public void setBaseUrlEu(String baseUrlEu) {
        this.baseUrlEu = baseUrlEu;
    }

    public String getBaseUrlGlobal() {
        return baseUrlGlobal;
    }

    public void setBaseUrlGlobal(String baseUrlGlobal) {
        this.baseUrlGlobal = baseUrlGlobal;
    }

    public String getApiRouter() {
        return apiRouter;
    }

    public void setApiRouter(String apiRouter) {
        this.apiRouter = apiRouter;
    }

    public int getTimeoutMs() {
        return timeoutMs;
    }

    public void setTimeoutMs(int timeoutMs) {
        this.timeoutMs = timeoutMs;
    }
}
