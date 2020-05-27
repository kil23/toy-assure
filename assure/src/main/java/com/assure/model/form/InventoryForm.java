package com.assure.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class InventoryForm {

    @NotNull(message = "BinId cannot be null.")
    @Min(value = 1, message = "Min Bin value should be 1.")
    private Long binId;

    @Pattern(regexp = "^[a-zA-Z]{2,15}[-\\s]?[a-zA-Z0-9]{1,10}$", message = "Invalid Client-SKU-Id. Value did not match the requirement.")
    @NotNull(message = "Client-SKU-Id cannot be null.")
    private String clientSkuId;

    @Min(value = 1, message = "Min Quantity value should be 1.")
    @Max(value = 1000, message = "Max Quantity value should be 1000")
    @NotNull(message = "Quantity cannot be null.")
    private Long quantity;
}
