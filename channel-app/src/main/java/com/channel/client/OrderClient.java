package com.channel.client;

import com.commons.form.ChannelOrderForm;
import com.commons.response.OrderDataResponse;
import com.commons.util.AbstractRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Arrays;
import java.util.List;

@Component
public class OrderClient extends AbstractRestTemplate {
    @Value("${server.url}") private String SERVER_URL;

    public void postOrderDetails(ChannelOrderForm form){
        String POST_ORDER_URL = SERVER_URL + "/channel/order";
        try{
            HttpEntity<ChannelOrderForm> requestBody = new HttpEntity<>(form, getHeaders());
            restTemplate.exchange(POST_ORDER_URL, HttpMethod.POST, requestBody, String.class);
        }catch (HttpStatusCodeException e) {
            e.getMessage();
        }
    }

    public List<OrderDataResponse> getOrderDetails(long channelId){
        String GET_ORDER_URL = SERVER_URL + "/order/"+channelId;
        try {
            HttpEntity<OrderDataResponse[]> entity = new HttpEntity<>(getHeaders());
            OrderDataResponse[] response = restTemplate.exchange(GET_ORDER_URL,
                    HttpMethod.GET, entity, OrderDataResponse[].class).getBody();
            return Arrays.asList(response);
        } catch (HttpStatusCodeException e) {
            return null;
        }
    }

    public List<OrderDataResponse> getAllOrderDetails(long clientId) {
        String GET_ORDER_URL = SERVER_URL + "/order/client/"+clientId;
        try {
            HttpEntity<OrderDataResponse[]> entity = new HttpEntity<>(getHeaders());
            OrderDataResponse[] response = restTemplate.exchange(GET_ORDER_URL,
                    HttpMethod.GET, entity, OrderDataResponse[].class).getBody();
            return Arrays.asList(response);
        } catch (HttpStatusCodeException e) {
            return null;
        }
    }
}
