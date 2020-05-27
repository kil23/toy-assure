package com.assure.api;

import com.assure.dao.ProductDao;
import com.assure.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductApi {

    @Autowired private ProductDao productDao;

    @Transactional
    public Product addProduct(Product productToInsert) {
        return productDao.insert(productToInsert);
    }

    @Transactional
    public void updateProduct(Product productToUpdate) {
        Product product = productDao.findOne(productToUpdate.getGlobalSkuId());
        product.setName(productToUpdate.getName());
        product.setBrandId(productToUpdate.getBrandId());
        product.setMrp(productToUpdate.getMrp());
        product.setDescription(productToUpdate.getDescription());
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts(Long clientId) {
        return productDao.getAllClientProducts(clientId);
    }

    @Transactional(readOnly = true)
    public Product getProductDetails(String clientSkuId, Long clientId) {
        return productDao.getProductDetails(clientSkuId, clientId);
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long globalSkuId) {
        return productDao.findOne(globalSkuId);
    }
}
