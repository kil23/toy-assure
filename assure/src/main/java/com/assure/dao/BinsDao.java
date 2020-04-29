package com.assure.dao;

import com.assure.pojo.Bin;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BinsDao extends AbstractDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Bin insertBin(Bin bin){
        em.persist(bin);
        em.flush();
        return bin;
    }

    public Bin getBinById(long id){
        String select_id = "Select b From Bin b where bin_id=:id";
        TypedQuery<Bin> query = getQuery(select_id, Bin.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<Bin> getAllBins() {
        String select_all = "select b from Bin b";
        TypedQuery<Bin> query = getQuery(select_all, Bin.class);
        return query.getResultList();
    }
}
