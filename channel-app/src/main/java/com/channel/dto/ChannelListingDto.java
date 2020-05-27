package com.channel.dto;

import com.channel.api.ChannelListingApi;
import com.channel.client.ClientClient;
import com.channel.client.ProductClient;
import com.channel.model.form.ChannelListingForm;
import com.channel.model.form.ListingFormData;
import com.channel.model.response.ChannelListingDataResponse;
import com.channel.pojo.ChannelListing;
import com.commons.response.ClientDataResponse;
import com.commons.response.ProductDataResponse;
import com.commons.service.ApiException;
import com.commons.util.ConvertUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Component
public class ChannelListingDto {

    private static final Logger logger = Logger.getLogger(ChannelListingDto.class);

    @Autowired
    private ChannelListingApi channelListingApi;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private ClientClient clientClient;

    @Transactional(rollbackFor = ApiException.class)
    public void addListing(@Valid ChannelListingForm formData, Long channelId) {
        for(ListingFormData listingFormData : formData.getListingData()){
            logger.info(listingFormData.getChannelSkuId());
            ChannelListing channelListing = ConvertUtil.convert(formData, ChannelListing.class);
            if (channelListing != null) {
                channelListing.setChannelId(channelId);
                BeanUtils.copyProperties(listingFormData, channelListing);
                List<ProductDataResponse> productDataResponse = productClient.getProductDetails(channelListing.getClientId());
                logger.info(productDataResponse.isEmpty());
                if(productDataResponse.isEmpty()){
                    throw new ApiException("ProductClient with given clientId("+formData.getClientId()+") and clientSKuId("+listingFormData.getClientSkuId()+") does not exist in the database. Please add ProductClient first.");
                }
                for(ProductDataResponse productData : productDataResponse){
                    if(productData.getClientSkuId().equalsIgnoreCase(listingFormData.getClientSkuId())){
                        logger.info("here ");
                        channelListing.setGlobalSkuId(productData.getGlobalSkuId());
                        channelListingApi.addListing(channelListing);
                    }
                }
            }
        }
    }

    @Transactional(rollbackFor = ApiException.class)
    public void deleteListing(Long listingId) {
        ChannelListing listing = channelListingApi.getListing(listingId);
        if(listing == null){
            throw new ApiException("No Channel-listing exists");
        }
        channelListingApi.deleteListing(listing);
    }

    @Transactional(readOnly = true)
    public ChannelListingDataResponse getListing(Long listingId) {
        ChannelListing channelListing = channelListingApi.getListing(listingId);
        if(channelListing==null){
            throw new ApiException("No Channel-listings found.");
        }
        ChannelListingDataResponse dataResponse = ConvertUtil.convert(channelListing, ChannelListingDataResponse.class);
        if (dataResponse != null) {
            ProductDataResponse product = productClient.getProductDetail(channelListing.getGlobalSkuId());
            if(product != null){
                dataResponse.setName(product.getName());
                dataResponse.setBrandId(product.getBrandId());
                return dataResponse;
            } else {
                logger.info("No Product found for id : " + channelListing.getGlobalSkuId());
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<ChannelListingDataResponse> getAllChannelWiseListingForClient(Long channelId, long clientId) {
        List<ChannelListing> listings = channelListingApi.getAllChannelListing(channelId, clientId);
        if(listings.isEmpty()){
            throw new ApiException("No Channel-listings found for channel.");
        }
        List<ChannelListingDataResponse> responses = new ArrayList<>();
        List<ChannelListingDataResponse> dataResponses = ConvertUtil.convert(listings, ChannelListingDataResponse.class);
        if (dataResponses != null) {
            for(int i=0; i<listings.size(); i++){
                ProductDataResponse product = productClient.getProductDetail(listings.get(i).getGlobalSkuId());
                if(product != null) {
                    dataResponses.get(i).setName(product.getName());
                    dataResponses.get(i).setBrandId(product.getBrandId());
                    responses.add(dataResponses.get(i));
                } else {
                    logger.info("No Product found for id : " + listings.get(i).getGlobalSkuId());
                }
            }
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public List<ChannelListingDataResponse> getAllChannelListingForChannel(Long channelId) {
        List<ChannelListing> listings = channelListingApi.getAllChannelListingForChannel(channelId);
        if(listings.isEmpty()){
            throw new ApiException("No Channel-listings found. Please add few first.");
        }
        List<ChannelListingDataResponse> responses = new ArrayList<>();
        List<ChannelListingDataResponse> dataResponses = ConvertUtil.convert(listings, ChannelListingDataResponse.class);
        if (dataResponses != null) {
            for(int i=0; i<listings.size(); i++){
                ProductDataResponse product = productClient.getProductDetail(listings.get(i).getGlobalSkuId());
                if(product != null) {
                    dataResponses.get(i).setName(product.getName());
                    dataResponses.get(i).setBrandId(product.getBrandId());
                    responses.add(dataResponses.get(i));
                }else {
                    logger.info("No Product found for id : " + listings.get(i).getGlobalSkuId());
                }
            }
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public List<ClientDataResponse> getAllChannelListingClients(Long channelId) {
        List<ClientDataResponse> resultList = new ArrayList<>();
        List<ClientDataResponse> clientList = clientClient.getClientDetails();
        if(clientList.isEmpty()){
            throw new ApiException("No Client found.");
        }
        for(ClientDataResponse client : clientList){
            if(channelListingApi.checkIfClientIdIsPresentInChannelListing(channelId, client.getId())){ resultList.add(client); }
        }
        return resultList;
    }

    public ClientDataResponse getClientDetails(Long clientId) {
        return clientClient.getClientDetails(clientId);
    }

    public ChannelListing getChannelListing(String channelSkuId, Long clientId) {
        return channelListingApi.getChannelListingData(channelSkuId, clientId);
    }

    public List<ProductDataResponse> getProductDetails(Long clientId) {
        return productClient.getProductDetails(clientId);
    }

    public ProductDataResponse getProductDetail(Long globalSkuId) {
        return productClient.getProductDetail(globalSkuId);
    }
}
