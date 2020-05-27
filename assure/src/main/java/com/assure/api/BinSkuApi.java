package com.assure.api;

import com.assure.dao.BinSkuDao;
import com.assure.pojo.BinSku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BinSkuApi {

    @Autowired private BinSkuDao binSkuDao;

    @Transactional
    public BinSku addBinSku(BinSku binSkuToInsert) {
        return binSkuDao.insert(binSkuToInsert);
    }

    @Transactional
    public void updateBinSku(BinSku binSkuToUpdate) {
        BinSku binSku = binSkuDao.findOne(binSkuToUpdate.getId());
        binSku.setQuantity(binSkuToUpdate.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<BinSku> getBinwiseInventory(Long binId) {
        return binSkuDao.getAllBinSkuDetails(binId);
    }

    @Transactional(readOnly = true)
    public BinSku getBinSkuDetailsById(Long binSkuId)  {
        return binSkuDao.findOne(binSkuId);
    }

    @Transactional(readOnly = true)
    public List<BinSku> getAllBinSkuDetails(Long globalSkuId) {
        return binSkuDao.getAllBinSkuByGlobalSkuId(globalSkuId);
    }

    @Transactional(readOnly = true)
    public BinSku getBinSkuData(Long globalSkuId, Long binId) {
        return binSkuDao.getBinSkuDetails(globalSkuId, binId);
    }
}
