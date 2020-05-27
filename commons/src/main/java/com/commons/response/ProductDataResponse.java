package com.commons.response;

import com.commons.form.ProductForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDataResponse extends ProductForm {

    private Long globalSkuId;
    private String clientName;

}
