package com.channel.model.form;

import com.commons.form.ChannelForm;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
public class AddChannelForm {

    @NotEmpty(message = "Channel list cannot be empty.")
    @Valid
    @JsonProperty("channel")
    private List<ChannelForm> channelList;
}
