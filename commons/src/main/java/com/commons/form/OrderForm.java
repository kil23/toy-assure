package com.commons.form;

public class OrderForm {

    private long channelId;
    private long clientId;
    private long globalSkuId;
    private long customerId;
    private String channelOrderId;
    private String clientSkuId;
    private long orderedQuantity;
    private double sellingPricePerUnit;
    private long newFlag;

    public long getChannelId() {
        return channelId;
    }
    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public long getClientId() {
        return clientId;
    }
    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getGlobalSkuId() {
        return globalSkuId;
    }
    public void setGlobalSkuId(long globalSkuId) {
        this.globalSkuId = globalSkuId;
    }

    public long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getChannelOrderId() {
        return channelOrderId;
    }
    public void setChannelOrderId(String channelOrderId) {
        this.channelOrderId = channelOrderId;
    }

    public String getClientSkuId() {
        return clientSkuId;
    }
    public void setClientSkuId(String clientSkuId) {
        this.clientSkuId = clientSkuId;
    }

    public long getOrderedQuantity() {
        return orderedQuantity;
    }
    public void setOrderedQuantity(long orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    public double getSellingPricePerUnit() {
        return sellingPricePerUnit;
    }
    public void setSellingPricePerUnit(double sellingPricePerUnit) {
        this.sellingPricePerUnit = sellingPricePerUnit;
    }

    public long getNewFlag() {
        return newFlag;
    }
    public void setNewFlag(long newFlag) {
        this.newFlag = newFlag;
    }

}