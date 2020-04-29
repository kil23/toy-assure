package com.commons.form;

public class ProductForm {

    private String clientSkuId;
    private String name;
    private String brandId;
    private double mrp;
    private String description;

    public String getClientSkuId() {
        return clientSkuId;
    }
    public void setClientSkuId(String clientSkuId) {
        this.clientSkuId = clientSkuId;
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

    public double getMrp() {
        return mrp;
    }
    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
