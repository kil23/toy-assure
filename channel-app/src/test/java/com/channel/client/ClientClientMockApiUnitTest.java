package com.channel.client;

import com.channel.service.AbstractUnitTest;
import com.commons.enums.ClientType;
import com.commons.response.ClientDataResponse;
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
public class ClientClientMockApiUnitTest extends AbstractUnitTest {

    List<ClientDataResponse> clientResponseList;
    ClientDataResponse client1, client2;

    @Autowired
    private ClientClient client;

    @Value("${server.url}")
    private String SERVER_URL;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);

        clientResponseList = new ArrayList<>();

        client1 = createObject("cli1", ClientType.CLIENT, 1L);
        clientResponseList.add(client1);

        client2 = createObject("cli2", ClientType.CUSTOMER, 2L);
        clientResponseList.add(client2);
    }

    public ClientDataResponse createObject(String name, ClientType type, Long id){
        ClientDataResponse clientDataResponse = new ClientDataResponse();
        clientDataResponse.setName(name);
        clientDataResponse.setType(type);
        clientDataResponse.setId(id);
        return clientDataResponse;
    }

    @Test
    public void testGetChannelDetails() {

        List<ClientDataResponse> responseList = null;
        try {
            mockServer.expect(ExpectedCount.once(),
                    requestTo(new URI(SERVER_URL + "/client")))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withStatus(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(mapper.writeValueAsString(clientResponseList))
                    );
            responseList = client.getClientDetails();

        } catch (URISyntaxException | JsonProcessingException e) {
            e.printStackTrace();
        }
        mockServer.verify();
        Assert.assertNotNull(responseList);
        Assert.assertTrue(responseList.size() > 0);

        Assert.assertEquals(client1.getId(), responseList.get(0).getId());
        Assert.assertEquals(client1.getName(), responseList.get(0).getName());
        Assert.assertEquals(client1.getType(), responseList.get(0).getType());

        Assert.assertEquals(client2.getId(), responseList.get(1).getId());
        Assert.assertEquals(client2.getName(), responseList.get(1).getName());
        Assert.assertEquals(client2.getType(), responseList.get(1).getType());
    }
}
