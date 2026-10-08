package com.hjlx.temu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * 兼容启邦（qibang）router 的入参结构，便于国内 erp-backend 无缝切换。
 *
 * <pre>
 * {
 *   "type": "bg.order.list.v2.get",
 *   "region": "US",
 *   "access_token": "xxx",
 *   "request": { ... 业务参数 ... }
 * }
 * </pre>
 *
 * <p>client / token 字段仅为兼容旧调用方，本服务使用请求头 x-api-key 鉴权。</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemuRouterRequest {

    private String type;

    private String region;

    @JsonProperty("access_token")
    private String accessToken;

    private Map<String, Object> request;

    private String client;

    private String token;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Map<String, Object> getRequest() {
        return request;
    }

    public void setRequest(Map<String, Object> request) {
        this.request = request;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
