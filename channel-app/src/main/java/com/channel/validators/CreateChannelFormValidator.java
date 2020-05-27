package com.channel.validators;

import com.channel.dto.ChannelDto;
import com.channel.model.form.AddChannelForm;
import com.channel.pojo.Channel;
import com.commons.enums.InvoiceType;
import com.commons.form.ChannelForm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class CreateChannelFormValidator implements Validator {

    private static final Logger logger = Logger.getLogger(CreateChannelFormValidator.class);

    @Autowired
    private ChannelDto channelDto;

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateChannelFormValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AddChannelForm addChannelForm = (AddChannelForm) target;
        List<ChannelForm> channelList = addChannelForm.getChannelList();
        int index = 0;
        for(ChannelForm form : channelList){
            String clientName = form.getName();
            InvoiceType invoiceType = form.getInvoiceType();
            logger.info(clientName);
            logger.info(invoiceType);
            if(clientName.equals("")){
                logger.info("empty name check");
                errors.pushNestedPath("channelList");
                errors.rejectValue("name", "empty","Channel Name cannot be empty.");
                errors.popNestedPath();
            }
            Channel channel = channelDto.getChannelByName(clientName);
            logger.info(channel);
            if(channel != null) {
                logger.info("duplicate name check");
                errors.pushNestedPath("channelList["+index+"]");
                errors.rejectValue("name", "duplicate","Duplicate value found with Channel Name : "+ clientName);
                errors.popNestedPath();
            }
            index++;
        }
    }
}
