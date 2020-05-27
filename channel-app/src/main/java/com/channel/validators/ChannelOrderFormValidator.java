package com.channel.validators;

import com.channel.dto.ChannelDto;
import com.channel.dto.ChannelListingDto;
import com.channel.pojo.Channel;
import com.commons.form.ChannelOrderForm;
import com.commons.form.ChannelOrderItemForm;
import com.commons.response.ClientDataResponse;
import com.commons.response.OrderDataResponse;
import com.commons.response.ProductDataResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class ChannelOrderFormValidator implements Validator {

    private static final Logger logger = Logger.getLogger(ChannelOrderFormValidator.class);

    @Autowired private ChannelDto channelDto;
    @Autowired private ChannelListingDto channelListingDto;

    @Override
    public boolean supports(Class<?> clazz) {
        return ChannelOrderFormValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChannelOrderForm form = (ChannelOrderForm) target;

        Long clientId = form.getClientId();
        Long channelId = form.getChannelId();
        Long customerId = form.getCustomerId();
        String channelOrderId = form.getChannelOrderId();
        List<ChannelOrderItemForm> orderItemFormList = form.getChannelOrderItemFormList();

        ClientDataResponse client = channelListingDto.getClientDetails(clientId);
        if(client == null){
            logger.info("client not found");
            errors.rejectValue("clientId", "not found","Client not found with ID : "+clientId);
        }

        Channel channel = channelDto.getChannelDetail(channelId);
        if(channel == null){
            logger.info("channel not found");
            errors.rejectValue("channelId", "not found","Channel not found with ID : "+channelId);
        }

        ClientDataResponse customer = channelListingDto.getClientDetails(customerId);
        if(customer == null){
            logger.info("customer not found");
            errors.rejectValue("customerId", "not found","Customer not found with ID : "+customerId);
        }

        boolean matchFound = false;
        List<OrderDataResponse> orderList = channelDto.getOrderDataForClient(clientId);
        for(OrderDataResponse order : orderList){
            if (order.getChannelOrderId().equalsIgnoreCase(channelOrderId)) {
                matchFound = true;
                break;
            }
        }
        if(matchFound){
            logger.info("dup channel order id");
            errors.rejectValue("channelOrderId", "duplicate","Duplicate value found with Channel Order ID : "+ channelOrderId);
        }
        int index = 0;
        for(ChannelOrderItemForm orderItemForm : orderItemFormList){
            Long globalSkuId = orderItemForm.getGlobalSkuId();

            ProductDataResponse product = channelListingDto.getProductDetail(globalSkuId);
            if(product == null){
                logger.info("product not found");
                errors.pushNestedPath("channelOrderFormList["+index+"]");
                errors.rejectValue("globalSkuId", "not found","Product not found with Global SKU ID : "+globalSkuId);
                errors.popNestedPath();
            }
            index++;
        }
    }
}
