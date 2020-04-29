package com.channel.socket;

import com.commons.response.ProductDataResponse;
import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class Products {

    private static final Logger logger = Logger.getLogger(Products.class);

    public static ProductDataResponse getProductByClientSkuId(String clientSkuId, long clientId){

        String URL_EMPLOYEES = "http://localhost:9002/oms/api/"+clientId+"/products/";

        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductDataResponse[]> entity = new HttpEntity<>(headers);

        // RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Send request with GET method, and Headers.
        ResponseEntity<ProductDataResponse[]> response = restTemplate.exchange(URL_EMPLOYEES, //
                HttpMethod.GET, entity, ProductDataResponse[].class);

        HttpStatus statusCode = response.getStatusCode();
        logger.info("Response Status Code: " + statusCode);

        // Status Code: 200
        if (statusCode == HttpStatus.OK) {
            // Response Body Data

            ProductDataResponse[] list =  response.getBody();
            logger.info("Result records : "+ list.length);
            for(ProductDataResponse data : list){
                if(data.getClientSkuId().equalsIgnoreCase(clientSkuId)){
                    logger.info("Products matched with id : "+data.getGlobalSkuId());
                    return data;
                }else{
                    logger.info("No product found.");
                }
            }

        }
        return null;
    }
}
