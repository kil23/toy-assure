package com.assure.api;

import com.assure.dao.ClientDao;
import com.assure.pojo.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientApi {
    @Autowired private ClientDao clientDao;

    @Transactional
    public Client addClient(Client clientToInsert) {
        return clientDao.insert(clientToInsert);
    }

    @Transactional
    public void updateClient(Client clientToUpdate) {
        Client client = clientDao.findOne(clientToUpdate.getId());
        client.setName(clientToUpdate.getName());
        client.setType(clientToUpdate.getType());
    }

    @Transactional(readOnly = true)
    public Client getClientDetails(Long id) {
        return clientDao.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<Client> getAllClientDetails() {
        return clientDao.getAllClientDetails();
    }

    @Transactional(readOnly = true)
    public List<Client> getAllCustomerDetails() {
        return clientDao.getAllCustomerDetails();
    }

    @Transactional(readOnly = true)
    public List<Client> getAllClients() {
        return clientDao.findAll();
    }

    @Transactional(readOnly = true)
    public Client getClientDetails(String name) {
        return clientDao.getClientByName(name);
    }
}
