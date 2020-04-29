package com.channel.dao;

import com.channel.pojo.ChannelListing;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ChannelListingDao extends AbstractDao{

    private static final Logger logger = Logger.getLogger(ChannelListingDao.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insertListing(ChannelListing pojo){
        logger.info("inserting-new-channel-listing");
        em.persist(pojo);
        em.flush();
    }

    @Transactional
    public int deleteListing(long listingId) {
        logger.info("deleting-channel-listing-with-id("+listingId+")");
        String delete_id = "delete from ChannelListing l where id=:id";
        Query query = em.createQuery(delete_id);
        query.setParameter("id", listingId);
        return query.executeUpdate();
    }

    public ChannelListing getChannelListingById(long listingId) {
        logger.info("getting-channel-listing-data-with-id("+listingId+")");
        String select_id = "Select c From ChannelListing c where id=:id";
        TypedQuery<ChannelListing> query = getQuery(select_id, ChannelListing.class);
        query.setParameter("id", listingId);
        return getSingle(query);
    }

    public List<ChannelListing> getChannelListingByChannelId(long channelId) {
        logger.info("getting-all-channel-listing-data-with-channel-id("+channelId+")");
        String select_id = "Select c From ChannelListing c where channel_id=:id";
        TypedQuery<ChannelListing> query = getQuery(select_id, ChannelListing.class);
        query.setParameter("id", channelId);
        return query.getResultList();
    }

    public List<ChannelListing> getChannelListingByChannelIdAndClientId(long channelId, long clientId) {
        logger.info("getting-all-channel-listing-data-with-channel-id("+channelId+")-and-client_id("+clientId+")");
        String select_id = "Select c From ChannelListing c where channel_id='"+channelId+"' AND client_id='"+clientId+"'";
        TypedQuery<ChannelListing> query = getQuery(select_id, ChannelListing.class);
        return query.getResultList();
    }

    public List<ChannelListing> getDistinctChannelListingByClientId(long clientId) {
        logger.info("getting-distinct-channel-listing-data-with-client_id("+clientId+")");
        String select_id = "Select DISTINCT c From ChannelListing c where client_id='"+clientId+"'";
        Query query = em.createQuery(select_id);
        return query.getResultList();
    }

    public ChannelListing getChannelListing(long globalSkuId, long channelId) {
        logger.info("getting-all-channel-listing-data-with-global-sku-id("+globalSkuId+")-and-channel-id("+channelId+")");
        String select_id = "Select c From ChannelListing c where global_sku_id='"+globalSkuId+"' AND channel_id='"+channelId+"'";
        TypedQuery<ChannelListing> query = getQuery(select_id, ChannelListing.class);
        return getSingle(query);
    }
}
