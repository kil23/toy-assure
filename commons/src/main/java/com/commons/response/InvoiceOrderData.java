package com.commons.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class InvoiceOrderData implements Serializable {

    private Long orderId;
    private String clientName;
    private String customerName;
    private String channelOrderId;
    private Long channelId;
}
