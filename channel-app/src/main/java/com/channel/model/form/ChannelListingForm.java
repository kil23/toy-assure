package com.channel.model.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ChannelListingForm {

    @NotNull(message = "Client ID cannot be null.")
    @Min(value = 1, message = "Min Client ID value should be 1.")
    @JsonProperty(value = "clientId")
    private Long clientId;

    @Valid
    @JsonProperty(value = "listing")
    private List<ListingFormData> listingData;

}
