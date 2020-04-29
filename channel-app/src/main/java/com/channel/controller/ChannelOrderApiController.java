package com.channel.controller;

import com.channel.model.response.ChannelOrdersDataResponse;
import com.channel.service.ChannelService;
import com.channel.socket.Order;
import com.commons.form.OrderForm;
import com.commons.service.ApiException;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChannelOrderApiController {

    private static final Logger logger = Logger.getLogger(ChannelOrderApiController.class);

    @Autowired
    private ChannelService channelService;

    @ApiOperation(value = "Gets list of all order items")
    @RequestMapping(value = "/api/channel/order-lists", method = RequestMethod.GET)
    public List<ChannelOrdersDataResponse> getAllChannelOrderListing() throws ApiException {
        logger.info("get-All-orders");
        return channelService.getAllChannelOrderList();
    }

    @ApiOperation(value = "Adds a new channel")
    @RequestMapping(value = "/api/order", method = RequestMethod.POST)
    public void createChannelOrders(@RequestBody OrderForm formData) throws ApiException {
        logger.info("add-channel");
        Order.postOrderDetails(formData);
    }

}
