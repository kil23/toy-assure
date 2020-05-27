package com.assure.dao;

import com.assure.pojo.OrderItem;
import com.assure.service.AbstractUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class OrderItemDaoTest extends AbstractUnitTest {

    private OrderItem orderItem1, orderItem2;
    @Autowired private OrderItemDao orderItemDao;

    @Before
    public void setUp(){
        orderItem1 = new OrderItem();
        orderItem1.setOrderId(1L);
        orderItem1.setGlobalSkuId(2L);
        orderItem1.setOrderedQuantity(3L);
        orderItem1.setAllocatedQuantity(4L);
        orderItem1.setFulfilledQuantity(5L);
        orderItem1.setSellingPricePerUnit(6.0);

        orderItem2 = new OrderItem();
        orderItem2.setOrderId(7L);
        orderItem2.setGlobalSkuId(8L);
        orderItem2.setOrderedQuantity(9L);
        orderItem2.setAllocatedQuantity(10L);
        orderItem2.setFulfilledQuantity(11L);
        orderItem2.setSellingPricePerUnit(12.0);
    }

    @Test
    public void testInsertOrderItem() {
        OrderItem newOrderItem = orderItemDao.insert(orderItem1);
        assertNotNull(newOrderItem);
        assertTrue(newOrderItem.getId()>0);
        assertEquals(orderItem1.getOrderId(), newOrderItem.getOrderId());
        assertEquals(orderItem1.getGlobalSkuId(), newOrderItem.getGlobalSkuId());
        assertEquals(orderItem1.getOrderedQuantity(), newOrderItem.getOrderedQuantity());
        assertEquals(orderItem1.getAllocatedQuantity(), newOrderItem.getAllocatedQuantity());
        assertEquals(orderItem1.getFulfilledQuantity(), newOrderItem.getFulfilledQuantity());
        assertEquals(orderItem1.getSellingPricePerUnit(), newOrderItem.getSellingPricePerUnit());
    }

    @Test
    public void testUpdateOrderItem() {
        OrderItem newOrderItem = orderItemDao.insert(orderItem2);
        assertNotNull(newOrderItem);
        newOrderItem.setOrderId(21L);
        newOrderItem.setGlobalSkuId(22L);
        newOrderItem.setOrderedQuantity(23L);
        newOrderItem.setAllocatedQuantity(24L);
        newOrderItem.setFulfilledQuantity(25L);
        newOrderItem.setSellingPricePerUnit(26.0);
        orderItemDao.update(newOrderItem);
        OrderItem updated = orderItemDao.findOne(newOrderItem.getId());
        assertNotNull(updated);
        assertEquals(newOrderItem.getOrderId(), updated.getOrderId());
        assertEquals(newOrderItem.getGlobalSkuId(), updated.getGlobalSkuId());
        assertEquals(newOrderItem.getOrderedQuantity(), updated.getOrderedQuantity());
        assertEquals(newOrderItem.getAllocatedQuantity(), updated.getAllocatedQuantity());
        assertEquals(newOrderItem.getFulfilledQuantity(), updated.getFulfilledQuantity());
        assertEquals(newOrderItem.getSellingPricePerUnit(), updated.getSellingPricePerUnit());
    }

    @Test
    public void testGetOrderItem() {
        OrderItem newOrderItem = orderItemDao.insert(orderItem1);
        OrderItem result = orderItemDao.findOne(newOrderItem.getId());
        assertNotNull(result);
        assertEquals(newOrderItem.getOrderId(), result.getOrderId());
        assertEquals(newOrderItem.getGlobalSkuId(), result.getGlobalSkuId());
        assertEquals(newOrderItem.getOrderedQuantity(), result.getOrderedQuantity());
        assertEquals(newOrderItem.getAllocatedQuantity(), result.getAllocatedQuantity());
        assertEquals(newOrderItem.getFulfilledQuantity(), result.getFulfilledQuantity());
        assertEquals(newOrderItem.getSellingPricePerUnit(), result.getSellingPricePerUnit());
    }

    @Test
    public void getOrderItemListByOrderId() {
        OrderItem newOrderItem = orderItemDao.insert(orderItem2);
        List<OrderItem> resultList = orderItemDao.getOrderItemListByOrderId(newOrderItem.getOrderId());
        assertNotNull(resultList);
        assertTrue(resultList.size()>0);
        assertEquals(newOrderItem.getOrderId(), resultList.get(0).getOrderId());
        assertEquals(newOrderItem.getGlobalSkuId(), resultList.get(0).getGlobalSkuId());
        assertEquals(newOrderItem.getOrderedQuantity(), resultList.get(0).getOrderedQuantity());
        assertEquals(newOrderItem.getAllocatedQuantity(), resultList.get(0).getAllocatedQuantity());
        assertEquals(newOrderItem.getFulfilledQuantity(), resultList.get(0).getFulfilledQuantity());
        assertEquals(newOrderItem.getSellingPricePerUnit(), resultList.get(0).getSellingPricePerUnit());
    }

    @Test
    public void testGetAllOrderItem() {
        orderItemDao.insert(orderItem1);
        orderItemDao.insert(orderItem2);
        List<OrderItem> itemList = orderItemDao.findAll();

        assertNotNull(itemList);
        assertTrue(itemList.size()>0);

        assertEquals(orderItem1.getOrderId(), itemList.get(0).getOrderId());
        assertEquals(orderItem1.getGlobalSkuId(), itemList.get(0).getGlobalSkuId());
        assertEquals(orderItem1.getOrderedQuantity(), itemList.get(0).getOrderedQuantity());
        assertEquals(orderItem1.getAllocatedQuantity(), itemList.get(0).getAllocatedQuantity());
        assertEquals(orderItem1.getFulfilledQuantity(), itemList.get(0).getFulfilledQuantity());
        assertEquals(orderItem1.getSellingPricePerUnit(), itemList.get(0).getSellingPricePerUnit());

        assertEquals(orderItem2.getOrderId(), itemList.get(1).getOrderId());
        assertEquals(orderItem2.getGlobalSkuId(), itemList.get(1).getGlobalSkuId());
        assertEquals(orderItem2.getOrderedQuantity(), itemList.get(1).getOrderedQuantity());
        assertEquals(orderItem2.getAllocatedQuantity(), itemList.get(1).getAllocatedQuantity());
        assertEquals(orderItem2.getFulfilledQuantity(), itemList.get(1).getFulfilledQuantity());
        assertEquals(orderItem2.getSellingPricePerUnit(), itemList.get(1).getSellingPricePerUnit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValuedOrderItem() {
        orderItemDao.insert(null);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testOrderIdColumnNullConstraint() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(null);
        orderItem.setGlobalSkuId(2L);
        orderItem.setOrderedQuantity(3L);
        orderItem.setAllocatedQuantity(4L);
        orderItem.setFulfilledQuantity(5L);
        orderItem.setSellingPricePerUnit(26.0);
        orderItemDao.insert(orderItem);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testGlobalSkuIdColumnNullConstraint() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(1L);
        orderItem.setGlobalSkuId(null);
        orderItem.setOrderedQuantity(3L);
        orderItem.setAllocatedQuantity(4L);
        orderItem.setFulfilledQuantity(5L);
        orderItem.setSellingPricePerUnit(26.0);
        orderItemDao.insert(orderItem);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testOrderedQuantityColumnNullConstraint() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(1L);
        orderItem.setGlobalSkuId(2L);
        orderItem.setOrderedQuantity(null);
        orderItem.setAllocatedQuantity(4L);
        orderItem.setFulfilledQuantity(5L);
        orderItem.setSellingPricePerUnit(26.0);
        orderItemDao.insert(orderItem);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testAllocatedQuantityColumnNullConstraint() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(1L);
        orderItem.setGlobalSkuId(2L);
        orderItem.setOrderedQuantity(3L);
        orderItem.setAllocatedQuantity(null);
        orderItem.setFulfilledQuantity(5L);
        orderItem.setSellingPricePerUnit(26.0);
        orderItemDao.insert(orderItem);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testFulfilledQuantityColumnNullConstraint() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(1L);
        orderItem.setGlobalSkuId(2L);
        orderItem.setOrderedQuantity(3L);
        orderItem.setAllocatedQuantity(4L);
        orderItem.setFulfilledQuantity(null);
        orderItem.setSellingPricePerUnit(26.0);
        orderItemDao.insert(orderItem);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testSellingPricePerUnitColumnNullConstraint() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(1L);
        orderItem.setGlobalSkuId(2L);
        orderItem.setOrderedQuantity(3L);
        orderItem.setAllocatedQuantity(4L);
        orderItem.setFulfilledQuantity(5L);
        orderItem.setSellingPricePerUnit(null);
        orderItemDao.insert(orderItem);
    }

    @Test(expected = NumberFormatException.class)
    public void testOrderIdColumnEmptyConstraint(){
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(Long.parseLong(""));
        orderItem.setGlobalSkuId(2L);
        orderItem.setOrderedQuantity(3L);
        orderItem.setAllocatedQuantity(4L);
        orderItem.setFulfilledQuantity(5L);
        orderItem.setSellingPricePerUnit(26.0);
        orderItemDao.insert(orderItem);
    }

    @Test(expected = NumberFormatException.class)
    public void testGlobalSkuIdColumnEmptyConstraint(){
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(1L);
        orderItem.setGlobalSkuId(Long.parseLong(""));
        orderItem.setOrderedQuantity(3L);
        orderItem.setAllocatedQuantity(4L);
        orderItem.setFulfilledQuantity(5L);
        orderItem.setSellingPricePerUnit(26.0);
        orderItemDao.insert(orderItem);
    }

    @Test(expected = NumberFormatException.class)
    public void testOrderedQuantityColumnEmptyConstraint(){
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(1L);
        orderItem.setGlobalSkuId(2L);
        orderItem.setOrderedQuantity(Long.parseLong(""));
        orderItem.setAllocatedQuantity(4L);
        orderItem.setFulfilledQuantity(5L);
        orderItem.setSellingPricePerUnit(26.0);
        orderItemDao.insert(orderItem);
    }

    @Test(expected = NumberFormatException.class)
    public void testAllocatedQuantityColumnEmptyConstraint(){
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(1L);
        orderItem.setGlobalSkuId(2L);
        orderItem.setOrderedQuantity(3L);
        orderItem.setAllocatedQuantity(Long.parseLong(""));
        orderItem.setFulfilledQuantity(5L);
        orderItem.setSellingPricePerUnit(26.0);
        orderItemDao.insert(orderItem);
    }

    @Test(expected = NumberFormatException.class)
    public void testFulfilledQuantityColumnEmptyConstraint(){
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(1L);
        orderItem.setGlobalSkuId(2L);
        orderItem.setOrderedQuantity(3L);
        orderItem.setAllocatedQuantity(4L);
        orderItem.setFulfilledQuantity(Long.parseLong(""));
        orderItem.setSellingPricePerUnit(26.0);
        orderItemDao.insert(orderItem);
    }

    @Test(expected = NumberFormatException.class)
    public void testSellingPricePerUnitColumnEmptyConstraint(){
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(1L);
        orderItem.setGlobalSkuId(2L);
        orderItem.setOrderedQuantity(3L);
        orderItem.setAllocatedQuantity(4L);
        orderItem.setFulfilledQuantity(5L);
        orderItem.setSellingPricePerUnit(Double.parseDouble(""));
        orderItemDao.insert(orderItem);
    }
}
