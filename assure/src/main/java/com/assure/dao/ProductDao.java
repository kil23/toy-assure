package com.assure.dao;

import com.assure.pojo.Product;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao {

    private static final Logger logger = Logger.getLogger(ProductDao.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Product insertProduct(Product p){
        logger.info("inserting-new-product");
        em.persist(p);
        em.flush();
        return p;
    }

    @Transactional
    public void updateProduct(Product p){
        logger.info("updating-product-with-id("+p.getGlobalSkuId()+")");
        em.merge(p);
        em.flush();
    }

    @Transactional
    public int deleteProductById(long id){
        logger.info("deleting-product-by-id("+id+")");
        String delete_id = "delete from Product p where global_sku_id=:id";
        Query query = em.createQuery(delete_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public Product getProductById(long globalSkuId){
        logger.info("getting-product-by-id("+globalSkuId+")");
        String select_id = "Select p From Product p where global_sku_id=:id";
        TypedQuery<Product> query = getQuery(select_id, Product.class);
        query.setParameter("id", globalSkuId);
        return getSingle(query);
    }

    public List<Product> getAllProducts() {
        logger.info("getting-all-products");
        String select_all = "select p from Product p";
        TypedQuery<Product> query = getQuery(select_all, Product.class);
        return query.getResultList();
    }

    public Product getProductByClientSkuIdAndClientId(String clientSkuId, long clientId){
        logger.info("getting-product-by-client_sku_id("+clientSkuId+")-and-client_id("+clientId+")");
        String select_id = "Select p From Product p where client_sku_id='"+clientSkuId+"' AND client_id='"+clientId+"'";
        TypedQuery<Product> query = getQuery(select_id, Product.class);
        return getSingle(query);
    }

    public List<Product> getAllClientProducts(long clientId){
        logger.info("getting-all-client-product-by-clientId("+clientId+")");
        String select_id = "Select p From Product p where client_id=:id";
        TypedQuery<Product> query = getQuery(select_id, Product.class);
        query.setParameter("id", clientId);
        return query.getResultList();
    }
}
