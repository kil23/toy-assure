package com.assure.client;

import com.commons.response.ChannelDataResponse;
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
public class ChannelClient extends AbstractRestTemplate {
    @Value("${channel.url}") private String CHANNEL_URL;

    public void postOrderData(OrderDataResponse order) {
        String POST_ORDER_URL = CHANNEL_URL + "/generate-invoice";
        try {
            HttpEntity<OrderDataResponse> requestBody = new HttpEntity<>(order, getHeaders());
            restTemplate.exchange(POST_ORDER_URL, HttpMethod.POST, requestBody, String.class);
        } catch (HttpStatusCodeException e) {
            e.getMessage();
        }
    }

    public ChannelDataResponse getChannelDetails(Long channelId){
        String GET_CHANNEL_URL = CHANNEL_URL + "/" +channelId;
        try {
            HttpEntity<ChannelDataResponse> entity = new HttpEntity<>(getHeaders());
            return restTemplate.exchange(GET_CHANNEL_URL, HttpMethod.GET, entity, ChannelDataResponse.class).getBody();
        } catch (HttpStatusCodeException e) {
            return null;
        }
    }

    public List<ChannelDataResponse> getAllChannelDetails() {
        String GET_CLIENT_URL = CHANNEL_URL;
        try {
            ChannelDataResponse[] response;
            HttpEntity<ChannelDataResponse[]> entity = new HttpEntity<>(getHeaders());
            response = restTemplate.exchange(GET_CLIENT_URL, HttpMethod.GET, entity, ChannelDataResponse[].class).getBody();
            return Arrays.asList(response);
        } catch (HttpStatusCodeException e) {
            return null;
        }
    }
}
