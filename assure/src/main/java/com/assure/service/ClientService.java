package com.assure.service;

import com.assure.dao.ClientDao;
import com.assure.pojo.Client;
import com.commons.form.ClientForm;
import com.commons.response.ClientDataResponse;
import com.commons.service.ApiException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.commons.util.Validation.validateClientName;

@Service
public class ClientService {

    private static final Logger logger = Logger.getLogger(ClientService.class);

    @Autowired
    private ClientDao clientDao;

    public static ClientDataResponse convertPojoToFormData(Client p) {
        ClientDataResponse cd = new ClientDataResponse();
        cd.setId(p.getId());
        cd.setName(p.getName());
        cd.setType(p.getType());
        return cd;
    }

    public static Client convertFormToPojo(ClientForm cf){
        Client cp = new Client();
        cp.setName(cf.getName());
        cp.setType(cf.getType());
        return cp;
    }

    public static Client convertFormToPojo(ClientForm cf, Long id){
        Client cp = new Client();
        cp.setId(id);
        cp.setName(cf.getName());
        cp.setType(cf.getType());
        return cp;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void addClient(ClientForm formData) throws ApiException {
        logger.info("add-client");
        Client clientToInserted = convertFormToPojo(formData);
        List<Client> clientList = clientDao.getAllClients();
        validateClientName(clientToInserted.getName());
        if(clientList.size()==0){
            Client client = clientDao.insertClient(clientToInserted);
            logger.info("Client created with id : "+client.getId()+" and name : "+client.getName());
        }else{
            Client client_db = clientDao.getClientByName(clientToInserted.getName());
            if(client_db==null){
                Client client = clientDao.insertClient(clientToInserted);
                logger.info("Client created with id : "+client.getId()+" and name : "+client.getName());
            }else{
                logger.info("name cannot be duplicate");
                throw new ApiException("Duplicate name found while creating New Client");
            }
        }
    }

    @Transactional(rollbackOn = ApiException.class)
    public void updateClient(ClientForm formData, Long id) throws ApiException {
        logger.info("update-client");
        Client client_to_insert = convertFormToPojo(formData, id);
        validateClientName(client_to_insert.getName());
        Client cp = clientDao.getClientById(client_to_insert.getId());
        if (cp == null) {
            logger.info("No Client found with given id.");
            throw new ApiException("Client with given ID does not exit, id: " + client_to_insert.getId());
        }else {
            Client client_db = clientDao.getClientByName(client_to_insert.getName());
            if(client_db==null){
                logger.info("calling update method now...");
                clientDao.updateClient(client_to_insert);
            }else{
                logger.info("name cannot be duplicate");
                throw new ApiException("Duplicate name found while updating Client Name");
            }
        }
    }

    public ClientDataResponse getClientData(Long id) throws ApiException {
        logger.info("get-client");
        Client pojo = clientDao.getClientById(id);
        if(pojo == null){
            logger.info("No Client found with id : "+id);
            throw new ApiException("No Client found. Please create a new Client to proceed.");
        }
        return convertPojoToFormData(pojo);
    }

    public List<ClientDataResponse> getAllClientData() throws ApiException {
        logger.info("get-All-clients");
        List<Client> list = clientDao.getAllClients();
        if(list.isEmpty()){
            logger.info("No Client found.");
            throw new ApiException("No Client found. Please create a new Client to proceed.");
        }
        List<ClientDataResponse> list2 = new ArrayList<>();
        for (Client client : list) {
            list2.add(convertPojoToFormData(client));
        }
        return list2;
    }

    public Client getClient(Long id) throws ApiException {
        logger.info("get-client");
        Client client = clientDao.getClientById(id);
        if(client==null){
            logger.info("No Client found for client-id : "+id);
            throw new ApiException("No Client found.");
        }else{
            logger.info("Client found with id : " + id);
            return client;
        }
    }
}
