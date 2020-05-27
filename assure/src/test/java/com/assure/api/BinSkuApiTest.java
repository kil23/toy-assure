package com.assure.api;

import com.assure.dao.BinSkuDao;
import com.assure.pojo.BinSku;
import com.assure.service.AbstractUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class BinSkuApiTest extends AbstractUnitTest {

    private BinSku newBinSku, binSku;
    @Autowired private BinSkuApi binSkuApi;
    @Autowired private BinSkuDao binSkuDao;

    @Before
    public void setUp() {
        binSku = createObject(1000L, 5L, 15L);
    }

    public BinSku createObject(Long binId, Long globalSkuId, Long quantity) {
        BinSku binSku = new BinSku();
        binSku.setBinId(binId);
        binSku.setQuantity(quantity);
        binSku.setGlobalSkuId(globalSkuId);
        return binSku;
    }

    @Test
    public void testAddBinSku() {
        newBinSku = binSkuApi.addBinSku(binSku);
        assertNotNull(newBinSku);
    }

    @Test()
    public void testUpdateBinSku() {
        newBinSku = binSkuDao.insert(binSku);
        newBinSku.setBinId(1001L);
        newBinSku.setQuantity(6L);
        newBinSku.setGlobalSkuId(5L);
        binSkuApi.updateBinSku(newBinSku);
        assertNotNull(binSkuApi.getBinSkuDetailsById(newBinSku.getId()));
    }

    @Test
    public void testGetBinwiseInventory() {
        newBinSku = binSkuDao.insert(binSku);
        List<BinSku> list = binSkuApi.getBinwiseInventory(newBinSku.getBinId());
        assertNotNull(list);
    }

    @Test
    public void testGetBinSkuDetailsById() {
        newBinSku = binSkuDao.insert(binSku);
        BinSku binSku = binSkuApi.getBinSkuDetailsById(newBinSku.getId());
        assertNotNull(binSku);
    }

    @Test
    public void testGetBinSkuDetails() {
        newBinSku = binSkuDao.insert(binSku);
        List<BinSku> list = binSkuApi.getAllBinSkuDetails(newBinSku.getGlobalSkuId());
        assertNotNull(list);
    }

    @Test
    public void testGetBinSkuData() {
        newBinSku = binSkuDao.insert(binSku);
        BinSku binSku = binSkuApi.getBinSkuData(newBinSku.getGlobalSkuId(), newBinSku.getBinId());
        assertNotNull(binSku);
    }
}
