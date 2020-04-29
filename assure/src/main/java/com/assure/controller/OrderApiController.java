package com.assure.controller;

import com.assure.service.OrderService;
import com.commons.form.OrderForm;
import com.commons.response.OrderDataResponse;
import com.commons.service.ApiException;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    private static final Logger logger = Logger.getLogger(OrderApiController.class);

    @ApiOperation(value = "Adds a new order")
    @RequestMapping(value = "/api/order", method = RequestMethod.POST)
    public void addOrderDetail(@RequestBody OrderForm formData) throws ApiException {
        logger.info("add-new-order");
        logger.info("flag: "+formData.getNewFlag());
        orderService.insertOrder(formData);
    }

    @ApiOperation(value = "Allocate Orders")
    @RequestMapping(value = "/api/order/allocate", method = RequestMethod.GET)
    public List<OrderDataResponse> allocateOrder() throws ApiException {
        logger.info("allocate-orders");
        return orderService.allocateOrders();
    }

    @ApiOperation(value = "Fulfill Orders")
    @RequestMapping(value = "/api/order/fulfill", method = RequestMethod.GET)
    public List<OrderDataResponse> fulfillOrder() throws ApiException {
        logger.info("fulfill-allocated-orders");
        return orderService.fulfillOrders();
    }

    @ApiOperation(value = "Gets order details by channel-id")
    @RequestMapping(value = "/api/order/{channelId}", method = RequestMethod.GET)
    public List<OrderDataResponse> getOrderDetail(@PathVariable long channelId) throws ApiException {
        logger.info("get-order-list-for-channel");
        return orderService.getOrderDataForChannel(channelId);
    }

    @ApiOperation(value = "Gets list of all orders")
    @RequestMapping(value = "/api/order", method = RequestMethod.GET)
    public List<OrderDataResponse> getAllOrders() throws ApiException {
        logger.info("get-all-orders");
        return orderService.getAllOrderData();
    }

    @ApiOperation(value = "Gets list of all orders")
    @RequestMapping(value = "/api/order/1", method = RequestMethod.GET)
    public List<OrderDataResponse> getInternalChannelAllOrders() throws ApiException {
        logger.info("get-Internal-channel-all-orders");
        return orderService.getInternalChannelAllOrders();
    }
}
