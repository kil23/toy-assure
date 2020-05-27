package com.assure.validators;

import com.assure.dto.OrderDto;
import com.assure.pojo.Client;
import com.assure.pojo.Order;
import com.assure.pojo.Product;
import com.commons.form.ChannelOrderForm;
import com.commons.form.ChannelOrderItemForm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class ChannelOrderFormValidator implements Validator {

    private static final Logger logger = Logger.getLogger(ChannelOrderFormValidator.class);

    @Autowired
    private OrderDto orderDto;

    @Override
    public boolean supports(Class<?> clazz) {
        return ChannelOrderFormValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChannelOrderForm form = (ChannelOrderForm) target;

        Long clientId = form.getClientId();
        String channelOrderId = form.getChannelOrderId();
        List<ChannelOrderItemForm> orderItemFormList = form.getChannelOrderItemFormList();

        Client client = orderDto.getClientDetails(clientId);
        if(client == null){
            logger.info("client not found");
            errors.rejectValue("clientId", "Client not found with ID : "+clientId);
        }

        Order order = orderDto.getOrderDetails(channelOrderId);
        if(order != null){
            logger.info("dup channel order id");
            errors.rejectValue("channelOrderId", "Duplicate value found with Channel Order ID : "+ channelOrderId);
        }

        int index = 0;
        for(ChannelOrderItemForm orderItemForm : orderItemFormList){
            Long globalSkuId = orderItemForm.getGlobalSkuId();

            Product product = orderDto.getProduct(globalSkuId);
            if(product == null){
                logger.info("product not found");
                errors.pushNestedPath("channelOrderItemFormList["+index+"]");
                errors.rejectValue("globalSkuId", "Product not found with Client SKU ID : "+globalSkuId);
                errors.popNestedPath();
            }
            index++;
        }
    }
}
