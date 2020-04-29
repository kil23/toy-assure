package com.assure.model.response;

public class BinDataResponse {

    public long binId;
    public long inventoryCount;

    public long getInventoryCount() {
        return inventoryCount;
    }
    public void setInventoryCount(long inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public long getBinId() {
        return binId;
    }
    public void setBinId(long binId) {
        this.binId = binId;
    }
}
