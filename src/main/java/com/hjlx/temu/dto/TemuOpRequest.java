package com.hjlx.temu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

/**
 * 便捷接口统一入参。各接口按需使用其中字段。
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemuOpRequest {

    /**
     * 店铺 access_token（获取 token 接口不需要）。
     */
    private String accessToken;

    /**
     * 区域：US / EU / GLOBAL，为空使用默认区域。
     */
    private String region;

    /**
     * 业务参数（部分接口使用，如订单列表、物流报价、下单等）。
     */
    private Map<String, Object> request;

    /**
     * 父订单号。
     */
    private String parentOrderSn;

    /**
     * 包裹号。
     */
    private String packageSn;

    /**
     * 语言（物流轨迹查询）。
     */
    private String language;

    /**
     * 授权 code（获取 access_token）。
     */
    private String code;

    /**
     * 文档类型（面单下载），如 SHIPPING_LABEL_PDF。
     */
    private String documentType;

    /**
     * 包裹号列表（面单下载、在线下单结果查询）。
     */
    private List<String> packageSnList;

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

    public Map<String, Object> getRequest() {
        return request;
    }

    public void setRequest(Map<String, Object> request) {
        this.request = request;
    }

    public String getParentOrderSn() {
        return parentOrderSn;
    }

    public void setParentOrderSn(String parentOrderSn) {
        this.parentOrderSn = parentOrderSn;
    }

    public String getPackageSn() {
        return packageSn;
    }

    public void setPackageSn(String packageSn) {
        this.packageSn = packageSn;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public List<String> getPackageSnList() {
        return packageSnList;
    }

    public void setPackageSnList(List<String> packageSnList) {
        this.packageSnList = packageSnList;
    }
}
