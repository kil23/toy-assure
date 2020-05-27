package com.channel.client;

import com.commons.response.ProductDataResponse;
import com.commons.util.AbstractRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Arrays;
import java.util.List;

@Component
public class ProductClient extends AbstractRestTemplate {
    @Value("${server.url}") private String SERVER_URL;

    public List<ProductDataResponse> getProductDetails(Long clientId){
        String GET_PRODUCT_URL = SERVER_URL + "/products/list/"+clientId;
        try{
            HttpEntity<ProductDataResponse[]> entity = new HttpEntity<>(getHeaders());
            ProductDataResponse[] response = restTemplate.exchange(GET_PRODUCT_URL,
                    HttpMethod.GET, entity, ProductDataResponse[].class).getBody();
            return Arrays.asList(response);
        } catch (HttpStatusCodeException e) {
            return null;
        }
    }

    public ProductDataResponse getProductDetail(Long globalSkuId){
        String GET_PRODUCT_URL = SERVER_URL + "/products/"+ globalSkuId;
        try{
            HttpEntity<ProductDataResponse> entity = new HttpEntity<>(getHeaders());
            ResponseEntity<ProductDataResponse> response = restTemplate.exchange(GET_PRODUCT_URL,
                    HttpMethod.GET, entity, ProductDataResponse.class);
            return response.getBody();
        }catch (HttpStatusCodeException e) {
            return null;
        }
    }
}
