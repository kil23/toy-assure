package com.assure.pojo;

import com.commons.enums.OrderStatus;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ASSURE_ORDER", uniqueConstraints= @UniqueConstraint(columnNames = {"channelId", "channelOrderId"}))
public class Order extends AbstractPojo implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private Long channelId;

    @Column(nullable = false)
    private String channelOrderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getChannelId() {
        return channelId;
    }
    public void setChannelId(Long channelId) {
        this.channelId = channelId;
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
