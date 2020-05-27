package com.assure.dto;

import com.assure.api.ClientApi;
import com.assure.api.ProductApi;
import com.assure.pojo.Client;
import com.assure.pojo.Product;
import com.commons.form.ProductCsvInputForm;
import com.commons.form.ProductForm;
import com.commons.response.ProductDataResponse;
import com.commons.service.ApiException;
import com.commons.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class  ProductDto {

    @Autowired private ProductApi productApi;
    @Autowired private ClientApi clientApi;

    @Transactional(rollbackFor = ApiException.class)
    public void addProduct(ProductCsvInputForm formData) {
        for(ProductForm data : formData.getProductList()){
            Product product = ConvertUtil.convert(data, Product.class);
            if (product!=null) {
                productApi.addProduct(product);
            }
        }
    }

    @Transactional(rollbackFor = ApiException.class)
    public void updateProduct(ProductForm formData, Long globalSkuId) {
        Product product = ConvertUtil.convert(formData, Product.class);
        if (product != null) {
            product.setGlobalSkuId(globalSkuId);
            productApi.updateProduct(product);
        }
    }

    @Transactional(readOnly = true)
    public ProductDataResponse getProduct(Long globalSkuId) {
        Product product = productApi.getProductById(globalSkuId);
        ProductDataResponse dataResponse = ConvertUtil.convert(product, ProductDataResponse.class);
        if (dataResponse != null) {
            Client client = clientApi.getClientDetails(product.getClientId());
            dataResponse.setClientName(client.getName());
        }
        return dataResponse;
    }

    @Transactional(readOnly = true)
    public List<ProductDataResponse> getAllProducts(Long clientId) {
        List<Product> productList = productApi.getAllProducts(clientId);
        if(productList.isEmpty()){
            throw new ApiException("No product found.");
        }
        List<ProductDataResponse> dataResponseList = ConvertUtil.convert(productList, ProductDataResponse.class);
        if (dataResponseList != null) {
            for(ProductDataResponse data : dataResponseList) {
                Client client = clientApi.getClientDetails(clientId);
                data.setClientName(client.getName());
            }
        }
        return dataResponseList;
    }

    @Transactional(readOnly = true)
    public List<ProductDataResponse> getAllProducts() {
        List<Product> productList = productApi.getAllProducts();
        if(productList.isEmpty()){
            throw new ApiException("No Product found. Please add few Products first.");
        }
        List<ProductDataResponse> dataResponseList = ConvertUtil.convert(productList, ProductDataResponse.class);
        if (dataResponseList != null) {
            for(int i=0; i<productList.size(); i++) {
                Client client = clientApi.getClientDetails(productList.get(i).getClientId());
                dataResponseList.get(i).setClientName(client.getName());
            }
        }
        return dataResponseList;
    }

    @Transactional(readOnly = true)
    public Product getProductDetails(String clientSkuId, Long clientId) {
        return productApi.getProductDetails(clientSkuId, clientId);
    }

    @Transactional(readOnly = true)
    public Client getClient(Long clientId) {
        return clientApi.getClientDetails(clientId);
    }

    @Transactional(readOnly = true)
    public Product getProductData(Long globalSkuId) {
        return productApi.getProductById(globalSkuId);
    }
}
