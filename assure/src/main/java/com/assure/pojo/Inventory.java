package com.assure.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ASSURE_INVENTORY", uniqueConstraints= @UniqueConstraint(columnNames = {"globalSkuId"}))
public class Inventory extends AbstractPojo implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long globalSkuId;

    private Long availableQuantity;

    private Long allocatedQuantity;

    private Long fulfilledQuantity;

    public Inventory(Long id, Long globalSkuId, Long availableQuantity) {
        this.id = id;
        this.globalSkuId = globalSkuId;
        this.availableQuantity = availableQuantity;
    }

    public Inventory(Long globalSkuId, Long availableQuantity) {
        this.globalSkuId = globalSkuId;
        this.availableQuantity = availableQuantity;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getGlobalSkuId() {
        return globalSkuId;
    }
    public void setGlobalSkuId(Long globalSkuId) {
        this.globalSkuId = globalSkuId;
    }

    public Long getAvailableQuantity() {
        return availableQuantity;
    }
    public void setAvailableQuantity(Long availableQuantity) {
        this.availableQuantity = availableQuantity;
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
}
