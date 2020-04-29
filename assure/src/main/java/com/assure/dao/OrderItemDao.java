package com.assure.dao;

import com.assure.pojo.OrderItem;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao {

    private static final Logger logger = Logger.getLogger(OrderItemDao.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public OrderItem insertOrderItem(OrderItem oip){
        logger.info("inserting-new-order-item");
        em.persist(oip);
        em.flush();
        return oip;
    }

    @Transactional
    public void updateOrderItem(OrderItem oip){
        logger.info("updating-order-items-with-id("+oip.getId()+")");
        em.merge(oip);
        em.flush();
    }

    public int deleteOrderItem(int id) {
        logger.info("deleting-order-items-with-id("+id+")");
        String delete_id = "delete from OrderItem i where id=:id";
        Query query = em.createQuery(delete_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public OrderItem getOrderItemById(long id) {
        logger.info("getting-order-item-with-id("+id+")");
        String select_id = "Select i From OrderItem i where id=:id";
        TypedQuery<OrderItem> query = getQuery(select_id, OrderItem.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<OrderItem> getAllOrderItems() {
        logger.info("getting-all-order-items");
        String select_all = "select i from OrderItem i";
        TypedQuery<OrderItem> query = getQuery(select_all, OrderItem.class);
        return query.getResultList();
    }

    public List<OrderItem> getOrderItemListByOrderId(long orderId) {
        logger.info("getting-order-items-by-order-id("+orderId+")");
        String select_id = "Select i From OrderItem i where order_id=:id";
        TypedQuery<OrderItem> query = getQuery(select_id, OrderItem.class);
        query.setParameter("id", orderId);
        return query.getResultList();
    }
}
