package com.assure.dao;

import com.assure.pojo.Inventory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class InventoryDao extends AbstractDao{

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Inventory insertInventory(Inventory ip){
        em.persist(ip);
        em.flush();
        return ip;
    }

    @Transactional
    public void updateInventory(Inventory ip){
        em.merge(ip);
        em.flush();
    }

    public Inventory getInventoryById(long id) {
        String select_id = "Select b From Inventory b where id=:id";
        TypedQuery<Inventory> query = getQuery(select_id, Inventory.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public List<Inventory> getAllInventory() {
        String select_all = "select b from Inventory b";
        TypedQuery<Inventory> query = getQuery(select_all, Inventory.class);
        return query.getResultList();
    }

    public Inventory getInventoryByGlobalSkuId(long globalSkuId) {
        String select_id = "Select b From Inventory b where global_sku_id=:id";
        TypedQuery<Inventory> query = getQuery(select_id, Inventory.class);
        query.setParameter("id", globalSkuId);
        return getSingle(query);
    }
}
