package com.channel.model.form;

public class ChannelListingForm {

    private Long clientId;
    private String channelSkuId;
    private String clientSkuId;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getChannelSkuId() {
        return channelSkuId;
    }
    public void setChannelSkuId(String channelSkuId) {
        this.channelSkuId = channelSkuId;
    }

    public String getClientSkuId() {
        return clientSkuId;
    }
    public void setClientSkuId(String clientSkuId) {
        this.clientSkuId = clientSkuId;
    }

}
