package com.assure.dao;

import com.assure.pojo.OrderItem;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao<OrderItem> {

    private static final String SELECT_ALL_BY_ORDERID = "Select i From OrderItem i where i.orderId=:id";

    public OrderItemDao() {
        super(OrderItem.class);
    }

    @Transactional(readOnly = true)
    public List<OrderItem> getOrderItemListByOrderId(Long orderId) {
        TypedQuery<OrderItem> query = getQuery(SELECT_ALL_BY_ORDERID);
        query.setParameter("id", orderId);
        return query.getResultList();
    }
}
