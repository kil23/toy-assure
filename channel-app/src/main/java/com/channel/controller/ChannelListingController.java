package com.channel.controller;

import com.channel.dto.ChannelListingDto;
import com.channel.model.form.ChannelListingForm;
import com.channel.model.response.ChannelListingDataResponse;
import com.channel.validators.CreateChannelListingFormValidator;
import com.commons.response.ClientDataResponse;
import com.commons.service.CustomValidationException;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ChannelListingController {

    @Autowired private ChannelListingDto channelListingDto;
    @Autowired private CreateChannelListingFormValidator channelListingValidator;

    private static final Logger logger = Logger.getLogger(ChannelListingController.class);

    @ApiOperation(value = "Adds a new channel-listing")
    @RequestMapping(value = "/api/{channelId}/listing", method = RequestMethod.POST)
    public ResponseEntity<String> addChannelListing(@PathVariable("channelId") Long channelId,
                                                    @Valid @RequestBody ChannelListingForm formData,
                                                    BindingResult result) {
        logger.info("add-channel-listing");
        logger.info(result.getErrorCount());
        channelListingValidator.validate(formData, result);
        logger.info(result.getErrorCount());
        if(result.hasErrors()){
            throw new CustomValidationException(result);
        }
        channelListingDto.addListing(formData, channelId);
        return ResponseEntity.ok("Request Submitted successfully.");
    }

    @ApiOperation(value = "Delete a channel-listing by Id")
    @RequestMapping(path = "/api/{channelId}/listing/{clientId}/{listingId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteChannelListing(@PathVariable("listingId") Long listingId) {
        logger.info("delete a channel-listing");
        channelListingDto.deleteListing(listingId);
        return ResponseEntity.ok("Request Submitted successfully.");
    }

//    @ApiOperation(value = "Gets channel-listing by Id")
//    @RequestMapping(value = "/api/{channelId}/listings/{listingId}", method = RequestMethod.GET)
//    public ResponseEntity<ChannelListingDataResponse> getChannelListing(@PathVariable("listingId") Long listingId) throws ApiException {
//        logger.info("get-channel-listing-by-listingId");
//        ChannelListingDataResponse response = channelListingDto.getListing(listingId);
//        return ResponseEntity.ok(response);
//    }

    @ApiOperation(value = "Gets list of all channel-listing for the channel's client")
    @RequestMapping(value = "/api/{channelId}/listing/{clientId}", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelListingDataResponse>> getAllChannelListing(
                                        @PathVariable("channelId") Long channelId,
                                        @PathVariable("clientId") long clientId) {
        logger.info("get-All-channel-listings-for-a-client");
        List<ChannelListingDataResponse> responses = channelListingDto.getAllChannelWiseListingForClient(channelId, clientId);
        return ResponseEntity.ok(responses);
    }

    @ApiOperation(value = "Gets list of all channel-listing for the channel")
    @RequestMapping(value = "/api/{channelId}/listing", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelListingDataResponse>> getAllChannelListing(
                                        @PathVariable("channelId") Long channelId) {
        logger.info("get-All-channel-listings-for-a-channel");
        List<ChannelListingDataResponse> responses = channelListingDto.getAllChannelListingForChannel(channelId);
        return ResponseEntity.ok(responses);
    }

    @ApiOperation(value = "Gets list of all channel-listing for the channel's client")
    @RequestMapping(value = "/api/{channelId}/listing-client", method = RequestMethod.GET)
    public ResponseEntity<List<ClientDataResponse>> getAllChannelListingClients(
                                        @PathVariable("channelId") Long channelId) {
        logger.info("get-All-channel-listings-for-a-channels-client");
        List<ClientDataResponse> responses = channelListingDto.getAllChannelListingClients(channelId);
        return ResponseEntity.ok(responses);
    }
}
