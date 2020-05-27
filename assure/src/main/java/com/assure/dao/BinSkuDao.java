package com.assure.dao;

import com.assure.pojo.BinSku;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BinSkuDao extends AbstractDao<BinSku> {

    private static final String SELECT_BY_GLOBALSKUID_BINID = "Select b From BinSku b where b.globalSkuId=:globalSkuId AND b.binId=:binId";
    private static final String SELECT_ALL_BY_BINID = "select b from BinSku b where b.binId=:id AND b.quantity>0";
    private static final String SELECT_ALL_BY_GLOBALSKUID = "select b from BinSku b where b.globalSkuId=:id AND b.quantity>0";

    public BinSkuDao() {
        super(BinSku.class);
    }

    @Transactional(readOnly = true)
    public BinSku getBinSkuDetails(Long globalSkuId, Long binId){
        TypedQuery<BinSku> query = getQuery(SELECT_BY_GLOBALSKUID_BINID);
        query.setParameter("globalSkuId", globalSkuId);
        query.setParameter("binId", binId);
        return getSingle(query);
    }

    @Transactional(readOnly = true)
    public List<BinSku> getAllBinSkuDetails(Long binId) {
        TypedQuery<BinSku> query = getQuery(SELECT_ALL_BY_BINID);
        query.setParameter("id", binId);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public List<BinSku> getAllBinSkuByGlobalSkuId(Long globalSkuId) {
        TypedQuery<BinSku> query = getQuery(SELECT_ALL_BY_GLOBALSKUID);
        query.setParameter("id", globalSkuId);
        return query.getResultList();
    }
}
