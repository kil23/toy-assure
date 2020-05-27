package com.channel.dao;

import com.channel.pojo.Channel;
import com.channel.service.AbstractUnitTest;
import com.commons.enums.InvoiceType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ChannelDaoTest extends AbstractUnitTest {

    private Channel channel1, channel2;

    @Autowired
    private ChannelDao channelDao;

    @Before
    public void setUp(){
        channel1 = createObject("test1", InvoiceType.CHANNEL);
        channel2 = createObject("test2", InvoiceType.SELF);
    }

    public Channel createObject(String name, InvoiceType type) {
        Channel channel = new Channel();
        channel.setName(name);
        channel.setInvoiceType(type);
        return channel;
    }

    @Test
    public void testInsertChannel() {
        Channel newChannel = channelDao.insert(channel1);
        Assert.assertNotNull(newChannel);
        Assert.assertTrue( newChannel.getId()>0);
        Assert.assertEquals(channel1.getName(), newChannel.getName());
        Assert.assertEquals(channel1.getInvoiceType(), newChannel.getInvoiceType());
    }

    @Test
    public void testGetChannelDetail() {
        channelDao.insert(channel1);
        Channel channel = channelDao.getChannelDetail(channel1.getName());
        Assert.assertNotNull(channel);
        Assert.assertEquals(channel1.getName(), channel.getName());
        Assert.assertEquals(channel1.getInvoiceType(), channel.getInvoiceType());
    }

    @Test
    public void testGetAllChannelDetails() {
        channelDao.insert(channel1);
        channelDao.insert(channel2);
        List<Channel> list = channelDao.findAll();

        Assert.assertTrue(list.size()>0);
        Assert.assertEquals(channel1.getName(), list.get(0).getName());
        Assert.assertEquals(channel1.getInvoiceType(), list.get(0).getInvoiceType());
        Assert.assertEquals(channel2.getName(), list.get(1).getName());
        Assert.assertEquals(channel2.getInvoiceType(), list.get(1).getInvoiceType());
    }

}
