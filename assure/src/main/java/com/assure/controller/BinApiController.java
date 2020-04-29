package com.assure.controller;

import com.assure.model.form.BinInventoryCsvInputForm;
import com.assure.model.response.BinDataResponse;
import com.assure.service.BinService;
import com.assure.service.BinSkuService;
import com.commons.service.ApiException;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BinApiController {

    @Autowired
    BinService binService;

    @Autowired
    BinSkuService binSkuService;

    private static final Logger logger = Logger.getLogger(BinApiController.class);

    @RequestMapping(path = "/api/bin", method = RequestMethod.POST)
    public void createBins(@RequestParam(value = "val") Long num) {
        logger.info("create-bin");
        binService.createBins(num);
    }

    @ApiOperation(value = "Gets list of all bins")
    @RequestMapping(value = "/api/bin", method = RequestMethod.GET)
    public List<BinDataResponse> getAllBins() throws ApiException {
        logger.info("get-All-bins");
        return binService.getAllBin();
    }

    @ApiOperation(value = "Adds a new bin-inventory")
    @RequestMapping(value = "/api/bin/inventory", method = RequestMethod.POST)
    public void addBinInventoryDetail(@RequestBody BinInventoryCsvInputForm formData) throws ApiException {
        logger.info("adding-bin-inventory");
        binSkuService.insertBinInventory(formData.getBinId(), formData.getClientId(), formData.getClientSkuId(), formData.getQuantity());
    }
}
