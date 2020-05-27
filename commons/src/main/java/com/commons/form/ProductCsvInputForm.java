package com.commons.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
public class ProductCsvInputForm {

    @NotEmpty(message = "Product list cannot be empty.")
    @Valid
    @JsonProperty("product")
    private List<ProductForm> productList;

}
