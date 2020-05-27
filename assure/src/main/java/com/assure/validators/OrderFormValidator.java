package com.assure.validators;

import com.assure.dto.OrderDto;
import com.assure.model.form.OrderForm;
import com.assure.model.form.OrderItemForm;
import com.assure.pojo.Client;
import com.assure.pojo.Order;
import com.assure.pojo.Product;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class OrderFormValidator implements Validator {

    @Autowired private OrderDto orderDto;

    private static final Logger logger = Logger.getLogger(OrderFormValidator.class);

    @Override
    public boolean supports(Class<?> clazz) {
        return OrderFormValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OrderForm form = (OrderForm) target;
        Long clientId = form.getClientId();
        String channelOrderId = form.getChannelOrderId();
        logger.info(channelOrderId);
        List<OrderItemForm> orderItemFormList = form.getOrderItemFormList();
        logger.info(orderItemFormList.size());
        Client client = orderDto.getClientDetails(clientId);
        if (client == null) {
            logger.info("client not found");
            errors.rejectValue("clientId", "not found","Client not found with ID : " + clientId);
        }

        Order order = orderDto.getOrderDetails(channelOrderId);
        if (order != null) {
            logger.info("duplicate channel order id");
            errors.rejectValue("channelOrderId", "duplicate","Duplicate value found for Channel Order ID : " + channelOrderId);
        }
        int index = 0;
        for (OrderItemForm itemForm : orderItemFormList) {
            String clientSkuId = itemForm.getClientSkuId();

            Product product = orderDto.getProduct(clientSkuId, clientId);
            if (product == null) {
                logger.info("no product found");
                errors.pushNestedPath("orderItemFormList["+index+"]");
                errors.rejectValue("clientSkuId", "not found","Product not found with Client SKU ID : " + clientSkuId);
                errors.popNestedPath();
            }
            index++;
        }
    }
}
