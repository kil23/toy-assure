package com.channel.api;

import com.channel.dao.ChannelListingDao;
import com.channel.pojo.ChannelListing;
import com.commons.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ChannelListingApi {

    @Autowired
    private ChannelListingDao listingDao;

    @Transactional
    public ChannelListing addListing(ChannelListing channelListing) {
        return listingDao.insert(channelListing);
    }

    @Transactional
    public void deleteListing(ChannelListing listing) {
        listingDao.delete(listing);
    }

    @Transactional(readOnly = true)
    public ChannelListing getChannelListing(Long globalSkuId, Long channelId) {
        return listingDao.getChannelListing(globalSkuId, channelId);
    }

    @Transactional(readOnly = true)
    public ChannelListing getListing(Long listingId) {
        return listingDao.findOne(listingId);
    }

    @Transactional(readOnly = true)
    public List<ChannelListing> getAllChannelListing(Long channelId, Long clientId) {
        return listingDao.getChannelListings(channelId, clientId);
    }

    @Transactional(readOnly = true)
    public List<ChannelListing> getAllChannelListingForChannel(Long channelId) {
        return listingDao.getChannelListing(channelId);
    }

    @Transactional(readOnly = true)
    public Set<Long> getUniqueChannelIdByClientId(Long clientId) {
        List<ChannelListing> list = listingDao.getDistinctChannelListings(clientId);
        if(list.isEmpty()){
            throw new ApiException("No Unique Channel-id found.");
        }
        Set<Long> channelSet = new HashSet<>();
        for(ChannelListing pojo : list){
            channelSet.add(pojo.getChannelId());
        }
        return channelSet;
    }

    @Transactional(readOnly = true)
    public boolean checkIfClientIdIsPresentInChannelListing(Long channelId, Long clientId) {
        List<ChannelListing> list = listingDao.getChannelListings(channelId, clientId);
        return !list.isEmpty();
    }

    public ChannelListing getChannelListingData(String channelSkuId, Long clientId) {
        return listingDao.getChannelListingData(channelSkuId, clientId);
    }
}
