package com.commons.response;

import com.commons.enums.OrderStatus;

public class OrderDataResponse {

    private long orderId;
    private long clientId;
    private String clientName;
    private long customerId;
    private long channelId;
    private String channelName;
    private String channelOrderId;
    private OrderStatus status;

    public long getOrderId() {
        return orderId;
    }
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getClientId() {
        return clientId;
    }
    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getChannelId() {
        return channelId;
    }
    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelOrderId() {
        return channelOrderId;
    }
    public void setChannelOrderId(String channelOrderId) {
        this.channelOrderId = channelOrderId;
    }

    public OrderStatus getStatus() {
        return status;
    }
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

}
