package com.channel.api;

import com.channel.dao.ChannelDao;
import com.channel.pojo.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChannelApi {

    @Autowired
    private ChannelDao channelDao;

    @Transactional
    public Channel addChannel(Channel channelToInsert) {
        return channelDao.insert(channelToInsert);
    }

    @Transactional
    public void updateChannel(Channel channelToInsert) {
        channelDao.update(channelToInsert);
    }

    @Transactional(readOnly = true)
    public Channel getChannelById(Long channelId) {
        return channelDao.findOne(channelId);
    }

    @Transactional(readOnly = true)
    public List<Channel> getAllChannelDetails() {
        return channelDao.findAll();
    }

    @Transactional(readOnly = true)
    public Channel getChannelDetails(String name) {
        return channelDao.getChannelDetail(name);
    }
}
