package com.channel.client;

import com.commons.response.InvoiceOrderItemData;
import com.commons.response.OrderItemDataResponse;
import com.commons.util.AbstractRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Arrays;
import java.util.List;

@Component
public class OrderItemClient extends AbstractRestTemplate {
    @Value("${server.url}") private String SERVER_URL;

    public List<OrderItemDataResponse> getOrderItemDetails(Long orderId) {
        String GET_ORDER_ITEM_URL = SERVER_URL + "/" + orderId + "/item-list";
        try{
            HttpEntity<OrderItemDataResponse[]> entity = new HttpEntity<>(getHeaders());
            OrderItemDataResponse[] response = restTemplate.exchange(GET_ORDER_ITEM_URL,
                    HttpMethod.GET, entity, OrderItemDataResponse[].class).getBody();
            return Arrays.asList(response);
        }catch (HttpStatusCodeException e){
            return null;
        }
    }

    public List<InvoiceOrderItemData> getOrderItemDataForInvoice(Long orderId) {
        String GET_ORDER_ITEM_URL = SERVER_URL + "/" + orderId + "/invoice";
        try {
            HttpEntity<InvoiceOrderItemData[]> entity = new HttpEntity<>(getHeaders());
            InvoiceOrderItemData[] response = restTemplate.exchange(GET_ORDER_ITEM_URL,
                    HttpMethod.GET, entity, InvoiceOrderItemData[].class).getBody();
            return Arrays.asList(response);
        } catch (HttpStatusCodeException e) {
            return null;
        }
    }
}
