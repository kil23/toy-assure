package com.assure.dto;

import com.assure.api.ClientApi;
import com.assure.pojo.Client;
import com.commons.form.AddClientForm;
import com.commons.form.ClientForm;
import com.commons.response.ClientDataResponse;
import com.commons.service.ApiException;
import com.commons.util.ConvertUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ClientDto {

    private static final Logger logger = Logger.getLogger(ClientDto.class);

    @Autowired
    private ClientApi clientApi;

    @Transactional
    public void addClientDetails(AddClientForm form) {
        logger.info("here");
        for(ClientForm formData : form.getClientList()){
            Client client = ConvertUtil.convert(formData, Client.class);
            if (client != null) {
                clientApi.addClient(client);
            }
        }
    }

    @Transactional
    public void updateClientDetails(ClientForm formData, long id) {
        logger.info("here");
        Client client = ConvertUtil.convert(formData, Client.class);
        if (client != null) {
            client.setId(id);
            Client clientInDB = clientApi.getClientDetails(client.getName());
            if(clientInDB != null) {
                throw new ApiException("Duplicate Client Name found.");
            }
            clientApi.updateClient(client);
        }
    }

    @Transactional(readOnly = true)
    public ClientDataResponse getClientDetails(long id) {
        Client client = clientApi.getClientDetails(id);
        return ConvertUtil.convert(client, ClientDataResponse.class);
    }

    @Transactional(readOnly = true)
    public List<ClientDataResponse> getAllClientData() {
        List<Client> clientList = clientApi.getAllClientDetails();
        if(clientList.isEmpty()){
            throw new ApiException("No Client Found. Please add few Client first.");
        }
        logger.info(clientList.size());
        return ConvertUtil.convert(clientList, ClientDataResponse.class);
    }

    @Transactional(readOnly = true)
    public List<ClientDataResponse> getAllCustomerData() {
        List<Client> clientList = clientApi.getAllCustomerDetails();
        if(clientList.isEmpty()){
            throw new ApiException("No Customer Found. Please add few Customer first.");
        }
        logger.info(clientList.size());
        return ConvertUtil.convert(clientList, ClientDataResponse.class);
    }

    @Transactional(readOnly = true)
    public List<ClientDataResponse> getAllClients() {
        List<Client> clientList = clientApi.getAllClients();
        if(clientList.isEmpty()){
            throw new ApiException("No Record Found. Please add few Client first.");
        }
        logger.info(clientList.size());
        return ConvertUtil.convert(clientList, ClientDataResponse.class);
    }

    @Transactional(readOnly = true)
    public Client getClientDetailsById(long id) {
        return clientApi.getClientDetails(id);
    }

    @Transactional(readOnly = true)
    public Client getClientDetailsByName(String clientName) {
        logger.info("calling");
        Client clientDetails = clientApi.getClientDetails(clientName);
        logger.info(clientDetails);
        return clientDetails;
    }
}
