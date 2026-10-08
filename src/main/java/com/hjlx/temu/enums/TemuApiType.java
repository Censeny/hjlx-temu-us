package com.hjlx.temu.enums;

/**
 * Temu 开放平台接口方法名。
 */
public enum TemuApiType {

    BG_OPEN_ACCESSTOKEN_CREATE("bg.open.accesstoken.create", "店铺授权"),
    BG_OPEN_ACCESSTOKEN_INFO_GET("bg.open.accesstoken.info.get", "查询 Temu access token 信息"),
    BG_ORDER_LIST_V2_GET("bg.order.list.v2.get", "查询订单列表"),
    BG_ORDER_SHIPPINGINFO_V2_GET("bg.order.shippinginfo.v2.get", "查询脱敏订单收货地址"),
    BG_ORDER_DECRYPTSHIPPINGINFO_GET("bg.order.decryptshippinginfo.get", "查询订单解密收货地址"),
    BG_LOGISTICS_WAREHOUSE_LIST_GET("bg.logistics.warehouse.list.get", "查询可发货仓库"),
    BG_LOGISTICS_SHIPPINGSERVICES("bg.logistics.shippingservices.get", "查询可用物流服务和报价"),
    BG_LOGISTICS_SHIPMENT_RESULT_GET("bg.logistics.shipment.result.get", "查询在线下单结果"),
    BG_ORDER_DETAIL_V2_GET("bg.order.detail.v2.get", "查询订单详情"),
    BG_LOGISTICS_SHIPMENT_CREATE("bg.logistics.shipment.create", "在线下单创建 Temu 物流包裹和面单"),
    BG_LOGISTICS_SHIPMENT_UPDATE("bg.logistics.shipment.update", "更新发货信息"),
    TEMU_LOGISTICS_LABEL_LIST_GET("temu.logistics.label.list.get", "查询物流面单状态和包裹信息"),
    BG_LOGISTICS_SHIPMENT_DOCUMENT_GET("bg.logistics.shipment.document.get", "获取面单下载地址"),
    TEMU_TRACK_TRACKINGINFO_GET("temu.track.trackinginfo.get", "查询物流轨迹");

    private final String code;
    private final String info;

    TemuApiType(String code, String info) {
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
