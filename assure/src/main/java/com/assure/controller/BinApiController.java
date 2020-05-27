package com.assure.controller;

import com.assure.dto.BinInventoryDto;
import com.assure.model.form.BinForm;
import com.assure.model.response.BinDataResponse;
import com.commons.service.CustomValidationException;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.xml.validation.Validator;
import java.util.List;

@RestController
@Validated
public class BinApiController {

    private Validator validator;

    @Autowired
    private BinInventoryDto binInventoryDto;

    private static final Logger logger = Logger.getLogger(BinApiController.class);

    @ApiOperation(value = "Create bins")
    @RequestMapping(path = "/api/bin", method = RequestMethod.POST)
    public ResponseEntity<List<BinDataResponse>> createBins(@Valid @RequestBody BinForm binForm, BindingResult result) {
        logger.info("create-bin");
        logger.info(result.getErrorCount());
        if(result.hasErrors()){
            throw new CustomValidationException(result);
        }
        binInventoryDto.createBins(binForm);
        return ResponseEntity.ok(getAllBins());
    }

    @ApiOperation(value = "Gets list of all bins")
    @RequestMapping(value = "/api/bin", method = RequestMethod.GET)
    public List<BinDataResponse> getAllBins() {
        logger.info("get-All-bins");
        return binInventoryDto.getAllBin();
    }
}
