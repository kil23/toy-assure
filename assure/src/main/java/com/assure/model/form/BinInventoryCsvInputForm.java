package com.assure.model.form;

public class BinInventoryCsvInputForm {

    private long binId;
    private String clientSkuId;
    private long quantity;
    private long clientId;

    public long getBinId() {
        return binId;
    }
    public void setBinId(long binSkuId) {
        this.binId = binSkuId;
    }

    public String getClientSkuId() {
        return clientSkuId;
    }
    public void setClientSkuId(String clientSkuId) {
        this.clientSkuId = clientSkuId;
    }

    public long getQuantity() {
        return quantity;
    }
    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getClientId() {
        return clientId;
    }
    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

}
