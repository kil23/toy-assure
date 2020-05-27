package com.commons.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ChannelOrderForm {

    @NotNull(message = "Channel ID cannot be null.")
    @Min(value = 1, message = "Min Channel ID value should be 1.")
    @JsonProperty(value = "channelId")
    private Long channelId;

    @NotNull(message = "Client ID cannot be null.")
    @Min(value = 1, message = "Min Client ID value should be 1.")
    @JsonProperty(value = "clientId")
    private Long clientId;

    @NotNull(message = "Customer ID cannot be null.")
    @Min(value = 1, message = "Min Customer ID value should be 1.")
    private Long customerId;

    @NotNull(message = "Channel Order ID cannot be null.")
    @JsonProperty(value = "channelOrderId")
    private String channelOrderId;

    @NotEmpty(message = "Order Item list cannot be empty.")
    @Valid
    @JsonProperty("order")
    private List<ChannelOrderItemForm> channelOrderItemFormList;
}