package com.channel.model.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ListingFormData {

    @NotNull(message = "Channel SKU ID cannot be null.")
    @JsonProperty(value = "channelSkuId")
    private String channelSkuId;

    @NotNull(message = "Client SKU ID cannot be null.")
    @JsonProperty(value = "clientSkuId")
    private String clientSkuId;
}
