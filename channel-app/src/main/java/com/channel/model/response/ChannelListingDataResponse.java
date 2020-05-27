package com.channel.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelListingDataResponse {

    private Long id;
    private Long globalSkuId;
    private String name;
    private String brandId;
    private Long clientId;
    private String ChannelSkuId;
}
