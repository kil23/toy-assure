package com.commons.response;

import com.commons.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDataResponse {

    private Long id;
    private Long clientId;
    private String clientName;
    private String customerName;
    private Long channelId;
    private String channelName;
    private String channelOrderId;
    private OrderStatus status;
}
