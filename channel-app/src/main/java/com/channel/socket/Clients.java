package com.channel.socket;

import com.commons.response.ClientDataResponse;
import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Clients {

    private static final Logger logger = Logger.getLogger(Clients.class);

    static final String URL_EMPLOYEES = "http://localhost:9002/oms/api/client/";

    public static List<ClientDataResponse> getClientDetails(){

        logger.info("URL : "+URL_EMPLOYEES);

        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ClientDataResponse[]> entity = new HttpEntity<>(headers);

        // RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Send request with GET method, and Headers.
        ResponseEntity<ClientDataResponse[]> response = restTemplate.exchange(URL_EMPLOYEES, //
                HttpMethod.GET, entity, ClientDataResponse[].class);

        HttpStatus statusCode = response.getStatusCode();
        logger.info("Response Status Code: " + statusCode);

        // Status Code: 200
        if (statusCode == HttpStatus.OK) {
            // Response Body Data
            ClientDataResponse[] list =  response.getBody();
            return new ArrayList<>(Arrays.asList(list));
        }else {
            logger.info("Response : " + Arrays.toString(response.getBody()));
        }
        return null;
    }
}
