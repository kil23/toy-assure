package com.assure.api;

import com.assure.dao.InventoryDao;
import com.assure.pojo.Inventory;
import com.assure.service.AbstractUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class InventoryApiTest extends AbstractUnitTest {

    private Inventory inventory1, inventory2;
    @Autowired private InventoryApi inventoryApi;
    @Autowired private InventoryDao inventoryDao;

    @Before
    public void setUp(){
        inventory1 = createObject(1L, 20L, 2L, 5L);
        inventory2 = createObject(2L, 30L, 3L, 6L);
    }

    public Inventory createObject(Long globalSkuId, Long availableQty, Long allocatedQty, Long fulfilledQty) {
        Inventory inventory = new Inventory();
        inventory.setGlobalSkuId(globalSkuId);
        inventory.setAvailableQuantity(availableQty);
        inventory.setAllocatedQuantity(allocatedQty);
        inventory.setFulfilledQuantity(fulfilledQty);
        return inventory;
    }

    @Test
    public void testAddInventory(){
        Inventory newInventory = inventoryApi.addInventory(inventory1);
        assertNotNull(newInventory);
        assertTrue(newInventory.getId()>0);
        assertEquals(inventory1.getGlobalSkuId(), newInventory.getGlobalSkuId());
        assertEquals(inventory1.getAvailableQuantity(), newInventory.getAvailableQuantity());
        assertEquals(inventory1.getAllocatedQuantity(), newInventory.getAllocatedQuantity());
        assertEquals(inventory1.getFulfilledQuantity(), newInventory.getFulfilledQuantity());
    }

    @Test
    public void testUpdateInventory(){
        Inventory newInventory = inventoryDao.insert(inventory1);
        newInventory.setAvailableQuantity(40L);
        newInventory.setAllocatedQuantity(4L);
        newInventory.setFulfilledQuantity(10L);

        inventoryApi.updateInventory(newInventory);
        Inventory updatedInventory = inventoryApi.getInventoryDetails(newInventory.getGlobalSkuId());

        assertNotNull(newInventory);
        assertEquals(newInventory.getGlobalSkuId(), updatedInventory.getGlobalSkuId());
        assertEquals(newInventory.getAvailableQuantity(), updatedInventory.getAvailableQuantity());
        assertEquals(newInventory.getAllocatedQuantity(), updatedInventory.getAllocatedQuantity());
        assertEquals(newInventory.getFulfilledQuantity(), updatedInventory.getFulfilledQuantity());
    }

    @Test
    public void testGetInventoryDetails(){
        Inventory newInventory = inventoryDao.insert(inventory1);
        Inventory result = inventoryApi.getInventoryDetails(newInventory.getGlobalSkuId());

        assertNotNull(result);
        assertEquals(newInventory.getGlobalSkuId(), result.getGlobalSkuId());
        assertEquals(newInventory.getAvailableQuantity(), result.getAvailableQuantity());
        assertEquals(newInventory.getAllocatedQuantity(), result.getAllocatedQuantity());
        assertEquals(newInventory.getFulfilledQuantity(), result.getFulfilledQuantity());
    }

    @Test
    public void testGetAllInventory(){
        inventoryDao.insert(inventory1);
        inventoryDao.insert(inventory2);

        List<Inventory> list = inventoryApi.getAllInventory();

        assertTrue(list.size()>0);
        assertEquals(list.get(0), this.inventory1);
        assertEquals(list.get(1), this.inventory2);
    }
}
