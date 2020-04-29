package com.assure.dao;

import com.assure.pojo.BinSku;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BinSkuDao extends AbstractDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public BinSku insertBinSku(BinSku binSku){
        em.persist(binSku);
        em.flush();
        return binSku;
    }

    @Transactional
    public void updateBinSku(BinSku binSku){
        em.merge(binSku);
        em.flush();
    }

    @Transactional
    public int deleteBinSku(long id){
        String delete_id = "delete from BinSku b where id=:id";
        Query query = em.createQuery(delete_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public BinSku getBinSkuById(long id){
        String select_id = "Select b From BinSku b where id=:id";
        TypedQuery<BinSku> query = getQuery(select_id, BinSku.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public BinSku getBinSkuByGlobalSkuIdAndBinId(long globalSkuId, long binId){
        String select_id = "Select b From BinSku b where global_sku_id='"+globalSkuId+"' AND bin_id='"+binId+"'";
        TypedQuery<BinSku> query = getQuery(select_id, BinSku.class);
        return getSingle(query);
    }

    public List<BinSku> getAllBinSku() {
        String select_all = "select b from BinSku b";
        TypedQuery<BinSku> query = getQuery(select_all, BinSku.class);
        return query.getResultList();
    }

    public List<BinSku> getAllBinSkuByBinId(long binId) {
        String select_all = "select b from BinSku b where bin_id=:id";
        TypedQuery<BinSku> query = getQuery(select_all, BinSku.class);
        query.setParameter("id", binId);
        return query.getResultList();
    }

    public List<BinSku> getAllBinSkuByGlobalSkuId(long globalSkuId) {
        String select_all = "select b from BinSku b where global_sku_id=:id";
        TypedQuery<BinSku> query = getQuery(select_all, BinSku.class);
        query.setParameter("id", globalSkuId);
        return query.getResultList();
    }
}
