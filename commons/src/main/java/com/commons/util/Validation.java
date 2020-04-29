package com.commons.util;

import com.commons.service.ApiException;
import org.apache.log4j.Logger;

public class Validation {

    private static final Logger logger = Logger.getLogger(Validation.class);

    public static void validateClientName(String name) throws ApiException {

        if(StringUtil.isEmpty(name)){
            logger.info("Client name cannot be empty");
            throw new ApiException("Client name cannot be empty");
        }
    }

    public static void validateProductName(String name) throws ApiException {

        if(StringUtil.isEmpty(name)){
            logger.info("Product name cannot be empty");
            throw new ApiException("Product name cannot be empty");
        }
    }

    public static void validateProductBrandId(String brandId) throws ApiException {

        if(StringUtil.isEmpty(brandId)){
            logger.info("BrandId cannot be empty");
            throw new ApiException("BrandId cannot be empty");
        }
    }

    public static void validateClientSkuId(String clientSkuId) throws ApiException {

        if(StringUtil.isEmpty(clientSkuId)){
            logger.info("ClientSkuId cannot be empty");
            throw new ApiException("ClientSkuId cannot be empty");
        }
    }

    public static void validateProductMrp(double mrp) throws ApiException {

        if(mrp > 0.0 && StringUtil.isEmpty(String.valueOf(mrp))){
            logger.info("Product Mrp cannot be empty");
            throw new ApiException("Product Mrp cannot be empty");
        }
    }

    public static void validateChannelSkuId(String channelSkuId) throws ApiException {

        if(StringUtil.isEmpty(channelSkuId)){
            logger.info("ChannelSkuId cannot be empty");
            throw new ApiException("ChannelSkuId cannot be empty");
        }
    }

    public static void validateChannelOrderId(String channelOrderId) throws ApiException {

        if(StringUtil.isEmpty(channelOrderId)){
            logger.info("ChannelOrderId cannot be empty");
            throw new ApiException("ChannelOrderId cannot be empty");
        }
    }

    public static void validateChannelName(String channelName) throws ApiException {

        if(StringUtil.isEmpty(channelName)){
            logger.info("Client name cannot be empty");
            throw new ApiException("Client name cannot be empty");
        }
    }
}
