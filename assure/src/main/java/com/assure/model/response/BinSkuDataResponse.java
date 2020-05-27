package com.assure.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinSkuDataResponse {

    private Long id;
    private Long binId;
    private Long globalSkuId;
    private String name;
    private String brandId;
    private Long quantity;
}
