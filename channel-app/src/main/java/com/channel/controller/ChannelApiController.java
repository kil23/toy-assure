package com.channel.controller;

import com.channel.service.ChannelService;
import com.commons.form.ChannelForm;
import com.commons.response.ChannelDataResponse;
import com.commons.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class ChannelApiController {

    private static final Logger logger = Logger.getLogger(ChannelApiController.class);

    @Autowired
    private ChannelService channelService;

    @ApiOperation(value = "Adds a new channel")
    @RequestMapping(value = "/api/channel", method = RequestMethod.POST)
    public void addChannelDetail(@RequestBody ChannelForm formData) throws ApiException {
        logger.info("add-channel");
        channelService.addChannel(formData);
    }

    @ApiOperation(value = "Updates a channel")
    @RequestMapping(value = "/api/channel/{id}", method = RequestMethod.PUT)
    public void updateChannelDetail(@PathVariable("id") long id, @RequestBody ChannelForm formData) throws ApiException {
        logger.info("update-channel");
        channelService.updateChannel(formData, id);
    }

    @ApiOperation(value = "Gets a channel by ID")
    @RequestMapping(value = "/api/channel/{id}", method = RequestMethod.GET)
    public ChannelDataResponse getChannelDetail(@PathVariable long id) throws ApiException {
        logger.info("get-channel");
        return channelService.getChannelDetails(id);
    }

    @ApiOperation(value = "Gets list of all channel details")
    @RequestMapping(value = "/api/channels/{clientId}", method = RequestMethod.GET)
    public List<ChannelDataResponse> getAllChannelsByClient(@PathVariable long clientId) throws ApiException {
        logger.info("get-All-channels-by-client-id");
        return channelService.getAllChannelDetails(clientId);
    }

    @ApiOperation(value = "Gets list of all channel details")
    @RequestMapping(value = "/api/channel", method = RequestMethod.GET)
    public List<ChannelDataResponse> getAllChannels() throws ApiException {
        logger.info("get-All-channels");
        return channelService.getAllChannelDetails();
    }
}
