package com.commons.response;

import com.commons.form.ProductForm;

public class ProductDataResponse extends ProductForm {

    private long globalSkuId;
    private long clientId;

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

}
