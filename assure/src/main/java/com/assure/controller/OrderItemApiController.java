package com.assure.controller;

import com.assure.model.response.OrderItemListingDataResponse;
import com.assure.service.OrderItemService;
import com.commons.response.OrderItemDataResponse;
import com.commons.service.ApiException;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderItemApiController {

    private static final Logger logger = Logger.getLogger(OrderItemApiController.class);

    @Autowired
    private OrderItemService itemService;

    @ApiOperation(value = "Gets list of all order items")
    @RequestMapping(value = "/api/{orderId}/item-list", method = RequestMethod.GET)
    public List<OrderItemListingDataResponse> getAllOrderItems(@PathVariable long orderId) throws ApiException {
        logger.info("get-all-order-items");
        return itemService.getAllOrderItemsListingData(orderId);
    }

    @ApiOperation(value = "Gets list of all order items for channel")
    @RequestMapping(value = "/api/{orderId}/order-list", method = RequestMethod.GET)
    public List<OrderItemDataResponse> getAllOrderItemsForChannel(@PathVariable("orderId") long orderId) throws ApiException {
        logger.info("get-all-order-items-for-channel");
        return itemService.getAllOrderItemsData(orderId);
    }
}
