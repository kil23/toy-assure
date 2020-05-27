package com.assure.api;

import com.assure.dao.InventoryDao;
import com.assure.pojo.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryApi {

    @Autowired
    private InventoryDao inventoryDao;

    @Transactional
    public Inventory addInventory(Inventory ip) {
        return inventoryDao.insert(ip);
    }

    @Transactional
    public void updateInventory(Inventory inventoryToUpdate) {
        Inventory inv = inventoryDao.findOne(inventoryToUpdate.getId());
        inv.setAvailableQuantity(inventoryToUpdate.getAvailableQuantity());
        inv.setFulfilledQuantity(inventoryToUpdate.getFulfilledQuantity());
        inv.setAllocatedQuantity(inventoryToUpdate.getAllocatedQuantity());
    }

    @Transactional(readOnly = true)
    public Inventory getInventoryDetails(Long globalSkuId) {
        return inventoryDao.getInventoryByGlobalSkuId(globalSkuId);
    }

    @Transactional(readOnly = true)
    public List<Inventory> getAllInventory() {
        return inventoryDao.findAll();
    }
}
