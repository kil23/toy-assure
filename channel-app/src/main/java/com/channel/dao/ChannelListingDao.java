package com.channel.dao;

import com.channel.pojo.ChannelListing;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ChannelListingDao extends AbstractDao<ChannelListing> {

    private static final String SELECT_BY_CHANNELID = "Select c From ChannelListing c where c.channelId=:channelId";
    private static final String SELECT_ALL_CHANNELID_CLIENTID = "Select c From ChannelListing c where c.channelId=:channelId AND c.clientId=:clientId";
    private static final String SELECT_ALL_DISTINCT_BY_CLIENTID = "Select DISTINCT c From ChannelListing c where c.clientId=:clientId";
    private static final String SELECT_BY_GLOBALSKUID_CHANNELID = "Select c From ChannelListing c where c.globalSkuId=:globalSkuId AND c.channelId=:channelId";
    private static final String SELECT_BY_CHANNELSKUID = "Select c From ChannelListing c where c.channelSkuId=:id AND c.clientId=:clientId";

    public ChannelListingDao(){
        super(ChannelListing.class);
    }

    @Transactional(readOnly = true)
    public List<ChannelListing> getChannelListing(long channelId) {
        TypedQuery<ChannelListing> query = getQuery(SELECT_BY_CHANNELID);
        query.setParameter("channelId", channelId);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public List<ChannelListing> getChannelListings(long channelId, long clientId) {
        TypedQuery<ChannelListing> query = getQuery(SELECT_ALL_CHANNELID_CLIENTID);
        query.setParameter("channelId", channelId);
        query.setParameter("clientId", clientId);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public List<ChannelListing> getDistinctChannelListings(long clientId) {
        Query query = em.createQuery(SELECT_ALL_DISTINCT_BY_CLIENTID);
        query.setParameter("clientId", clientId);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public ChannelListing getChannelListing(long globalSkuId, long channelId) {
        TypedQuery<ChannelListing> query = getQuery(SELECT_BY_GLOBALSKUID_CHANNELID);
        query.setParameter("globalSkuId", globalSkuId);
        query.setParameter("channelId", channelId);
        return getSingle(query);
    }

    public ChannelListing getChannelListingData(String channelSkuId, Long clientId) {
        TypedQuery<ChannelListing> query = getQuery(SELECT_BY_CHANNELSKUID);
        query.setParameter("id", channelSkuId);
        query.setParameter("clientId", clientId);
        return getSingle(query);
    }
}
