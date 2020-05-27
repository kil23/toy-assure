package com.assure.controller;

import com.assure.dto.BinInventoryDto;
import com.assure.model.form.BinInventoryCsvInputForm;
import com.assure.model.form.BinInventoryUpdateForm;
import com.assure.model.response.BinSkuDataResponse;
import com.assure.model.response.InventoryDataResponse;
import com.assure.pojo.Bin;
import com.assure.pojo.BinSku;
import com.assure.pojo.Client;
import com.assure.validators.BinInventoryCsvInputFormValidator;
import com.commons.service.ApiException;
import com.commons.service.CustomValidationException;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class InventoryApiController {

    @Autowired
    private BinInventoryDto binInventoryDto;

    @Autowired
    private BinInventoryCsvInputFormValidator validator;

    private static final Logger logger = Logger.getLogger(InventoryApiController.class);

    @ApiOperation(value = "Adds a new Inventory")
    @RequestMapping(value = "/api/inventory", method = RequestMethod.POST)
    public ResponseEntity<String> addInventoryDetail(@Valid @RequestBody BinInventoryCsvInputForm formData,
                                                     BindingResult result) {
        logger.info("adding-inventory");
        validator.validate(formData, result);
        if(result.hasErrors()) {
            throw new CustomValidationException(result);
        }
        binInventoryDto.addBinInventory(formData);
        return ResponseEntity.ok("Request submitted successfully.");
    }

    @ApiOperation(value = "Updates inventory by Id")
    @RequestMapping(value = "/api/{binId}/inventory/{globalSkuId}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateBinInventoryDetail(@PathVariable("globalSkuId") Long globalSkuId,
                                                           @Valid @RequestBody BinInventoryUpdateForm formData,
                                                           BindingResult result) {
        logger.info("update-bin-inventory");
        List<BinSku> binSkuList = binInventoryDto.getBinSkuData(globalSkuId);
        if(binSkuList.isEmpty()) {
            result.rejectValue("globalSkuId", "No Product found with id :" +globalSkuId);
        }
        if (result.hasErrors()){
            throw new CustomValidationException(result);
        }
        binInventoryDto.updateBinInventory(formData, globalSkuId);
        return ResponseEntity.ok("Request submitted successfully.");
    }

    @ApiOperation(value = "Delete a inventory by Id")
    @RequestMapping(path = "/api/{binId}/inventory/{binSkuId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteBinInventory(@PathVariable("binSkuId") Long binSkuId) {
        logger.info("delete-bin-inventory");
        BinSku binSku = binInventoryDto.getBinSkuDetails(binSkuId);
        if(binSku == null) {
            throw new ApiException("Bad Request with BinSkuId : "+binSkuId);
        }
        binInventoryDto.deleteBinInventory(binSkuId);
        return ResponseEntity.ok("Request submitted successfully.");
    }

    @ApiOperation(value = "Gets a inventory details for update-popup")
    @RequestMapping(value = "/api/{binId}/inventory/{binSkuId}", method = RequestMethod.GET)
    public ResponseEntity<BinSkuDataResponse> getBinInventoryDetails(@PathVariable("binSkuId") Long binSkuId) {
        logger.info("get-bin-wise-inventory-by-binSkuId");
        BinSku binSku = binInventoryDto.getBinSkuDetails(binSkuId);
        if(binSku == null) {
            throw new ApiException("Bad Request with BinSkuId : "+binSkuId);
        }
        BinSkuDataResponse response = binInventoryDto.getBinInventory(binSkuId);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Gets list of inventory for a binId")
    @RequestMapping(value = "/api/{binId}/inventory", method = RequestMethod.GET)
    public ResponseEntity<List<BinSkuDataResponse>> getBinwiseInventory(@PathVariable("binId") Long binId) {
        logger.info("get-All-inventory-for-bin");
        Bin bin = binInventoryDto.getBinData(binId);
        if(bin == null) {
            throw new ApiException("Bad Request with Bin ID : "+binId);
        }
        List<BinSkuDataResponse> responses = binInventoryDto.getBinwiseInventory(binId);
        return ResponseEntity.ok(responses);
    }

    @ApiOperation(value = "Gets list of inventory for a client")
    @RequestMapping(value = "/api/inventory/{clientId}", method = RequestMethod.GET)
    public ResponseEntity<List<BinSkuDataResponse>> getClientInventory(@PathVariable("clientId") Long clientId) {
        logger.info("get-All-inventory-for-client");
        Client client = binInventoryDto.getClientDetails(clientId);
        if(client == null) {
            throw new ApiException("Bad Request with Client ID : "+clientId);
        }
        List<BinSkuDataResponse> responses = binInventoryDto.getClientInventory(clientId);
        return ResponseEntity.ok(responses);
    }

    @ApiOperation(value = "Gets list of all inventory for View-All-Inventory")
    @RequestMapping(value = "/api/all-inventories", method = RequestMethod.GET)
    public ResponseEntity<List<InventoryDataResponse>> getAllInventory() {
        logger.info("get-All-inventory");
        List<InventoryDataResponse> responses = binInventoryDto.getAllInventory();
        return ResponseEntity.ok(responses);
    }
}
