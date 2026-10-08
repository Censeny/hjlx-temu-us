package com.hjlx.temu.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Temu 开放平台签名工具。
 *
 * <p>签名算法（官方 MD5 变体）：</p>
 * <ol>
 *     <li>收集除 {@code sign} 外的所有参数（公共参数 + 业务参数），剔除空值；</li>
 *     <li>按参数名 ASCII 升序排序；</li>
 *     <li>按 {@code key + value} 直接拼接（无分隔符），
 *     其中 value 使用紧凑 JSON 序列化并去掉字符串两侧的引号；</li>
 *     <li>在拼接串首尾各拼接一次 app_secret；</li>
 *     <li>MD5 后转大写即为 sign。</li>
 * </ol>
 */
public final class TemuSignUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final char[] HEX = "0123456789ABCDEF".toCharArray();

    private TemuSignUtil() {
    }

    public static String sign(Map<String, Object> params, String appSecret) {
        String secret = appSecret == null ? "" : appSecret;

        TreeMap<String, Object> sorted = new TreeMap<>();
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if ("sign".equals(entry.getKey())) {
                    continue;
                }
                if (entry.getValue() == null) {
                    continue;
                }
                sorted.put(entry.getKey(), entry.getValue());
            }
        }

        StringBuilder raw = new StringBuilder();
        raw.append(secret);
        for (Map.Entry<String, Object> entry : sorted.entrySet()) {
            raw.append(entry.getKey()).append(toSignValue(entry.getValue()));
        }
        raw.append(secret);

        return md5Upper(raw.toString());
    }

    /**
     * 将参数值转换为参与签名的字符串：紧凑 JSON，并去掉字符串两侧的引号。
     */
    private static String toSignValue(Object value) {
        try {
            String json = MAPPER.writeValueAsString(value);
            if (json.length() >= 2 && json.charAt(0) == '"' && json.charAt(json.length() - 1) == '"') {
                return json.substring(1, json.length() - 1);
            }
            return json;
        } catch (JsonProcessingException e) {
            return String.valueOf(value);
        }
    }

    private static String md5Upper(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                sb.append(HEX[(b >> 4) & 0x0F]);
                sb.append(HEX[b & 0x0F]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 算法不可用", e);
        }
    }
}
