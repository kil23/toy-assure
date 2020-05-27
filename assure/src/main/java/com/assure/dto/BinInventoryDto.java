package com.assure.dto;

import com.assure.api.*;
import com.assure.model.form.BinForm;
import com.assure.model.form.BinInventoryCsvInputForm;
import com.assure.model.form.BinInventoryUpdateForm;
import com.assure.model.form.InventoryForm;
import com.assure.model.response.BinDataResponse;
import com.assure.model.response.BinSkuDataResponse;
import com.assure.model.response.InventoryDataResponse;
import com.assure.pojo.*;
import com.commons.service.ApiException;
import com.commons.util.ConvertUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class BinInventoryDto {

    private static final Logger logger = Logger.getLogger(BinInventoryDto.class);

    @Autowired
    private BinSkuApi binSkuApi;

    @Autowired
    private BinApi binApi;

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private ClientApi clientApi;

    @Transactional(rollbackFor = ApiException.class)
    public void addBinInventory(BinInventoryCsvInputForm formData) {
        List<InventoryForm> inventoryFormList = formData.getInventoryList();
        for(InventoryForm form : inventoryFormList){
            Product product = getProductIfPresentByClientSkuId(form.getClientSkuId(),formData.getClientId());
            BinSku binSkuToInsert = ConvertUtil.convert(form, BinSku.class);
            if (binSkuToInsert != null) {
                binSkuToInsert.setGlobalSkuId(product.getGlobalSkuId());
                BinSku binSkuRecordInDB = binSkuApi.getBinSkuData(binSkuToInsert.getGlobalSkuId(), binSkuToInsert.getBinId());
                if(binSkuRecordInDB == null){
                    addNewBinSkuRecord(binSkuToInsert);
                }else{
                    updateBinSkuRecord(binSkuRecordInDB, binSkuToInsert);
                }
            }
        }
    }

    public void addNewBinSkuRecord(BinSku binSkuToInsert){
        binSkuApi.addBinSku(binSkuToInsert);
        Inventory ip = createInventoryPojo(binSkuToInsert.getGlobalSkuId(), binSkuToInsert.getQuantity());
        inventoryApi.addInventory(ip);
    }

    public void updateBinSkuRecord(BinSku binSkuRecordInDB, BinSku binSkuToInsert) {
        Long qnty = binSkuRecordInDB.getQuantity() + binSkuToInsert.getQuantity();
        BinSku pojo = createBinSuPojo(binSkuRecordInDB.getId(), binSkuToInsert.getBinId(), binSkuToInsert.getGlobalSkuId(), qnty);
        binSkuApi.updateBinSku(pojo);
        updateInventoryRecord(binSkuToInsert);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void updateBinInventory(BinInventoryUpdateForm formData, long globalSkuId) {
        BinSku binSkuToInsert = ConvertUtil.convert(formData, BinSku.class);
        if (binSkuToInsert != null) {
            binSkuToInsert.setId(formData.getBinSkuId());
            Long quantity = Math.subtractExact(formData.getQuantity(), formData.getOriginalQuantity());
            binSkuToInsert.setGlobalSkuId(globalSkuId);
            binSkuToInsert.setQuantity(quantity);
            BinSku binSkuRecordInDb = binSkuApi.getBinSkuDetailsById(binSkuToInsert.getId());
            Long qty = binSkuRecordInDb.getQuantity() + binSkuToInsert.getQuantity();
            binSkuToInsert.setQuantity(qty);
            binSkuApi.updateBinSku(binSkuToInsert);
            updateInventoryRecord(binSkuToInsert);
        }
    }

    public void updateInventoryRecord(BinSku binSku) {
        Inventory inventory = inventoryApi.getInventoryDetails(binSku.getGlobalSkuId());
        if(inventory==null){
            throw new ApiException("No inventory found for a Product.");
        }
        Long i_quantity = inventory.getAvailableQuantity() + binSku.getQuantity();
        Inventory new_ip = createInventoryPojo(inventory.getId(), inventory.getGlobalSkuId(), i_quantity);
        inventoryApi.updateInventory(new_ip);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void deleteBinInventory(long binSkuId) {
        BinSku binSku = binSkuApi.getBinSkuDetailsById(binSkuId);
        Inventory ip = inventoryApi.getInventoryDetails(binSku.getGlobalSkuId());
        Long i_quantity = ip.getAvailableQuantity() - binSku.getQuantity();
        Inventory new_ip;
        if(i_quantity>0){
            new_ip = createInventoryPojo(ip.getId(), ip.getGlobalSkuId(), i_quantity);
        }else{
            new_ip = createInventoryPojo(ip.getId(), ip.getGlobalSkuId(), 0L);
        }
        binSku.setQuantity(0L);
        inventoryApi.updateInventory(new_ip);
        binSkuApi.updateBinSku(binSku);
//        binSkuApi.deleteBinSku(binSku);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void createBins(BinForm binForm) {
        logger.info("create-bin-dto");
        for(int i = 0; i<binForm.getNoOfBins(); i++){
            Bin bin = binApi.createBins(new Bin());
            logger.info("Bin created with id : "+bin.getBinId());
        }
    }

    @Transactional(readOnly = true)
    public List<BinDataResponse> getAllBin() {
        List<Bin> binList = binApi.getAllBin();
        if(binList == null){
            throw new ApiException("No Bins Available. Please create few Bins first.");
        }
        return ConvertUtil.convert(binList, BinDataResponse.class);
    }

    @Transactional(readOnly = true)
    public BinSkuDataResponse getBinInventory(long binSkuId) {
        BinSku binSku = binSkuApi.getBinSkuDetailsById(binSkuId);
        BinSkuDataResponse response = ConvertUtil.convert(binSku, BinSkuDataResponse.class);
        if (response != null) {
            Product product = productApi.getProductById(binSku.getGlobalSkuId());
            if(product != null) {
                response.setName(product.getName());
                response.setBrandId(product.getBrandId());
                return response;
            }
            logger.info("No Product found for id : "+binSku.getGlobalSkuId());
        }
        return null;
    }

    @Transactional(readOnly = true)
    public List<BinSkuDataResponse> getBinwiseInventory(long binId) {
        List<BinSku> binSkuList = binSkuApi.getBinwiseInventory(binId);
        List<BinSkuDataResponse> responses = new ArrayList<>();
        List<BinSkuDataResponse> dataResponses = ConvertUtil.convert(binSkuList, BinSkuDataResponse.class);
        if (dataResponses != null) {
            for(int i=0; i<binSkuList.size(); i++){
                Product product = productApi.getProductById(binSkuList.get(i).getGlobalSkuId());
                if(product == null) {
                    logger.info("No Product found for name : "+binSkuList.get(i).getGlobalSkuId());
                } else {
                    dataResponses.get(i).setName(product.getName());
                    dataResponses.get(i).setBrandId(product.getBrandId());
                    responses.add(dataResponses.get(i));
                }
            }
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public List<BinSkuDataResponse> getClientInventory(long clientId) {
        List<Product> productList = productApi.getAllProducts(clientId);
        List<BinSkuDataResponse> responses = new ArrayList<>();
        for(Product product : productList){
            List<BinSku> binSkuList = binSkuApi.getAllBinSkuDetails(product.getGlobalSkuId());
            List<BinSkuDataResponse> dataResponses = ConvertUtil.convert(binSkuList, BinSkuDataResponse.class);
            if (dataResponses != null) {
                for(int i=0; i<binSkuList.size(); i++){
                    dataResponses.get(i).setName(product.getName());
                    dataResponses.get(i).setBrandId(product.getBrandId());
                    responses.add(dataResponses.get(i));
                }
            }
            logger.info("No Product found for name : "+product.getGlobalSkuId());
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public List<InventoryDataResponse> getAllInventory() {
        List<Inventory> inventoryList = inventoryApi.getAllInventory();
        if(inventoryList.isEmpty()){
            throw new ApiException("No Inventory found for any Product. Please add Inventory.");
        }
        List<InventoryDataResponse> responses = new ArrayList<>();
        List<InventoryDataResponse> dataResponseList = ConvertUtil.convert(inventoryList, InventoryDataResponse.class);
        for(int i=0; i<inventoryList.size(); i++){
            if (dataResponseList != null) {
                Product product = productApi.getProductById(inventoryList.get(i).getGlobalSkuId());
                if(product != null) {
                    dataResponseList.get(i).setName(product.getName());
                    dataResponseList.get(i).setBrandId(product.getBrandId());
                    responses.add(dataResponseList.get(i));
                }else {
                    logger.info("No Product found for name : "+dataResponseList.get(i).getName());
                }
            }
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public Product getProductIfPresentByClientSkuId(String clientSkuId, Long clientId) {
        return productApi.getProductDetails(clientSkuId, clientId);
    }

    @Transactional(readOnly = true)
    public List<BinSku> getBinSkuData(long globalSkuId) {
        return binSkuApi.getAllBinSkuDetails(globalSkuId);
    }

    @Transactional(readOnly = true)
    public BinSku getBinSkuDetails(long binSkuId) {
        return binSkuApi.getBinSkuDetailsById(binSkuId);
    }

    @Transactional(readOnly = true)
    public Bin getBinData(long binId) {
        return binApi.getBin(binId);
    }

    @Transactional(readOnly = true)
    public Client getClientDetails(Long clientId) {
        return clientApi.getClientDetails(clientId);
    }

    public Inventory createInventoryPojo(Long globalSkuId, Long quantity){
        Inventory inventory = new Inventory();
        inventory.setGlobalSkuId(globalSkuId);
        inventory.setAvailableQuantity(quantity);
        return inventory;
    }

    public Inventory createInventoryPojo(Long id, Long globalSkuId, Long quantity){
        Inventory inventory = new Inventory();
        inventory.setId(id);
        inventory.setGlobalSkuId(globalSkuId);
        inventory.setAvailableQuantity(quantity);
        return inventory;
    }

    public BinSku createBinSuPojo(Long id, Long binId, Long globalSkuId, Long qnty){
        BinSku binSku = new BinSku();
        binSku.setId(id);
        binSku.setBinId(binId);
        binSku.setGlobalSkuId(globalSkuId);
        binSku.setQuantity(qnty);
        return binSku;
    }
}
