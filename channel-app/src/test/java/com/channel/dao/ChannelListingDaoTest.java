package com.channel.dao;

import com.channel.pojo.ChannelListing;
import com.channel.service.AbstractUnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ChannelListingDaoTest extends AbstractUnitTest {

    private ChannelListing channelListing1, channelListing2;

    @Autowired
    private ChannelListingDao channelListingDao;

    @Before
    public void setUp(){
        channelListing1 = createObject(1L, 2L, 3L, "abc");
        channelListing2 = createObject(4L, 5L, 6L, "pqr");
    }

    public ChannelListing createObject(Long clientId, Long globalSkuId, Long channelId, String channelSkuId) {
        ChannelListing channelListing = new ChannelListing();
        channelListing.setClientId(clientId);
        channelListing.setGlobalSkuId(globalSkuId);
        channelListing.setChannelId(channelId);
        channelListing.setChannelSkuId(channelSkuId);
        return channelListing;
    }

    @Test
    public void testInsertChannelListings() {
        ChannelListing newChannelListing = channelListingDao.insert(channelListing1);
        Assert.assertNotNull(newChannelListing);
        Assert.assertTrue( newChannelListing.getId()>0);
        Assert.assertEquals(channelListing1.getClientId(), newChannelListing.getClientId());
        Assert.assertEquals(channelListing1.getGlobalSkuId(), newChannelListing.getGlobalSkuId());
        Assert.assertEquals(channelListing1.getChannelId(), newChannelListing.getChannelId());
        Assert.assertEquals(channelListing1.getChannelSkuId(), newChannelListing.getChannelSkuId());
    }

    @Test
    public void testGetChannelListingDetail() {
        channelListingDao.insert(channelListing2);
        List<ChannelListing> channelListings = channelListingDao.getChannelListing(channelListing2.getChannelId());
        Assert.assertTrue(channelListings.size()>0);
        Assert.assertEquals(channelListing2.getClientId(), channelListings.get(0).getClientId());
        Assert.assertEquals(channelListing2.getGlobalSkuId(), channelListings.get(0).getGlobalSkuId());
        Assert.assertEquals(channelListing2.getChannelId(), channelListings.get(0).getChannelId());
        Assert.assertEquals(channelListing2.getChannelSkuId(), channelListings.get(0).getChannelSkuId());
    }

    @Test
    public void testGetChannelListings() {
        channelListingDao.insert(channelListing1);
        List<ChannelListing> channelListings = channelListingDao.getChannelListings(
                channelListing1.getChannelId(), channelListing1.getClientId());
        Assert.assertTrue(channelListings.size()>0);
        Assert.assertEquals(channelListing1.getClientId(), channelListings.get(0).getClientId());
        Assert.assertEquals(channelListing1.getGlobalSkuId(), channelListings.get(0).getGlobalSkuId());
        Assert.assertEquals(channelListing1.getChannelId(), channelListings.get(0).getChannelId());
        Assert.assertEquals(channelListing1.getChannelSkuId(), channelListings.get(0).getChannelSkuId());
    }

    @Test
    public void testGetChannelListing() {
        channelListingDao.insert(channelListing2);
        ChannelListing listing = channelListingDao.getChannelListing(channelListing2.getGlobalSkuId(), channelListing2.getChannelId());
        Assert.assertNotNull(listing);
        Assert.assertEquals(channelListing2.getClientId(), listing.getClientId());
        Assert.assertEquals(channelListing2.getGlobalSkuId(), listing.getGlobalSkuId());
        Assert.assertEquals(channelListing2.getChannelId(), listing.getChannelId());
        Assert.assertEquals(channelListing2.getChannelSkuId(), listing.getChannelSkuId());
    }

    @Test
    public void testGetAllChannelListingDetails() {
        channelListingDao.insert(channelListing1);
        channelListingDao.insert(channelListing2);
        List<ChannelListing> list = channelListingDao.findAll();

        Assert.assertTrue(list.size()>0);

        Assert.assertEquals(channelListing1.getClientId(), list.get(0).getClientId());
        Assert.assertEquals(channelListing1.getGlobalSkuId(), list.get(0).getGlobalSkuId());
        Assert.assertEquals(channelListing1.getChannelId(), list.get(0).getChannelId());
        Assert.assertEquals(channelListing1.getChannelSkuId(), list.get(0).getChannelSkuId());

        Assert.assertEquals(channelListing2.getClientId(), list.get(1).getClientId());
        Assert.assertEquals(channelListing2.getGlobalSkuId(), list.get(1).getGlobalSkuId());
        Assert.assertEquals(channelListing2.getChannelId(), list.get(1).getChannelId());
        Assert.assertEquals(channelListing2.getChannelSkuId(), list.get(1).getChannelSkuId());
    }
}
