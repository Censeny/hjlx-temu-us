package com.hjlx.temu.enums;

/**
 * Temu 开放平台区域。
 */
public enum TemuApiRegion {

    US("US", "美国"),
    EU("EU", "欧洲"),
    GLOBAL("GLOBAL", "全球");

    private final String code;
    private final String info;

    TemuApiRegion(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
