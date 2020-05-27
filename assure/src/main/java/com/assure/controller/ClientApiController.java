package com.assure.controller;

import com.assure.dto.ClientDto;
import com.assure.pojo.Client;
import com.assure.validators.CreateClientFormValidator;
import com.assure.validators.UpdateClientFormValidator;
import com.commons.form.AddClientForm;
import com.commons.form.ClientForm;
import com.commons.response.ClientDataResponse;
import com.commons.service.ApiException;
import com.commons.service.CustomValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api")
public class ClientApiController {

    private static final Logger logger = Logger.getLogger(ClientApiController.class);

    @Autowired
    private ClientDto clientDto;

    @Autowired
    private CreateClientFormValidator createValidator;

    @Autowired
    private UpdateClientFormValidator updateValidator;

    @ApiOperation(value = "Adds a new client")
    @RequestMapping(value = "/client", method = RequestMethod.POST)
    public ResponseEntity<String> addClientDetail(@Valid @RequestBody AddClientForm form, BindingResult result) {
        logger.info("add-client");
        logger.info(result.getErrorCount());
        createValidator.validate(form, result);
        logger.info(result.getErrorCount());
        if(result.hasErrors()){
            throw new CustomValidationException(result);
        }
        clientDto.addClientDetails(form);
        return ResponseEntity.ok("Request submitted successfully.");
    }

    @ApiOperation(value = "Updates a client by Id")
    @RequestMapping(value = "/client/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateClientDetail(@PathVariable("id") long id, @Valid @RequestBody ClientForm formData, BindingResult result) {
        logger.info("update-client with id : "+ id);
        Client client = clientDto.getClientDetailsById(id);
        if(client==null){
            result.rejectValue("name", "No Client present with Id : "+id);
        }
        logger.info(result.getErrorCount());
        updateValidator.validate(formData, result);
        logger.info(result.getErrorCount());
        if(result.hasErrors()){
            throw new CustomValidationException(result);
        }
        clientDto.updateClientDetails(formData, id);
        return ResponseEntity.ok("Request submitted successfully.");
    }

    @ApiOperation(value = "Gets a client by Id")
    @RequestMapping(value = "/client/{id}", method = RequestMethod.GET)
    public ResponseEntity<ClientDataResponse> getClientDetail(@PathVariable long id) {
        logger.info("get-client");
        Client client = clientDto.getClientDetailsById(id);
        if(client==null){
            throw new ApiException("No Client present with Id : "+id);
        }
        return ResponseEntity.ok(clientDto.getClientDetails(id));
    }

    @ApiOperation(value = "Gets list of all clients")
    @RequestMapping(value = "/client", method = RequestMethod.GET)
    public ResponseEntity<List<ClientDataResponse>> getAllClientDetails() {
        logger.info("get-All-clients");
        List<ClientDataResponse> responses = clientDto.getAllClientData();
        return ResponseEntity.ok(responses);
    }

    @ApiOperation(value = "Gets list of all customer")
    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    public ResponseEntity<List<ClientDataResponse>> getAllCustomerDetails() {
        logger.info("get-All-customers");
        List<ClientDataResponse> responses = clientDto.getAllCustomerData();
        return ResponseEntity.ok(responses);
    }

    @ApiOperation(value = "Gets list of all clients")
    @RequestMapping(value = "/all-client", method = RequestMethod.GET)
    public ResponseEntity<List<ClientDataResponse>> getAllClientData() {
        logger.info("get-All-clients");
        List<ClientDataResponse> responses = clientDto.getAllClients();
        return ResponseEntity.ok(responses);
    }
}
