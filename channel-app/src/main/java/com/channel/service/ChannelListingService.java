package com.channel.service;

import com.channel.dao.ChannelListingDao;
import com.channel.model.form.ChannelListingForm;
import com.channel.pojo.Channel;
import com.channel.pojo.ChannelListing;
import com.channel.model.response.ChannelListingDataResponse;
import com.channel.socket.Clients;
import com.channel.socket.Products;
import com.commons.response.ClientDataResponse;
import com.commons.response.ProductDataResponse;
import com.commons.service.ApiException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.commons.util.Validation.validateChannelSkuId;
import static com.commons.util.Validation.validateClientSkuId;

@Service
public class ChannelListingService {

    private static final Logger logger = Logger.getLogger(ChannelListingService.class);

    @Autowired
    private ChannelListingDao listingDao;

    @Autowired
    private ChannelService channelService;

    public ChannelListing convertFormToPojo(ChannelListingForm formData, Long channelId, Long globalSkuId) throws ApiException {
        Channel channel = channelService.getChannelById(channelId);
        ChannelListing listingPojo = new ChannelListing();
        listingPojo.setChannelId(channel.getId());
        listingPojo.setChannelSkuId(formData.getChannelSkuId());
        listingPojo.setClientId(formData.getClientId());
        listingPojo.setGlobalSkuId(globalSkuId);
        return listingPojo;
    }

    public ChannelListingDataResponse convertPojoToFormData(ChannelListing listingPojo) {
        ChannelListingDataResponse listingData = new ChannelListingDataResponse();
        listingData.setListingId(listingPojo.getId());
        listingData.setChannelSkuId(listingPojo.getChannelSkuId());
        listingData.setClientId(listingPojo.getClientId());
        listingData.setGlobalSkuId(listingPojo.getGlobalSkuId());
        return listingData;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void insertListing(ChannelListingForm formData, Long channelId) throws ApiException {
        logger.info("insert-channel-listing");
        validateClientSkuId(formData.getClientSkuId());
        ProductDataResponse productDataResponse = Products.getProductByClientSkuId(formData.getClientSkuId(), formData.getClientId());
        if(productDataResponse == null){
            logger.info("No Products found with given id.");
            throw new ApiException("Products with given clientId("+formData.getClientId()+") and clientSKuId("+formData.getClientSkuId()+") does not exist in the database. Please add Products first.");
        }else{
            logger.info("Matching product found.");
            ChannelListing listingPojo = convertFormToPojo(formData, channelId, productDataResponse.getGlobalSkuId());
            validateChannelSkuId(listingPojo.getChannelSkuId());
            listingDao.insertListing(listingPojo);
        }
    }

    @Transactional(rollbackOn = ApiException.class)
    public void deleteListing(Long listingId) throws ApiException {
        logger.info("delete-a-channel-listing");
        ChannelListing listingPojo = listingDao.getChannelListingById(listingId);
        if(listingPojo != null){
            logger.info("deleting channel-listing with listingId : "+ listingId);
            int res = listingDao.deleteListing(listingId);
            logger.info("Records deleted : "+res);
        }else{
            throw new ApiException("No Channel-listing exists with listingId : "+listingId);
        }
    }

    public ChannelListingDataResponse getListing(Long listingId) throws ApiException {
        logger.info("get-channel-listing-by-id");
        ChannelListing listing = listingDao.getChannelListingById(listingId);
        if(listing==null){
            logger.info("No Channel-listing found with listing-id : "+listingId);
            throw new ApiException("No Channel-listings found.");
        }
        return convertPojoToFormData(listing);
    }

    public List<ChannelListingDataResponse> getAllChannelWiseListing(Long channelId, Long clientId) throws ApiException {
        logger.info("get-All-channel-listings");
        List<ChannelListing> list = getAllChannelListing(channelId, clientId);
        if(list.isEmpty()){
            logger.info("No Channel-listings found for channel.");
            throw new ApiException("No Channel-listings found for channel.");
        }
        List<ChannelListingDataResponse> list2 = new ArrayList<>();
        for (ChannelListing listing : list) {
            list2.add(convertPojoToFormData(listing));
        }
        return list2;
    }

    public List<ChannelListing> getAllChannelListing(Long channelId, Long clientId) {
        logger.info("get-All-channel-listings-by-channelId-and-clientId");
        List<ChannelListing> channelListings =  listingDao.getChannelListingByChannelIdAndClientId(channelId, clientId);
        if(channelListings.isEmpty()){
            logger.info("No Channel-listings found.");
        }
        return channelListings;
    }

    public List<ChannelListingDataResponse> getAllChannelListingForChannel(Long channelId) throws ApiException {
        logger.info("getting-a-list-of-all-the-clients-with-channel-listings");
        List<ChannelListingDataResponse> resultList = new ArrayList<>();
        logger.info("getting-all-clients");
        List<ChannelListing> channelListings = listingDao.getChannelListingByChannelId(channelId);
        if(channelListings.isEmpty()){
            logger.info("No Channel-listings found for channel-id : "+channelId);
            throw new ApiException("No Channel-listings found. Please add few first.");
        }
        for(ChannelListing data : channelListings){
            resultList.add(convertPojoToFormData(data));
        }
        return resultList;
    }

    public List<ClientDataResponse> getAllChannelListingClients(Long channelId) throws ApiException {
        logger.info("getting-a-list-of-all-the-clients-with-channel-listings");
        List<ClientDataResponse> resultList = new ArrayList<>();
        logger.info("getting-all-clients");
        List<ClientDataResponse> clientList = Clients.getClientDetails();
        if(clientList==null || clientList.isEmpty()){
            logger.info("No Client found.");
            throw new ApiException("No Client found.");
        }else{
            for(ClientDataResponse client : clientList){
                if(checkIfClientIdIsPresentInChannelListing(channelId, client.getId())){
                    resultList.add(client);
                }
            }
            return resultList;
        }
    }

    private boolean checkIfClientIdIsPresentInChannelListing(Long channelId, Long clientId) {
        List<ChannelListing> list = listingDao.getChannelListingByChannelIdAndClientId(channelId, clientId);
        if(list.isEmpty()){
            logger.info("No Client with Channel-listings found. Please create channel-listings for few clients first.");
            return false;
        }else{
            logger.info("Client("+clientId+") has "+list.size()+" matching Channel-listings records.");
            return true;
        }
    }

    public Set<Long> getUniqueChannelIdByClientId(Long clientId) throws ApiException {
        List<ChannelListing> list = listingDao.getDistinctChannelListingByClientId(clientId);
        if(list.isEmpty()){
            logger.info("No Unique channel-id found for client-id("+clientId+")");
            throw new ApiException("No Unique Channel-id found.");
        }
        List<Long> channelList = new ArrayList<>();
        for(ChannelListing pojo : list){
            channelList.add(pojo.getChannelId());
        }
        return new HashSet<>(channelList);
    }

    public ChannelListing getChannelListing(Long globalSkuId, Long channelId) {
        ChannelListing listing = listingDao.getChannelListing(globalSkuId, channelId);
        if(listing == null){
            logger.info("No Channel-listing found for globalSkuId: "+globalSkuId+" and channelId: "+channelId);
            return null;
        }
        return listing;
    }
}
