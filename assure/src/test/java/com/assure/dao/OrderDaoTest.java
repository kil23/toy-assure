package com.assure.dao;

import com.assure.pojo.Order;
import com.assure.service.AbstractUnitTest;
import com.commons.enums.OrderStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OrderDaoTest extends AbstractUnitTest {

    private Order order1, order2, order3;
    @Autowired private OrderDao orderDao;

    @Before
    public void setUp(){
        order1 = new Order();
        order1.setClientId(2L);
        order1.setChannelId(4L);
        order1.setCustomerId(6L);
        order1.setChannelOrderId("abc");
        order1.setStatus(OrderStatus.CREATED);

        order2 = new Order();
        order2.setClientId(1L);
        order2.setChannelId(3L);
        order2.setCustomerId(5L);
        order2.setChannelOrderId("pqr");
        order2.setStatus(OrderStatus.ALLOCATED);

        order3 = new Order();
        order3.setClientId(7L);
        order3.setChannelId(8L);
        order3.setCustomerId(9L);
        order3.setChannelOrderId("xyz");
        order3.setStatus(OrderStatus.FULFILLED);
    }

    @Test
    public void testInsertOrder() {
        Order newOrder = orderDao.insert(order1);
        Assert.assertNotNull(newOrder);
        Assert.assertTrue(newOrder.getId()>0);
        Assert.assertEquals(order1.getClientId(), newOrder.getClientId());
        Assert.assertEquals(order1.getChannelId(), newOrder.getChannelId());
        Assert.assertEquals(order1.getCustomerId(), newOrder.getCustomerId());
        Assert.assertEquals(order1.getChannelOrderId(), newOrder.getChannelOrderId());
        Assert.assertEquals(order1.getStatus(), newOrder.getStatus());
    }

    @Test
    public void testUpdateOrder() {
        Order newOrder = orderDao.insert(order2);
        Assert.assertNotNull(newOrder);
        newOrder.setClientId(11L);
        newOrder.setChannelId(12L);
        newOrder.setCustomerId(13L);
        newOrder.setChannelOrderId("updated123");
        newOrder.setStatus(OrderStatus.ALLOCATED);
        orderDao.update(newOrder);
        Order updated = orderDao.findOne(newOrder.getId());
        Assert.assertNotNull(updated);
        Assert.assertEquals(newOrder.getClientId(), updated.getClientId());
        Assert.assertEquals(newOrder.getChannelId(), updated.getChannelId());
        Assert.assertEquals(newOrder.getCustomerId(), updated.getCustomerId());
        Assert.assertEquals(newOrder.getChannelOrderId(), updated.getChannelOrderId());
        Assert.assertEquals(newOrder.getStatus(), updated.getStatus());
    }

    @Test
    public void testGetOrder() {
        Order newOrder = orderDao.insert(order3);
        Order result = orderDao.findOne(newOrder.getId());
        Assert.assertNotNull(result);
        Assert.assertEquals(newOrder.getClientId(), result.getClientId());
        Assert.assertEquals(newOrder.getChannelId(), result.getChannelId());
        Assert.assertEquals(newOrder.getCustomerId(), result.getCustomerId());
        Assert.assertEquals(newOrder.getChannelOrderId(), result.getChannelOrderId());
        Assert.assertEquals(newOrder.getStatus(), result.getStatus());
    }

    @Test
    public void testGetOrderByChannelOrderId() {
        Order newOrder = orderDao.insert(order1);
        Order result = orderDao.getOrderByChannelOrderId(newOrder.getChannelOrderId());
        Assert.assertNotNull(result);
        Assert.assertEquals(newOrder.getClientId(), result.getClientId());
        Assert.assertEquals(newOrder.getChannelId(), result.getChannelId());
        Assert.assertEquals(newOrder.getCustomerId(), result.getCustomerId());
        Assert.assertEquals(newOrder.getChannelOrderId(), result.getChannelOrderId());
        Assert.assertEquals(newOrder.getStatus(), result.getStatus());
    }

    @Test
    public void testGetOrderByChannelId() {
        Order newOrder = orderDao.insert(order2);
        List<Order> orderList = orderDao.getOrderByChannelId(newOrder.getChannelId());
        Assert.assertNotNull(orderList);
        Assert.assertTrue(orderList.size()>0);
        Assert.assertEquals(newOrder.getClientId(), orderList.get(0).getClientId());
        Assert.assertEquals(newOrder.getChannelId(), orderList.get(0).getChannelId());
        Assert.assertEquals(newOrder.getCustomerId(), orderList.get(0).getCustomerId());
        Assert.assertEquals(newOrder.getChannelOrderId(), orderList.get(0).getChannelOrderId());
        Assert.assertEquals(newOrder.getStatus(), orderList.get(0).getStatus());
    }

    @Test
    public void testGetOrdersByStatus() {
        Order newOrder = orderDao.insert(order3);
        List<Order> orderList = orderDao.getOrdersByStatus(newOrder.getStatus());
        Assert.assertNotNull(orderList);
        Assert.assertTrue(orderList.size()>0);
        Assert.assertEquals(newOrder.getClientId(), orderList.get(0).getClientId());
        Assert.assertEquals(newOrder.getChannelId(), orderList.get(0).getChannelId());
        Assert.assertEquals(newOrder.getCustomerId(), orderList.get(0).getCustomerId());
        Assert.assertEquals(newOrder.getChannelOrderId(), orderList.get(0).getChannelOrderId());
        Assert.assertEquals(newOrder.getStatus(), orderList.get(0).getStatus());
    }

    @Test
    public void testGetAllOrder() {
        orderDao.insert(order1);
        orderDao.insert(order2);
        orderDao.insert(order3);
        List<Order> orderList = orderDao.findAll();

        Assert.assertNotNull(orderList);
        Assert.assertTrue(orderList.size()>0);

        Assert.assertEquals(order1.getClientId(), orderList.get(0).getClientId());
        Assert.assertEquals(order1.getChannelId(), orderList.get(0).getChannelId());
        Assert.assertEquals(order1.getCustomerId(), orderList.get(0).getCustomerId());
        Assert.assertEquals(order1.getChannelOrderId(), orderList.get(0).getChannelOrderId());
        Assert.assertEquals(order1.getStatus(), orderList.get(0).getStatus());

        Assert.assertEquals(order2.getClientId(), orderList.get(1).getClientId());
        Assert.assertEquals(order2.getChannelId(), orderList.get(1).getChannelId());
        Assert.assertEquals(order2.getCustomerId(), orderList.get(1).getCustomerId());
        Assert.assertEquals(order2.getChannelOrderId(), orderList.get(1).getChannelOrderId());
        Assert.assertEquals(order2.getStatus(), orderList.get(1).getStatus());

        Assert.assertEquals(order3.getClientId(), orderList.get(2).getClientId());
        Assert.assertEquals(order3.getChannelId(), orderList.get(2).getChannelId());
        Assert.assertEquals(order3.getCustomerId(), orderList.get(2).getCustomerId());
        Assert.assertEquals(order3.getChannelOrderId(), orderList.get(2).getChannelOrderId());
        Assert.assertEquals(order3.getStatus(), orderList.get(2).getStatus());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValuedOrder() {
        orderDao.insert(null);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testClientIdColumnNullConstraint() {
        Order order = new Order();
        order.setClientId(null);
        order.setChannelId(2L);
        order.setCustomerId(3L);
        order.setChannelOrderId("jkl");
        order.setStatus(OrderStatus.CREATED);
        orderDao.insert(order);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testChannelIdColumnNullConstraint() {
        Order order = new Order();
        order.setClientId(1L);
        order.setChannelId(null);
        order.setCustomerId(3L);
        order.setChannelOrderId("jkl");
        order.setStatus(OrderStatus.ALLOCATED);
        orderDao.insert(order);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testCustomerColumnNullConstraint() {
        Order order = new Order();
        order.setClientId(1L);
        order.setChannelId(2L);
        order.setCustomerId(null);
        order.setChannelOrderId("jkl");
        order.setStatus(OrderStatus.FULFILLED);
        orderDao.insert(order);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testChannelOrderIdColumnNullConstraint() {
        Order order = new Order();
        order.setClientId(1L);
        order.setChannelId(2L);
        order.setCustomerId(3L);
        order.setChannelOrderId(null);
        order.setStatus(OrderStatus.CREATED);
        orderDao.insert(order);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testStatusColumnNullConstraint() {
        Order order = new Order();
        order.setClientId(1L);
        order.setChannelId(2L);
        order.setCustomerId(3L);
        order.setChannelOrderId("jkl");
        order.setStatus(null);
        orderDao.insert(order);
    }

    @Test(expected = NumberFormatException.class)
    public void testClientIdColumnEmptyConstraint(){
        Order order = new Order();
        order.setClientId(Long.parseLong(""));
        order.setChannelId(2L);
        order.setCustomerId(3L);
        order.setChannelOrderId("jkl");
        order.setStatus(OrderStatus.ALLOCATED);
        orderDao.insert(order);
    }

    @Test(expected = NumberFormatException.class)
    public void testChannelIdColumnEmptyConstraint(){
        Order order = new Order();
        order.setClientId(1L);
        order.setChannelId(Long.parseLong(""));
        order.setCustomerId(3L);
        order.setChannelOrderId("jkl");
        order.setStatus(OrderStatus.FULFILLED);
        orderDao.insert(order);
    }

    @Test(expected = NumberFormatException.class)
    public void testCustomerIdColumnEmptyConstraint(){
        Order order = new Order();
        order.setClientId(1L);
        order.setChannelId(2L);
        order.setCustomerId(Long.parseLong(""));
        order.setChannelOrderId("jkl");
        order.setStatus(OrderStatus.CREATED);
        orderDao.insert(order);
    }

    @Test
    public void testGetOrderByClientId() {
        orderDao.insert(order1);
        List<Order> order = orderDao.getOrderByClientId(2L);
        assertNotNull(order);

        assertEquals(order1.getClientId(), order.get(0).getClientId());
        assertEquals(order1.getChannelId(), order.get(0).getChannelId());
        assertEquals(order1.getChannelOrderId(), order.get(0).getChannelOrderId());
        assertEquals(order1.getCustomerId(), order.get(0).getCustomerId());
        assertEquals(order1.getStatus(), order.get(0).getStatus());
    }

    @Test
    public void testGetOrderByCustomerId() {
        orderDao.insert(order2);
        List<Order> order = orderDao.getOrderByCustomerId(5L);
        assertNotNull(order);

        assertEquals(order2.getClientId(), order.get(0).getClientId());
        assertEquals(order2.getChannelId(), order.get(0).getChannelId());
        assertEquals(order2.getChannelOrderId(), order.get(0).getChannelOrderId());
        assertEquals(order2.getCustomerId(), order.get(0).getCustomerId());
        assertEquals(order2.getStatus(), order.get(0).getStatus());
    }
}
