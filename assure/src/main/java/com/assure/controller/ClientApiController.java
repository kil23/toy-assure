package com.assure.controller;

import com.assure.service.ClientService;
import com.commons.form.ClientForm;
import com.commons.response.ClientDataResponse;
import com.commons.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class ClientApiController {

    private static final Logger logger = Logger.getLogger(ClientApiController.class);

    @Autowired
    private ClientService clientService;

    @ApiOperation(value = "Adds a new client")
    @RequestMapping(value = "/api/client", method = RequestMethod.POST)
    public void addClientDetail(@RequestBody @Validated ClientForm formData) throws ApiException {
        logger.info("add-client");
        clientService.addClient(formData);
    }

    @ApiOperation(value = "Updates a client by ID")
    @RequestMapping(value = "/api/client/{id}", method = RequestMethod.PUT)
    public void updateClientDetail(@PathVariable("id") long id, @RequestBody ClientForm formData) throws ApiException {
        logger.info("update-client with id : "+ id);
        clientService.updateClient(formData, id);
    }

    @ApiOperation(value = "Gets a client by ID")
    @RequestMapping(value = "/api/client/{id}", method = RequestMethod.GET)
    public ClientDataResponse getClientDetail(@PathVariable long id) throws ApiException {
        logger.info("get-client");
        return clientService.getClientData(id);
    }

    @ApiOperation(value = "Gets list of all clients")
    @RequestMapping(value = "/api/client", method = RequestMethod.GET)
    public List<ClientDataResponse> getAllClient() throws ApiException {
        logger.info("get-All-clients");
        return clientService.getAllClientData();
    }
}
