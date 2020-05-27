package com.assure.controller;

import com.assure.dto.ProductDto;
import com.assure.pojo.Client;
import com.assure.pojo.Product;
import com.assure.validators.ProductCsvInputFormValidator;
import com.assure.validators.ProductFormValidator;
import com.commons.form.ProductCsvInputForm;
import com.commons.form.ProductForm;
import com.commons.response.ProductDataResponse;
import com.commons.service.ApiException;
import com.commons.service.CustomValidationException;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ProductApiController {

    @Autowired
    private ProductDto productDto;

    @Autowired private ProductFormValidator validator;
    @Autowired private ProductCsvInputFormValidator inputFormValidator;

    private static final Logger logger = Logger.getLogger(ProductApiController.class);

    @ApiOperation(value = "Adds a new product")
    @RequestMapping(value = "/api/products", method = RequestMethod.POST)
    public void addProductDetail(@Valid @RequestBody ProductCsvInputForm formData,
                                 BindingResult result) {
        logger.info("add-product");
        logger.info(result.getErrorCount());
        inputFormValidator.validate(formData, result);
        logger.info(result.getErrorCount());
        if(result.hasErrors()){
            throw new CustomValidationException(result);
        }
        productDto.addProduct(formData);
    }

    @ApiOperation(value = "Updates a product by Id")
    @RequestMapping(value = "/api/products/{globalSkuId}", method = RequestMethod.PUT)
    public void updateProductDetail(@PathVariable("globalSkuId") Long globalSkuId,
                                    @Valid @RequestBody ProductForm formData,
                                    BindingResult result) {
        logger.info("update-product with id : "+ globalSkuId);
        Product product = productDto.getProductData(globalSkuId);
        if(product == null) {
            result.rejectValue("globalSkuId", "No Product found with id : "+globalSkuId);
        }
        logger.info(result.getErrorCount());
        if(result.hasErrors()){
            logger.info("here");
            throw new CustomValidationException(result);
        }
        logger.info("here");
        productDto.updateProduct(formData, globalSkuId);
    }

//    @ApiOperation(value = "Delete a product by Id")
//    @RequestMapping(path = "/api/products/{globalSkuId}", method = RequestMethod.DELETE)
//    public ResponseEntity<String> deleteProduct(@PathVariable("globalSkuId") Long globalSkuId) {
//        logger.info("delete a product");
//        Product product = productDto.getProductData(globalSkuId);
//        if(product == null) {
//            throw new ApiException("No Product found with id : "+globalSkuId);
//        }
//        productDto.deleteProduct(globalSkuId);
//        return ResponseEntity.ok("Request submitted successfully");
//    }

    @ApiOperation(value = "Gets a product by Id")
    @RequestMapping(value = "/api/products/{globalSkuId}", method = RequestMethod.GET)
    public ProductDataResponse getProductDetails(@PathVariable("globalSkuId") Long globalSkuId) {
        logger.info("get-client-product-by-globalSkuId");
        logger.info(globalSkuId);
        Product product = productDto.getProductData(globalSkuId);
        if(product == null) {
            logger.info("here");
            throw new ApiException("No Product found with id : "+globalSkuId);
        }
        return productDto.getProduct(globalSkuId);
    }

//    @ApiOperation(value = "Gets a product by Id for channel")
//    @RequestMapping(value = "/api/products/{globalSkuId}", method = RequestMethod.GET)
//    public ProductDataResponse getProductDetailsForChannel(
//            @PathVariable("globalSkuId") Long globalSkuId) {
//        logger.info("get-client-product-by-globalSkuId-for-channel");
//        Product product = productDto.getProductData(globalSkuId);
//        if(product == null) {
//            throw new ApiException("No Product found with id : "+globalSkuId);
//        }
//        return productDto.getProduct(globalSkuId);
//    }

    @ApiOperation(value = "Gets list of all products for the client")
    @RequestMapping(value = "/api/products/list/{clientId}", method = RequestMethod.GET)
    public List<ProductDataResponse> getAllClientProducts(@PathVariable("clientId") Long clientId) {
        logger.info("get-All-products-for-a-client");
        Client client = productDto.getClient(clientId);
        if(client == null) {
            throw new ApiException("No Client found with id : "+clientId);
        }
        return productDto.getAllProducts(clientId);
    }

    @ApiOperation(value = "Gets list of all products for ViewAllProducts page")
    @RequestMapping(value = "/api/products/list", method = RequestMethod.GET)
    public List<ProductDataResponse> getAllProducts() {
        logger.info("get-All-products");
        return productDto.getAllProducts();
    }
}
