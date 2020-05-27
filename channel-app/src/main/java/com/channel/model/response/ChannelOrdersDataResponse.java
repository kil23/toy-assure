package com.channel.model.response;

import com.commons.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelOrdersDataResponse {

    private String clientName;
    private String channelName;
    private Long customerName;
    private String channelOrderId;
    private String channelItem;
    private Long quantity;
    private OrderStatus status;
    private String invoicePdf;
}
