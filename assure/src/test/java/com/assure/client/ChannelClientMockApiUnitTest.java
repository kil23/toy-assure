package com.assure.client;

import com.assure.service.AbstractUnitTest;
import com.commons.enums.InvoiceType;
import com.commons.response.ChannelDataResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
public class ChannelClientMockApiUnitTest extends AbstractUnitTest {

    @Autowired private ChannelClient client;
    @Autowired private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testGetChannelDetails() {
        Long channelId = 1L;

        ChannelDataResponse channelDataResponse = new ChannelDataResponse();
        channelDataResponse.setName("test");
        channelDataResponse.setInvoiceType(InvoiceType.CHANNEL);
        channelDataResponse.setId(1L);

        try {
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI("http://localhost:9003/oms/api/channel/1")))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(channelDataResponse))
                    );
        } catch (URISyntaxException | JsonProcessingException e) {
            e.printStackTrace();
        }

        ChannelDataResponse response = client.getChannelDetails(channelId);
        mockServer.verify();
        Assert.assertEquals(channelDataResponse.getId(), response.getId());
        Assert.assertEquals(channelDataResponse.getName(), response.getName());
        Assert.assertEquals(channelDataResponse.getInvoiceType(), response.getInvoiceType());
    }

    @Test
    public void testGetAllChannelDetails() {
        ChannelDataResponse channelDataResponse = new ChannelDataResponse();
        channelDataResponse.setName("test");
        channelDataResponse.setInvoiceType(InvoiceType.CHANNEL);
        channelDataResponse.setId(1L);

        try {
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI("http://localhost:9003/oms/api/channel")))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(channelDataResponse))
                    );
        } catch (URISyntaxException | JsonProcessingException e) {
            e.printStackTrace();
        }

        List<ChannelDataResponse> response = client.getAllChannelDetails();
        mockServer.verify();
        Assert.assertEquals(channelDataResponse.getId(), response.get(0).getId());
        Assert.assertEquals(channelDataResponse.getName(), response.get(0).getName());
        Assert.assertEquals(channelDataResponse.getInvoiceType(), response.get(0).getInvoiceType());
    }
}
