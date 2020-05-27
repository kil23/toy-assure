package com.assure.validators;

import com.assure.dto.ProductDto;
import com.assure.pojo.Product;
import com.commons.form.ProductCsvInputForm;
import com.commons.form.ProductForm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class ProductFormValidator implements Validator {

    private static final Logger logger = Logger.getLogger(ProductFormValidator.class);

    @Autowired
    private ProductDto productDto;

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductFormValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductCsvInputForm form = (ProductCsvInputForm) target;
        List<ProductForm> productList = form.getProductList();
        int index = 0;
        for(ProductForm product : productList){
            logger.info("product-form: "+product.getName());
            logger.info("product-form: "+product.getClientSkuId());
            logger.info("product-form: "+product.getClientId());
            String clientSkuId = product.getClientSkuId();
            Long clientId = product.getClientId();
            Product productInDb = productDto.getProductDetails(clientSkuId, clientId);
            if(productInDb!=null) {
                logger.info("Product: "+ productInDb.getGlobalSkuId());
                errors.pushNestedPath("productList["+index+"]");
                errors.rejectValue("clientSkuId", "duplicate", "Duplicate Product found for Client SKU ID : "+clientSkuId);
                errors.popNestedPath();
            }
            index++;
        }
    }
}
