package com.assure.validators;

import com.assure.dto.ClientDto;
import com.assure.pojo.Client;
import com.commons.enums.ClientType;
import com.commons.form.ClientForm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UpdateClientFormValidator implements Validator {

    private static final Logger logger = Logger.getLogger(UpdateClientFormValidator.class);

    @Autowired
    private ClientDto clientDto;

    @Override
    public boolean supports(Class<?> clazz) {
        return UpdateClientFormValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ClientForm form = (ClientForm) target;

        String clientName = form.getName();
        ClientType type = form.getType();
        if(type == null || type.toString().length() == 0) {
            logger.info("null type check");
            errors.rejectValue("type", "invalid", "Client Type cannot be null or empty.");
        }else if(!type.equals(ClientType.CLIENT) && !type.equals(ClientType.CUSTOMER)) {
            logger.info("valid type check");
            errors.rejectValue("type", "Type.invalid", "Invalid Client Type. Please add valid Client type.");
        }
        Client client = clientDto.getClientDetailsByName(clientName);
        logger.info(client);
        if(client != null) {
            logger.info("duplicate check");
            errors.rejectValue("name", "duplicate","Duplicate value found with Client Name "+ clientName);
        }
    }
}
