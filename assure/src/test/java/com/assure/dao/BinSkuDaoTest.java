package com.assure.dao;

import com.assure.pojo.BinSku;
import com.assure.service.AbstractUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class BinSkuDaoTest extends AbstractUnitTest {

    private BinSku binSku1, binSku2;
    @Autowired private BinSkuDao binSkuDao;

    @Before
    public void setUp(){
        binSku1 = new BinSku();
        binSku1.setBinId(1000L);
        binSku1.setGlobalSkuId(2L);
        binSku1.setQuantity(30L);

        binSku2 = new BinSku();
        binSku2.setBinId(1002L);
        binSku2.setGlobalSkuId(12L);
        binSku2.setQuantity(3L);
    }

    @Test
    public void testInsertBins() {
        BinSku newBinSku = binSkuDao.insert(binSku1);
        assertNotNull(newBinSku);
        assertTrue( newBinSku.getId()>0);
        assertEquals(binSku1.getBinId(), newBinSku.getBinId());
        assertEquals(binSku1.getGlobalSkuId(), newBinSku.getGlobalSkuId());
        assertEquals(binSku1.getQuantity(), newBinSku.getQuantity());
    }

    @Test
    public void testGetAllBinSkuByGlobalSkuId() {
        binSkuDao.insert(binSku1);
        List<BinSku> binSkuList = binSkuDao.getAllBinSkuByGlobalSkuId(binSku1.getGlobalSkuId());
        assertTrue(binSkuList.size()>0);
        assertEquals(binSku1.getGlobalSkuId(), binSkuList.get(0).getGlobalSkuId());
        assertEquals(binSku1.getBinId(), binSkuList.get(0).getBinId());
        assertEquals(binSku1.getQuantity(), binSkuList.get(0).getQuantity());
    }

    @Test
    public void testGetBinSkuDetails() {
        binSkuDao.insert(binSku1);
        BinSku binSkuInDb = binSkuDao.getBinSkuDetails(binSku1.getGlobalSkuId(), binSku1.getBinId());
        assertEquals(binSku1.getGlobalSkuId(), binSkuInDb.getGlobalSkuId());
        assertEquals(binSku1.getBinId(), binSkuInDb.getBinId());
        assertEquals(binSku1.getQuantity(), binSkuInDb.getQuantity());
    }

    @Test
    public void testGetAllBinSkuDetails() {
        binSkuDao.insert(binSku1);
        binSkuDao.insert(binSku2);
        List<BinSku> list = binSkuDao.findAll();
        assertTrue(list.size()>0);

        assertEquals(binSku1.getBinId(), list.get(0).getBinId());
        assertEquals(binSku1.getGlobalSkuId(), list.get(0).getGlobalSkuId());
        assertEquals(binSku1.getQuantity(), list.get(0).getQuantity());

        assertEquals(binSku2.getBinId(), list.get(1).getBinId());
        assertEquals(binSku2.getGlobalSkuId(), list.get(1).getGlobalSkuId());
        assertEquals(binSku2.getQuantity(), list.get(1).getQuantity());
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testNullValuedBinSku(){
        binSkuDao.insert(null);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testQuantityColumnNullConstraint(){
        BinSku binSku = new BinSku();
        binSku.setQuantity(null);
        binSku.setGlobalSkuId(5L);
        binSku.setBinId(1001L);
        binSkuDao.insert(binSku);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testGlobalSkuIdColumnNullConstraint(){
        BinSku binSku = new BinSku();
        binSku.setQuantity(10L);
        binSku.setGlobalSkuId(null);
        binSku.setBinId(1001L);
        binSkuDao.insert(binSku);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testBinIdColumnNullConstraint(){
        BinSku binSku = new BinSku();
        binSku.setQuantity(10L);
        binSku.setGlobalSkuId(5L);
        binSku.setBinId(null);
        binSkuDao.insert(binSku);
    }

    @Test(expected = NumberFormatException.class)
    public void testQuantityColumnEmptyConstraint(){
        BinSku binSku = new BinSku();
        binSku.setQuantity(Long.parseLong(""));
        binSku.setGlobalSkuId(5L);
        binSku.setBinId(1001L);
        binSkuDao.insert(binSku);
    }

    @Test(expected = NumberFormatException.class)
    public void testGlobalSkuIdColumnEmptyConstraint(){
        BinSku binSku = new BinSku();
        binSku.setQuantity(10L);
        binSku.setGlobalSkuId(Long.parseLong(""));
        binSku.setBinId(1001L);
        binSkuDao.insert(binSku);
    }

    @Test(expected = NumberFormatException.class)
    public void testBinIdColumnEmptyConstraint(){
        BinSku binSku = new BinSku();
        binSku.setQuantity(10L);
        binSku.setGlobalSkuId(5L);
        binSku.setBinId(Long.parseLong(""));
        binSkuDao.insert(binSku);
    }
}
