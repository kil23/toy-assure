package com.assure.model.response;

public class OrderItemListingDataResponse {

    private long globalSkuId;
    private long orderedQuantity;
    private long allocatedQuantity;
    private long fulfilledQuantity;
    private double sellingPricePerUnit;
    private String clientSkuId;
    private String name;
    private String brandId;

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

    public long getAllocatedQuantity() {
        return allocatedQuantity;
    }
    public void setAllocatedQuantity(long allocatedQuantity) {
        this.allocatedQuantity = allocatedQuantity;
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

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getBrandId() {
        return brandId;
    }
    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }


}
