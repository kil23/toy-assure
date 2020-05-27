package com.assure.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BinInventoryUpdateForm {

    @NotNull(message = "BinSkuId cannot be null.")
    @Min(value = 1, message = "Min Bin SKU Id value should be 1.")
    private Long binSkuId;

    @NotNull(message = "BinId cannot be null.")
    @Min(value = 1, message = "Min Bin value should be 1.")
    private Long binId;

    @Min(value = 1, message = "Min Quantity value must be 1.")
    @Max(value = 1000, message = "Max Quantity value can be 1000")
    @NotNull(message = "New Quantity cannot be null.")
    private Long quantity;

    @Min(value = 1, message = "Min Quantity value must be 1.")
    @Max(value = 1000, message = "Max Quantity value can be 1000")
    @NotNull(message = "Older Quantity cannot be null.")
    private Long originalQuantity;
}
