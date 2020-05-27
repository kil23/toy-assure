package com.channel.dao;

import com.channel.pojo.Channel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;

@Repository
public class ChannelDao extends AbstractDao<Channel> {

    private static final String SELECT_BY_CHANNEL_NAME = "Select c From Channel c where c.name=:name";

    public ChannelDao(){
        super(Channel.class);
    }

    @Transactional(readOnly = true)
    public Channel getChannelDetail(String channelName) {
        TypedQuery<Channel> query = getQuery(SELECT_BY_CHANNEL_NAME);
        query.setParameter("name", channelName);
        return getSingle(query);
    }
}
