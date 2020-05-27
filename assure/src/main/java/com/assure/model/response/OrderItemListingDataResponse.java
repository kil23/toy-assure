package com.assure.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemListingDataResponse {

    private Long globalSkuId;
    private Long orderedQuantity;
    private Double sellingPricePerUnit;
    private String skuId;
    private String name;
    private String brandId;
}
