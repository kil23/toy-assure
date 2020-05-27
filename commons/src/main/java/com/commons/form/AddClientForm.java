package com.commons.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
public class AddClientForm {

    @NotEmpty(message = "Client list cannot be empty.")
    @Valid
    @JsonProperty("client")
    private List<ClientForm> clientList;
}
