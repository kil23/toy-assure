package com.assure.model.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class OrderItemForm {

//    @Pattern(regexp = "^[a-zA-Z]{2,15}[-\\s]?[a-zA-Z0-9]{1,10}$", message = "Invalid Client-SKU-ID. Value did not match the requirement.")
    @NotNull(message = "Client-SKU-ID cannot be null.")
    @JsonProperty(value = "clientSkuId")
    private String clientSkuId;

    @Min(value = 1, message = "Min Ordered Quantity value should be 1.")
    @Max(value = 50, message = "Max Ordered Quantity value should be 50.")
    @NotNull(message = "Ordered Quantity value cannot be null.")
    @JsonProperty(value = "orderedQuantity")
    private Long orderedQuantity;

    @Min(value = 1, message = "Min Selling Price Per Unit value should be 1.")
    @Max(value = 30000, message = "Max Selling Price Per Unit value should be 30000.")
    @NotNull(message = "Selling Price Per Unit cannot be null.")
    @JsonProperty(value = "sellingPricePerUnit")
    private Double sellingPricePerUnit;

}
