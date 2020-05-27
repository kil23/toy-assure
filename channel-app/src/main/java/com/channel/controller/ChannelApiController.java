package com.channel.controller;

import com.channel.dto.ChannelDto;
import com.channel.model.form.AddChannelForm;
import com.channel.validators.CreateChannelFormValidator;
import com.commons.form.ChannelForm;
import com.commons.response.ChannelDataResponse;
import com.commons.service.ApiException;
import com.commons.service.CustomValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api
@RestController
public class ChannelApiController {

    private static final Logger logger = Logger.getLogger(ChannelApiController.class);

    @Autowired private ChannelDto channelDto;
    @Autowired private CreateChannelFormValidator createValidator;

    @ApiOperation(value = "Adds a new channel")
    @RequestMapping(value = "/api/channel", method = RequestMethod.POST)
    public ResponseEntity<String> addChannelDetail(@Valid @RequestBody AddChannelForm form,
                                                   BindingResult result) {
        logger.info("add-channel");
        logger.info(result.getErrorCount());
        createValidator.validate(form, result);
        logger.info(result.getErrorCount());
        if(result.hasErrors()){
            throw new CustomValidationException(result);
        }
        channelDto.addChannel(form);
        return ResponseEntity.ok("Request Submitted successfully.");
    }

    @ApiOperation(value = "Updates a channel")
    @RequestMapping(value = "/api/channel/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateChannelDetail(@PathVariable("id") Long id, @Valid @RequestBody ChannelForm formData) throws ApiException {
        logger.info("update-channel");
        channelDto.updateChannel(formData, id);
        return ResponseEntity.ok("Request Submitted successfully.");
    }

    @ApiOperation(value = "Gets a channel by ID")
    @RequestMapping(value = "/api/channel/{id}", method = RequestMethod.GET)
    public ResponseEntity<ChannelDataResponse> getChannelDetail(@PathVariable Long id) throws ApiException {
        logger.info("get-channel");
        ChannelDataResponse response = channelDto.getChannelDetails(id);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Gets list of all channel details")
    @RequestMapping(value = "/api/channels/{clientId}", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDataResponse>> getAllChannelsByClient(@PathVariable Long clientId) throws ApiException {
        logger.info("get-All-channels-by-client-id");
        List<ChannelDataResponse> responses = channelDto.getAllChannelDetails(clientId);
        return ResponseEntity.ok(responses);
    }

    @ApiOperation(value = "Gets list of all channel details")
    @RequestMapping(value = "/api/channel", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDataResponse>> getAllChannels() throws ApiException {
        logger.info("get-All-channels");
        List<ChannelDataResponse> responses = channelDto.getAllChannelDetails();
        return ResponseEntity.ok(responses);
    }
}
