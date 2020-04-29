package com.channel.socket;

import com.commons.response.OrderItemDataResponse;
import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OrderItems {

    private static final Logger logger = Logger.getLogger(OrderItems.class);

    public static List<OrderItemDataResponse> getOrderItemDetails(long orderId) {

        String URL_EMPLOYEES = "http://localhost:9002/oms/api/"+orderId+"/order-list";

        logger.info("URL : "+URL_EMPLOYEES);

        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderItemDataResponse[]> entity = new HttpEntity<>(headers);

        // RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Send request with GET method, and Headers.
        ResponseEntity<OrderItemDataResponse[]> response = restTemplate.exchange(URL_EMPLOYEES, //
                HttpMethod.GET, entity, OrderItemDataResponse[].class);

        HttpStatus statusCode = response.getStatusCode();
        logger.info("Response Status Code: " + statusCode);

        // Status Code: 200
        if (statusCode == HttpStatus.OK) {
            // Response Body Data
            OrderItemDataResponse[] data = response.getBody();
            if(data==null){
                logger.info("No order data found.");
                return null;
            }else{
                logger.info("Success Response : " + Arrays.toString(response.getBody()));
                return new ArrayList<>(Arrays.asList(data));
            }
        }else {
            logger.info("Failure Response : " + Arrays.toString(response.getBody()));
            return null;
        }
    }
}
