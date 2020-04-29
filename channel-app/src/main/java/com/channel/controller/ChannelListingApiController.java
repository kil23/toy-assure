package com.channel.controller;

import com.channel.model.form.ChannelListingForm;
import com.channel.model.response.ChannelListingDataResponse;
import com.channel.service.ChannelListingService;
import com.channel.socket.Clients;
import com.commons.response.ClientDataResponse;
import com.commons.service.ApiException;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChannelListingApiController {

    @Autowired
    private ChannelListingService service;

    private static final Logger logger = Logger.getLogger(ChannelListingApiController.class);

    @ApiOperation(value = "Adds a new channel-listing")
    @RequestMapping(value = "/api/{channelId}/listings", method = RequestMethod.POST)
    public void addChannelListing(@PathVariable("channelId") long channelId, @RequestBody ChannelListingForm formData) throws ApiException {
        logger.info("add-channel-listing");
        service.insertListing(formData, channelId);
    }

    @ApiOperation(value = "Delete a channel-listing by Id")
    @RequestMapping(path = "/api/{channelId}/listings/{clientId}/{listingId}", method = RequestMethod.DELETE)
    public void deleteChannelListing(@PathVariable("listingId") long listingId) throws ApiException {
        logger.info("delete a channel-listing");
        service.deleteListing(listingId);
    }

    @ApiOperation(value = "Gets channel-listing by Id")
    @RequestMapping(value = "/api/{channelId}/listings/{listingId}", method = RequestMethod.GET)
    public ChannelListingDataResponse getChannelListing(@PathVariable("listingId") long listingId) throws ApiException {
        logger.info("get-channel-listing-by-listingId");
        return service.getListing(listingId);
    }

    @ApiOperation(value = "Gets list of all channel-listing for the channel's client")
    @RequestMapping(value = "/api/{channelId}/channel/{clientId}/listings", method = RequestMethod.GET)
    public List<ChannelListingDataResponse> getAllChannelListing(@PathVariable("channelId") long channelId, @PathVariable("clientId") long clientId) throws ApiException {
        logger.info("get-All-channel-listings-for-a-client");
        return service.getAllChannelWiseListing(channelId, clientId);
    }

    @ApiOperation(value = "Gets list of all channel-listing for the channel's client")
    @RequestMapping(value = "/api/{channelId}/listings", method = RequestMethod.GET)
    public List<ChannelListingDataResponse> getAllChannelListing(@PathVariable("channelId") long channelId) throws ApiException {
        logger.info("get-All-channel-listings-for-a-channels-client");
        return service.getAllChannelListingForChannel(channelId);
    }

    @ApiOperation(value = "Gets list of all channel-listing for the channel's client")
    @RequestMapping(value = "/api/{channelId}/listings-client", method = RequestMethod.GET)
    public List<ClientDataResponse> getAllChannelListingClients(@PathVariable("channelId") long channelId) throws ApiException {
        logger.info("get-All-channel-listings-for-a-channels-client");
        return service.getAllChannelListingClients(channelId);
    }

    @ApiOperation(value = "Gets list of all clients")
    @RequestMapping(value = "/api/client", method = RequestMethod.GET)
    public List<ClientDataResponse> getAllClients() throws ApiException {
        logger.info("get-All-client-for-channel");
        return Clients.getClientDetails();
    }
}
