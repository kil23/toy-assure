package com.channel.api;

import com.channel.dao.ChannelDao;
import com.channel.pojo.Channel;
import com.channel.service.AbstractUnitTest;
import com.commons.enums.InvoiceType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ChannelApiTest extends AbstractUnitTest {

    private Channel channel1, channel2;

    @Autowired
    private ChannelApi channelApi;

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
    public void testAddChannel() {
        Channel newChannel = channelApi.addChannel(channel1);
        assertNotNull(newChannel);
        assertTrue( newChannel.getId()>0);
        assertEquals(channel1.getName(), newChannel.getName());
        assertEquals(channel1.getInvoiceType(), newChannel.getInvoiceType());
    }

    @Test
    public void testUpdateChannel() {
        Channel newChannel = channelApi.addChannel(channel2);
        newChannel.setName("updatedName");
        newChannel.setInvoiceType(InvoiceType.CHANNEL);
        channelApi.updateChannel(newChannel);
        Channel result = channelDao.findOne(newChannel.getId());
        assertNotNull(result);
        assertEquals(newChannel.getId(), result.getId());
        assertEquals(newChannel.getName(), result.getName());
        assertEquals(newChannel.getInvoiceType(), result.getInvoiceType());
    }

    @Test
    public void testGetChannelById() {
        channelDao.insert(channel1);
        Channel channel = channelApi.getChannelById(channel1.getId());
        assertNotNull(channel);
        assertEquals(channel1.getName(), channel.getName());
        assertEquals(channel1.getInvoiceType(), channel.getInvoiceType());
    }

    @Test
    public void testGetChannelDetail() {
        channelDao.insert(channel1);
        Channel channel = channelApi.getChannelDetails(channel1.getName());
        assertNotNull(channel);
        assertEquals(channel1.getName(), channel.getName());
        assertEquals(channel1.getInvoiceType(), channel.getInvoiceType());
    }

    @Test
    public void testGetAllChannelDetails() {
        channelDao.insert(channel1);
        channelDao.insert(channel2);
        List<Channel> list = channelApi.getAllChannelDetails();

        assertTrue(list.size()>0);
        assertEquals(channel1.getName(), list.get(0).getName());
        assertEquals(channel1.getInvoiceType(), list.get(0).getInvoiceType());
        assertEquals(channel2.getName(), list.get(1).getName());
        assertEquals(channel2.getInvoiceType(), list.get(1).getInvoiceType());
    }

}
