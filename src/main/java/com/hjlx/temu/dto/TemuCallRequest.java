package com.hjlx.temu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

/**
 * 通用调用入参。
 *
 * <pre>
 * {
 *   "type": "bg.order.list.v2.get",
 *   "accessToken": "xxx",
 *   "region": "US",
 *   "params": { ... 业务参数 ... }
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemuCallRequest {

    private String type;

    private String accessToken;

    private String region;

    private Map<String, Object> params;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
