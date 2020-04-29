package com.assure.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ASSURE_ORDER_ITEM",
        uniqueConstraints= { @UniqueConstraint(columnNames = {"channelId", "channelOrderId"}),
                            @UniqueConstraint(columnNames = {"orderId", "globalSkuId" })
        })
public class OrderItem extends AbstractPojo implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long globalSkuId;

    @Column(nullable = false)
    private Long orderedQuantity;

    private Long allocatedQuantity;

    private Long fulfilledQuantity;

    @Column(nullable = false)
    private Double sellingPricePerUnit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getGlobalSkuId() {
        return globalSkuId;
    }

    public void setGlobalSkuId(Long globalSkuId) {
        this.globalSkuId = globalSkuId;
    }

    public Long getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(Long orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    public Long getAllocatedQuantity() {
        return allocatedQuantity;
    }

    public void setAllocatedQuantity(Long allocatedQuantity) {
        this.allocatedQuantity = allocatedQuantity;
    }

    public Long getFulfilledQuantity() {
        return fulfilledQuantity;
    }

    public void setFulfilledQuantity(Long fulfilledQuantity) {
        this.fulfilledQuantity = fulfilledQuantity;
    }

    public Double getSellingPricePerUnit() {
        return sellingPricePerUnit;
    }

    public void setSellingPricePerUnit(Double sellingPricePerUnit) {
        this.sellingPricePerUnit = sellingPricePerUnit;
    }
}
