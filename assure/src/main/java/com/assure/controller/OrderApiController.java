package com.assure.controller;

import com.assure.dto.OrderDto;
import com.assure.model.form.OrderForm;
import com.commons.response.*;
import com.assure.pojo.Order;
import com.assure.validators.ChannelOrderFormValidator;
import com.assure.validators.OrderFormValidator;
import com.commons.form.ChannelOrderForm;
import com.commons.service.ApiException;
import com.commons.service.CustomValidationException;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
public class OrderApiController {

    @Autowired private OrderDto orderDto;
    @Autowired private OrderFormValidator orderFormValidator;
    @Autowired private ChannelOrderFormValidator channelOrderFormValidator;

    private static final Logger logger = Logger.getLogger(OrderApiController.class);

    @ApiOperation(value = "Adds a new order")
    @RequestMapping(value = "/api/order", method = RequestMethod.POST)
    public ResponseEntity<String> addOrderDetail(@Valid @RequestBody OrderForm formData,
                                                 BindingResult result) {
        logger.info("add-new-order");
        orderFormValidator.validate(formData, result);
        if (result.hasErrors()) {
            throw new CustomValidationException(result);
        }
        orderDto.addOrder(formData);
        return ResponseEntity.ok("Request submitted successfully.");
    }

    @ApiOperation(value = "Adds a new order")
    @RequestMapping(value = "/api/channel/order", method = RequestMethod.POST)
    public ResponseEntity<String> addChannelOrderDetail(@Valid @RequestBody ChannelOrderForm formData,
                                                        BindingResult result) {
        logger.info("add-new-channel-order");
        logger.info("ChannelId " +formData.getChannelId());
        logger.info("Channel Order Id " +formData.getChannelOrderId());
        logger.info("Client Id " +formData.getClientId());
        logger.info("Customer Id " +formData.getCustomerId());
        channelOrderFormValidator.validate(formData, result);
        if (result.hasErrors()) {
            logger.info(result.getErrorCount());
            logger.info(result.getFieldErrors().get(0).getDefaultMessage());
            throw new CustomValidationException(result);
        }
        orderDto.addChannelOrder(formData);
        return ResponseEntity.ok("Request submitted successfully.");
    }

    @ApiOperation(value = "Allocate Orders")
    @RequestMapping(value = "/api/order/allocate", method = RequestMethod.GET)
    public ResponseEntity<List<OrderDataResponse>> allocateOrder() {
        logger.info("allocate-orders");
        List<OrderDataResponse> responses = orderDto.allocateOrders();
        return ResponseEntity.ok(responses);
    }

    @ApiOperation(value = "Fulfill Orders")
    @RequestMapping(value = "/api/order/{orderId}/fulfill/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<List<OrderDataResponse>> fulfillOrder(@PathVariable("orderId") Long orderId,
                                                                @PathVariable("channelId") Long channelId) {
        logger.info("fulfill-allocated-orders");
        Order order = orderDto.getOrderDetails(orderId);
        if(order == null) {
            logger.info("No Order found with id : "+orderId);
            throw new ApiException("No Order found with id : "+orderId);
        }
        ChannelDataResponse channel = orderDto.getChannelDetails(channelId);
        if(channel == null) {
            logger.info("No Channel found with id : "+channelId);
            throw new ApiException("No Channel found with id : "+channelId);
        }
        List<OrderDataResponse> responses = orderDto.fulfillOrders(order, channel);
        return ResponseEntity.ok(responses);
    }

    @ApiOperation(value = "Gets order details by channel-id")
    @RequestMapping(value = "/api/order/{channelId}", method = RequestMethod.GET)
    public ResponseEntity<List<OrderDataResponse>> getOrderDetail(@PathVariable("channelId") long channelId) {
        logger.info("get-order-list-for-channel");
        List<OrderDataResponse> responses = orderDto.getOrderDataForChannel(channelId);
        return ResponseEntity.ok(responses);
    }

    @ApiOperation(value = "Gets order details by client-id")
    @RequestMapping(value = "/api/order/client/{clientId}", method = RequestMethod.GET)
    public ResponseEntity<List<OrderDataResponse>> getAllOrderDetail(@PathVariable("clientId") long clientId) {
        logger.info("get-order-list-for-client");
        List<OrderDataResponse> responses = orderDto.getOrderDataForClient(clientId);
        return ResponseEntity.ok(responses);
    }

    @ApiOperation(value = "Gets order details by customer-id")
    @RequestMapping(value = "/api/order/customer/{customerId}", method = RequestMethod.GET)
    public ResponseEntity<List<OrderDataResponse>> getAllOrderDetails(@PathVariable("customerId") long customerId) {
        logger.info("get-order-list-for-customer");
        List<OrderDataResponse> responses = orderDto.getOrderDataForCustomer(customerId);
        return ResponseEntity.ok(responses);
    }

    @ApiOperation(value = "Gets list of all orders")
    @RequestMapping(value = "/api/order", method = RequestMethod.GET)
    public ResponseEntity<List<OrderDataResponse>> getAllOrders() {
        logger.info("get-all-orders");
        List<OrderDataResponse> responses = orderDto.getAllOrderData();
        return ResponseEntity.ok(responses);
    }

//    @ApiOperation(value = "Gets list of all orders")
//    @RequestMapping(value = "/api/order/1", method = RequestMethod.GET)
//    public ResponseEntity<List<OrderDataResponse>> getInternalChannelAllOrders() {
//        logger.info("get-Internal-channel-all-orders");
//        List<OrderDataResponse> responses = orderDto.getInternalChannelAllOrders();
//        return ResponseEntity.ok(responses);
//    }

    @ApiOperation(value = "Gets list of all order items")
    @RequestMapping(value = "/api/{orderId}/item-list", method = RequestMethod.GET)
    public ResponseEntity<List<OrderItemDataResponse>> getAllOrderItems(
            @PathVariable Long orderId) {
        logger.info("get-all-order-items");
        List<OrderItemDataResponse> responses = orderDto.getAllOrderItemDetails(orderId);
        return ResponseEntity.ok(responses);
    }

    @ApiOperation(value = "Gets list of all order items")
    @RequestMapping(value = "/api/{orderId}/invoice", method = RequestMethod.GET)
    public ResponseEntity<List<InvoiceOrderItemData>> getOrderItemDataForInvoice(
            @PathVariable("orderId") Long orderId) {
        logger.info("get-all-order-items");
        List<InvoiceOrderItemData> responses = orderDto.getFulfilledOrderItemDataForInvoice(orderId);
        return ResponseEntity.ok(responses);
    }

    @ApiOperation(value = "Download Invoice Pdf")
    @RequestMapping(value = "/api/download/{fileName}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable("fileName") String fileName)
            throws IOException {
        logger.info("get-pdf");
        byte[] response = orderDto.getPDfInBytes(fileName);
        logger.info(response.length);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Gets list of all channel")
    @RequestMapping(value = "/api/channel", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDataResponse>> getAllChannelData() {
        logger.info("get-All-channel");
        List<ChannelDataResponse> responses = orderDto.getAllChannelData();
        return ResponseEntity.ok(responses);
    }
}
