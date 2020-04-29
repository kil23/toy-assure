package com.assure.service;

import com.assure.dao.BinSkuDao;
import com.assure.model.response.BinSkuDataResponse;
import com.assure.pojo.BinSku;
import com.assure.pojo.Inventory;
import com.assure.pojo.Product;
import com.commons.service.ApiException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class BinSkuService {

    private static final Logger logger = Logger.getLogger(BinSkuService.class);

    @Autowired
    private BinSkuDao binSkuDao;

    @Autowired
    private ProductService productService;

    @Autowired
    private BinService binService;

    @Autowired
    private InventoryService inventoryService;

    public Long checkQuantityDifference(long quantity, long originalQuantity){
        return (quantity - originalQuantity);
    }

    public BinSkuDataResponse convertPojoToFormData(BinSku binSku) {
        return new BinSkuDataResponse(binSku.getId(), binSku.getBinId(), binSku.getGlobalSkuId(), binSku.getQuantity());
    }

    @Transactional(rollbackOn = ApiException.class)
    public void insertBinInventory(long binId, long clientId, String clientSkuId, long quantity) throws ApiException {

        Product product = getProductIfPresentByClientSkuId(clientSkuId,clientId);
        logger.info("Product present in db with id : "+product.getGlobalSkuId());

        BinSku binSkuToInsert = new BinSku(binId, clientId, product.getGlobalSkuId(), quantity);
        logger.info("BinId present in DB with id : "+binId);

        BinSku binSkuRecordInDB = binSkuDao.getBinSkuByGlobalSkuIdAndBinId(binSkuToInsert.getGlobalSkuId(), binSkuToInsert.getBinId());

        if(binSkuRecordInDB == null){

            logger.info("Adding new binSku record.");
            BinSku binSku = binSkuDao.insertBinSku(binSkuToInsert);

            logger.info("BinSku record created with id : "+ binSku.getId());
            logger.info("Adding new inventory record.");
            Inventory ip = new Inventory(binSkuToInsert.getGlobalSkuId(), binSkuToInsert.getQuantity());
            inventoryService.insertInventory(ip);
        }else{
            logger.info("BinSku Record found in db with id: "+binSkuRecordInDB.getId());
            logger.info("binId("+binSkuRecordInDB.getBinId() + ") and globalSkuId("+binSkuRecordInDB.getGlobalSkuId()+") matches with one db record.");
            Long qnty = binSkuRecordInDB.getQuantity() + binSkuToInsert.getQuantity();
            BinSku pojo = new BinSku(binSkuRecordInDB.getId(), binSkuToInsert.getBinId(), binSkuToInsert.getGlobalSkuId(), qnty);
            logger.info("updating binSkuId record");
            binSkuDao.updateBinSku(pojo);
            Inventory ip = inventoryService.getInventoryByGlobalSkuId(binSkuToInsert.getGlobalSkuId());
            Long i_quantity = ip.getAvailableQuantity() + binSkuToInsert.getQuantity();
            Inventory new_ip = new Inventory(ip.getId(), ip.getGlobalSkuId(), i_quantity);
            logger.info("updating inventory record");
            inventoryService.updateInventory(new_ip);
        }
    }

    @Transactional(rollbackOn = ApiException.class)
    public void updateBinInventory(long binSkuId, long binId, long quantity, long originalQuantity, long globalSkuId) throws ApiException {
        logger.info("Update bin inventory.");
        BinSku binSku = new BinSku(binSkuId, binId, globalSkuId, checkQuantityDifference(quantity, originalQuantity));

        BinSku bp = getBinSkuDetailsById(binSku.getId());
        if(bp == null){
            logger.info("No BinSku record found for binSkuId : " + binSku.getId());
            throw new ApiException("No BinSku record found.");
        }else{
            if(bp.getGlobalSkuId().equals(binSku.getGlobalSkuId())){

                Long qnty = bp.getQuantity() + binSku.getQuantity();
                BinSku pojo = new BinSku(bp.getId(), binSku.getBinId(), binSku.getGlobalSkuId(), qnty);
                binSkuDao.updateBinSku(pojo);
                logger.info("Bin_Sku table updated for existing product.");

                Inventory ip = inventoryService.getInventoryByGlobalSkuId(binSku.getGlobalSkuId());
                Long i_quantity = ip.getAvailableQuantity() + binSku.getQuantity();
                Inventory new_ip = new Inventory(ip.getId(), ip.getGlobalSkuId(), i_quantity);
                inventoryService.updateInventory(new_ip);
                logger.info("Inventory table updated for existing item.");
            }
        }
    }

    @Transactional(rollbackOn = ApiException.class)
    public void deleteBinInventory(Long binSkuId) throws ApiException {
        BinSku binSku = binSkuDao.getBinSkuById(binSkuId);
        if(binSku != null){
            logger.info("deleting inventory with binSkuId : " + binSkuId);
            int res = binSkuDao.deleteBinSku(binSkuId);
            //reduce inventory quantity as well
            logger.info("Records deleted : "+ res);
        }else{
            logger.info("No BinSku record found for binSkuId: " + binSkuId);
            throw new ApiException("No BinSku record found.");
        }
    }

    public List<BinSkuDataResponse> getBinwiseInventory(Long binId) throws ApiException {
        logger.info("Get bin-wise Inventory records.");
        List<BinSku> list1 = binSkuDao.getAllBinSkuByBinId(binId);
        if(list1.isEmpty()){
            logger.info("No Inventory found for bin with id ; "+binId);
            throw new ApiException("No Inventory found.");
        }
        List<BinSkuDataResponse> list2 = new ArrayList<>();
        for(BinSku data : list1){
            list2.add(convertPojoToFormData(data));
        }
        return list2;
    }

    public BinSku getBinSkuDetailsById(Long binSkuId) throws ApiException {
        BinSku binSku = binSkuDao.getBinSkuById(binSkuId);
        if(binSku==null){
            logger.info("No BinSku found for id : "+binSkuId);
            throw new ApiException("No BinSKu found.");
        }
        return binSku;
    }

    public BinSkuDataResponse getBinInventory(Long binSkuId) {
        logger.info("Get bin inventory by binSkuId");
        BinSku binSku = binSkuDao.getBinSkuById(binSkuId);
        return convertPojoToFormData(binSku);
    }

    @Transactional(rollbackOn = ApiException.class)
    public Product getProductIfPresentByClientSkuId(String clientSkuId, Long clientId) throws ApiException{
        Product product = productService.getProduct(clientSkuId, clientId);
        if(product == null){
            logger.info("No product found with clientSkuId: "+clientSkuId+" and clientId: "+clientId);
            throw new ApiException("No product found. Please enter Product information first.");
        }
        return product;
    }

    public List<BinSku> getAllBinSkuByGlobalSkuId(Long globalSkuId) throws ApiException {
        logger.info("Get all binSku by GlobalSkuId");
        List<BinSku> binSkuList = binSkuDao.getAllBinSkuByGlobalSkuId(globalSkuId);
        if(binSkuList.isEmpty()){
            logger.info("No BinSku record found for globalSkuId");
            throw new ApiException("No BinSku record found.");
        }
        return binSkuList;
    }

    public void updateBinSku(BinSku binSku) {
        logger.info("updating-binSku");
        binSkuDao.updateBinSku(binSku);
    }
}
