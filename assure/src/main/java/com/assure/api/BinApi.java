package com.assure.api;

import com.assure.dao.BinsDao;
import com.assure.pojo.Bin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BinApi {

    @Autowired private BinsDao binsDao;

    @Transactional
    public Bin createBins(Bin bin) {
        return binsDao.insert(bin);
    }

    @Transactional(readOnly = true)
    public List<Bin> getAllBin()  {
        return binsDao.getAllBins();
    }

    @Transactional(readOnly = true)
    public Bin getBin(long binId) {
        return binsDao.findOne(binId);
    }
}
