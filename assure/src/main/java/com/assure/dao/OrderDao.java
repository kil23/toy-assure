package com.assure.dao;

import com.assure.pojo.Order;
import com.commons.enums.OrderStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao<Order> {

    private static final String SELECT_ALL_BY_CHANNELID = "Select p From Order p where p.channelId=:id";
    private static final String SELECT_ALL_BY_CLIENTID = "Select p From Order p where p.clientId=:id";
    private static final String SELECT_ALL_BY_CUSTOMERID = "Select p From Order p where p.customerId=:id";
    private static final String SELECT_BY_CHANNELORDERID = "Select p From Order p where p.channelOrderId=:id";
    private static final String SELECT_ALL_BY_STATUS = "Select p From Order p where p.status=:status";

    public OrderDao() {
        super(Order.class);
    }

    @Transactional(readOnly = true)
    public Order getOrderByChannelOrderId(String channelOrderId) {
        TypedQuery<Order> query = getQuery(SELECT_BY_CHANNELORDERID);
        query.setParameter("id", channelOrderId);
        return getSingle(query);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrderByChannelId(Long channelId) {
        TypedQuery<Order> query = getQuery(SELECT_ALL_BY_CHANNELID);
        query.setParameter("id", channelId);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(OrderStatus status) {
        TypedQuery<Order> query = getQuery(SELECT_ALL_BY_STATUS);
        query.setParameter("status", status);
        return query.getResultList();
    }

    public List<Order> getOrderByClientId(long clientId) {
        TypedQuery<Order> query = getQuery(SELECT_ALL_BY_CLIENTID);
        query.setParameter("id", clientId);
        return query.getResultList();
    }

    public List<Order> getOrderByCustomerId(long customerId) {
        TypedQuery<Order> query = getQuery(SELECT_ALL_BY_CUSTOMERID);
        query.setParameter("id", customerId);
        return query.getResultList();
    }
}
