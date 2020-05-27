package com.assure.dao;

import com.assure.pojo.Bin;
import com.assure.service.AbstractUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BinsDaoTest extends AbstractUnitTest {

    @Autowired private BinsDao binsDao;

    @Before
    public void setUp(){
        Bin bin = new Bin();
    }

    @Test
    public void testInsertBins() {
        Bin newBin = binsDao.insert(new Bin());
        assertNotNull(newBin);
        assertTrue( newBin.getBinId()>=1000);
    }

    @Test
    public void testGetAllBins() {
        List<Bin> list = binsDao.getAllBins();
        assertNotNull(list);
    }
}
