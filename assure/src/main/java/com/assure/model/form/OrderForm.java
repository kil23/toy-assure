package com.assure.model.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class OrderForm {

    @NotNull(message = "Channel ID cannot be null.")
    @Min(value = 1, message = "Min Channel ID value should be 1.")
    private Long channelId;

    @NotNull(message = "Client ID cannot be null.")
    @Min(value = 1, message = "Min Client ID value should be 1.")
    private Long clientId;

    @NotNull(message = "Customer ID cannot be null.")
    @Min(value = 1, message = "Min Customer ID value should be 1.")
    private Long customerId;

//    @Pattern(regexp = "^[a-zA-Z]{2,15}[-\\s]?[a-zA-Z0-9]{1,10}$", message = "Invalid Channel Order ID. Value did not match the requirement.")
    @NotNull(message = "Channel Order ID cannot be null.")
    private String channelOrderId;

    @NotEmpty(message = "Order-Item list cannot be empty.")
    @JsonProperty("order")
    @Valid
    private List<OrderItemForm> orderItemFormList;
}