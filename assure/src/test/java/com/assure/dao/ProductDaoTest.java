package com.assure.dao;

import com.assure.pojo.Product;
import com.assure.service.AbstractUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ProductDaoTest extends AbstractUnitTest {

    private Product product1, product2;
    @Autowired private ProductDao productDao;

    @Before
    public void setUp(){
        product1 = new Product();
        product1.setClientId(1L);
        product1.setClientSkuId("abc");
        product1.setName("test1");
        product1.setBrandId("brand1");
        product1.setMrp(99.9);
        product1.setDescription("text1");

        product2 = new Product();
        product2.setClientId(6L);
        product2.setClientSkuId("pqr");
        product2.setName("test2");
        product2.setBrandId("brand2");
        product2.setMrp(49.9);
        product2.setDescription("text2");
    }

    @Test
    public void testInsertProduct() {
        Product newProduct = productDao.insert(product1);
        assertNotNull(newProduct);
        assertTrue(newProduct.getGlobalSkuId()>0);
        assertEquals(product1.getClientId(), newProduct.getClientId());
        assertEquals(product1.getClientSkuId(), newProduct.getClientSkuId());
        assertEquals(product1.getName(), newProduct.getName());
        assertEquals(product1.getBrandId(), newProduct.getBrandId());
        assertEquals(product1.getMrp(), newProduct.getMrp());
        assertEquals(product1.getDescription(), newProduct.getDescription());
    }

    @Test
    public void testUpdateProduct() {
        Product newProduct = productDao.insert(product1);
        assertNotNull(newProduct);
        newProduct.setClientId(4L);
        newProduct.setClientSkuId("xyz");
        newProduct.setName("updatedName");
        newProduct.setBrandId("branded1");
        newProduct.setMrp(29.99);
        newProduct.setDescription("textUpdated");
        productDao.update(newProduct);
        Product updated = productDao.findOne(newProduct.getGlobalSkuId());

        assertNotNull(updated);
        assertEquals(newProduct.getClientId(), updated.getClientId());
        assertEquals(newProduct.getClientSkuId(), updated.getClientSkuId());
        assertEquals(newProduct.getName(), updated.getName());
        assertEquals(newProduct.getBrandId(), updated.getBrandId());
        assertEquals(newProduct.getMrp(), updated.getMrp());
        assertEquals(newProduct.getDescription(), updated.getDescription());
    }

    @Test
    public void testGetProduct() {
        Product newProduct = productDao.insert(product2);
        Product result = productDao.findOne(newProduct.getGlobalSkuId());
        assertNotNull(result);
        assertEquals(newProduct.getClientId(), result.getClientId());
        assertEquals(newProduct.getClientSkuId(), result.getClientSkuId());
        assertEquals(newProduct.getName(), result.getName());
        assertEquals(newProduct.getBrandId(), result.getBrandId());
        assertEquals(newProduct.getMrp(), result.getMrp());
        assertEquals(newProduct.getDescription(), result.getDescription());
    }

    @Test
    public void testGetProductDetails() {
        Product newProduct = productDao.insert(product2);
        Product result = productDao.getProductDetails(newProduct.getClientSkuId(), newProduct.getClientId());
        assertNotNull(result);
        assertEquals(newProduct.getClientId(), result.getClientId());
        assertEquals(newProduct.getClientSkuId(), result.getClientSkuId());
        assertEquals(newProduct.getName(), result.getName());
        assertEquals(newProduct.getBrandId(), result.getBrandId());
        assertEquals(newProduct.getMrp(), result.getMrp());
        assertEquals(newProduct.getDescription(), result.getDescription());
    }

    @Test
    public void testGetAllClientProducts() {
        Product newProduct = productDao.insert(product2);
        List<Product> productList = productDao.getAllClientProducts(newProduct.getClientId());
        assertNotNull(productList);
        assertEquals(product2.getClientId(), productList.get(0).getClientId());
        assertEquals(product2.getClientSkuId(), productList.get(0).getClientSkuId());
        assertEquals(product2.getName(), productList.get(0).getName());
        assertEquals(product2.getBrandId(), productList.get(0).getBrandId());
        assertEquals(product2.getMrp(), productList.get(0).getMrp());
        assertEquals(product2.getDescription(), productList.get(0).getDescription());
    }

    @Test
    public void testGetAllProduct() {
        productDao.insert(product1);
        productDao.insert(product2);
        List<Product> productList = productDao.findAll();

        assertNotNull(productList);
        assertTrue(productList.size()>0);

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

    @Test(expected = IllegalArgumentException.class)
    public void testNullValuedProduct() {
        productDao.insert(null);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testClientIdColumnNullConstraint() {
        Product product = new Product();
        product.setClientId(null);
        product.setClientSkuId("abc");
        product.setName("named");
        product.setBrandId("jkl");
        product.setMrp(19.9);
        product.setDescription("texted");
        productDao.insert(product);
    }

    @Test(expected = javax.persistence.PersistenceException.class)
    public void testMrpColumnNullConstraint() {
        Product product = new Product();
        product.setClientId(2L);
        product.setClientSkuId("abc");
        product.setName("named");
        product.setBrandId("jkl");
        product.setMrp(null);
        product.setDescription("texted");
        productDao.insert(product);
    }

    @Test(expected = NumberFormatException.class)
    public void testClientIdColumnEmptyConstraint() {
        Product product = new Product();
        product.setClientId(Long.parseLong(""));
        product.setClientSkuId("abc");
        product.setName("named");
        product.setBrandId("jkl");
        product.setMrp(19.9);
        product.setDescription("texted");
        productDao.insert(product);
    }

    @Test(expected = NumberFormatException.class)
    public void testMrpColumnEmptyConstraint() {
        Product product = new Product();
        product.setClientId(2L);
        product.setClientSkuId("abc");
        product.setName("named");
        product.setBrandId("jkl");
        product.setMrp(Double.parseDouble(""));
        product.setDescription("texted");
        productDao.insert(product);
    }
}
