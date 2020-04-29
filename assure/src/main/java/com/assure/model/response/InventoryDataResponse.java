package com.assure.model.response;

public class InventoryDataResponse {

    private long inventoryId;
    private long globalSkuId;
    private long availableQuantity;
    private long allocatedQuantity;
    private long fulfilledQuantity;

    public InventoryDataResponse(long inventoryId, long globalSkuId, long availableQuantity, long allocatedQuantity, long fulfilledQuantity) {
        this.inventoryId = inventoryId;
        this.globalSkuId = globalSkuId;
        this.availableQuantity = availableQuantity;
        this.allocatedQuantity = allocatedQuantity;
        this.fulfilledQuantity = fulfilledQuantity;
    }

    public long getInventoryId() {
        return inventoryId;
    }
    public void setInventoryId(long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public long getGlobalSkuId() {
        return globalSkuId;
    }
    public void setGlobalSkuId(long globalSkuId) {
        this.globalSkuId = globalSkuId;
    }

    public long getAvailableQuantity() {
        return availableQuantity;
    }
    public void setAvailableQuantity(long availableQuantity) {
        this.availableQuantity = availableQuantity;
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

}
