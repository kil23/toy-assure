package com.commons.response;

public class OrderItemDataResponse {

    private long orderId;
    private long globalSkuId;
    private long orderedQuantity;

    public long getOrderId() {
        return orderId;
    }
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getGlobalSkuId() {
        return globalSkuId;
    }
    public void setGlobalSkuId(long globalSkuId) {
        this.globalSkuId = globalSkuId;
    }

    public long getOrderedQuantity() {
        return orderedQuantity;
    }
    public void setOrderedQuantity(long orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

}
