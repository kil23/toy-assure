package com.assure.dao;

import com.assure.pojo.Product;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao<Product> {

    private static final String SELECT_BY_CLIENTSKUID_CLIENTID = "Select p From Product p where p.clientSkuId=:clientSkuId AND p.clientId=:clientId";
    private static final String SELECT_ALL_BY_CLIENTID = "Select p From Product p where p.clientId=:clientId";

    public ProductDao() {
        super(Product.class);
    }

    @Transactional(readOnly = true)
    public Product getProductDetails(String clientSkuId, Long clientId){
        TypedQuery<Product> query = getQuery(SELECT_BY_CLIENTSKUID_CLIENTID);
        query.setParameter("clientSkuId", clientSkuId);
        query.setParameter("clientId", clientId);
        return getSingle(query);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllClientProducts(Long clientId){
        TypedQuery<Product> query = getQuery(SELECT_ALL_BY_CLIENTID);
        query.setParameter("clientId", clientId);
        return query.getResultList();
    }
}
