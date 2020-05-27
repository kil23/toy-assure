package com.commons.form;

import com.commons.enums.ClientType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class ClientForm {

    @Pattern(regexp = "^[a-zA-Z]{2,15}[-\\s]?[a-zA-Z0-9]{1,10}$", message = "Invalid Client Name. Value did not match the requirement.")
    @NotNull(message = "Client Name cannot be null.")
    @JsonProperty(value = "name")
    private String name;

    @NotNull(message = "Client Type cannot be null.")
    @JsonProperty(value = "type")
    private ClientType type;
}
