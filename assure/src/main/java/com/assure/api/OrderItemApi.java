package com.assure.api;

import com.assure.dao.OrderItemDao;
import com.assure.pojo.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderItemApi {

    @Autowired private OrderItemDao itemDao;

    @Transactional
    public OrderItem addOrderItems(OrderItem orderItem) {
        return itemDao.insert(orderItem);
    }

    @Transactional
    public void updateOrderItem(OrderItem orderItemToUpdate) {
        OrderItem orderItem = itemDao.findOne(orderItemToUpdate.getId());
        orderItem.setOrderedQuantity(orderItemToUpdate.getOrderedQuantity());
        orderItem.setAllocatedQuantity(orderItemToUpdate.getAllocatedQuantity());
        orderItem.setFulfilledQuantity(orderItemToUpdate.getFulfilledQuantity());
        orderItem.setSellingPricePerUnit(orderItemToUpdate.getSellingPricePerUnit());
        itemDao.update(orderItemToUpdate);
    }

    @Transactional(readOnly = true)
    public List<OrderItem> getOrderItemByOrderId(Long orderId) {
        return itemDao.getOrderItemListByOrderId(orderId);
    }
}
