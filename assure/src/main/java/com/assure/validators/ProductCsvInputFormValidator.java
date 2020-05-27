package com.assure.validators;

import com.assure.dto.ProductDto;
import com.assure.pojo.Client;
import com.assure.pojo.Product;
import com.commons.form.ProductCsvInputForm;
import com.commons.form.ProductForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class ProductCsvInputFormValidator implements Validator {

    @Autowired private ProductDto productDto;

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductCsvInputFormValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductCsvInputForm productCsvInputForm = (ProductCsvInputForm) target;

        List<ProductForm> productFormList = productCsvInputForm.getProductList();
        int index = 0;
        for (ProductForm productForm : productFormList){
            String clientSkuId = productForm.getClientSkuId();
            Long clientId = productForm.getClientId();
            String name = productForm.getName();
            String brandId = productForm.getBrandId();

            Client client = productDto.getClient(clientId);
            if(client == null) {
                errors.pushNestedPath("productList["+index+"]");
                errors.rejectValue("clientId", "not found","Client not found with ID : "+clientId);
                errors.popNestedPath();
            }

            Product product = productDto.getProductDetails(clientSkuId, clientId);
            if(product != null){
                errors.pushNestedPath("productList["+index+"]");
                errors.rejectValue("clientSkuId", "duplicate","Duplicate value found for Product with Client SKU ID : "+clientSkuId);
                errors.popNestedPath();
            }

            if(name.equals("")) {
                errors.pushNestedPath("productList["+index+"]");
                errors.rejectValue("name", "empty","Product Name cannot be empty.");
                errors.popNestedPath();
            }

            if(brandId.equals("")) {
                errors.pushNestedPath("productList["+index+"]");
                errors.rejectValue("brandId", "empty","Product Brand ID cannot be empty.");
                errors.popNestedPath();
            }
            index++;
        }
    }
}
