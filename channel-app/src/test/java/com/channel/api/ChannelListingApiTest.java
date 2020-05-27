package com.channel.api;

import com.channel.dao.ChannelListingDao;
import com.channel.pojo.ChannelListing;
import com.channel.service.AbstractUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class ChannelListingApiTest extends AbstractUnitTest {

    private ChannelListing channelListing1, channelListing2, channelListing3;

    @Autowired
    private ChannelListingApi listingApi;

    @Autowired
    private ChannelListingDao channelListingDao;

    @Before
    public void setUp(){
        channelListing1 = createObject(1L, 2L, 3L, "abc");
        channelListing2 = createObject(1L, 5L, 6L, "pqr");
        channelListing3 = createObject(7L, 8L, 6L, "xyz");
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
    public void testAddListing() {
        ChannelListing newChannelListing = listingApi.addListing(channelListing1);
        assertNotNull(newChannelListing);
        assertTrue( newChannelListing.getId()>0);
        assertEquals(channelListing1.getClientId(), newChannelListing.getClientId());
        assertEquals(channelListing1.getGlobalSkuId(), newChannelListing.getGlobalSkuId());
        assertEquals(channelListing1.getChannelId(), newChannelListing.getChannelId());
        assertEquals(channelListing1.getChannelSkuId(), newChannelListing.getChannelSkuId());
    }

    @Test
    public void testDeleteListing() {
        channelListingDao.insert(channelListing1);
        listingApi.deleteListing(channelListing1);
        ChannelListing listing = channelListingDao.findOne(channelListing1.getId());
        assertNull(listing);
    }

    @Test
    public void testGetChannelListing() {
        channelListingDao.insert(channelListing2);
        ChannelListing listing = listingApi.getChannelListing(channelListing2.getGlobalSkuId(), channelListing2.getChannelId());
        assertNotNull(listing);
        assertEquals(channelListing2.getClientId(), listing.getClientId());
        assertEquals(channelListing2.getGlobalSkuId(), listing.getGlobalSkuId());
        assertEquals(channelListing2.getChannelId(), listing.getChannelId());
        assertEquals(channelListing2.getChannelSkuId(), listing.getChannelSkuId());
    }

    @Test
    public void testGetListing(){
        channelListingDao.insert(channelListing3);
        ChannelListing listing = listingApi.getListing(channelListing3.getId());
        assertNotNull(listing);
        assertEquals(channelListing3.getClientId(), listing.getClientId());
        assertEquals(channelListing3.getGlobalSkuId(), listing.getGlobalSkuId());
        assertEquals(channelListing3.getChannelId(), listing.getChannelId());
        assertEquals(channelListing3.getChannelSkuId(), listing.getChannelSkuId());
    }

    @Test
    public void testGetAllChannelListings() {
        channelListingDao.insert(channelListing1);
        List<ChannelListing> channelListings = listingApi.getAllChannelListing(
                channelListing1.getChannelId(), channelListing1.getClientId());
        assertTrue(channelListings.size()>0);
        assertEquals(channelListing1.getClientId(), channelListings.get(0).getClientId());
        assertEquals(channelListing1.getGlobalSkuId(), channelListings.get(0).getGlobalSkuId());
        assertEquals(channelListing1.getChannelId(), channelListings.get(0).getChannelId());
        assertEquals(channelListing1.getChannelSkuId(), channelListings.get(0).getChannelSkuId());
    }

    @Test
    public void testGetChannelListingForChannel() {
        channelListingDao.insert(channelListing2);
        List<ChannelListing> channelListings = listingApi.getAllChannelListingForChannel(channelListing2.getChannelId());
        assertTrue(channelListings.size()>0);
        assertEquals(channelListing2.getClientId(), channelListings.get(0).getClientId());
        assertEquals(channelListing2.getGlobalSkuId(), channelListings.get(0).getGlobalSkuId());
        assertEquals(channelListing2.getChannelId(), channelListings.get(0).getChannelId());
        assertEquals(channelListing2.getChannelSkuId(), channelListings.get(0).getChannelSkuId());
    }

    @Test
    public void testCheckIfClientIdIsPresentInChannelListing() {
        channelListingDao.insert(channelListing1);
        boolean res = listingApi.checkIfClientIdIsPresentInChannelListing(channelListing1.getChannelId(),
                channelListing1.getClientId());
        assertTrue(res);
    }

    @Test
    public void testGetUniqueChannelIdByClientId() {
        channelListingDao.insert(channelListing1);
        channelListingDao.insert(channelListing2);
        channelListingDao.insert(channelListing3);
        Set<Long> uniqueChannelId = listingApi.getUniqueChannelIdByClientId(channelListing1.getClientId());
        assertTrue(uniqueChannelId.size()>0);
    }

}
