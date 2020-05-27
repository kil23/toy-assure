package com.channel.validators;

import com.channel.dto.ChannelListingDto;
import com.channel.model.form.ChannelListingForm;
import com.channel.model.form.ListingFormData;
import com.channel.pojo.ChannelListing;
import com.commons.response.ClientDataResponse;
import com.commons.response.ProductDataResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class CreateChannelListingFormValidator implements Validator {

    private static final Logger logger = Logger.getLogger(CreateChannelListingFormValidator.class);

    @Autowired private ChannelListingDto channelListingDto;

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateChannelListingFormValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChannelListingForm form = (ChannelListingForm) target;

        Long clientId = form.getClientId();
        List<ListingFormData> listingFormDataList = form.getListingData();

        ClientDataResponse client = channelListingDto.getClientDetails(clientId);
        if(client == null){
            logger.info("client not found");
            errors.rejectValue("clientId", "not found","Client not found with ID : "+clientId);
        }

        int index = 0;
        for(ListingFormData channelListingData : listingFormDataList){
            String channelSkuId = channelListingData.getChannelSkuId();
            String clientSkuId = channelListingData.getClientSkuId();
            ChannelListing listing = channelListingDto.getChannelListing(channelSkuId, clientId);
            if(listing != null){
                logger.info("dup Channel SKU id");
                errors.pushNestedPath("listingData["+ index +"]");
                errors.rejectValue("channelSkuId", "duplicate","Duplicate value found with Channel SKU ID : "+ channelSkuId);
                errors.popNestedPath();
            }
//            else {
//                errors.pushNestedPath("listingData["+ index +"]");
//                errors.rejectValue("channelSkuId", "not found","Channel Listing not found with Channel SKU ID : "+ channelSkuId);
//            }
            boolean matchFound = false;
            List<ProductDataResponse> productList = channelListingDto.getProductDetails(clientId);
            if(productList != null){
                for(ProductDataResponse data : productList){
                    if (data.getClientSkuId().equals(clientSkuId)) {
                        matchFound = true;
                        break;
                    }
                }
            }
            if(!matchFound) {
                logger.info("product not found");
                errors.pushNestedPath("listingData["+ index +"]");
                errors.rejectValue("clientSkuId", "not found","Product not found with Client SKU ID : "+clientSkuId);
                errors.popNestedPath();
            }
            index++;
        }
    }
}
