package com.commons.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChannelOrderItemForm {

    @NotNull(message = "Global SKU ID cannot be null.")
    @JsonProperty(value = "globalSkuId")
    private Long globalSkuId;

    @Min(value = 1, message = "Min Ordered Quantity value should be 1.")
    @Max(value = 50, message = "Max Ordered Quantity value should be 50.")
    @NotNull(message = "Ordered Quantity value cannot be null.")
    @JsonProperty(value = "orderedQuantity")
    private Long orderedQuantity;
}
