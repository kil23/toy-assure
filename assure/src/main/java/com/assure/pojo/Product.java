package com.assure.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ASSURE_PRODUCT", uniqueConstraints= @UniqueConstraint(columnNames = {"clientSkuId", "clientId"}))
public class Product extends AbstractPojo implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="s")
    @SequenceGenerator(name="s", initialValue=1, allocationSize=100)
    private Long globalSkuId;

    @Column(nullable = false)
    private String clientSkuId;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brandId;

    @Column(nullable = false)
    private Double mrp;

    @Column(columnDefinition = "TEXT")
    private String description;

    public Long getGlobalSkuId() {
        return globalSkuId;
    }

    public void setGlobalSkuId(Long globalSkuId) {
        this.globalSkuId = globalSkuId;
    }

    public String getClientSkuId() {
        return clientSkuId;
    }

    public void setClientSkuId(String clientSkuId) {
        this.clientSkuId = clientSkuId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
