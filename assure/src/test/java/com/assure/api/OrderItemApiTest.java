package com.assure.api;

import com.assure.dao.OrderItemDao;
import com.assure.pojo.OrderItem;
import com.assure.service.AbstractUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrderItemApiTest extends AbstractUnitTest {

    private OrderItem orderItem1, orderItem2, orderItem3;
    @Autowired private OrderItemApi itemApi;
    @Autowired private OrderItemDao orderItemDao;

    @Before
    public void setUp(){
        orderItem1 = createObject(1L, 2L, 5L, 3L, 4L, 66.0);
        orderItem2 = createObject(2L, 4L, 6L, 8L, 10L, 43.0);
        orderItem3 = createObject(2L, 5L, 3L, 4L, 7L, 17.0);
    }

    public OrderItem createObject(Long orderId, Long globalSkuId, Long orderedQty, Long allocatedQty,
                                  Long fulfilledQty, Double sellingPrice) {
        OrderItem item = new OrderItem();
        item.setOrderId(orderId);
        item.setGlobalSkuId(globalSkuId);
        item.setOrderedQuantity(orderedQty);
        item.setAllocatedQuantity(allocatedQty);
        item.setFulfilledQuantity(fulfilledQty);
        item.setSellingPricePerUnit(sellingPrice);
        return item;
    }

    @Test
    public void testAddOrderItem(){
        OrderItem item = itemApi.addOrderItems(orderItem1);
        assertTrue(item.getId()>0);
        assertEquals(orderItem1.getOrderId(), item.getOrderId());
        assertEquals(orderItem1.getGlobalSkuId(), item.getGlobalSkuId());
        assertEquals(orderItem1.getOrderedQuantity(), item.getOrderedQuantity());
        assertEquals(orderItem1.getAllocatedQuantity(), item.getAllocatedQuantity());
        assertEquals(orderItem1.getFulfilledQuantity(), item.getFulfilledQuantity());
        assertEquals(orderItem1.getSellingPricePerUnit(), item.getSellingPricePerUnit());
    }

    @Test
    public void testUpdateOrderItem() {
        OrderItem newOrderItem = orderItemDao.insert(orderItem1);
        newOrderItem.setOrderedQuantity(10L);
        newOrderItem.setSellingPricePerUnit(19.9);
        itemApi.updateOrderItem(newOrderItem);
        OrderItem updatedOrderItem = orderItemDao.findOne(newOrderItem.getId());

        assertEquals(newOrderItem.getOrderId(), updatedOrderItem.getOrderId());
        assertEquals(newOrderItem.getGlobalSkuId(), updatedOrderItem.getGlobalSkuId());
        assertEquals(newOrderItem.getOrderedQuantity(), updatedOrderItem.getOrderedQuantity());
        assertEquals(newOrderItem.getAllocatedQuantity(), updatedOrderItem.getAllocatedQuantity());
        assertEquals(newOrderItem.getFulfilledQuantity(), updatedOrderItem.getFulfilledQuantity());
        assertEquals(newOrderItem.getSellingPricePerUnit(), updatedOrderItem.getSellingPricePerUnit());
    }

    @Test
    public void testGetOrderItemByOrderId() {
        orderItemDao.insert(orderItem2);
        orderItemDao.insert(orderItem3);
        List<OrderItem> itemList = itemApi.getOrderItemByOrderId(orderItem2.getOrderId());

        assertEquals(orderItem2.getOrderId(), itemList.get(0).getOrderId());
        assertEquals(orderItem2.getGlobalSkuId(), itemList.get(0).getGlobalSkuId());
        assertEquals(orderItem2.getOrderedQuantity(), itemList.get(0).getOrderedQuantity());
        assertEquals(orderItem2.getAllocatedQuantity(), itemList.get(0).getAllocatedQuantity());
        assertEquals(orderItem2.getFulfilledQuantity(), itemList.get(0).getFulfilledQuantity());
        assertEquals(orderItem2.getSellingPricePerUnit(), itemList.get(0).getSellingPricePerUnit());

        assertEquals(orderItem3.getOrderId(), itemList.get(1).getOrderId());
        assertEquals(orderItem3.getGlobalSkuId(), itemList.get(1).getGlobalSkuId());
        assertEquals(orderItem3.getOrderedQuantity(), itemList.get(1).getOrderedQuantity());
        assertEquals(orderItem3.getAllocatedQuantity(), itemList.get(1).getAllocatedQuantity());
        assertEquals(orderItem3.getFulfilledQuantity(), itemList.get(1).getFulfilledQuantity());
        assertEquals(orderItem3.getSellingPricePerUnit(), itemList.get(1).getSellingPricePerUnit());
    }
}
