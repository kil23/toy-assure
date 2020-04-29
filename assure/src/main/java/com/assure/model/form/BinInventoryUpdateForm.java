package com.assure.model.form;

public class BinInventoryUpdateForm {

    private long binSkuId;
    private long binId;
    private long quantity;
    private long originalQuantity;

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

    public long getQuantity() {
        return quantity;
    }
    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getOriginalQuantity() {
        return originalQuantity;
    }
    public void setOriginalQuantity(long originalQuantity) {
        this.originalQuantity = originalQuantity;
    }

}
