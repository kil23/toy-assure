package com.channel.dto;

import com.channel.api.ChannelApi;
import com.channel.dao.ChannelDao;
import com.channel.model.form.AddChannelForm;
import com.channel.pojo.Channel;
import com.channel.service.AbstractUnitTest;
import com.commons.enums.InvoiceType;
import com.commons.form.ChannelForm;
import com.commons.response.ChannelDataResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class ChannelDtoTest extends AbstractUnitTest {

    private ChannelForm channel1Form, channel2Form;
    private Channel channel1, channel2, channel3, resultantChannel1;
    private ChannelDataResponse resultantChannel2Response;
    private AddChannelForm addChannelForm;

    List<ChannelForm> channelFormList;

    @InjectMocks
    private ChannelDto channelDto;

    @Mock
    private ChannelApi channelApi;

    @Autowired
    private ChannelDao channelDao;

    public ChannelDtoTest() {
    }

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        addChannelForm = new AddChannelForm();
        channelFormList = new ArrayList<>();
        channel1Form = createObject("channel1", InvoiceType.CHANNEL);
        channel2Form = createObject("channel2", InvoiceType.CHANNEL);
        channel1 = createObject(1L, "channel1", InvoiceType.CHANNEL);
        channel2 = createObject(2L, "channel2", InvoiceType.CHANNEL);
        resultantChannel1 = createObject(4L, "channel1", InvoiceType.CHANNEL);
        resultantChannel2Response = new ChannelDataResponse();
        resultantChannel2Response.setName("channel2");
        resultantChannel2Response.setId(2L);
        resultantChannel2Response.setInvoiceType(InvoiceType.CHANNEL);
        channel3 = new Channel();
        channel3.setName("channel3");
        channel3.setInvoiceType(InvoiceType.SELF);

        channelFormList.add(channel1Form);
        channelFormList.add(channel2Form);
        addChannelForm.setChannelList(channelFormList);
    }

    public Channel createObject(Long id, String name, InvoiceType type) {
        Channel channel = new Channel();
        channel.setId(id);
        channel.setName(name);
        channel.setInvoiceType(type);
        return channel;
    }

    public ChannelForm createObject(String name, InvoiceType type) {
        ChannelForm channel = new ChannelForm();
        channel.setName(name);
        channel.setInvoiceType(type);
        return channel;
    }

    @Test
    public void testAddChannel() {
        Channel newChannel = channelDao.getChannelDetail(channel1.getName());
        when(channelApi.getChannelDetails(channel1.getName())).thenReturn(newChannel);
        when(channelApi.addChannel(channel1)).thenReturn(channelDao.insert(channel3));
        channelDto.addChannel(addChannelForm);
    }

    @Test
    public void testUpdateChannel() {
        Long channelId = 2L;
        channelDto.addChannel(addChannelForm);
        when(channelApi.getChannelById(channel2.getId())).thenReturn(channel2);
        when(channelApi.getChannelDetails(channel2.getName())).thenReturn(null);
        channelDto.updateChannel(channel2Form, channelId);
    }

    @Test
    public void testGetChannelDetails() {
        Long channelId = 2L;
        channelDto.addChannel(addChannelForm);
        when(channelApi.getChannelById(channelId)).thenReturn(channel2);

        ChannelDataResponse response = channelDto.getChannelDetails(channelId);
        Assert.assertEquals(resultantChannel2Response.getId(), response.getId());
        Assert.assertEquals(resultantChannel2Response.getName(), response.getName());
        Assert.assertEquals(resultantChannel2Response.getInvoiceType(), response.getInvoiceType());

    }
}
