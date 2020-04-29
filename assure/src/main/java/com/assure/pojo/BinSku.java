package com.assure.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ASSURE_BIN_SKU", uniqueConstraints= @UniqueConstraint(columnNames = {"binId", "globalSkuId"}))
public class BinSku extends AbstractPojo implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long binId;

    @Column(nullable = false)
    private Long globalSkuId;

    private Long quantity;

    public BinSku(Long id, Long binId, Long globalSkuId, Long quantity) {
        this.id = id;
        this.binId = binId;
        this.globalSkuId = globalSkuId;
        this.quantity = quantity;
    }

    public BinSku(Long binId, Long globalSkuId, Long quantity) {
        this.binId = binId;
        this.globalSkuId = globalSkuId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getBinId() {
        return binId;
    }
    public void setBinId(Long binId) {
        this.binId = binId;
    }

    public Long getGlobalSkuId() {
        return globalSkuId;
    }
    public void setGlobalSkuId(Long globalSkuId) {
        this.globalSkuId = globalSkuId;
    }

    public Long getQuantity() {
        return quantity;
    }
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
