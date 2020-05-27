package com.assure.api;

import com.assure.dao.ClientDao;
import com.assure.pojo.Client;
import com.assure.service.AbstractUnitTest;
import com.commons.enums.ClientType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ClientApiTest extends AbstractUnitTest {

    private Client client1, client2, newClient1;
    @Autowired private ClientApi clientApi;
    @Autowired private ClientDao clientDao;

    @Before
    public void setUp(){
        client1 = createObject("testClient", ClientType.CLIENT);
        client2 = createObject("testCustomer", ClientType.CUSTOMER);
    }

    public Client createObject(String name, ClientType type) {
        Client client = new Client();
        client.setName(name);
        client.setType(type);
        return client;
    }

    @Test
    public void testAddClient() {
        newClient1 = clientApi.addClient(client1);
        assertNotNull(newClient1);
        assertEquals(client1.getName(), newClient1.getName());
        assertEquals(client1.getType(), newClient1.getType());

        Client newClient2 = clientApi.addClient(client2);
        assertNotNull(newClient2);
        assertEquals(client2.getName(), newClient2.getName());
        assertEquals(client2.getType(), newClient2.getType());
    }

    @Test
    public void testUpdateClient() {
        newClient1 = clientDao.insert(client1);
        newClient1.setName("updatedName");
        newClient1.setType(ClientType.CUSTOMER);
        clientApi.updateClient(newClient1);
        Client result = clientApi.getClientDetails(newClient1.getId());
        assertNotNull(result);
        assertEquals("updatedName", result.getName());
    }

    @Test
    public void testGetClientDetails() {
        clientDao.insert(client1);
        Client client = clientApi.getClientDetails(client1.getId());
        assertNotNull(client);
        assertEquals(client1.getId(), client.getId());
        assertEquals(client1.getName(), client.getName());
        assertEquals(client1.getType(), client.getType());
    }

    @Test
    public void testGetAllClientDetails() {
        clientDao.insert(client1);
        clientApi.addClient(client2);
        List<Client> list = clientApi.getAllClientDetails();
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    public void testGetAllCustomerDetails() {
        clientDao.insert(client1);
        clientApi.addClient(client2);
        List<Client> list = clientApi.getAllCustomerDetails();
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    public void testGetAllClients() {
        clientDao.insert(client1);
        clientApi.addClient(client2);
        List<Client> list = clientApi.getAllClients();
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    public void testGetClientDetailsByName() {
        clientDao.insert(client1);
        Client client = clientApi.getClientDetails(client1.getName());
        assertNotNull(client);
        assertEquals(client1.getName(), client.getName());
    }

}
