package com.assure.validators;

import com.assure.dto.BinInventoryDto;
import com.assure.model.form.BinInventoryUpdateForm;
import com.assure.pojo.Bin;
import com.assure.pojo.BinSku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BinInventoryUpdateFormValidator implements Validator {

    @Autowired
    BinInventoryDto binInventoryDto;

    @Override
    public boolean supports(Class<?> clazz) {
        return BinInventoryUpdateFormValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BinInventoryUpdateForm updateForm = (BinInventoryUpdateForm) target;

        Long binId = updateForm.getBinId();
        Long binSkuId = updateForm.getBinSkuId();

        Bin bin = binInventoryDto.getBinData(binId);
        if(bin == null){
            errors.rejectValue("binId", "Bin not found with ID : "+binId);
        }

        BinSku binSku = binInventoryDto.getBinSkuDetails(binSkuId);
        if(binSku == null) {
            errors.rejectValue("binSkuId", "BinSku not found with Bin SKU ID : "+binSkuId);
        }
    }
}
