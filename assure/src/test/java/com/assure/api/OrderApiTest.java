package com.assure.api;

import com.assure.dao.OrderDao;
import com.assure.pojo.Order;
import com.assure.service.AbstractUnitTest;
import com.commons.enums.OrderStatus;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class OrderApiTest extends AbstractUnitTest {

    private Order order1, order2;
    @Autowired private OrderApi orderApi;
    @Autowired private OrderDao orderDao;

    @Before
    public void setUp(){
        order1 = createObject(2L, "abc", 1L, 12L, OrderStatus.CREATED);
        order2 = createObject(4L, "pqr", 3L, 9L, OrderStatus.CREATED);
    }

    public Order createObject(Long clientId, String channelOrderId, Long channelId, Long customerId, OrderStatus status) {
       Order order = new Order();
       order.setClientId(clientId);
       order.setChannelOrderId(channelOrderId);
       order.setChannelId(channelId);
       order.setCustomerId(customerId);
       order.setStatus(status);
       return order;
    }

    @Test
    public void testAddOrder(){
        Order newOrder = orderApi.addOrder(order1);
        assertNotNull(newOrder);
        assertTrue(newOrder.getId()>0);

        assertEquals(order1.getClientId(), newOrder.getClientId());
        assertEquals(order1.getChannelId(), newOrder.getChannelId());
        assertEquals(order1.getChannelOrderId(), newOrder.getChannelOrderId());
        assertEquals(order1.getCustomerId(), newOrder.getCustomerId());
        assertEquals(order1.getStatus(), newOrder.getStatus());
    }

    @Test
    public void testUpdateOrder() {
        Order newOrder = orderDao.insert(order2);
        assertNotNull(newOrder);
        newOrder.setClientId(6L);
        newOrder.setChannelId(7L);
        newOrder.setCustomerId(8L);
        newOrder.setChannelOrderId("xyz");
        newOrder.setStatus(OrderStatus.ALLOCATED);

        orderApi.updateOrder(newOrder);
        Order updatedOrder = orderDao.findOne(newOrder.getId());
        assertNotNull(updatedOrder);
        assertEquals(newOrder.getClientId(), updatedOrder.getClientId());
        assertEquals(newOrder.getChannelId(), updatedOrder.getChannelId());
        assertEquals(newOrder.getChannelOrderId(), updatedOrder.getChannelOrderId());
        assertEquals(newOrder.getCustomerId(), updatedOrder.getCustomerId());
        assertEquals(newOrder.getStatus(), updatedOrder.getStatus());
    }

    @Test
    public void testGetOrderDataForChannel() {
        Order newOrder = orderDao.insert(order1);
        List<Order> orderList = orderApi.getOrderDataForChannel(newOrder.getChannelId());
        assertNotNull(orderList);
        assertEquals(1, orderList.size());
        assertEquals(newOrder.getClientId(), orderList.get(0).getClientId());
        assertEquals(newOrder.getChannelId(), orderList.get(0).getChannelId());
        assertEquals(newOrder.getChannelOrderId(), orderList.get(0).getChannelOrderId());
        assertEquals(newOrder.getCustomerId(), orderList.get(0).getCustomerId());
        assertEquals(newOrder.getStatus(), orderList.get(0).getStatus());
    }

    @Test
    public void testGetAllData() {
        orderDao.insert(order1);
        orderDao.insert(order2);
        List<Order> orderList = orderApi.getAllOrderData();
        assertNotNull(orderList);
        assertEquals(2, orderList.size());

        assertEquals(order1.getClientId(), orderList.get(0).getClientId());
        assertEquals(order1.getChannelId(), orderList.get(0).getChannelId());
        assertEquals(order1.getChannelOrderId(), orderList.get(0).getChannelOrderId());
        assertEquals(order1.getCustomerId(), orderList.get(0).getCustomerId());
        assertEquals(order1.getStatus(), orderList.get(0).getStatus());

        assertEquals(order2.getClientId(), orderList.get(1).getClientId());
        assertEquals(order2.getChannelId(), orderList.get(1).getChannelId());
        assertEquals(order2.getChannelOrderId(), orderList.get(1).getChannelOrderId());
        assertEquals(order2.getCustomerId(), orderList.get(1).getCustomerId());
        assertEquals(order2.getStatus(), orderList.get(1).getStatus());
    }

    @Test
    public void testGetOrdersByStatus() {
        orderDao.insert(order1);
        orderDao.insert(order2);
        List<Order> orderList = orderApi.getOrdersByStatus(OrderStatus.CREATED);
        assertNotNull(orderList);
        assertEquals(2, orderList.size());

        assertEquals(order1.getStatus(), orderList.get(0).getStatus());
        assertEquals(order2.getStatus(), orderList.get(1).getStatus());
    }

    @Test
    public void testGetOrderDetails() {
        orderDao.insert(order1);
        Order newOrder = orderApi.getOrderDetails(order1.getChannelOrderId());
        assertNotNull(newOrder);

        assertEquals(order1.getClientId(), newOrder.getClientId());
        assertEquals(order1.getChannelId(), newOrder.getChannelId());
        assertEquals(order1.getChannelOrderId(), newOrder.getChannelOrderId());
        assertEquals(order1.getCustomerId(), newOrder.getCustomerId());
        assertEquals(order1.getStatus(), newOrder.getStatus());
    }

    @Test
    public void testGetOrderDetailsById() {
        orderDao.insert(order1);
        Order order = orderApi.getOrderDetails(order1.getId());
        assertNotNull(order);

        assertEquals(order1.getClientId(), order.getClientId());
        assertEquals(order1.getChannelId(), order.getChannelId());
        assertEquals(order1.getChannelOrderId(), order.getChannelOrderId());
        assertEquals(order1.getCustomerId(), order.getCustomerId());
        assertEquals(order1.getStatus(), order.getStatus());
    }

    @Test
    public void testGetOrderDataForClient() {
        orderDao.insert(order1);
        List<Order> order = orderApi.getOrderDataForClient(2L);
        assertNotNull(order);

        assertEquals(order1.getClientId(), order.get(0).getClientId());
        assertEquals(order1.getChannelId(), order.get(0).getChannelId());
        assertEquals(order1.getChannelOrderId(), order.get(0).getChannelOrderId());
        assertEquals(order1.getCustomerId(), order.get(0).getCustomerId());
        assertEquals(order1.getStatus(), order.get(0).getStatus());
    }

    @Test
    public void testGetOrderDataForCustomer() {
        orderDao.insert(order2);
        List<Order> order = orderApi.getOrderDataForCustomer(9L);
        assertNotNull(order);

        assertEquals(order2.getClientId(), order.get(0).getClientId());
        assertEquals(order2.getChannelId(), order.get(0).getChannelId());
        assertEquals(order2.getChannelOrderId(), order.get(0).getChannelOrderId());
        assertEquals(order2.getCustomerId(), order.get(0).getCustomerId());
        assertEquals(order2.getStatus(), order.get(0).getStatus());
    }
}
