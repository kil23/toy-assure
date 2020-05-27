package com.assure.api;

import com.assure.dao.OrderDao;
import com.assure.pojo.Order;
import com.commons.enums.OrderStatus;
import com.commons.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderApi {

    @Autowired
    private OrderDao orderDao;

    @Transactional
    public Order addOrder(Order orderToInsert) {
        return orderDao.insert(orderToInsert);
    }

    @Transactional
    public void updateOrder(Order modifiedOrder) {
        Order order = orderDao.findOne(modifiedOrder.getId());
        order.setChannelOrderId(modifiedOrder.getChannelOrderId());
        order.setCustomerId(modifiedOrder.getCustomerId());
        order.setChannelId(modifiedOrder.getChannelId());
        order.setClientId(modifiedOrder.getClientId());
        order.setStatus(modifiedOrder.getStatus());
    }

    @Transactional(readOnly = true)
    public List<Order> getOrderDataForChannel(Long channelId) throws ApiException {
        return orderDao.getOrderByChannelId(channelId);
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrderData() throws ApiException {
        return orderDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderDao.getOrdersByStatus(status);
    }

    @Transactional(readOnly = true)
    public Order getOrderDetails(String channelOrderId) {
        return orderDao.getOrderByChannelOrderId(channelOrderId);
    }

    public Order getOrderDetails(Long orderId) {
        return orderDao.findOne(orderId);
    }

    public List<Order> getOrderDataForClient(long clientId) {
        return orderDao.getOrderByClientId(clientId);
    }

    public List<Order> getOrderDataForCustomer(long customerId) {
        return orderDao.getOrderByCustomerId(customerId);
    }
}
