package com.channel.model.response;

import com.channel.model.form.ChannelListingForm;

public class ChannelListingDataResponse extends ChannelListingForm {

    private Long listingId;
    private Long globalSkuId;

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    public Long getGlobalSkuId() {
        return globalSkuId;
    }

    public void setGlobalSkuId(Long globalSkuId) {
        this.globalSkuId = globalSkuId;
    }
}
