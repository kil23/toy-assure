package com.commons.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter

public class ProductForm {
    @Pattern(regexp = "^[a-zA-Z]{2,15}[-\\s]?[a-zA-Z0-9]{1,10}$", message = "Invalid Client SKU ID. Value did not match the requirement.")
    @NotNull(message = "Client SKU ID cannot be null.")
    private String clientSkuId;

    @Pattern(regexp = "^[a-zA-Z]{2,15}[-\\s]?[a-zA-Z0-9]{1,10}$", message = "Invalid Product Name. String did not match the requirement.")
    @NotNull(message = "Product Name cannot be null.")
    private String name;

    @Pattern(regexp = "^[a-zA-Z]{2,15}[-\\s]?[a-zA-Z0-9]{1,10}$", message = "Invalid Brand ID. String did not match the requirement.")
    @NotNull(message = "Brand ID cannot be null.")
    private String brandId;

    @Min(value = (long)1.00, message = "Min MRP value should be 1.0")
    @Max(value = (long)30000.00, message = "Max MRP value should be 30000.")
    private Double mrp;

    @NotNull(message = "Client ID cannot be null.")
    @Min(value = 1, message = "Min Client ID value should be 1.")
    private Long clientId;

    private String description;

}
