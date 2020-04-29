package com.assure.service;

import com.assure.dao.BinsDao;
import com.assure.pojo.Bin;
import com.assure.model.response.BinDataResponse;
import com.commons.service.ApiException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BinService {

    private static final Logger logger = Logger.getLogger(BinService.class);

    @Autowired
    private BinsDao binsDao;

    @Autowired
    private BinSkuService skuService;

    public static BinDataResponse convertPojoToFormData(Bin p, Long inventoryCount) {
        BinDataResponse data = new BinDataResponse();
        data.setBinId(p.getBinId());
        data.setInventoryCount(inventoryCount);
        return data;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void createBins(Long bins) {
        logger.info("creating "+bins+" new bins");
        for(int i = 0; i<bins; i++){
            Bin pojo = binsDao.insertBin(new Bin());
            logger.info("Bin created with id : "+pojo.getBinId());
        }
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<BinDataResponse> getAllBin() throws ApiException {
        logger.info("get-all-bins");
        List<Bin> list = binsDao.getAllBins();
        if(list.isEmpty()){
            logger.info("No bins found. Please create few bins first.");
            throw new ApiException("No bins found. Please create few bins first.");
        }else{
            List<BinDataResponse> list2 = new ArrayList<>();
            for (Bin bin : list) {
                long inventoryCount = skuService.getBinwiseInventory(bin.getBinId()).size();
                list2.add(convertPojoToFormData(bin, inventoryCount));
            }
            return list2;
        }
    }

    public Bin getBinById(Long binId) throws ApiException {
        logger.info("get-bin-data-by-id");
        Bin bin = binsDao.getBinById(binId);
        if(bin==null){
            logger.info("No Bin found with binId : "+binId);
            throw new ApiException("No Bin found.");
        }
        return bin;
    }
}
