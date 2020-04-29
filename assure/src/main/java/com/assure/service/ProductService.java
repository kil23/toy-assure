package com.assure.service;

import com.assure.dao.ClientDao;
import com.assure.dao.ProductDao;
import com.assure.pojo.Client;
import com.assure.pojo.Product;
import com.commons.form.ProductForm;
import com.commons.response.ProductDataResponse;
import com.commons.service.ApiException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.commons.util.Validation.*;

@Service
public class ProductService {

    private static final Logger logger = Logger.getLogger(ProductService.class);

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ClientDao clientDao;

    public ProductDataResponse convertPojoToFormData(Product p) {
        ProductDataResponse pd = new ProductDataResponse();
        pd.setGlobalSkuId(p.getGlobalSkuId());
        pd.setName(p.getName());
        pd.setBrandId(p.getBrandId());
        pd.setClientSkuId(p.getClientSkuId());
        pd.setDescription(p.getDescription());
        pd.setMrp(p.getMrp());
        pd.setClientId(p.getClientId());
        return pd;
    }

    public Product convertFormToPojo(ProductForm formData, Long clientId) {
        Client client = clientDao.getClientById(clientId);
        logger.info("Client with id : "+client.getId()+" and name: " + client.getName());
        Product product = new Product();
        product.setClientSkuId(formData.getClientSkuId());
        product.setClientId(client.getId());
        product.setBrandId(formData.getBrandId());
        product.setName(formData.getName());
        product.setMrp(formData.getMrp());
        product.setDescription(formData.getDescription());
        logger.info("Product name: "+ product.getName());
        return product;
    }

    public Product convertFormToPojo(ProductForm formData, Long clientId, Long globalSkuId) {
        Client client = clientDao.getClientById(clientId);
        Product product = new Product();
        product.setGlobalSkuId(globalSkuId);
        product.setClientSkuId(formData.getClientSkuId());
        product.setClientId(client.getId());
        product.setBrandId(formData.getBrandId());
        product.setName(formData.getName());
        product.setMrp(formData.getMrp());
        product.setDescription(formData.getDescription());
        return product;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void insertProduct(ProductForm formData, Long clientId) throws ApiException {
        logger.info("insert-product");
        Product pojo = convertFormToPojo(formData, clientId);
        logger.info("product is converted to pojo");
        validateProductName(pojo.getName());
        validateProductBrandId(pojo.getBrandId());
        validateClientSkuId(pojo.getClientSkuId());
        validateProductMrp(pojo.getMrp());
        logger.info("validation is done");
        List<Product> clientProducts = productDao.getAllClientProducts(pojo.getClientId());
        logger.info("clientProductsList size : "+clientProducts.size());
        if(clientProducts.size()==0){
            logger.warn("calling insert when No product for client...");
            productDao.insertProduct(pojo);
        }else{
            for(Product product : clientProducts){
                logger.warn("Few Product already present...");
                if(!pojo.getClientSkuId().equalsIgnoreCase(product.getClientSkuId())){
                    logger.info("calling insert method now...");
                    productDao.insertProduct(pojo);
                }else{
                    logger.warn("ClientSkuId cannot be duplicate");
                    throw new ApiException("Duplicate ClientSkuId found.");
                }
            }
        }
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void updateProduct(ProductForm formData, Long clientId, Long globalSkuId) throws ApiException {
        logger.info("update-product");
        Product pojo = convertFormToPojo(formData, clientId, globalSkuId);
        validateProductName(pojo.getName());
        validateProductBrandId(pojo.getBrandId());
        validateClientSkuId(pojo.getClientSkuId());
        validateProductMrp(pojo.getMrp());
        logger.info("Validation done.");
        Product product = productDao.getProductById(pojo.getGlobalSkuId());
        if (product == null) {
            logger.info("No Product found with given id: "+pojo.getGlobalSkuId());
            throw new ApiException("Product with given ID does not exist.");
        }
        productDao.updateProduct(pojo);
    }

    public ProductDataResponse getProduct(Long globalSkuId) throws ApiException {
        logger.info("get-product");
        Product product = productDao.getProductById(globalSkuId);
        if(product==null){
            logger.info("No Product found with required id.");
            throw new ApiException("No Product found with required id.");
        }
        return convertPojoToFormData(product);
    }

    public List<ProductDataResponse> getAllProducts() throws ApiException {
        logger.info("get-All-products");
        List<Product> list = productDao.getAllProducts();
        if(list.isEmpty()){
            logger.info("No Product found. Please add Product first.");
            throw new ApiException("No Product found. Pleas add Product first.");
        }
        List<ProductDataResponse> list2 = new ArrayList<>();
        for (Product product : list) {
            list2.add(convertPojoToFormData(product));
        }
        return list2;
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<ProductDataResponse> getAllProducts(Long clientId) throws ApiException {
        logger.info("get-All-products-for-a-client");
        List<ProductDataResponse> list2 = new ArrayList<>();
        List<Product> list = productDao.getAllClientProducts(clientId);
        if(list.isEmpty()){
            logger.info("No product found for clientId: "+clientId);
            throw new ApiException("No product found.");
        }else{
            for (Product product : list) {
                list2.add(convertPojoToFormData(product));
            }
        }
        return list2;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void deleteProduct(Long globalSkuId) throws ApiException {
        logger.info("delete-a-product");
        Product product = productDao.getProductById(globalSkuId);
        if(product != null){
            logger.info("deleting product with globalSkuId : "+ globalSkuId);
            int res = productDao.deleteProductById(globalSkuId);
            logger.info("Records deleted : "+res);
        }else{
            logger.info("No Product exists.");
            throw new ApiException("No Product exists.");
        }
    }

    public Product getProduct(String clientSkuId, Long clientId) throws ApiException {
        logger.info("get-product-by-clientSkuId-and-clientId");
        Product product = productDao.getProductByClientSkuIdAndClientId(clientSkuId, clientId);
        if(product==null){
            logger.info("No Product found.");
            throw new ApiException("No Product found");
        }
        return product;
    }

    public Product getProductById(Long globalSkuId) throws ApiException {
        logger.info("get-product-by-global-sku-id("+globalSkuId+")");
        Product product = productDao.getProductById(globalSkuId);
        if(product==null){
            logger.info("No Product found.");
            throw new ApiException("No Product found.");
        }
        return product;
    }
}
