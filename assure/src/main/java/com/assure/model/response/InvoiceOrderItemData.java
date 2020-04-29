package com.assure.model.response;

import java.io.Serializable;

public class InvoiceOrderItemData implements Serializable {
    
    private long orderId;
    private long fulfilledQuantity;
    private double sellingPricePerUnit;
    private String clientSkuId;
    private String productName;
    private String brandId;

    public long getOrderId() {
        return orderId;
    }
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getFulfilledQuantity() {
        return fulfilledQuantity;
    }
    public void setFulfilledQuantity(long fulfilledQuantity) {
        this.fulfilledQuantity = fulfilledQuantity;
    }

    public double getSellingPricePerUnit() {
        return sellingPricePerUnit;
    }
    public void setSellingPricePerUnit(double sellingPricePerUnit) {
        this.sellingPricePerUnit = sellingPricePerUnit;
    }

    public String getClientSkuId() {
        return clientSkuId;
    }
    public void setClientSkuId(String clientSkuId) {
        this.clientSkuId = clientSkuId;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrandId() {
        return brandId;
    }
    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
}
