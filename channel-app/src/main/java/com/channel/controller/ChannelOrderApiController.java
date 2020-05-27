package com.channel.controller;

import com.channel.dto.ChannelDto;
import com.channel.validators.ChannelOrderFormValidator;
import com.commons.form.ChannelOrderForm;
import com.commons.response.ClientDataResponse;
import com.commons.response.OrderDataResponse;
import com.commons.response.OrderItemDataResponse;
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
public class ChannelOrderApiController {

    private static final Logger logger = Logger.getLogger(ChannelOrderApiController.class);

    @Autowired private ChannelDto channelDto;
    @Autowired private ChannelOrderFormValidator orderFormValidator;

    @ApiOperation(value = "Adds a new channel")
    @RequestMapping(value = "/api/order", method = RequestMethod.POST)
    public ResponseEntity<String> createChannelOrders(@Valid @RequestBody ChannelOrderForm formData,
                                                      BindingResult result) {
        logger.info("add-channel-order");
        logger.info(result.getErrorCount());
        orderFormValidator.validate(formData, result);
        logger.info(result.getErrorCount());
        if(result.hasErrors()){
            throw new CustomValidationException(result);
        }
        channelDto.postOrderDetails(formData);
        return ResponseEntity.ok("Request Submitted successfully.");
    }

    @ApiOperation(value = "Gets list of all channel orders")
    @RequestMapping(value = "/api/{channelId}/order", method = RequestMethod.GET)
    public ResponseEntity<List<OrderDataResponse>> getAllChannelOrders(
                                            @PathVariable("channelId") Long channelId) {
        logger.info("get-All-orders");
        List<OrderDataResponse> responses = channelDto.getAllChannelOrderList(channelId);
        return ResponseEntity.ok(responses);
    }

    @ApiOperation(value = "Gets order details by client-id")
    @RequestMapping(value = "/api/order/client/{clientId}", method = RequestMethod.GET)
    public ResponseEntity<List<OrderDataResponse>> getAllOrderDetail(@PathVariable("clientId") long clientId) {
        logger.info("get-order-list-for-client");
        List<OrderDataResponse> responses = channelDto.getOrderDataForClient(clientId);
        return ResponseEntity.ok(responses);
    }

    @ApiOperation(value = "Gets list of all channel order items")
    @RequestMapping(value = "/api/{channelId}/order/{orderId}/item-list", method = RequestMethod.GET)
    public ResponseEntity<List<OrderItemDataResponse>> getAllChannelOrderItems(
                                            @PathVariable("orderId") Long orderId,
                                            @PathVariable("channelId") Long channelId) {
        logger.info("get-order-items");
        List<OrderItemDataResponse> responses = channelDto.getChannelOrderItems(orderId, channelId);
        return ResponseEntity.ok(responses);
    }

    @ApiOperation(value = "Download Invoice Pdf")
    @RequestMapping(value = "/api/download/{fileName}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable("fileName") String fileName) throws IOException {
        logger.info("get-pdf");
        byte[] response = channelDto.getPDfInBytes(fileName);
        logger.info(response.length);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Generate Invoice Pdf")
    @RequestMapping(value = "/api/channel/generate-invoice", method = RequestMethod.POST)
    public ResponseEntity<String> generateInvoicePdf(@RequestBody OrderDataResponse orderDataResponse) {
        logger.info("generate-invoice-pdf");
        channelDto.generatePdf(orderDataResponse);
        return ResponseEntity.ok("Successful");
    }

    @ApiOperation(value = "Gets list of all clients")
    @RequestMapping(value = "/api/client", method = RequestMethod.GET)
    public ResponseEntity<List<ClientDataResponse>> getAllClients() {
        logger.info("get-All-client-for-channel");
        List<ClientDataResponse> responses = channelDto.getAllClients();
        return ResponseEntity.ok(responses);
    }

    @ApiOperation(value = "Gets list of all customer")
    @RequestMapping(value = "/api/customer", method = RequestMethod.GET)
    public ResponseEntity<List<ClientDataResponse>> getAllCustomers() {
        logger.info("get-All-client-for-channel");
        List<ClientDataResponse> responses = channelDto.getAllCustomers();
        return ResponseEntity.ok(responses);
    }

}
