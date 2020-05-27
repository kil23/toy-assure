package com.commons.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class InvoiceOrderItemData implements Serializable {

    private Long orderedQuantity;
    private Double sellingPricePerUnit;
    private String productName;
    private String brandId;
}
