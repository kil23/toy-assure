package com.assure.model.response;

public class BinSkuDataResponse {

    private long binSkuId;
    private long binId;
    private long globalSkuId;
    private long quantity;

    public BinSkuDataResponse(long binSkuId, long binId, long globalSkuId, long quantity) {
        this.binSkuId = binSkuId;
        this.binId = binId;
        this.globalSkuId = globalSkuId;
        this.quantity = quantity;
    }

    public long getBinSkuId() {
        return binSkuId;
    }
    public void setBinSkuId(long binSkuId) {
        this.binSkuId = binSkuId;
    }

    public long getBinId() {
        return binId;
    }
    public void setBinId(long binSkuId) {
        this.binId = binSkuId;
    }

    public long getGlobalSkuId() {
        return globalSkuId;
    }
    public void setGlobalSkuId(long globalSkuId) {
        this.globalSkuId = globalSkuId;
    }

    public long getQuantity() {
        return quantity;
    }
    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

}
