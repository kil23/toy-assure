package com.channel.client;

import com.channel.service.AbstractUnitTest;
import com.commons.response.OrderItemDataResponse;
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
public class OrderItemClientMockApiUnitTest extends AbstractUnitTest {

    List<OrderItemDataResponse> orderItemResponseList;
    OrderItemDataResponse orderItem1;
    OrderItemDataResponse orderItem2;

    @Autowired
    private OrderItemClient orderItemClient;

    @Value("${server.url}")
    private String SERVER_URL;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        orderItemResponseList = new ArrayList<>();
        orderItem1 = createObject(5L, 6L);
        orderItemResponseList.add(orderItem1);
        orderItem2 = createObject(15L, 9L);
        orderItemResponseList.add(orderItem2);
    }

    public OrderItemDataResponse createObject(Long globalSkuId, Long orderedQuantity) {
        OrderItemDataResponse response = new OrderItemDataResponse();
        response.setGlobalSkuId(globalSkuId);
        response.setOrderedQuantity(orderedQuantity);
        return response;
    }

    @Test
    public void testGetOrderItemDetails() {
        Long orderId = 10L;

        try {
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI(SERVER_URL+"/"+orderId+"/item-list")))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(orderItemResponseList))
                    );
        } catch (URISyntaxException | JsonProcessingException e) {
            e.printStackTrace();
        }

        List<OrderItemDataResponse> responseList = orderItemClient.getOrderItemDetails(orderId);
        mockServer.verify();
        Assert.assertNotNull(responseList);
        Assert.assertTrue(responseList.size()>0);

        Assert.assertEquals(orderItem1.getGlobalSkuId(), responseList.get(0).getGlobalSkuId());
        Assert.assertEquals(orderItem1.getOrderedQuantity(), responseList.get(0).getOrderedQuantity());

        Assert.assertEquals(orderItem2.getGlobalSkuId(), responseList.get(1).getGlobalSkuId());
        Assert.assertEquals(orderItem2.getOrderedQuantity(), responseList.get(1).getOrderedQuantity());
    }
}
