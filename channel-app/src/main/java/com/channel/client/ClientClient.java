package com.channel.client;

import com.commons.response.ClientDataResponse;
import com.commons.util.AbstractRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Arrays;
import java.util.List;

@Component
public class ClientClient extends AbstractRestTemplate {
    @Value("${server.url}") private String SERVER_URL;

    public List<ClientDataResponse> getClientDetails() {
        String GET_CLIENT_URL = SERVER_URL + "/client";
        try {
            ClientDataResponse[] response;
            HttpEntity<ClientDataResponse[]> entity = new HttpEntity<>(getHeaders());
            response = restTemplate.exchange(GET_CLIENT_URL, HttpMethod.GET, entity, ClientDataResponse[].class).getBody();
            return Arrays.asList(response);
        } catch (HttpStatusCodeException e) {
            return null;
        }
    }

    public List<ClientDataResponse> getCustomerDetails() {
        String GET_CLIENT_URL = SERVER_URL + "/customer";
        try {
            ClientDataResponse[] response;
            HttpEntity<ClientDataResponse[]> entity = new HttpEntity<>(getHeaders());
            response = restTemplate.exchange(GET_CLIENT_URL, HttpMethod.GET, entity, ClientDataResponse[].class).getBody();
            return Arrays.asList(response);
        } catch (HttpStatusCodeException e) {
            return null;
        }
    }

    public ClientDataResponse getClientDetails(Long clientId) {
        String GET_CLIENT_URL = SERVER_URL + "/client/"+clientId;
        try {
            HttpEntity<ClientDataResponse> entity = new HttpEntity<>(getHeaders());
            return restTemplate.exchange(GET_CLIENT_URL, HttpMethod.GET, entity, ClientDataResponse.class).getBody();
        } catch (HttpStatusCodeException e) {
            return null;
        }
    }
}
