package com.assure.dao;

import com.assure.pojo.Order;
import com.commons.enums.OrderStatus;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao{

    private static final Logger logger = Logger.getLogger(OrderDao.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Order insertOrder(Order op){
        logger.info("inserting-new-order");
        em.persist(op);
        em.flush();
        return op;
    }

    @Transactional
    public void updateOrder(Order op){
        logger.info("updating-order-with-id("+op.getId()+")");
        em.merge(op);
        em.flush();
    }

    public int deleteOrder(int id) {
        logger.info("deleting-order-with-id("+id+")");
        String delete_id = "delete from Order p where id=:id";
        Query query = em.createQuery(delete_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public List<Order> getOrderByChannelId(long channelId) {
        logger.info("getting-order-by-channel-id("+channelId+")");
        String select_id = "Select p From Order p where channel_id=:id";
        TypedQuery<Order> query = getQuery(select_id, Order.class);
        query.setParameter("id", channelId);
        return query.getResultList();
    }

    public Order getOrderById(long orderId) {
        logger.info("getting-order-by-order-id("+orderId+")");
        String select_id = "Select p From Order p where id=:id";
        TypedQuery<Order> query = getQuery(select_id, Order.class);
        query.setParameter("id", orderId);
        return getSingle(query);
    }

    public List<Order> getAllOrders() {
        logger.info("getting-all-orders");
        String select_all = "select b from Order b";
        TypedQuery<Order> query = getQuery(select_all, Order.class);
        return query.getResultList();
    }

    public List<String> getDistinctChannelOrderIds(){
        logger.info("getting-distinct-channel-order-ids");
        Query query = em.createQuery("SELECT DISTINCT o.channel_order_id FROM Order o");
        return query.getResultList();
    }

    public Order getOrderByChannelOrderId(String channelOrderId) {
        logger.info("getting-order-by-channel-order-id("+channelOrderId+")");
        String select_id = "Select p From Order p where channel_order_id=:id";
        TypedQuery<Order> query = getQuery(select_id, Order.class);
        query.setParameter("id", channelOrderId);
        return getSingle(query);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        logger.info("getting-order-by-status("+status+")");
        String select_id = "Select p From Order p where status=:status";
        TypedQuery<Order> query = getQuery(select_id, Order.class);
        query.setParameter("status", status);
        return query.getResultList();
    }
}
