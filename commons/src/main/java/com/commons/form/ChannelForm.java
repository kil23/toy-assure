package com.commons.form;

import com.commons.enums.InvoiceType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class ChannelForm {

    @Pattern(regexp = "^[a-zA-Z]{2,15}[-\\s]?[a-zA-Z0-9]{1,10}$")
    @NotNull(message = "Channel Name cannot be empty or null.")
    @JsonProperty(value = "name")
    private String name;

    @NotNull(message = "Channel Type cannot be empty or null.")
    @JsonProperty(value = "invoiceType")
    private InvoiceType invoiceType;

}
