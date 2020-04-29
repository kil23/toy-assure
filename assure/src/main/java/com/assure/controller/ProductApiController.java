package com.assure.controller;

import com.assure.service.ProductService;
import com.commons.form.ProductForm;
import com.commons.response.ProductDataResponse;
import com.commons.service.ApiException;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductApiController {

    @Autowired
    private ProductService service;

    private static final Logger logger = Logger.getLogger(ProductApiController.class);

    @ApiOperation(value = "Adds a new product")
    @RequestMapping(value = "/api/{clientId}/products", method = RequestMethod.POST)
    public void addProductDetail(@PathVariable("clientId") long clientId, @RequestBody ProductForm formData) throws ApiException {
        logger.info("add-product");
        service.insertProduct(formData, clientId);
    }

    @ApiOperation(value = "Updates a product by Id")
    @RequestMapping(value = "/api/{clientId}/products/{globalSkuId}", method = RequestMethod.PUT)
    public void updateProductDetail(@PathVariable("clientId") long clientId, @PathVariable("globalSkuId") long globalSkuId, @RequestBody ProductForm formData) throws ApiException {
        logger.info("update-product with id : "+ globalSkuId);
        service.updateProduct(formData, clientId, globalSkuId);
    }

    @ApiOperation(value = "Delete a product by Id")
    @RequestMapping(path = "/api/{clientId}/products/{globalSkuId}", method = RequestMethod.DELETE)
    public void deleteProduct(@PathVariable("globalSkuId") long globalSkuId) throws ApiException {
        logger.info("delete a product");
        service.deleteProduct(globalSkuId);
    }

    @ApiOperation(value = "Gets a product by Id")
    @RequestMapping(value = "/api/{clientId}/products/{globalSkuId}", method = RequestMethod.GET)
    public ProductDataResponse getProductDetails(@PathVariable("globalSkuId") long globalSkuId) throws ApiException {
        logger.info("get-client-product-by-globalSkuId");
        return service.getProduct(globalSkuId);
    }

    @ApiOperation(value = "Gets a product by Id for channel")
    @RequestMapping(value = "/api/products/{globalSkuId}", method = RequestMethod.GET)
    public ProductDataResponse getProductDetailsForChannel(@PathVariable("globalSkuId") long globalSkuId) throws ApiException {
        logger.info("get-client-product-by-globalSkuId-for-channel");
        return service.getProduct(globalSkuId);
    }

    @ApiOperation(value = "Gets list of all products for the client")
    @RequestMapping(value = "/api/{clientId}/products", method = RequestMethod.GET)
    public List<ProductDataResponse> getAllClientProducts(@PathVariable("clientId") long clientId) throws ApiException {
        logger.info("get-All-products-for-a-client");
        return service.getAllProducts(clientId);
    }

    @ApiOperation(value = "Gets list of all products for ViewAllProducts page")
    @RequestMapping(value = "/api/products", method = RequestMethod.GET)
    public List<ProductDataResponse> getAllProducts() throws ApiException {
        logger.info("get-All-products");
        return service.getAllProducts();
    }
}
