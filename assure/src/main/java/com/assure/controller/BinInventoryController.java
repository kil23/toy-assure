package com.assure.controller;

import com.assure.model.form.BinInventoryUpdateForm;
import com.assure.model.response.BinSkuDataResponse;
import com.assure.model.response.InventoryDataResponse;
import com.assure.service.BinSkuService;
import com.assure.service.InventoryService;
import com.commons.service.ApiException;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BinInventoryController {

    @Autowired
    private BinSkuService binSkuService;

    @Autowired
    private InventoryService inventoryService;

    private static final Logger logger = Logger.getLogger(BinInventoryController.class);

    @ApiOperation(value = "Updates bin inventory by Id")
    @RequestMapping(value = "/api/{binId}/bin-inventory-list/{globalSkuId}", method = RequestMethod.PUT)
    public void updateBinInventoryDetail(@PathVariable("globalSkuId") long globalSkuId, @RequestBody BinInventoryUpdateForm formData) throws ApiException {
        logger.info("update-bin-inventory");
        binSkuService.updateBinInventory(formData.getBinSkuId(), formData.getBinId(), formData.getQuantity(), formData.getOriginalQuantity(), globalSkuId);
    }

    @ApiOperation(value = "Delete a bin inventory by Id")
    @RequestMapping(path = "/api/{binId}/bin-inventory-list/{binSkuId}", method = RequestMethod.DELETE)
    public void deleteBinInventory(@PathVariable("binSkuId") long binSkuId) throws ApiException {
        logger.info("delete-bin-inventory");
        binSkuService.deleteBinInventory(binSkuId);
    }

    @ApiOperation(value = "Gets a bin inventory details for update-popup")
    @RequestMapping(value = "/api/{binId}/bin-inventory-list/{binSkuId}", method = RequestMethod.GET)
    public BinSkuDataResponse getBinInventoryDetails(@PathVariable("binSkuId") long binSkuId) {
        logger.info("get-bin-wise-inventory-by-binSkuId");
        return binSkuService.getBinInventory(binSkuId);
    }

    @ApiOperation(value = "Gets list of inventory for a binId")
    @RequestMapping(value = "/api/{binId}/bin-inventory-list", method = RequestMethod.GET)
    public List<BinSkuDataResponse> getBinwiseInventory(@PathVariable("binId") long binId) throws ApiException {
        logger.info("get-All-inventory-for-bin");
        return binSkuService.getBinwiseInventory(binId);
    }

    @ApiOperation(value = "Gets list of all inventory for ViewAllInventory page")
    @RequestMapping(value = "/api/all-inventories", method = RequestMethod.GET)
    public List<InventoryDataResponse> getAllInventory() throws ApiException {
        logger.info("get-All-inventory");
        return inventoryService.getAllInventory();
    }
}
