package com.assure.service;

import com.assure.dao.InventoryDao;
import com.assure.pojo.Inventory;
import com.assure.model.response.InventoryDataResponse;
import com.commons.service.ApiException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryService {

    private static final Logger logger = Logger.getLogger(InventoryService.class);

    @Autowired
    private InventoryDao inventoryDao;

    public InventoryDataResponse convertPojoToFormData(Inventory inventory) {
        return new InventoryDataResponse(inventory.getId(), inventory.getGlobalSkuId(),
                inventory.getAllocatedQuantity(), inventory.getAvailableQuantity(), inventory.getFulfilledQuantity());
    }

    @Transactional
    public void insertInventory(Inventory ip) {
        logger.info("Insert inventory.");
        Inventory inventory = inventoryDao.insertInventory(ip);
        logger.info("Inventory added with id : "+ inventory.getId());
    }

    @Transactional
    public void updateInventory(Inventory new_ip) {
        logger.info("Updating inventory.");
        inventoryDao.updateInventory(new_ip);
    }

    @Transactional(rollbackOn = ApiException.class)
    public Inventory getInventoryByGlobalSkuId(Long globalSkuId) throws ApiException {
        logger.info("Get inventory by globalSkuId");
        Inventory inventory = inventoryDao.getInventoryByGlobalSkuId(globalSkuId);
        if(inventory==null){
            logger.info("Inventory not found for globalSkuId : "+globalSkuId);
            throw new ApiException("No inventory found for a Product.");
        }
        return inventory;
    }

    public List<InventoryDataResponse> getAllInventory() throws ApiException {
        logger.info("Get all Inventory records.");
        List<Inventory> list1 = inventoryDao.getAllInventory();
        List<InventoryDataResponse> list2 = new ArrayList<>();
        if(list1.isEmpty()){
            logger.info("No Inventory found.");
            throw new ApiException("No Inventory found for any Product. Please add Inventory.");
        }
        for(Inventory data : list1){
            list2.add(convertPojoToFormData(data));
        }
        return list2;
    }
}
