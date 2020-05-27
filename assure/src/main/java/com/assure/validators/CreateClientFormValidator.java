package com.assure.validators;

import com.assure.dto.ClientDto;
import com.assure.pojo.Client;
import com.commons.enums.ClientType;
import com.commons.form.AddClientForm;
import com.commons.form.ClientForm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class CreateClientFormValidator implements Validator {

    private static final Logger logger = Logger.getLogger(CreateClientFormValidator.class);

    @Autowired
    private ClientDto clientDto;

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateClientFormValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AddClientForm addClientForm = (AddClientForm) target;
        List<ClientForm> clientList = addClientForm.getClientList();
        int index = 0;
        for(ClientForm form : clientList){
            String clientName = form.getName();
            ClientType type = form.getType();
            logger.info(clientName);
            logger.info(type);
            if(clientName.equals("")){
                logger.info("empty name check");
                errors.pushNestedPath("clientList["+index+"]");
                errors.rejectValue("name", "empty","Client Name cannot be empty.");
                errors.popNestedPath();
            }
            Client client = clientDto.getClientDetailsByName(clientName);
            logger.info(client);
            if(client != null) {
                logger.info("duplicate name check");
                errors.pushNestedPath("clientList["+index+"]");
                errors.rejectValue("name", "duplicate","Duplicate value found with Client Name : "+ clientName);
                errors.popNestedPath();
            }
            index++;
        }
    }
}
