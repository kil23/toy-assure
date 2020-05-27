package com.assure.api;

import com.assure.dao.BinsDao;
import com.assure.pojo.Bin;
import com.assure.service.AbstractUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class BinApiTest extends AbstractUnitTest {

    private Bin bin;
    @Autowired private BinApi binApi;
    @Autowired private BinsDao binsDao;

    @Before
    public void setUp(){
        bin = new Bin();
    }

    @Test
    public void testCreateBins() {
        Bin newBin = binApi.createBins(bin);
        assertTrue(bin.getBinId()>=1000);
        assertNotNull(newBin);
    }

    @Test
    public void testGetAllBins() {
        binsDao.insert(bin);
        List<Bin> list = binApi.getAllBin();
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    public void testGetBin() {
        Bin newBin = binsDao.insert(bin);
        Bin result = binApi.getBin(newBin.getBinId());
        assertNotNull(result);
    }
}
