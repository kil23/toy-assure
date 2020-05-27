package com.assure.client;

import com.commons.util.AbstractRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Arrays;

@Component
public class ChannelInvoicePdfClient extends AbstractRestTemplate {
    @Value("${channel.url}") private String CHANNEL_URL;

    public byte[] getInvoicePDF(String fileName) {
        String GET_ORDER_URL = CHANNEL_URL + "/download/pdf/"+fileName;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_PDF, MediaType.APPLICATION_OCTET_STREAM));
            HttpEntity<String> entity = new HttpEntity<>(headers);
            return restTemplate.exchange(GET_ORDER_URL, HttpMethod.GET, entity, byte[].class, "1").getBody();
        } catch (HttpStatusCodeException e) {
            return null;
        }
    }
}
