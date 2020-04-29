package com.channel.dao;

import com.channel.pojo.Channel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ChannelDao extends AbstractDao{

    private static final Logger logger = Logger.getLogger(ChannelDao.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insertChannel(Channel pojo) {
        logger.info("inserting-new-channel");
        em.persist(pojo);
        em.flush();
    }

    @Transactional
    public void updateChannel(Channel pojo) {
        logger.info("updating-channel-with-id("+pojo.getId()+")");
        em.merge(pojo);
        em.flush();
    }

    public Channel getChannelDetailsById(long channelId) {
        logger.info("getting-channel-data-by-id("+channelId+")");
        String select_id = "Select c From Channel c where id=:id";
        TypedQuery<Channel> query = getQuery(select_id, Channel.class);
        query.setParameter("id", channelId);
        return getSingle(query);
    }

    public List<Channel> getAllChannelDetails() {
        logger.info("getting-all-channel-data");
        String select_all = "select c from Channel c";
        TypedQuery<Channel> query = getQuery(select_all, Channel.class);
        return query.getResultList();
    }

    public Channel getChannelDetailsByChannelName(String channelName) {
        logger.info("getting-channel-data-by-name("+channelName+")");
        String select_id = "Select c From Channel c where name='"+channelName+"'";
        TypedQuery<Channel> query = getQuery(select_id, Channel.class);
        return getSingle(query);
    }
}
