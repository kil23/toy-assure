package com.assure.api;

import com.assure.dao.ProductDao;
import com.assure.pojo.Product;
import com.assure.service.AbstractUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ProductApiTest extends AbstractUnitTest {

    private Product product1, product2, product3;
    @Autowired private ProductApi productApi;
    @Autowired private ProductDao productDao;

    @Before
    public void setUp(){
        product1 = createObject("prod1", 1L, "brand1", "sku1", 19.0, "text1");
        product2 = createObject("prod2", 2L, "brand2", "sku2", 29.0, "text2");
        product3 = createObject("prod3", 2L, "brand3", "sku3", 39.0, "text3");
    }

    public Product createObject(String name, Long clientId, String brandId, String clientSkuId, Double mrp, String description) {
        Product product = new Product();
        product.setName(name);
        product.setClientId(clientId);
        product.setBrandId(brandId);
        product.setClientSkuId(clientSkuId);
        product.setMrp(mrp);
        product.setDescription(description);
        return product;
    }

    @Test
    public void testAddProduct(){
        Product newProd = productApi.addProduct(product1);
        assertNotNull(newProd);
        assertTrue(newProd.getGlobalSkuId()>0);
        assertEquals(product1.getClientId(), newProd.getClientId());
        assertEquals(product1.getClientSkuId(), newProd.getClientSkuId());
        assertEquals(product1.getName(), newProd.getName());
        assertEquals(product1.getBrandId(), newProd.getBrandId());
        assertEquals(product1.getMrp(), newProd.getMrp());
        assertEquals(product1.getDescription(), newProd.getDescription());
    }

    @Test
    public void testUpdateProduct() {
        Product newProd = productDao.insert(product1);
        newProd.setName("updatedProd");
        newProd.setBrandId("updatedBrandId");
        newProd.setMrp(23.0);
        newProd.setDescription("updated text");
        productApi.updateProduct(newProd);
        Product updatedProd = productDao.findOne(newProd.getGlobalSkuId());
        assertNotNull(updatedProd);
        assertEquals(newProd.getName(), updatedProd.getName());
        assertEquals(newProd.getBrandId(), updatedProd.getBrandId());
        assertEquals(newProd.getMrp(), updatedProd.getMrp());
        assertEquals(newProd.getDescription(), updatedProd.getDescription());
    }

    @Test
    public void testGetAllProducts() {
        productDao.insert(product1);
        productDao.insert(product2);
        List<Product> productList = productApi.getAllProducts();
        assertNotNull(productList);
        assertEquals(2, productList.size());

        assertEquals(product1.getClientId(), productList.get(0).getClientId());
        assertEquals(product1.getClientSkuId(), productList.get(0).getClientSkuId());
        assertEquals(product1.getName(), productList.get(0).getName());
        assertEquals(product1.getBrandId(), productList.get(0).getBrandId());
        assertEquals(product1.getMrp(), productList.get(0).getMrp());
        assertEquals(product1.getDescription(), productList.get(0).getDescription());

        assertEquals(product2.getClientId(), productList.get(1).getClientId());
        assertEquals(product2.getClientSkuId(), productList.get(1).getClientSkuId());
        assertEquals(product2.getName(), productList.get(1).getName());
        assertEquals(product2.getBrandId(), productList.get(1).getBrandId());
        assertEquals(product2.getMrp(), productList.get(1).getMrp());
        assertEquals(product2.getDescription(), productList.get(1).getDescription());

    }

    @Test
    public void testGetAllProductsByClientId() {
        productDao.insert(product2);
        productDao.insert(product3);
        List<Product> productList = productApi.getAllProducts(product2.getClientId());
        assertNotNull(productList);
        assertEquals(2, productList.size());

        assertEquals(product2.getClientId(), productList.get(0).getClientId());
        assertEquals(product2.getClientSkuId(), productList.get(0).getClientSkuId());
        assertEquals(product2.getName(), productList.get(0).getName());
        assertEquals(product2.getBrandId(), productList.get(0).getBrandId());
        assertEquals(product2.getMrp(), productList.get(0).getMrp());
        assertEquals(product2.getDescription(), productList.get(0).getDescription());

        assertEquals(product3.getClientId(), productList.get(1).getClientId());
        assertEquals(product3.getClientSkuId(), productList.get(1).getClientSkuId());
        assertEquals(product3.getName(), productList.get(1).getName());
        assertEquals(product3.getBrandId(), productList.get(1).getBrandId());
        assertEquals(product3.getMrp(), productList.get(1).getMrp());
        assertEquals(product3.getDescription(), productList.get(1).getDescription());
    }

    @Test
    public void testGetProductDetails() {
        productDao.insert(product1);
        productDao.insert(product2);
        Product product = productApi.getProductDetails(product2.getClientSkuId(), product2.getClientId());
        assertNotNull(product);

        assertEquals(product2.getClientId(), product.getClientId());
        assertEquals(product2.getClientSkuId(), product.getClientSkuId());
        assertEquals(product2.getName(), product.getName());
        assertEquals(product2.getBrandId(), product.getBrandId());
        assertEquals(product2.getMrp(), product.getMrp());
        assertEquals(product2.getDescription(), product.getDescription());
    }

    @Test
    public void testGetProductById() {
        productDao.insert(product1);
        productDao.insert(product2);
        Product product = productApi.getProductById(product2.getGlobalSkuId());
        assertNotNull(product);

        assertEquals(product2.getClientId(), product.getClientId());
        assertEquals(product2.getClientSkuId(), product.getClientSkuId());
        assertEquals(product2.getName(), product.getName());
        assertEquals(product2.getBrandId(), product.getBrandId());
        assertEquals(product2.getMrp(), product.getMrp());
        assertEquals(product2.getDescription(), product.getDescription());
    }
}
