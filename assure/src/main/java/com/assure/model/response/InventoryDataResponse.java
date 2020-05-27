package com.assure.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryDataResponse {

    private Long id;
    private String name;
    private String brandId;
    private Long availableQuantity;
    private Long allocatedQuantity;
    private Long fulfilledQuantity;
}
