package com.channel.socket;

import com.commons.form.OrderForm;
import com.commons.response.OrderDataResponse;
import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Order {

    private static final Logger logger = Logger.getLogger(Order.class);

    private static String URL_EMPLOYEES = "http://localhost:9002/oms/api/order/";

    public static void postOrderDetails(OrderForm form){

        logger.info("URL : "+URL_EMPLOYEES);
        logger.info("Flag : "+form.getNewFlag());

        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        headers.setContentType(MediaType.APPLICATION_JSON);

        // Data attached to the request.
        HttpEntity<OrderForm> requestBody = new HttpEntity<>(form, headers);

        // RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Send request with POST method.
        ResponseEntity<OrderForm> result
                = restTemplate.postForEntity(URL_EMPLOYEES, requestBody, OrderForm.class);

        HttpStatus statusCode = result.getStatusCode();
        logger.info("Response Status Code: " + statusCode);
    }

    public static List<OrderDataResponse> getOrderDetails(long channelId){

        logger.info("URL : "+URL_EMPLOYEES+channelId);

        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OrderDataResponse[]> entity = new HttpEntity<>(headers);

        // RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Send request with GET method, and Headers.
        ResponseEntity<OrderDataResponse[]> response = restTemplate.exchange(URL_EMPLOYEES+channelId, //
                HttpMethod.GET, entity, OrderDataResponse[].class);

        HttpStatus statusCode = response.getStatusCode();
        logger.info("Response Status Code: " + statusCode);

        // Status Code: 200
        if (statusCode == HttpStatus.OK) {
            // Response Body Data
            OrderDataResponse[] list =  response.getBody();
            if(list==null){
                logger.info("No order data found.");
            }else{
                logger.info("Success Response : " + Arrays.toString(response.getBody()));
                return new ArrayList<>(Arrays.asList(list));
            }
        }else {
            logger.info("Failure Response : " + Arrays.toString(response.getBody()));
        }
        return null;
    }

}
