package com.channel.client;

import com.channel.service.AbstractUnitTest;
import com.commons.response.ProductDataResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
public class ProductClientMockApiUnitTest extends AbstractUnitTest {

    List<ProductDataResponse> productResponseList;
    ProductDataResponse product1, product2;

    @Autowired
    private ProductClient productClient;

    @Value("${server.url}")
    private String SERVER_URL;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        productResponseList = new ArrayList<>();
        product1 = createObject("client11", "abc", "prod1", "brand1", 19.9, "desc1");
        productResponseList.add(product1);
        product2 = createObject("client12", "pqr", "prod2", "brand2", 29.9, "desc2");
        productResponseList.add(product2);
    }

    public ProductDataResponse createObject(String clientName, String clientSkuId, String name,
                                            String brandId, Double mrp, String description) {
        ProductDataResponse product = new ProductDataResponse();
        product.setClientName(clientName);
        product.setClientSkuId(clientSkuId);
        product.setName(name);
        product.setBrandId(brandId);
        product.setMrp(mrp);
        product.setDescription(description);
        return product;
    }

    @Test
    public void testGetProductDetails() {
        long clientId = 1L;

        try {
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI(SERVER_URL+"/products/list/"+clientId)))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(productResponseList))
                    );
        } catch (URISyntaxException | JsonProcessingException e) {
            e.printStackTrace();
        }

        List<ProductDataResponse> responseList = productClient.getProductDetails(clientId);
        mockServer.verify();
        Assert.assertNotNull(responseList);
        Assert.assertTrue(responseList.size()>0);

        Assert.assertEquals(product1.getClientName(), responseList.get(0).getClientName());
        Assert.assertEquals(product1.getClientSkuId(), responseList.get(0).getClientSkuId());
        Assert.assertEquals(product1.getName(), responseList.get(0).getName());
        Assert.assertEquals(product1.getBrandId(), responseList.get(0).getBrandId());
        Assert.assertEquals(product1.getMrp(), responseList.get(0).getMrp());
        Assert.assertEquals(product1.getDescription(), responseList.get(0).getDescription());

        Assert.assertEquals(product2.getClientName(), responseList.get(1).getClientName());
        Assert.assertEquals(product2.getClientSkuId(), responseList.get(1).getClientSkuId());
        Assert.assertEquals(product2.getName(), responseList.get(1).getName());
        Assert.assertEquals(product2.getBrandId(), responseList.get(1).getBrandId());
        Assert.assertEquals(product2.getMrp(), responseList.get(1).getMrp());
        Assert.assertEquals(product2.getDescription(), responseList.get(1).getDescription());
    }
}
