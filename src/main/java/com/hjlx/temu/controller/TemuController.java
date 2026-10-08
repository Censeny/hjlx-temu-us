package com.hjlx.temu.controller;

import com.hjlx.temu.client.TemuOpenApiClient;
import com.hjlx.temu.dto.TemuCallRequest;
import com.hjlx.temu.dto.TemuOpRequest;
import com.hjlx.temu.dto.TemuRouterRequest;
import com.hjlx.temu.enums.TemuApiType;
import com.hjlx.temu.exception.BusinessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Temu OpenAPI 海外网关。
 *
 * <p>国内业务方携带请求头 {@code x-api-key} 调用，本服务负责使用
 * app_key / app_secret 完成 Temu 官方签名并转发。</p>
 */
@RestController
@RequestMapping("/api/temu")
public class TemuController {

    private final TemuOpenApiClient client;

    public TemuController(TemuOpenApiClient client) {
        this.client = client;
    }

    // ==================== 通用调用 ====================

    /**
     * 兼容启邦 router 的通用入口（国内 erp-backend 可直接切换 base-url 使用）。
     */
    @PostMapping("/openapi/router")
    public Map<String, Object> router(@RequestBody TemuRouterRequest req) {
        return client.call(req.getType(), req.getAccessToken(), req.getRequest(), req.getRegion());
    }

    /**
     * 通用调用入口。
     */
    @PostMapping("/call")
    public Map<String, Object> call(@RequestBody TemuCallRequest req) {
        return client.call(req.getType(), req.getAccessToken(), req.getParams(), req.getRegion());
    }

    // ==================== 授权 ====================

    /**
     * 通过授权 code 获取 access_token。
     */
    @PostMapping("/token/create")
    public Map<String, Object> tokenCreate(@RequestBody TemuOpRequest req) {
        Map<String, Object> params = new HashMap<>();
        params.put("code", req.getCode());
        return call(TemuApiType.BG_OPEN_ACCESSTOKEN_CREATE, req, params);
    }

    /**
     * 查询 access_token 信息。
     */
    @PostMapping("/token/info")
    public Map<String, Object> tokenInfo(@RequestBody TemuOpRequest req) {
        return call(TemuApiType.BG_OPEN_ACCESSTOKEN_INFO_GET, req, new HashMap<>());
    }

    // ==================== 订单 ====================

    /**
     * 查询订单列表。
     */
    @PostMapping("/order/list")
    public Map<String, Object> orderList(@RequestBody TemuOpRequest req) {
        return call(TemuApiType.BG_ORDER_LIST_V2_GET, req, req.getRequest());
    }

    /**
     * 查询订单详情。
     */
    @PostMapping("/order/detail")
    public Map<String, Object> orderDetail(@RequestBody TemuOpRequest req) {
        return call(TemuApiType.BG_ORDER_DETAIL_V2_GET, req, parentOrderSn(req));
    }

    /**
     * 查询脱敏收货信息。
     */
    @PostMapping("/order/shippinginfo")
    public Map<String, Object> orderShippingInfo(@RequestBody TemuOpRequest req) {
        return call(TemuApiType.BG_ORDER_SHIPPINGINFO_V2_GET, req, parentOrderSn(req));
    }

    /**
     * 查询解密收货信息。
     */
    @PostMapping("/order/decryptshippinginfo")
    public Map<String, Object> orderDecryptShippingInfo(@RequestBody TemuOpRequest req) {
        return call(TemuApiType.BG_ORDER_DECRYPTSHIPPINGINFO_GET, req, parentOrderSn(req));
    }

    // ==================== 物流 ====================

    /**
     * 查询可用发货仓库。
     */
    @PostMapping("/logistics/warehouse/list")
    public Map<String, Object> warehouseList(@RequestBody TemuOpRequest req) {
        return call(TemuApiType.BG_LOGISTICS_WAREHOUSE_LIST_GET, req, new HashMap<>());
    }

    /**
     * 查询可用物流服务和报价。
     */
    @PostMapping("/logistics/shippingservices")
    public Map<String, Object> shippingServices(@RequestBody TemuOpRequest req) {
        return call(TemuApiType.BG_LOGISTICS_SHIPPINGSERVICES, req, req.getRequest());
    }

    /**
     * 在线下单创建物流包裹和面单。
     */
    @PostMapping("/logistics/shipment/create")
    public Map<String, Object> shipmentCreate(@RequestBody TemuOpRequest req) {
        return call(TemuApiType.BG_LOGISTICS_SHIPMENT_CREATE, req, req.getRequest());
    }

    /**
     * 查询在线下单结果。
     */
    @PostMapping("/logistics/shipment/result")
    public Map<String, Object> shipmentResult(@RequestBody TemuOpRequest req) {
        Map<String, Object> params = new HashMap<>();
        params.put("packageSnList", req.getPackageSnList());
        return call(TemuApiType.BG_LOGISTICS_SHIPMENT_RESULT_GET, req, params);
    }

    /**
     * 查询物流面单状态和包裹信息。
     */
    @PostMapping("/logistics/label/list")
    public Map<String, Object> labelList(@RequestBody TemuOpRequest req) {
        return call(TemuApiType.TEMU_LOGISTICS_LABEL_LIST_GET, req, req.getRequest());
    }

    /**
     * 获取面单下载地址。
     */
    @PostMapping("/logistics/shipment/document")
    public Map<String, Object> shipmentDocument(@RequestBody TemuOpRequest req) {
        return call(TemuApiType.BG_LOGISTICS_SHIPMENT_DOCUMENT_GET, req, req.getRequest());
    }

    /**
     * 查询物流轨迹。
     */
    @PostMapping("/tracking/info")
    public Map<String, Object> trackingInfo(@RequestBody TemuOpRequest req) {
        Map<String, Object> params = new HashMap<>();
        params.put("packageSn", req.getPackageSn());
        params.put("language", req.getLanguage() != null ? req.getLanguage() : "zh");
        return call(TemuApiType.TEMU_TRACK_TRACKINGINFO_GET, req, params);
    }

    // ==================== 文档下载 ====================

    /**
     * 下载面单文件（返回二进制）。请求体示例：
     * <pre>
     * { "accessToken": "xxx", "region": "US",
     *   "documentType": "SHIPPING_LABEL_PDF", "packageSnList": ["PK-xxx"] }
     * </pre>
     */
    @PostMapping("/document/download")
    public ResponseEntity<byte[]> documentDownload(@RequestBody TemuOpRequest req) {
        if (req.getPackageSnList() == null || req.getPackageSnList().isEmpty()) {
            throw new BusinessException("packageSnList 不能为空");
        }
        Map<String, Object> params = new HashMap<>();
        if (req.getDocumentType() != null && !req.getDocumentType().isEmpty()) {
            params.put("documentType", req.getDocumentType());
        }
        params.put("packageSnList", req.getPackageSnList());

        byte[] bytes = client.documentDownload(req.getAccessToken(), params, req.getRegion());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "temu-document.pdf");
        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    // ==================== 内部工具 ====================

    private Map<String, Object> call(TemuApiType type, TemuOpRequest req, Map<String, Object> params) {
        return client.call(type.getCode(), req.getAccessToken(), params, req.getRegion());
    }

    private Map<String, Object> parentOrderSn(TemuOpRequest req) {
        Map<String, Object> params = new HashMap<>();
        params.put("parentOrderSn", req.getParentOrderSn());
        return params;
    }
}
