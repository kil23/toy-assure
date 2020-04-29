package com.assure.socket;

import com.commons.response.ChannelDataResponse;
import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class Channel {

    private static final Logger logger = Logger.getLogger(Channel.class);

    static final String URL_EMPLOYEES = "http://localhost:9003/oms/api/channel/";

    public static ChannelDataResponse getChannelDetails(long channelId){

        logger.info("URL : "+URL_EMPLOYEES);

        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChannelDataResponse> entity = new HttpEntity<>(headers);

        // RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Send request with GET method, and Headers.
        ResponseEntity<ChannelDataResponse> response = restTemplate.exchange(URL_EMPLOYEES+channelId, //
                HttpMethod.GET, entity, ChannelDataResponse.class);

        HttpStatus statusCode = response.getStatusCode();
        logger.info("Response Status Code: " + statusCode);

        // Status Code: 200
        if (statusCode == HttpStatus.OK) {
            // Response Body Data
            return response.getBody();
        }else {
            logger.info("Response : " + response.getBody());
        }
        return null;
    }

}
