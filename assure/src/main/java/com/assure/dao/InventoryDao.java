package com.assure.dao;

import com.assure.pojo.Inventory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;

@Repository
public class InventoryDao extends AbstractDao<Inventory> {

    private static final String SELECT_BY_GLOBALSKUID = "Select b From Inventory b where b.globalSkuId=:id";

    public InventoryDao() {
        super(Inventory.class);
    }

    @Transactional(readOnly = true)
    public Inventory getInventoryByGlobalSkuId(Long globalSkuId) {
        TypedQuery<Inventory> query = getQuery(SELECT_BY_GLOBALSKUID);
        query.setParameter("id", globalSkuId);
        return getSingle(query);
    }
}
