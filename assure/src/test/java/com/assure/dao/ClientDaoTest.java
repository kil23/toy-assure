package com.assure.dao;

import com.assure.pojo.Client;
import com.assure.service.AbstractUnitTest;
import com.commons.enums.ClientType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ClientDaoTest extends AbstractUnitTest {

    private Client client1, client2;
    @Autowired private ClientDao clientDao;

    @Before
    public void setUp(){
        client1 = new Client();
        client1.setName("test1");
        client1.setType(ClientType.CLIENT);

        client2 = new Client();
        client2.setName("test2");
        client2.setType(ClientType.CUSTOMER);
    }

    @Test
    public void testInsertClient() {
        Client newClient = clientDao.insert(client1);
        assertNotNull(newClient);
        assertTrue(newClient.getId()>0);
        assertEquals(client1.getName(), newClient.getName());
        assertEquals(client1.getType(), newClient.getType());
    }

    @Test
    public void testUpdateClient() {
        Client newClient = clientDao.insert(client1);
        assertNotNull(newClient);
        newClient.setName("updatedName");
        newClient.setType(ClientType.CUSTOMER);
        clientDao.update(newClient);
        Client updated = clientDao.findOne(newClient.getId());
        assertNotNull(updated);
        assertEquals(newClient.getName(), updated.getName());
        assertEquals(newClient.getType(), updated.getType());
    }

    @Test
    public void testGetClient() {
        Client newClient = clientDao.insert(client2);
        Client result = clientDao.findOne(newClient.getId());
        assertNotNull(result);
        assertEquals(client2.getName(), result.getName());
        assertEquals(client2.getType(), result.getType());
    }

    @Test
    public void testGetClientByName() {
        Client newClient = clientDao.insert(client2);
        Client result = clientDao.getClientByName(newClient.getName());
        assertNotNull(result);
        assertEquals(client2.getName(), result.getName());
        assertEquals(client2.getType(), result.getType());
    }

    @Test
    public void testGetAllClient() {
        clientDao.insert(client1);
        clientDao.insert(client2);
        List<Client> list = clientDao.findAll();
        assertNotNull(list);
        assertTrue(list.size()>0);
        assertEquals(client1.getName(), list.get(0).getName());
        assertEquals(client1.getType(), list.get(0).getType());
        assertEquals(client2.getName(), list.get(1).getName());
        assertEquals(client2.getType(), list.get(1).getType());
    }

    @Test
    public void testGetAllClients() {
        clientDao.insert(client1);
        clientDao.insert(client2);
        List<Client> list = clientDao.getAllClientDetails();
        assertNotNull(list);
        assertTrue(list.size()>0);
        assertEquals(client1.getName(), list.get(0).getName());
        assertEquals(client1.getType(), list.get(0).getType());
    }

    @Test
    public void testGetAllCustomer() {
        clientDao.insert(client1);
        clientDao.insert(client2);
        List<Client> list = clientDao.getAllCustomerDetails();
        assertNotNull(list);
        assertTrue(list.size()>0);
        assertEquals(client2.getName(), list.get(0).getName());
        assertEquals(client2.getType(), list.get(0).getType());
    }
}
