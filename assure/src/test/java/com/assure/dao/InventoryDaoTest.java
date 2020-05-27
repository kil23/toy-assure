package com.assure.dao;

import com.assure.pojo.Inventory;
import com.assure.service.AbstractUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class InventoryDaoTest extends AbstractUnitTest {

    private Inventory inventory1, inventory2;
    @Autowired private InventoryDao inventoryDao;

    @Before
    public void setUp(){
        inventory1 = new Inventory();
        inventory1.setGlobalSkuId(5L);
        inventory1.setAvailableQuantity(20L);
        inventory1.setFulfilledQuantity(5L);
        inventory1.setAllocatedQuantity(10L);

        inventory2 = new Inventory();
        inventory2.setGlobalSkuId(10L);
        inventory2.setAvailableQuantity(40L);
        inventory2.setFulfilledQuantity(10L);
        inventory2.setAllocatedQuantity(20L);
    }

    @Test
    public void testInsertInventory() {
        Inventory newInventory = inventoryDao.insert(inventory1);
        assertNotNull(newInventory);
        assertTrue(newInventory.getId()>0);
        assertEquals(inventory1.getGlobalSkuId(), newInventory.getGlobalSkuId());
        assertEquals(inventory1.getAvailableQuantity(), newInventory.getAvailableQuantity());
        assertEquals(inventory1.getFulfilledQuantity(), newInventory.getFulfilledQuantity());
        assertEquals(inventory1.getAllocatedQuantity(), newInventory.getAllocatedQuantity());
    }

    @Test
    public void testUpdateInventory() {
        Inventory newInventory = inventoryDao.insert(inventory1);
        assertNotNull(newInventory);
        newInventory.setGlobalSkuId(15L);
        newInventory.setAvailableQuantity(25L);
        newInventory.setFulfilledQuantity(10L);
        newInventory.setAllocatedQuantity(15L);
        inventoryDao.update(newInventory);
        Inventory updated = inventoryDao.findOne(newInventory.getId());
        assertNotNull(updated);
        assertEquals(newInventory.getGlobalSkuId(), updated.getGlobalSkuId());
        assertEquals(newInventory.getAvailableQuantity(), updated.getAvailableQuantity());
        assertEquals(newInventory.getFulfilledQuantity(), updated.getFulfilledQuantity());
        assertEquals(newInventory.getAllocatedQuantity(), updated.getAllocatedQuantity());
    }

    @Test
    public void testGetInventory() {
        Inventory newInventory = inventoryDao.insert(inventory2);
        Inventory result = inventoryDao.findOne(newInventory.getId());
        assertNotNull(result);
        assertEquals(newInventory.getGlobalSkuId(), result.getGlobalSkuId());
        assertEquals(newInventory.getAvailableQuantity(), result.getAvailableQuantity());
        assertEquals(newInventory.getFulfilledQuantity(), result.getFulfilledQuantity());
        assertEquals(newInventory.getAllocatedQuantity(), result.getAllocatedQuantity());
    }

    @Test
    public void testGetInventoryByGlobalSkuId() {
        Inventory newInventory = inventoryDao.insert(inventory2);
        Inventory result = inventoryDao.getInventoryByGlobalSkuId(newInventory.getGlobalSkuId());
        assertNotNull(result);
        assertEquals(newInventory.getGlobalSkuId(), result.getGlobalSkuId());
        assertEquals(newInventory.getAvailableQuantity(), result.getAvailableQuantity());
        assertEquals(newInventory.getFulfilledQuantity(), result.getFulfilledQuantity());
        assertEquals(newInventory.getAllocatedQuantity(), result.getAllocatedQuantity());
    }

    @Test
    public void testGetAllInventory() {
        inventoryDao.insert(inventory1);
        inventoryDao.insert(inventory2);
        List<Inventory> list = inventoryDao.findAll();
        assertNotNull(list);
        assertTrue(list.size()>0);

        assertEquals(inventory1.getGlobalSkuId(), list.get(0).getGlobalSkuId());
        assertEquals(inventory1.getAvailableQuantity(), list.get(0).getAvailableQuantity());
        assertEquals(inventory1.getFulfilledQuantity(), list.get(0).getFulfilledQuantity());
        assertEquals(inventory1.getAllocatedQuantity(), list.get(0).getAllocatedQuantity());

        assertEquals(inventory2.getGlobalSkuId(), list.get(1).getGlobalSkuId());
        assertEquals(inventory2.getAvailableQuantity(), list.get(1).getAvailableQuantity());
        assertEquals(inventory2.getFulfilledQuantity(), list.get(1).getFulfilledQuantity());
        assertEquals(inventory2.getAllocatedQuantity(), list.get(1).getAllocatedQuantity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValuedInventory() {
        inventoryDao.insert(null);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testGlobalSkuIdColumnNullConstraint() {
        Inventory inventory = new Inventory();
        inventory.setGlobalSkuId(null);
        inventory.setAvailableQuantity(20L);
        inventory.setFulfilledQuantity(5L);
        inventory.setAllocatedQuantity(10L);
        inventoryDao.insert(inventory);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testAvailableQuantityColumnNullConstraint() {
        Inventory inventory = new Inventory();
        inventory.setGlobalSkuId(5L);
        inventory.setAvailableQuantity(null);
        inventory.setFulfilledQuantity(5L);
        inventory.setAllocatedQuantity(10L);
        inventoryDao.insert(inventory);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testFulfilledQuantityColumnNullConstraint() {
        Inventory inventory = new Inventory();
        inventory.setGlobalSkuId(5L);
        inventory.setAvailableQuantity(20L);
        inventory.setFulfilledQuantity(null);
        inventory.setAllocatedQuantity(10L);
        inventoryDao.insert(inventory);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testAllocatedQuantityColumnNullConstraint() {
        Inventory inventory = new Inventory();
        inventory.setGlobalSkuId(5L);
        inventory.setAvailableQuantity(20L);
        inventory.setFulfilledQuantity(5L);
        inventory.setAllocatedQuantity(null);
        inventoryDao.insert(inventory);
    }

    @Test(expected = NumberFormatException.class)
    public void testGlobalSkuIdColumnEmptyConstraint(){
        Inventory inventory = new Inventory();
        inventory.setGlobalSkuId(Long.parseLong(""));
        inventory.setAvailableQuantity(20L);
        inventory.setFulfilledQuantity(5L);
        inventory.setAllocatedQuantity(10L);
        inventoryDao.insert(inventory);
    }

    @Test(expected = NumberFormatException.class)
    public void testAvailableQuantityColumnEmptyConstraint(){
        Inventory inventory = new Inventory();
        inventory.setGlobalSkuId(5L);
        inventory.setAvailableQuantity(Long.parseLong(""));
        inventory.setFulfilledQuantity(5L);
        inventory.setAllocatedQuantity(10L);
        inventoryDao.insert(inventory);
    }

    @Test(expected = NumberFormatException.class)
    public void testFulfilledQuantityColumnEmptyConstraint(){
        Inventory inventory = new Inventory();
        inventory.setGlobalSkuId(5L);
        inventory.setAvailableQuantity(20L);
        inventory.setFulfilledQuantity(Long.parseLong(""));
        inventory.setAllocatedQuantity(10L);
        inventoryDao.insert(inventory);
    }

    @Test(expected = NumberFormatException.class)
    public void testAllocatedQuantityColumnEmptyConstraint(){
        Inventory inventory = new Inventory();
        inventory.setGlobalSkuId(5L);
        inventory.setAvailableQuantity(20L);
        inventory.setFulfilledQuantity(5L);
        inventory.setAllocatedQuantity(Long.parseLong(""));
        inventoryDao.insert(inventory);
    }
}
